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

import com.divroll.http.exception.CannotCastValueException;
import org.junit.Test;

import static org.junit.Assert.*;

public class HttpBodyTest {

    @Test
    public void HttpBody_with_null_value_should_be_created() {
        HttpBody<String> body = new HttpStringBody(null);
        assertNull(body.getValue());
    }

    @Test
    public void HttpBody_with_non_null_value_should_be_created() {
        String value = "test";
        HttpBody<String> body = new HttpStringBody(value);
        assertNotNull(body.getValue());
        assertEquals(value, body.getValue());
    }

    @Test
    public void HttpBody_with_same_value_should_be_equal() {
        String value = "test";
        HttpBody<String> body1 = new HttpStringBody(value);
        HttpBody<String> body2 = new HttpStringBody(value);
        assertEquals(body1, body2);
    }

    @Test
    public void HttpBody_with_different_value_should_not_be_equal() {
        String value1 = "test1";
        String value2 = "test2";
        HttpBody<String> body1 = new HttpStringBody(value1);
        HttpBody<String> body2 = new HttpStringBody(value2);
        assertNotEquals(body1, body2);
    }

    @Test
    public void HttpBody_with_null_value_should_have_zero_hash_code() {
        HttpBody<String> body = new HttpStringBody(null);
        assertEquals(0, body.hashCode());
    }

    @Test
    public void HttpBody_with_non_null_value_should_have_non_zero_hash_code() {
        String value = "test";
        HttpBody<String> body = new HttpStringBody(value);
        assertNotEquals(0, body.hashCode());
    }

    @Test
    public void HttpBody_with_null_value_should_return_null_string_representation() {
        HttpBody<String> body = new HttpStringBody(null);
        assertEquals("null", body.toString());
    }

    @Test
    public void HttpBody_with_non_null_value_should_return_string_representation() {
        String value = "test";
        HttpBody<String> body = new HttpStringBody(value);
        assertEquals(value, body.toString());
    }

    @Test
    public void HttpBody_should_be_assignable_to_itself() {
        String value = "test";
        HttpBody<String> body = new HttpStringBody(value);
        assertTrue(body.isA(HttpBody.class));
    }

    @Test
    public void HttpBody_should_be_castable_to_itself() throws Exception {
        String value = "test";
        HttpBody<String> body = new HttpStringBody(value);
        HttpBody<?> castedBody = body.asA(HttpBody.class);
        assertNotNull(castedBody);
        assertEquals(body, castedBody);
    }

    @Test
    public void HttpBody_should_not_be_assignable_to_incompatible_type() {
        String value = "test";
        HttpBody<String> body = new HttpStringBody(value);
        assertFalse(body.isA(HttpJSONObjectBody.class));
    }

    @Test(expected = CannotCastValueException.class)
    public void HttpBody_should_throw_exception_when_casting_to_incompatible_type() throws CannotCastValueException{
        String value = "test";
        HttpBody<String> body = new HttpStringBody(value);
        body.asA(HttpJSONObjectBody.class);
    }
}
