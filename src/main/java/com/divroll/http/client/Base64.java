package com.divroll.http.client;

public class Base64 {
    public static native String btoa(String a) /*-{
        return window.btoa(a);
    }-*/;
}
