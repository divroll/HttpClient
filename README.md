# HttpClient
A fluent HTTP client for Java/TeaVM.

#### Example Usage

```java
PostRequest request =
    new PostRequest("http://httpbin.org/post", new HttpStringBody("hello world"));
HttpResponse<InputStream> response = HttpClient.post(request);
InputStream body = response.getBody();
```

or

```java
InputStream inputStream = createStream();
PostRequest request =
    new PostRequest("http://httpbin.org/post", new HttpInputStreamBody(inputStream));
HttpResponse<String> response = HttpClient.post(request);
String body = response.getBody();
```

#### Setup with Maven

```bash
mvn clean install
```

#### Add dependency

```xml
<dependency>
   <groupId>com.divroll</groupId>
   <artifactId>httpclient</artifactId>
   <version>SNAPSHOT</version>
</dependency>
```


#### Run test

```
mvn test -Dteavm.junit.target=target/js-tests -Dteavm.junit.js.runner=htmlunit -Dteavm.junit.js.threads=2
```

