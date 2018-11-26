/*
 * Divroll, Platform for Hosting Static Sites
 * Copyright 2018, Divroll, and individual contributors
 * as indicated by the @author tags. See the copyright.txt file in the
 * distribution for a full listing of individual contributors.
 *
 * This is free software; you can redistribute it and/or modify it
 * under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of
 * the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this software; if not, write to the Free
 * Software Foundation, Inc., 51 Franklin St, Fifth Floor, Boston, MA
 * 02110-1301 USA, or see the FSF site: http://www.fsf.org.
 */
package com.divroll.http.client;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Window;
import elemental2.dom.XMLHttpRequest;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Logger;

/**
 *
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
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
        return Single.create(emitter -> {
            if(queryMap != null && !queryMap.isEmpty()){
                url = url + "?";
                url = url +  queries(queryMap);
            }
            XMLHttpRequest xhr = new XMLHttpRequest();
            xhr.open("GET", url);
//            b.setTimeoutMillis(TIMEOUT);
            if(headerMap != null){
                // Check first if Content-Type and accept headers are already set else set defaults
                boolean hasContentType = false;
                boolean hasAccept = false;
                for (Map.Entry<String,String> entry : headerMap.entries()) {
                    if(entry.getKey() != null && entry.getValue() != null
                            && !entry.getKey().isEmpty() && !entry.getValue().isEmpty()) {
                        if(entry.getKey().equals("Content-Type")) {
                            hasContentType = true;
                        } else if (entry.getKey().equals("accept")) {
                            hasAccept = true;
                        }
                    }
                }
                if(!hasAccept) {
                    headerMap.put("accept", "application/json");
                }
                if(!hasContentType) {
                    headerMap.put("Content-Type", "application/json");
                }
                for (Map.Entry<String,String> entry : headerMap.entries()) {
                    if(entry.getKey() != null && entry.getValue() != null
                            && !entry.getKey().isEmpty() && !entry.getValue().isEmpty()) {
                        xhr.setRequestHeader(entry.getKey(), entry.getValue());
                    }
                }
            }
            if(fields != null && !fields.isEmpty()){
                StringBuilder sb = new StringBuilder();
                Iterator<Map.Entry<String,Object>> it = fields.entrySet().iterator();
                while(it.hasNext()){
                    Map.Entry<String,Object> entry = it.next();
                    if(entry.getValue() instanceof String){
                        if(!it.hasNext()){
                            sb.append(entry.getKey()).append("=").append(URL.encodeComponent((String.valueOf(entry.getValue()))));
                        } else {
                            sb.append(entry.getKey()).append("=").append(URL.encodeComponent((String.valueOf(entry.getValue())))).append("&");
                        }
                    }
                }
                xhr.setRequestHeader("Content-Type","application/x-www-form-urlencoded");
            }
            if(authorization != null){
                xhr.setRequestHeader("Authorization", authorization);
            }
            xhr.onprogress = p0 -> {
                double percentage = 100 - ( ( (p0.total - p0.loaded) / p0.total) * 100 );
                if(!Double.isInfinite(percentage)) {
                    Double.valueOf(percentage).longValue();
                }
            };
            xhr.onerror = p0 -> {
                // TODO: Check actual error
                emitter.onError(new HttpRequestException());
                return null;
            };
            xhr.onreadystatechange = p0 -> {
                if(xhr.readyState == XMLHttpRequest.DONE) {
                    // Not called
                }
                return null;
            };
            xhr.onload = p0 -> {
                Window.alert(xhr.statusText);
                Window.alert(xhr.status + "");
                Window.alert(xhr.responseText + "");
                emitter.onSuccess(new InputStreamHttpResponse(xhr.status, xhr.statusText, new ByteArrayInputStream(xhr.responseText.getBytes())));
            };
            xhr.send();
        });
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
