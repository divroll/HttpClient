package com.divroll.http.client;

import com.google.gwt.http.client.RequestException;

public class HttpRequestException extends RequestException {
    private int code;
    public HttpRequestException(String message, int code){
        super(message);
        this.code = code;
    }
    public HttpRequestException() {
    }

    public int getCode() {
        return code;
    }
}
