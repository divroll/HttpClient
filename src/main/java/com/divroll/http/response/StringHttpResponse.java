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
package com.divroll.http.response;

import java.util.Set;
import com.divroll.http.request.Header;

public class StringHttpResponse implements HttpResponse<String> {

  private Integer code;
  private Set<Header> headers;
  private String body;

  /**
   * create a new HTTP response
   *
   * @param code    the response code
   * @param headers the response headers
   * @param body    the response body
   */
  public StringHttpResponse(Integer code, Set<Header> headers, String body) {
    this.code = code;
    this.headers = headers;
    if (body == null) {
      this.body = "";
    } else {
      this.body = body;
    }
  }

  /**
   * get the response code
   *
   * @return the response code
   */
  public Integer getCode() {
    return code;
  }

  /**
   * get the response headers
   *
   * @return a list of the response headers. this list is not sorted
   */
  public Set<Header> getHeaders() {
    return headers;
  }

  /**
   * get the response body
   *
   * @return the response body, or the empty string if there was none
   */
  public String getBody() {
    return body;
  }

  /**
   * convenience method for getBody().length > 0
   *
   * @return true if it does have a body, false otherwise.
   */
  boolean hasBody() {
    return this.body.length() > 0;
  }
}
