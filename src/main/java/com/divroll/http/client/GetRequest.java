package com.divroll.http.client;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.http.client.*;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

public class GetRequest {

    static final Logger logger = Logger.getLogger(GetRequest.class.getName());

    private String url;
    private Multimap<String,String> headerMap;
    private Map<String, String> queryMap = null;
    private Map<String,Object> fields = null;

    private String authorization = null;
    private int TIMEOUT = 60000;

    public GetRequest(String url) {
        setUrl(url);
        headerMap = ArrayListMultimap.create();
    }

    public GetRequest header(String header, String value) {
        if(headerMap == null){
            headerMap = ArrayListMultimap.create();
        }
        if(value != null) {
            headerMap.put(header, value);
        }
        return this;
    }

    public GetRequest queryString(String name, String value){
        if(queryMap == null){
            queryMap = new LinkedHashMap<String,String>();
        }
        queryMap.put(name, value);
        return this;
    }

    public GetRequest basicAuth(String username, String password) {
        authorization = "Basic " + Base64.btoa(username + ":" + password);
        return this;
    }

    public Single<HttpResponse<String>> asString() {
        return Single.create(new SingleOnSubscribe<HttpResponse<String>>() {
            @Override
            public void subscribe(SingleEmitter<HttpResponse<String>> emitter) throws Exception {
                if(queryMap != null && !queryMap.isEmpty()){
                    url = url + "?";
                    url = url +  queries(queryMap);
                }
                RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
                requestBuilder.setTimeoutMillis(TIMEOUT);
                if(headerMap != null){
                    // Set default first
                    headerMap.put("Content-Type", "application/json");
                    headerMap.put("accept", "application/json");
                    for (Map.Entry<String,String> entry : headerMap.entries()) {
                        if(entry.getKey() != null && entry.getValue() != null
                                && !entry.getKey().isEmpty() && !entry.getValue().isEmpty()) {
                            requestBuilder.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                }
                if(authorization != null){
                    requestBuilder.setHeader("Authorization", authorization);
                }
                requestBuilder.sendRequest(null, new RequestCallback() {
                    public void onResponseReceived(Request request, Response response) {
                        String resp = response.getText();
                        int statusCode = response.getStatusCode();
                        String statusText = response.getStatusText();
                        emitter.onSuccess(new StringHttpResponse(statusCode, statusText, resp));
                    }
                    public void onError(Request request, Throwable exception) {
                        emitter.onError(exception);
                    }
                });
            }
        });
    }

    public Single<HttpResponse<InputStream>> asBinary() {
        return null;
    }

    public Single<HttpResponse<JsonNode>> asJson() {
        return Single.create(new SingleOnSubscribe<HttpResponse<JsonNode>>() {
            @Override public void subscribe(SingleEmitter<HttpResponse<JsonNode>> e) throws RequestException {
                if(queryMap != null && !queryMap.isEmpty()){
                    url = url + "?";
                    url = url +  queries(queryMap);
                }
                RequestBuilder requestBuilder = new RequestBuilder(RequestBuilder.GET, url);
                requestBuilder.setTimeoutMillis(TIMEOUT);
                if(headerMap != null){
                    // Set default first
                    headerMap.put("Content-Type", "application/json");
                    headerMap.put("accept", "application/json");
                    for (Map.Entry<String,String> entry : headerMap.entries()) {
                        if(entry.getKey() != null && entry.getValue() != null
                                && !entry.getKey().isEmpty() && !entry.getValue().isEmpty()) {
                            requestBuilder.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                }
                if(authorization != null){
                    requestBuilder.setHeader("Authorization", authorization);
                }
                requestBuilder.setCallback(new RequestCallback() {
                    @Override public void onResponseReceived(Request req, Response res) {
                        int statusCode = res.getStatusCode();
                        String statusText = res.getStatusText();
                        String resp = res.getText();
                        e.onSuccess(new JsonHttpResponse(statusCode, statusText, resp));
                    }
                    @Override public void onError(Request req, Throwable ex) {
                        e.onError(ex);
                    }
                });
                Request request = requestBuilder.send();
                e.setCancellable(request::cancel);
            }
        });
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    private String queries(Map<String,String> parmsRequest){
        StringBuilder sb = new StringBuilder();
        for ( String k: parmsRequest.keySet() ) {
            String vx = URL.encodeComponent( parmsRequest.get(k));
            if ( sb.length() > 0 ) {
                sb.append("&");
            }
            sb.append(k).append("=").append(vx);
        }
        return sb.toString();
    }

    public void setTimeout(int timeout) {
        this.TIMEOUT = timeout;
    }

    private static native void proxyXMLHttpRequestOpen() /*-{
        var proxied = $wnd.XMLHttpRequest.prototype.open;

        (function() {
            $wnd.XMLHttpRequest.prototype.open =
                function() {
                    arguments[2] = false;
                    return proxied.apply(this, [].slice.call(arguments));
                };
        })();
    }-*/;

}
