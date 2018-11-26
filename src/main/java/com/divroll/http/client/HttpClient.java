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

import com.google.gwt.http.client.RequestBuilder;

/**
 *
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
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
