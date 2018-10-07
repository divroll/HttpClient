package com.divroll.http.client.exceptions;

import com.divroll.http.client.HttpRequestException;

public class ServerErrorRequestException extends HttpRequestException {
    public ServerErrorRequestException(String message, int code) {
        super(message, code);
    }
    public ServerErrorRequestException() {
    }
}
