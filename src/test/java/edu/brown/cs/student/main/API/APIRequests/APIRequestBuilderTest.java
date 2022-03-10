package edu.brown.cs.student.main.API.APIRequests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.http.HttpRequest;

public class APIRequestBuilderTest {
  private APIRequestBuilder basicBuilder;
  private String[] getUrlParams, header, postBody, badInput;
  private final String url = "https://studentinfoapi.herokuapp.com/get-active";

  @Before
  public void init() {
    basicBuilder = new APIRequestBuilder(url);
    getUrlParams = new String[]{"auth", "jpark236", "key", "hello"};
    header = new String[]{"x-api-key", "asdf", "hi", "hello"};
    postBody = new String[]{"auth", "jpark236", "dummy", "value"};
    badInput = new String[]{"one"};
  }

  @Test
  public void testGet() {
    String getUrl = url + "?auth=jpark236&key=hello";
    HttpRequest getExpect = HttpRequest.newBuilder(URI.create(getUrl))
        .header("x-api-key", "asdf")
        .header("hi", "hello")
        .build();
    HttpRequest nullHeader = HttpRequest.newBuilder(URI.create(getUrl))
        .build();
    HttpRequest nullUrl = HttpRequest.newBuilder(URI.create(url))
        .header("x-api-key", "asdf")
        .header("hi", "hello")
        .build();
    HttpRequest bothNull = HttpRequest.newBuilder(URI.create(url))
        .build();
    assertEquals(getExpect, basicBuilder.get(getUrlParams, header));
    assertEquals(nullHeader, basicBuilder.get(getUrlParams, null));
    assertEquals(nullUrl, basicBuilder.get(null, header));
    assertEquals(bothNull, basicBuilder.get(null, null));
    assertThrows(IllegalArgumentException.class, () -> basicBuilder.get(badInput, header));
    assertThrows(IllegalArgumentException.class, () -> basicBuilder.get(getUrlParams, badInput));
  }

  @Test
  public void testPost() {
    String bodyString = "{\"auth\": \"jpark236\", \"dummy\": \"value\"}";
    HttpRequest postExpect = HttpRequest.newBuilder(URI.create(url))
        .POST(HttpRequest.BodyPublishers.ofString(bodyString))
        .header("x-api-key", "asdf")
        .header("hi", "hello")
        .build();
    HttpRequest nullBody = HttpRequest.newBuilder(URI.create(url))
        .POST(HttpRequest.BodyPublishers.ofString(""))
        .header("x-api-key", "asdf")
        .header("hi", "hello")
        .build();
    HttpRequest nullHeader = HttpRequest.newBuilder(URI.create(url))
        .POST(HttpRequest.BodyPublishers.ofString(bodyString))
        .build();
    HttpRequest bothNull = HttpRequest.newBuilder(URI.create(url))
        .POST(HttpRequest.BodyPublishers.ofString(""))
        .build();
    assertEquals(postExpect, basicBuilder.post(header, postBody));
    assertEquals(nullBody, basicBuilder.post(header, null));
    assertEquals(nullHeader, basicBuilder.post(null, postBody));
    assertEquals(bothNull, basicBuilder.post(null, null));
    assertThrows(IllegalArgumentException.class, () -> basicBuilder.post(badInput, postBody));
    assertThrows(IllegalArgumentException.class, () -> basicBuilder.post(header, badInput));
  }
}
