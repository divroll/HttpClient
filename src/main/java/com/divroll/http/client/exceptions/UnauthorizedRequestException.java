package com.divroll.http.client.exceptions;

import com.divroll.http.client.HttpRequestException;

public class UnauthorizedRequestException extends HttpRequestException {
    public UnauthorizedRequestException(String message, int code) {
        super(message, code);
    }
    public UnauthorizedRequestException() {
    }
}
