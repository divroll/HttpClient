package com.divroll.http.client;

import java.io.InputStream;

public class InputStreamHttpResponse implements HttpResponse<InputStream> {

    private int status;
    private String statusText;
    private InputStream inputStream;

    public InputStreamHttpResponse(int status, String statusText, InputStream inputStream) {
        this.inputStream = inputStream;
        this.status = status;
        this.statusText = statusText;
    }

    @Override
    public InputStream getBody() {
        return inputStream;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getStatusText() {
        return statusText;
    }
}
