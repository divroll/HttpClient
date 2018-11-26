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
import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.http.client.*;
import com.google.gwt.user.client.Window;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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
public class HttpRequestWithBody {

    static final Logger logger = Logger.getLogger(HttpRequestWithBody.class.getName());

    private String url;
    private Multimap<String,String> headerMap;
    private Map<String, String> queryMap;
    private Map<String,Object> fields;
    private Object body = null;
    private RequestBuilder.Method method;
    private int TIMEOUT = 60000;
    private String authorization;

    public HttpRequestWithBody(String url, RequestBuilder.Method method) {
        setUrl(url);
        this.method = method;
        headerMap = ArrayListMultimap.create();
    }

    public HttpRequestWithBody header(String header, String value) {
        if(headerMap == null){
            headerMap = ArrayListMultimap.create();
        }
        if(value != null) {
            headerMap.put(header, value);
        }
        return this;
    }

    public HttpRequestWithBody body(Object body){
        this.body = body;
        return this;
    }

    public HttpRequestWithBody queryString(String name, String value){
        if(queryMap == null){
            queryMap = new LinkedHashMap<String,String>();
        }
        queryMap.put(name, value);
        return this;
    }

    public HttpRequestWithBody field(String name, String value){
        if(fields == null){
            fields = new LinkedHashMap<String,Object>();
        }
        fields.put(name, value);
        return this;
    }

    public HttpRequestWithBody basicAuth(String username, String password) {
        authorization = "Basic " + Base64.btoa(username + ":" + password);
        return this;
    }

    public Single<HttpResponse<String>> asString() {
        return Single.create(new SingleOnSubscribe<HttpResponse<String>>() {
            @Override
            public void subscribe(SingleEmitter<HttpResponse<String>> emitter) throws RequestException {
                if(queryMap != null && !queryMap.isEmpty()){
                    url = url + "?";
                    url = url +  queries(queryMap);
                }
                RequestBuilder b = new RequestBuilder(method, url);
                b.setTimeoutMillis(TIMEOUT);
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
                            b.setHeader(entry.getKey(), entry.getValue());
                        }
                    }
                }
                Object payload = body;
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
                    payload = sb.toString();
                    b.setHeader("Content-Type","application/x-www-form-urlencoded");
                }
                if(body != null){
                    if(body instanceof JavaScriptObject){
                        // TODO for Sending File
                    }
                }
                if(authorization != null){
                    b.setHeader("Authorization", authorization);
                }
                if(payload != null) {
                    String requestBody = String.valueOf(payload);
                    if(payload instanceof InputStream) {
                        try {
                            Window.alert("Payload is InputStream");
                            InputStream is = (InputStream) payload;
                            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                            int nRead;
                            byte[] data = new byte[1024];
                            while ((nRead = is.read(data, 0, data.length)) != -1) {
                                buffer.write(data, 0, nRead);
                            }
                            buffer.flush();
                            byte[] byteArray = buffer.toByteArray();
                            Window.alert("byteArray size=" + byteArray.length);
                            b.setHeader("Content-Type", "application/octet-stream");
                            requestBody = new String(byteArray, StandardCharsets.UTF_8);
                        } catch (Exception e) {
                            emitter.onError(e);
                        }
                    } else {
                        requestBody = String.valueOf(payload);
                    }
                    b.sendRequest(requestBody, new RequestCallback() {
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
                } else {
                    b.sendRequest("", new RequestCallback() {
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
            }
        });
    }

    public Single<HttpResponse<JsonNode>> asJson() {
        return Single.create(emitter -> {
            if(queryMap != null && !queryMap.isEmpty()){
                url = url + "?";
                url = url +  queries(queryMap);
            }
            RequestBuilder b = new RequestBuilder(method, url);
            b.setTimeoutMillis(TIMEOUT);
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
                        b.setHeader(entry.getKey(), entry.getValue());
                    }
                }
            }
            Object payload = body;
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
                payload = sb.toString();
                b.setHeader("Content-Type","application/x-www-form-urlencoded");
            }
            if(body != null){
                if(body instanceof JavaScriptObject){
                    // TODO for Sending File
                }
            }
            if(authorization != null){
                b.setHeader("Authorization", authorization);
            }
            if(payload != null) {
                String requestBody = String.valueOf(payload);
                if(payload instanceof InputStream) {
                    try {
                        Window.alert("Payload is InputStream");
                        InputStream is = (InputStream) payload;
                        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
                        int nRead;
                        byte[] data = new byte[1024];
                        while ((nRead = is.read(data, 0, data.length)) != -1) {
                            buffer.write(data, 0, nRead);
                        }
                        buffer.flush();
                        byte[] byteArray = buffer.toByteArray();
                        Window.alert("byteArray size=" + byteArray.length);
                        b.setHeader("Content-Type", "application/octet-stream");
                        requestBody = new String(byteArray, StandardCharsets.UTF_8);
                    } catch (Exception e) {
                        emitter.onError(e);
                    }
                } else {
                    requestBody = String.valueOf(payload);
                }
                b.sendRequest(requestBody, new RequestCallback() {
                    public void onResponseReceived(Request request, Response response) {
                        String resp = response.getText();
                        int statusCode = response.getStatusCode();
                        String statusText = response.getStatusText();
                        emitter.onSuccess(new JsonHttpResponse(statusCode, statusText, resp));
                    }
                    public void onError(Request request, Throwable exception) {
                        emitter.onError(exception);
                    }
                });
            } else {
                b.sendRequest("", new RequestCallback() {
                    public void onResponseReceived(Request request, Response response) {
                        String resp = response.getText();
                        int statusCode = response.getStatusCode();
                        String statusText = response.getStatusText();
                        emitter.onSuccess(new JsonHttpResponse(statusCode, statusText, resp));
                    }
                    public void onError(Request request, Throwable exception) {
                        emitter.onError(exception);
                    }
                });
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

}
