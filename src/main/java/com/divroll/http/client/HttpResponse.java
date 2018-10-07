package com.divroll.http.client;

public interface HttpResponse<T> {
    public T getBody();
    public int getStatus();
    public String getStatusText();
}
