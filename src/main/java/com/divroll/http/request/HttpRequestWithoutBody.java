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

import com.divroll.http.response.HttpResponse;

import java.net.MalformedURLException;
import java.util.Map;
import java.util.Set;

public class HttpRequestWithoutBody extends HttpRequest {
  public HttpRequestWithoutBody(String url, Set<Header> headers, Map<String, String> queryParameters)
      throws MalformedURLException {
    super(url, headers, queryParameters);
  }

  public HttpRequestWithoutBody(String url, Set<Header> headers, Map<String, String> queryParameters,
                                Class<? extends HttpResponse> responseClass)
          throws MalformedURLException {
    super(url, headers, queryParameters, responseClass);
  }
}
