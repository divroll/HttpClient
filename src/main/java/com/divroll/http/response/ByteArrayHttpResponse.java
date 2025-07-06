package com.divroll.http.response;

import com.divroll.http.request.Header;

import java.util.Set;

public class ByteArrayHttpResponse implements HttpResponse<byte[]> {
    private final int status;
    private final Set<Header> headers;
    private final byte[] body;

    public ByteArrayHttpResponse(int status, Set<Header> headers, byte[] body) {
        this.status = status;
        this.headers = headers;
        this.body = body;
    }

    @Override
    public Integer getCode() {
        return status;
    }

    @Override
    public Set<Header> getHeaders() {
        return headers;
    }

    @Override
    public byte[] getBody() {
        return body;
    }
}
