package com.divroll.http.client.exceptions;

import com.divroll.http.client.HttpRequestException;

public class ClientErrorRequestException extends HttpRequestException {
    public ClientErrorRequestException(String message, int code) {
        super(message, code);
    }
    public ClientErrorRequestException() {
    }
}
