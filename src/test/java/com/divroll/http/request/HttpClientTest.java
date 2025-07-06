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

import com.divroll.http.HttpInputStreamBody;
import com.divroll.http.HttpStringBody;
import com.divroll.http.response.ByteArrayHttpResponse;
import com.divroll.http.response.HttpResponse;
import com.divroll.http.response.StringHttpResponse;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.divroll.http.util.Base64;

import static junit.framework.TestCase.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HttpClientTest {

  @Test
  public void Post_request_with_string_body_should_return_non_null_response() throws Exception {
    Set<Header> headers = new HashSet<>();
    headers.add(new Header("Content-Type", "application/json"));
    headers.add(new Header("Accept", "application/json"));
    PostRequest request =
            new PostRequest("http://httpbin.org/post", headers, new HttpStringBody("hello world"));
    HttpResponse<InputStream> response = HttpClient.post(request);
    InputStream body = response.getBody();
    assertNotNull(body);
  }

  @Test
  public void Post_request_with_input_stream_body_should_return_non_null_response() throws Exception {
    Set<Header> headers = new HashSet<>();
    headers.add(new Header("Content-Type", "application/json"));
    headers.add(new Header("Accept", "application/json"));
    InputStream inputStream = null;
    PostRequest request =
            new PostRequest("http://httpbin.org/post", headers, new HttpInputStreamBody(inputStream));
    HttpResponse<String> response = HttpClient.post(request);
    String body = response.getBody();
    assertNotNull(body);
  }

  @Test
  public void Put_request_with_string_body_should_return_non_null_response() throws Exception {
    Set<Header> headers = new HashSet<>();
    headers.add(new Header("Content-Type", "application/json"));
    headers.add(new Header("Accept", "application/json"));
    PutRequest request = new PutRequest("http://httpbin.org/put", headers, new HashMap<>(), new HttpStringBody("hello world"));
    HttpResponse<InputStream> response = HttpClient.put(request);
    InputStream body = response.getBody();
    assertNotNull(body);
  }

  @Test
  public void Get_request_should_return_non_null_response() throws Exception {
    Set<Header> headers = new HashSet<>();
    headers.add(new Header("Content-Type", "application/json"));
    headers.add(new Header("Accept", "application/json"));
    GetRequest<StringHttpResponse> request = new GetRequest("http://httpbin.org/get", headers, new HashMap<>(), StringHttpResponse.class);
    HttpResponse<InputStream> response = HttpClient.get(request);
    InputStream body = response.getBody();
    assertNotNull(body);
  }

  @Test
  public void Delete_request_should_return_non_null_response() throws Exception {
    Set<Header> headers = new HashSet<>();
    headers.add(new Header("Content-Type", "application/json"));
    headers.add(new Header("Accept", "application/json"));
    DeleteRequest request = new DeleteRequest("http://httpbin.org/delete", headers, new HashMap<>());
    HttpResponse<InputStream> response = HttpClient.delete(request);
    InputStream body = response.getBody();
    assertNotNull(body);
  }

  @Test
  public void Get_request_with_query_parameters_should_return_non_null_response() throws Exception {
    Set<Header> headers = new HashSet<>();
    headers.add(new Header("Content-Type", "application/json"));
    headers.add(new Header("Accept", "application/json"));

    Map<String, String> queryParameters = new HashMap<>();
    queryParameters.put("param1", "value1");
    queryParameters.put("param2", "value2");

    GetRequest<StringHttpResponse> request = new GetRequest("http://httpbin.org/get", headers, queryParameters, StringHttpResponse.class);
    HttpResponse<InputStream> response = HttpClient.get(request);
    InputStream body = response.getBody();
    assertNotNull(body);
  }

  @Test
  public void Post_request_with_json_body_should_return_non_null_response() throws Exception {
    Set<Header> headers = new HashSet<>();
    headers.add(new Header("Content-Type", "application/json"));
    headers.add(new Header("Accept", "application/json"));

    String jsonBody = "{\"key\":\"value\"}";
    PostRequest request = new PostRequest("http://httpbin.org/post", headers, new HttpStringBody(jsonBody));
    HttpResponse<InputStream> response = HttpClient.post(request);
    assertEquals(Integer.valueOf(200), response.getCode());
    assertNotNull(response.getBody());
  }

  @Test
  public void Basic_auth_with_correct_credentials_should_return_success() throws Exception {
    Set<Header> headers = new HashSet<>();
    String auth = "user:password";
    String encodedAuth = Base64.btoa(auth.getBytes());
    headers.add(new Header("Authorization", "Basic " + encodedAuth));

    GetRequest<StringHttpResponse> request = new GetRequest("http://httpbin.org/basic-auth/user/password", headers, new HashMap<>(), StringHttpResponse.class);
    HttpResponse<InputStream> response = HttpClient.get(request);
    assertEquals(Integer.valueOf(200), response.getCode());
    assertNotNull(response.getBody());
  }

  @Test
  public void Basic_auth_with_incorrect_credentials_should_return_401() throws Exception {
    Set<Header> headers = new HashSet<>();
    String auth = "wronguser:wrongpassword";
    String encodedAuth = Base64.btoa(auth.getBytes());
    headers.add(new Header("Authorization", "Basic " + encodedAuth));

    GetRequest<StringHttpResponse> request = new GetRequest("http://httpbin.org/basic-auth/user/password", headers, new HashMap<>(), StringHttpResponse.class);
    HttpResponse<InputStream> response = HttpClient.get(request);
    assertEquals(Integer.valueOf(401), response.getCode());
  }

  @Test
  public void Bearer_auth_with_valid_token_should_return_success() throws Exception {
    Set<Header> headers = new HashSet<>();
    headers.add(new Header("Authorization", "Bearer valid-token"));

    GetRequest<StringHttpResponse> request = new GetRequest("http://httpbin.org/bearer", headers, new HashMap<>(), StringHttpResponse.class);
    HttpResponse<InputStream> response = HttpClient.get(request);
    assertEquals(Integer.valueOf(200), response.getCode());
    assertNotNull(response.getBody());
  }

  @Test
  public void Xml_response_should_be_handled_correctly() throws Exception {
    Set<Header> headers = new HashSet<>();
    headers.add(new Header("Accept", "application/xml"));

    GetRequest<StringHttpResponse> request = new GetRequest("http://httpbin.org/xml", headers, new HashMap<>(), StringHttpResponse.class);
    HttpResponse<InputStream> response = HttpClient.get(request);
    assertEquals(Integer.valueOf(200), response.getCode());
    assertNotNull(response.getBody());
  }

  @Test
  public void Post_request_with_empty_body_should_return_non_null_response() throws Exception {
    Set<Header> headers = new HashSet<>();
    headers.add(new Header("Content-Type", "application/json"));
    headers.add(new Header("Accept", "application/json"));

    PostRequest request = new PostRequest("http://httpbin.org/post", headers, new HttpStringBody(""));
    HttpResponse<InputStream> response = HttpClient.post(request);
    assertEquals(Integer.valueOf(200), response.getCode());
    assertNotNull(response.getBody());
  }

  @Test(expected = MalformedURLException.class)
  public void Request_with_malformed_url_should_throw_exception() throws Exception {
    Set<Header> headers = new HashSet<>();
    headers.add(new Header("Content-Type", "application/json"));

    GetRequest<StringHttpResponse> request = new GetRequest("malformed-url", headers, new HashMap<>(), StringHttpResponse.class);
    HttpClient.get(request);
  }

  @Test
  public void Request_to_status_endpoint_should_return_specified_status_code() throws Exception {
    Set<Header> headers = new HashSet<>();
    GetRequest<StringHttpResponse> request = new GetRequest("http://httpbin.org/status/404", headers, new HashMap<>(), StringHttpResponse.class);
    HttpResponse<InputStream> response = HttpClient.get(request);
    assertEquals(Integer.valueOf(404), response.getCode());
  }

  @Test
  public void Get_cookies_should_return_non_null_response() throws Exception {
    Set<Header> headers = new HashSet<>();
    GetRequest<StringHttpResponse> request = new GetRequest("http://httpbin.org/cookies", headers, new HashMap<>(), StringHttpResponse.class);
    HttpResponse<InputStream> response = HttpClient.get(request);
    assertEquals(Integer.valueOf(200), response.getCode());
    assertNotNull(response.getBody());
  }

  @Test
  public void Set_cookies_should_work() throws Exception {
    Set<Header> headers = new HashSet<>();
    Map<String, String> queryParameters = new HashMap<>();
    queryParameters.put("cookie1", "value1");
    queryParameters.put("cookie2", "value2");

    GetRequest<StringHttpResponse> request = new GetRequest("http://httpbin.org/cookies/set", headers, queryParameters, StringHttpResponse.class);
    HttpResponse<InputStream> response = HttpClient.get(request);
    assertEquals(Integer.valueOf(200), response.getCode());
    assertNotNull(response.getBody());
  }

  @Test
  public void Delayed_response_should_be_handled_correctly() throws Exception {
    Set<Header> headers = new HashSet<>();
    GetRequest<StringHttpResponse> request = new GetRequest("http://httpbin.org/delay/2", headers, new HashMap<>(), StringHttpResponse.class);
    long startTime = System.currentTimeMillis();
    HttpResponse<InputStream> response = HttpClient.get(request);
    long endTime = System.currentTimeMillis();
    assertEquals(Integer.valueOf(200), response.getCode());
    assertNotNull(response.getBody());
    long elapsedTime = endTime - startTime;
    assertTrue(elapsedTime >= 2000 && elapsedTime < 3000); // Allow some buffer time
  }

  @Test
  public void Download_100MB_file_should_return_correct_size() {
    try {
      Set<Header> headers = new HashSet<>();
      GetRequest<ByteArrayHttpResponse> request
              = new GetRequest(
                      "https://ash-speed.hetzner.com/100MB.bin",
                      headers,
                      new HashMap<>(),
                      ByteArrayHttpResponse.class);
      HttpResponse<byte[]> response = HttpClient.get(request);

      assertEquals(Integer.valueOf(200), response.getCode());
      assertNotNull(response.getBody());
      assertEquals(100 * 1024 * 1024, response.getBody().length);
    } catch (Throwable t) {
      fail("Test failed: " + t.getMessage());
    }
  }
}
