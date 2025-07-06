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
package com.divroll.http.util;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.teavm.junit.SkipJVM;
import org.teavm.junit.TeaVMTestRunner;

import static org.junit.Assert.*;

@RunWith(TeaVMTestRunner.class)
@SkipJVM
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class Base64Test {

    @Test
    public void Base64_btoa_should_encode_string_correctly() {
        String input = "test";
        String expected = "dGVzdA==";
        String result = Base64.btoa(input);
        assertEquals(expected, result);
    }

    @Test
    public void Base64_btoa_should_encode_byte_array_correctly() {
        byte[] input = "test".getBytes();
        String expected = "dGVzdA==";
        String result = Base64.btoa(input);
        assertEquals(expected, result);
    }
}
