package com.divroll.http.client;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.json.client.JSONString;
import com.google.gwt.junit.client.GWTTestCase;
import com.google.gwt.user.client.Window;
import io.reactivex.Single;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.FixMethodOrder;
import org.junit.runners.MethodSorters;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.logging.Logger;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class HttpClientTest extends GWTTestCase {

    static final Logger logger = Logger.getLogger(HttpClientTest.class.getName());

    @Override
    public String getModuleName() {
        return "com.divroll.http.HttpClient";
    }

    public void testGet() throws Exception{
        Single<HttpResponse<JsonNode>> singleResponse = HttpClient.get("https://httpbin.org/get")
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .asJson();
        singleResponse.subscribe(response -> {
            Window.alert(response.getBody().toString());
            JSONObject json = response.getBody().getObject();
            String url = json.getString("url");
            JSONObject headers = json.getJSONObject("headers");
            assertNotNull(json);
            assertNotNull(url);
            assertNotNull(headers);
            Window.alert("Test Done");
            finishTest();
        });
        delayTestFinish(1000);
    }

    public void testPostFormField() throws Exception{
        Single<HttpResponse<JsonNode>> singleResponse = HttpClient.post("https://httpbin.org/post")
                .header("accept", "application/json")
                .header("Content-Type", "application/json")
                .queryString("name", "Mark")
                .field("middle", "O")
                .field("last", "Polo")
                .asJson();
        singleResponse.subscribe(response -> {
            Window.alert(response.toString());
            JSONObject json = response.getBody().getObject();
            String url = json.getString("url");
            JSONArray headers = json.getJSONArray("headers");
            JSONObject args = json.getJSONObject("args");
            assertNotNull(json);
            assertNotNull(url);
            assertNotNull(headers);
            assertNotNull(args);
            String name = args.getString("name");
            assertEquals("Mark", name);
        });
    }

    public void testPostJson() throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("hello", new JSONString("world"));

        Single<HttpResponse<JsonNode>> singleResponse = HttpClient.post("https://httpbin.org/post")
                //.header("accept", "application/json")
                //.header("Content-Type", "application/json")
                .queryString("name", "Mark")
                .body(payload.toString())
                .asJson();

        singleResponse.subscribe(response -> {
            Window.alert(response.toString());
            JSONObject json = response.getBody().getObject();
            String url = json.getString("url");
            JSONObject headers = json.getJSONObject("headers");
            JSONObject jsonField = json.getJSONObject("json");

            assertNotNull(json);
            assertNotNull(url);
            assertNotNull(headers);
            assertNotNull(jsonField);

            String accept = headers.getString("Accept");
            String contentType = headers.getString("Content-Type");
            String hello = jsonField.getString("hello");

            assertEquals("application/json", accept);
            assertEquals("application/json", contentType);
            assertEquals("world", hello);
        });
    }

    public void testPut() throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("hello", new JSONString("world"));

        Single<HttpResponse<JsonNode>> singleResponse = HttpClient.put("https://httpbin.org/put")
                .queryString("name", "Mark")
                .body(payload.toString())
                .basicAuth("john", "doe")
                .asJson();

        singleResponse.subscribe(response -> {
            Window.alert(response.toString());
            JSONObject json = response.getBody().getObject();
            String url = json.getString("url");
            JSONObject headers = json.getJSONObject("headers");
            JSONObject jsonField = json.getJSONObject("json");

            assertNotNull(json);
            assertNotNull(url);
            assertNotNull(headers);
            assertNotNull(jsonField);

            String accept = headers.getString("Accept");
            String authorization = headers.getString("Authorization");
            String contentType = headers.getString("Content-Type");
            String hello = jsonField.getString("hello");

            assertEquals("application/json", accept);
            assertEquals("application/json", contentType);
            assertEquals("world", hello);

            String actual = "Basic " + Base64.btoa("john" + ":" + "doe");
            assertEquals(actual, authorization);
        });

    }

    public void testDelete() throws Exception {
        JSONObject payload = new JSONObject();
        payload.put("hello", new JSONString("world"));

        Single<HttpResponse<JsonNode>> singleResponse = HttpClient.delete("https://httpbin.org/delete")
                .queryString("name", "Mark")
                .body(payload.toString())
                .basicAuth("john", "doe")
                .asJson();

        singleResponse.subscribe(response -> {
            Window.alert(response.toString());
            JSONObject json = response.getBody().getObject();
            String url = json.getString("url");
            JSONObject headers = json.getJSONObject("headers");

            assertNotNull(json);
            assertNotNull(url);
            assertNotNull(headers);

            String accept = headers.getString("Accept");
            String authorization = headers.getString("Authorization");
            String contentType = headers.getString("Content-Type");

            assertEquals("application/json", accept);
            assertEquals("application/json", contentType);

            String actual = "Basic " + Base64.btoa("john" + ":" + "doe");
            assertEquals(actual, authorization);
        });
    }

    public void testClientError() {
        Single<HttpResponse<JsonNode>> response = HttpClient.get("https://httpbin.org/status/400").asJson();
        response.subscribe(response1 -> {
            if(response1.getStatus() == 400) {
                finishTest();
            } else {
                fail();
            }
        });
        delayTestFinish(1000);
    }

    public void testServerError() {
        Single<HttpResponse<JsonNode>> response = HttpClient.get("https://httpbin.org/status/500").asJson();
        response.subscribe(response1 -> {
            if(response1.getStatus() == 500) {
                finishTest();
            } else {
                fail();
            }
        });
        delayTestFinish(1000);
    }

    public void testBlockingGet() {
//        Single<HttpResponse<JsonNode>> responseSingle = HttpClient.get("https://httpbin.org/get").asJson();
//        HttpResponse<JsonNode> response = responseSingle.blockingGet();
//        Window.alert(response.getBody().toString());
    }

    public void testBinaryGet() {
        Single<HttpResponse<InputStream>> response
                = HttpClient.get("https://cors-anywhere.herokuapp.com/https://i.imgur.com/qxOJUDB.jpg").asBinary();
        response.subscribe(response1 -> {

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();
            int nRead;
            byte[] data = new byte[1024];
            while ((nRead = response1.getBody().read(data, 0, data.length)) != -1) {
                buffer.write(data, 0, nRead);
            }
            buffer.flush();
            byte[] byteArray = buffer.toByteArray();

            Window.alert("length=" + byteArray.length);
            Window.alert(new String(byteArray, StandardCharsets.UTF_8));
            finishTest();
        }, error -> {
            Window.alert(error.toString());
            fail();
        });
        delayTestFinish(6000);
    }

    public void testBinaryPost() {
        Single<HttpResponse<InputStream>> response
                = HttpClient.get("https://cors-anywhere.herokuapp.com/https://i.imgur.com/qxOJUDB.jpg").asBinary();
        response.subscribe(response1 -> {
            InputStream inputStream = response1.getBody();
            Single<HttpResponse<String>> postRequest
                    = HttpClient.post("https://httpbin.org/post")
                    .body(inputStream)
                    .asString();
            postRequest.subscribe(postresponse -> {
                Window.alert("POST RESPONSE=" + postresponse.getBody());
                //FileSaver.saveFileAs("D:/test.txt", postresponse.getBody(), "text/plain");
                finishTest();
            });
        }, error -> {
            Window.alert(error.toString());
            fail();
        });
        delayTestFinish(6000);
    }

    public static native String createBlobUrl(JavaScriptObject blob) /*-{
        var url = $wnd.URL.createObjectURL(blob);
        return url;
    }-*/;


}
