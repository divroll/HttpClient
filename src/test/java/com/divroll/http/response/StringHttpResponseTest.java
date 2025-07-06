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

import com.divroll.http.request.Header;
import org.junit.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;

public class StringHttpResponseTest {

    @Test
    public void StringHttpResponse_with_code_headers_and_body_should_be_created() {
        Integer code = 200;
        Set<Header> headers = new HashSet<>();
        headers.add(new Header("Content-Type", "application/json"));
        String body = "test";
        HttpResponse<String> response = new StringHttpResponse(code, headers, body);
        assertEquals(code, response.getCode());
        assertEquals(headers, response.getHeaders());
        assertEquals(body, response.getBody());
    }

    @Test
    public void StringHttpResponse_with_empty_body_should_return_false_for_hasBody() {
        Integer code = 200;
        Set<Header> headers = new HashSet<>();
        headers.add(new Header("Content-Type", "application/json"));
        String body = "";
        StringHttpResponse response = new StringHttpResponse(code, headers, body);
        assertFalse(response.hasBody());
    }

    @Test
    public void StringHttpResponse_with_non_empty_body_should_return_true_for_hasBody() {
        Integer code = 200;
        Set<Header> headers = new HashSet<>();
        headers.add(new Header("Content-Type", "application/json"));
        String body = "test";
        StringHttpResponse response = new StringHttpResponse(code, headers, body);
        assertTrue(response.hasBody());
    }
}
