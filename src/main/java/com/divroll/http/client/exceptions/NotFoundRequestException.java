package com.divroll.http.client.exceptions;

import com.divroll.http.client.HttpRequestException;

public class NotFoundRequestException extends HttpRequestException {
    public NotFoundRequestException(String message, int code) {
        super(message, code);
    }
    public NotFoundRequestException() {
    }
}
