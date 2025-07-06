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

import org.teavm.jso.JSBody;

/**
 * Utility class for Base64 encoding.
 * This class provides a method to encode a string into Base64 format using the `btoa` function.
 */
public class Base64 {
    @JSBody(params = {"a"}, script = "return btoa(a);")
    public static native String btoa(String a);

    @JSBody(params = {"a"}, script = "return btoa(String.fromCharCode.apply(null, new Uint8Array(a)));")
    public static native String btoa(byte[] a);
}
