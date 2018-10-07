package com.divroll.http.client;

public class JsonHttpResponse implements HttpResponse<JsonNode> {

    private int status;
    private String statusText;
    private String rawBody;

    public JsonHttpResponse(int status, String statusText, String rawBody) {
        this.status = status;
        this.statusText = statusText;
        this.rawBody = rawBody;
    }

    @Override
    public JsonNode getBody() {
        if(rawBody != null && !rawBody.isEmpty()) {
            JsonNode jsonNode = new JsonNode(rawBody);
            return jsonNode;
        }
        return null;
    }

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getStatusText() {
        return null;
    }
}
