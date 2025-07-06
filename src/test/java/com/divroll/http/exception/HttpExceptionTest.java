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
package com.divroll.http.exception;

import org.junit.Test;

import static org.junit.Assert.*;

public class HttpExceptionTest {

    @Test
    public void HttpException_should_be_instance_of_Exception() {
        HttpException exception = new HttpException();
        assertTrue(exception instanceof Exception);
    }

    @Test
    public void HttpException_with_message_should_have_correct_message() {
        String message = "Test message";
        HttpException exception = new HttpException(message);
        assertEquals(message, exception.getMessage());
    }

    @Test
    public void HttpException_with_cause_should_have_correct_cause() {
        Throwable cause = new Throwable("Cause");
        HttpException exception = new HttpException(cause);
        assertEquals(cause, exception.getCause());
    }

    @Test
    public void HttpException_with_message_and_cause_should_have_correct_message_and_cause() {
        String message = "Test message";
        Throwable cause = new Throwable("Cause");
        HttpException exception = new HttpException(message, cause);
        assertEquals(message, exception.getMessage());
        assertEquals(cause, exception.getCause());
    }
}
