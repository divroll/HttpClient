package com.divroll.http.client;

import com.google.gwt.core.client.JavaScriptObject;

public class JavaScriptObjecttHttpResponse implements HttpResponse<JavaScriptObject> {

    private int status;
    private String statusText;
    private JavaScriptObject javaScriptObject;

    public JavaScriptObjecttHttpResponse(int status, String statusText , JavaScriptObject javaScriptObject) {
        this.status = status;
        this.statusText = statusText;
        this.javaScriptObject = javaScriptObject;
    }

    @Override
    public JavaScriptObject getBody() {
        return javaScriptObject;
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
