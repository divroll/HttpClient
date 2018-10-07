#HttpClient: A RX-based Fluent HTTP Client for GWT 

This library wraps GWT RequestBuilder into a fluent API. 

```
HttpResponse<JSONObject> reponse = HttpClient.post("http://httpbin.org/post")
  .queryString("name", "Mark")
  .field("last", "Polo")
  .asJson()
JSONObject jsonObject = response.getBody();
```

or

```
HttpResponse<String> reponse = HttpClient.post("http://httpbin.org/post")
  .queryString("name", "Mark")
  .field("last", "Polo")
  .asString()
String raw = response.getBody();
```

## Setup with Maven

```
mvn clean install
```

Add dependency

```
<dependency>
	<groupId>org.gwtproject</groupId>
	<artifactId>http-client</artifactId>
	<version>0-SNAPSHOT</version>
</dependency>
```