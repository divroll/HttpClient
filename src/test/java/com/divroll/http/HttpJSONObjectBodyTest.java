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
package com.divroll.http;

import org.json.JSONObject;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpJSONObjectBodyTest {

    @Test
    public void HttpJSONObjectBody_with_null_value_should_be_created() {
        HttpJSONObjectBody body = new HttpJSONObjectBody(null);
        assertNull(body.getValue());
    }

    @Test
    public void HttpJSONObjectBody_with_non_null_value_should_be_created() {
        JSONObject jsonObject = new JSONObject("{\"key\":\"value\"}");
        HttpJSONObjectBody body = new HttpJSONObjectBody(jsonObject);
        assertNotNull(body.getValue());
        assertEquals(jsonObject, body.getValue());
    }

    @Test
    public void HttpJSONObjectBody_with_same_value_should_be_equal() {
        JSONObject jsonObject = new JSONObject("{\"key\":\"value\"}");
        HttpJSONObjectBody body1 = new HttpJSONObjectBody(jsonObject);
        HttpJSONObjectBody body2 = new HttpJSONObjectBody(jsonObject);
        assertEquals(body1, body2);
    }

    @Test
    public void HttpJSONObjectBody_with_different_value_should_not_be_equal() {
        JSONObject jsonObject1 = new JSONObject("{\"key\":\"value1\"}");
        JSONObject jsonObject2 = new JSONObject("{\"key\":\"value2\"}");
        HttpJSONObjectBody body1 = new HttpJSONObjectBody(jsonObject1);
        HttpJSONObjectBody body2 = new HttpJSONObjectBody(jsonObject2);
        assertNotEquals(body1, body2);
    }

    @Test
    public void HttpJSONObjectBody_with_null_value_should_have_zero_hash_code() {
        HttpJSONObjectBody body = new HttpJSONObjectBody(null);
        assertEquals(0, body.hashCode());
    }

    @Test
    public void HttpJSONObjectBody_with_non_null_value_should_have_non_zero_hash_code() {
        JSONObject jsonObject = new JSONObject("{\"key\":\"value\"}");
        HttpJSONObjectBody body = new HttpJSONObjectBody(jsonObject);
        assertNotEquals(0, body.hashCode());
    }

    @Test
    public void HttpJSONObjectBody_with_null_value_should_return_null_string_representation() {
        HttpJSONObjectBody body = new HttpJSONObjectBody(null);
        assertEquals("null", body.toString());
    }

    @Test
    public void HttpJSONObjectBody_with_non_null_value_should_return_json_string_representation() {
        JSONObject jsonObject = new JSONObject("{\"key\":\"value\"}");
        HttpJSONObjectBody body = new HttpJSONObjectBody(jsonObject);
        assertEquals(jsonObject.toString(), body.toString());
    }

    @Test
    public void HttpJSONObjectBody_should_be_assignable_to_HttpBody() {
        JSONObject jsonObject = new JSONObject("{\"key\":\"value\"}");
        HttpJSONObjectBody body = new HttpJSONObjectBody(jsonObject);
        assertTrue(body.isA(HttpBody.class));
    }

    @Test
    public void HttpJSONObjectBody_should_be_castable_to_HttpBody() throws Exception {
        JSONObject jsonObject = new JSONObject("{\"key\":\"value\"}");
        HttpJSONObjectBody body = new HttpJSONObjectBody(jsonObject);
        HttpBody<?> castedBody = body.asA(HttpBody.class);
        assertNotNull(castedBody);
        assertEquals(body, castedBody);
    }
}
