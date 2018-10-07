package com.divroll.http.client.exceptions;

import com.divroll.http.client.HttpRequestException;

public class BadRequestException extends HttpRequestException {
    public BadRequestException(String message, int code) {
        super(message, code);
    }
    public BadRequestException() {
    }
}
