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

import com.google.gwt.core.client.JavaScriptObject;

/**
 *
 * @author <a href="mailto:kerby@divroll.com">Kerby Martino</a>
 * @version 0-SNAPSHOT
 * @since 0-SNAPSHOT
 */
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
