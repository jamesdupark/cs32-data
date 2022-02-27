package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;
import edu.brown.cs.student.main.API.APIRequestBuilder;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.http.HttpRequest;

public class APIRequestBuilderTest {
  APIRequestBuilder basicBuilder;
  String[] getUrlParams, header, postBody;
  String url = "https://studentinfoapi.herokuapp.com/get-active";

  @Before
  public void init() {
    basicBuilder = new APIRequestBuilder(url);
    getUrlParams = new String[]{"auth", "jpark236", "key", "hello"};
    header = new String[]{"x-api-key", "asdf"};
    postBody = new String[]{"auth", "jpark236", "dummy", "value"};
  }

  @Test
  public void testGet() {
    String getUrl = url + "?auth=jpark236&key=hello";
    HttpRequest getExpect = HttpRequest.newBuilder(URI.create(getUrl))
        .header("x-api-key", "asdf")
        .build();
    assertEquals(getExpect, basicBuilder.get(getUrlParams, header));
  }

  @Test
  public void testPost() {
    String bodyString = "{\"auth\": \"jpark236\", \"dummy\": \"value\"}";
    HttpRequest postExpect = HttpRequest.newBuilder(URI.create(url))
        .POST(HttpRequest.BodyPublishers.ofString(bodyString))
        .header("x-api-key", "asdf")
        .build();
    assertEquals(postExpect, basicBuilder.post(header, postBody));
  }
}
