/*
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.divroll.http.request;

import com.divroll.http.HttpStringBody;
import com.divroll.http.exception.HttpException;
import com.divroll.http.response.ByteArrayHttpResponse;
import com.divroll.http.response.HttpResponse;
import com.divroll.http.HttpBody;
import com.divroll.http.response.StringHttpResponse;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import java.net.URL;
import java.net.URLEncoder;
import java.io.UnsupportedEncodingException;
import java.io.IOException;

import org.teavm.interop.AsyncCallback;
import org.teavm.jso.ajax.XMLHttpRequest;
import org.teavm.interop.Async;
import org.teavm.jso.browser.Window;
import org.teavm.jso.typedarrays.ArrayBuffer;
import org.teavm.jso.typedarrays.Int8Array;

public class HttpClient {

  private static final int DEFAULT_TIMEOUT = 30000; // 30 seconds

  @Async
  public static native HttpResponse get(GetRequest request) throws HttpException, IOException;

  private static void get(GetRequest request, AsyncCallback<HttpResponse> callback) {
    executeAsync(request, "GET", null, request.getResponseClass(), callback);
  }

  @Async
  public static native HttpResponse put(PutRequest request) throws HttpException, IOException;

  private static void put(PutRequest request, AsyncCallback<HttpResponse> callback) {
    executeAsync(request, "PUT", request.getBody(), StringHttpResponse.class, callback);
  }

  @Async
  public static native HttpResponse post(PostRequest request) throws HttpException, IOException;

  private static void post(PostRequest request, AsyncCallback<HttpResponse> callback) {
    executeAsync(request, "POST", request.getBody(), StringHttpResponse.class, callback);
  }

  @Async
  public static native HttpResponse delete(DeleteRequest request) throws HttpException, IOException;

  private static void delete(DeleteRequest request, AsyncCallback<HttpResponse> callback) {
    executeAsync(request, "DELETE", null, StringHttpResponse.class, callback);
  }

  private static <T extends HttpResponse> void executeAsync(HttpRequest request, String method, HttpBody body, Class<T> responseClass, AsyncCallback<HttpResponse> callback) {
    try {
      XMLHttpRequest xhr = XMLHttpRequest.create();
      String url = buildUrlWithQueryParams(request.getUrl(), request.getQueryParameters());
      xhr.open(method, url, true);

      if (responseClass == ByteArrayHttpResponse.class) {
        xhr.setResponseType("arraybuffer");
      }

      for (Header header : request.getHeaders()) {
        xhr.setRequestHeader(header.getName(), header.getValue());
      }

      final int[] timeoutId = new int[1];
      timeoutId[0] = 0;

      if (DEFAULT_TIMEOUT > 0) {
        timeoutId[0] = Window.setTimeout(() -> {
          if (xhr.getReadyState() != XMLHttpRequest.DONE) {
            xhr.abort();
            callback.error(new IOException("Request timed out"));
          }
        }, DEFAULT_TIMEOUT);
      }

      xhr.setOnReadyStateChange(() -> {
        if (xhr.getReadyState() != XMLHttpRequest.DONE) return;

        try {
          byte[] raw = toByteArray((ArrayBuffer)xhr.getResponse());
          Set<Header> headers = parseResponseHeaders(xhr.getAllResponseHeaders());
          T response;

          if (responseClass == StringHttpResponse.class) {
            String text = new String(raw, StandardCharsets.UTF_8);
            response = responseClass.cast(
                    new StringHttpResponse(xhr.getStatus(), headers, text)
            );
          } else if (responseClass == ByteArrayHttpResponse.class) {
            response = responseClass.cast(
                    new ByteArrayHttpResponse(xhr.getStatus(), headers, raw)
            );
          } else {
            callback.error(new IllegalArgumentException(
                    "Unsupported response type: " + responseClass.getName()
            ));
            return;
          }

          callback.complete(response);
        } catch (Exception e) {
          callback.error(e);
        }
      });

      if (body != null) {
        xhr.send(getBodyContent(body));
      } else {
        xhr.send();
      }
    } catch (Exception e) {
      callback.error(e);
    }
  }

  private static String buildUrlWithQueryParams(URL baseUrl, Map<String, String> queryParams) {
    if (queryParams == null || queryParams.isEmpty()) {
      return baseUrl.toString();
    }

    StringBuilder urlBuilder = new StringBuilder(baseUrl.toString());

    if (baseUrl.getQuery() == null || baseUrl.getQuery().isEmpty()) {
      urlBuilder.append('?');
    } else {
      urlBuilder.append('&');
    }

    boolean first = true;
    for (Map.Entry<String, String> entry : queryParams.entrySet()) {
      if (!first) {
        urlBuilder.append('&');
      }
      first = false;

      try {
        urlBuilder.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
        urlBuilder.append('=');
        urlBuilder.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
      } catch (UnsupportedEncodingException e) {
        throw new RuntimeException("UTF-8 encoding not supported", e);
      }
    }

    return urlBuilder.toString();
  }

  private static Set<Header> parseResponseHeaders(String headerString) {
    Set<Header> headers = new HashSet<>();

    if (headerString == null || headerString.trim().isEmpty()) {
      return headers;
    }

    String[] headerLines = headerString.split("\r?\n");
    for (String line : headerLines) {
      if (line.trim().isEmpty()) {
        continue;
      }

      int colonIndex = line.indexOf(':');
      if (colonIndex > 0) {
        String name = line.substring(0, colonIndex).trim();
        String value = line.substring(colonIndex + 1).trim();
        headers.add(new Header(name, value));
      }
    }

    return headers;
  }

  private static HttpResponse createStringHttpResponse(int status, Set<Header> headers, String body) {
    return new StringHttpResponse(status, headers, body);
  }

  private static String getBodyContent(HttpBody body) {
    if (body != null && body instanceof HttpStringBody) {
      return ((HttpStringBody) body).getValue();
    }
    return null;
  }

  private static byte[] toByteArray(ArrayBuffer arrayBuffer) {
    Int8Array int8Array = Int8Array.create(arrayBuffer);
    byte[] bytes = new byte[int8Array.getLength()];
    for (int i = 0; i < bytes.length; i++) {
      bytes[i] = int8Array.get(i);
    }
    return bytes;
  }
}
