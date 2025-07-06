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

import org.junit.Test;

import static org.junit.Assert.*;

public class HeaderTest {

    @Test
    public void Header_with_name_and_value_should_be_created() {
        String name = "Content-Type";
        String value = "application/json";
        Header header = new Header(name, value);
        assertEquals(name, header.getName());
        assertEquals(value, header.getValue());
    }

    @Test
    public void Header_with_same_name_and_value_should_be_equal() {
        Header header1 = new Header("Content-Type", "application/json");
        Header header2 = new Header("Content-Type", "application/json");
        assertEquals(header1, header2);
    }

    @Test
    public void Header_with_different_name_should_not_be_equal() {
        Header header1 = new Header("Content-Type", "application/json");
        Header header2 = new Header("Accept", "application/json");
        assertNotEquals(header1, header2);
    }

    @Test
    public void Header_with_different_value_should_not_be_equal() {
        Header header1 = new Header("Content-Type", "application/json");
        Header header2 = new Header("Content-Type", "text/plain");
        assertNotEquals(header1, header2);
    }

    @Test
    public void Header_with_null_name_and_value_should_be_equal_to_another_null_name_and_value() {
        Header header1 = new Header(null, null);
        Header header2 = new Header(null, null);
        assertEquals(header1, header2);
    }

    @Test
    public void Header_with_null_name_and_value_should_have_zero_hash_code() {
        Header header = new Header(null, null);
        assertEquals(0, header.hashCode());
    }

    @Test
    public void Header_with_non_null_name_and_value_should_have_non_zero_hash_code() {
        Header header = new Header("Content-Type", "application/json");
        assertNotEquals(0, header.hashCode());
    }
}
