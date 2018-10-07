package com.divroll.http.client;

public class StringHttpResponse implements HttpResponse<String> {

    private int status;
    private String statusText;
    private String rawBody;

    public StringHttpResponse(int status, String statusText ,String rawBody) {
        this.status = status;
        this.statusText = statusText;
        this.rawBody = rawBody;
    }

    @Override
    public String getBody() {
        return rawBody;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getStatusText() {
        return statusText;
    }

    public void setStatusText(String statusText) {
        this.statusText = statusText;
    }
}
