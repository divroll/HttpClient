package com.divroll.http.client;

import com.google.gwt.http.client.RequestBuilder;

public class HttpClient {
    public static GetRequest get(String url){
        return new GetRequest(url);
    };
    public static GetRequest head(String url){
        return new GetRequest(url);
    };
    public static HttpRequestWithBody post(String url){
        return new HttpRequestWithBody(url, RequestBuilder.POST);
    };
    public static HttpRequestWithBody put(String url){
        return new HttpRequestWithBody(url, RequestBuilder.PUT);
    };
    // TODO Not supported by RequestBuilder, use Element2 instead
    //public static HttpRequestWithBody patch(String url){
    //    return new HttpRequestWithBody(url, RequestBuilder.PATCH);
    //};
    //public static HttpRequestWithBody options(String url){
    //    return new HttpRequestWithBody(url, RequestBuilder.OPTIONS);
    //};
    public static HttpRequestWithBody delete(String url){
        return new HttpRequestWithBody(url, RequestBuilder.DELETE);
    };
}
