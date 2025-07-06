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
import org.json.JSONObject;

public class JSONObjectHttpResponse implements HttpResponse<JSONObject> {

  private Integer code;
  private Set<Header> headers;
  private JSONObject body;

  public JSONObjectHttpResponse(Integer code, Set<Header> headers, JSONObject body) {
    this.code = code;
    this.headers = headers;
    this.body = body;
  }

  @Override public Integer getCode() {
    return code;
  }

  @Override public Set<Header> getHeaders() {
    return headers;
  }

  @Override public JSONObject getBody() {
    return body;
  }
}
