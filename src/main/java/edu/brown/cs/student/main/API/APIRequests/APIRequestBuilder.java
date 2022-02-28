package edu.brown.cs.student.main.API.APIRequests;

import java.net.URI;
import java.net.http.HttpRequest;

/**
 * Class that builds HTTPRequest objects with the given parameters.
 */
public class APIRequestBuilder {
  /**
   * URL of the endpoint to make the request for.
   */
  private final String url;

  /**
   * Constructor for APIRequestBuilders.
   * @param url URL of the endpoint to build the request for.
   */
  public APIRequestBuilder(String url) {
    this.url = url;
  }

  /**
   * Creates a GET request HTTPRequest object.
   * @param urlParams string array of url param names and values
   * @param headers string array of header param names and values.
   * @return HTTPRequest object to this APIRequestBuilder's endpoint with the
   * given headers.
   * @throws IllegalArgumentException when encountering an issue with the headers or url params.
   */
  public HttpRequest get(String[] urlParams, String[] headers) throws IllegalArgumentException {
    try {
      URI getUrl = URI.create(url);
      if (urlParams != null) { // no url params provided
        assert urlParams.length % 2 == 0 : "Length of url params must be even.";
        getUrl = addUrlParams(urlParams); // add url params to the request url
      }

      HttpRequest.Builder builder = HttpRequest.newBuilder(getUrl);
      if (headers != null) { // no header provided
        assert headers.length % 2 == 0 : "Length of headers must be even.";
        builder = addHeaders(headers, builder);
      }
      return builder.build();
    } catch (IllegalArgumentException iex) {
      throw new IllegalArgumentException("ERROR: invalid URL");
    } catch (AssertionError ae) {
      throw new IllegalArgumentException("ERROR:" + ae.getMessage());
    }
  }

  /**
   * Creates a POST request HTTPRequest object.
   * @param headers List of alternating header names and header values.
   * @param body body of the POST request in the format {headerName:value}
   * @return HTTPRequest object to this APIRequestBuilder's endpoint with the
   * given headers.
   * @throws IllegalArgumentException when encountering an issue with the headers or body.
   */
  public HttpRequest post(String[] headers, String[] body) throws IllegalArgumentException {
    try {
      String bodyString = "";
      if (body != null) {
        assert body.length % 2 == 0 : "Length of body must be even.";
        bodyString = buildBodyString(body);
      }
      HttpRequest.Builder builder = HttpRequest.newBuilder(URI.create(url))
          .POST(HttpRequest.BodyPublishers.ofString(bodyString));
      if (headers != null) { // no headers provided
        assert headers.length % 2 == 0 : "Length of headers must be even.";
        builder = addHeaders(headers, builder);
      }
      return builder.build();
    } catch (IllegalArgumentException iex) {
      throw new IllegalArgumentException("ERROR: invalid URL");
    } catch (AssertionError ae) {
      throw new IllegalArgumentException("ERROR:" + ae.getMessage());
    }
  }

  /**
   * Adds header key-value pairs to the given HttpRequest builder.
   * @param headers array of alternating key-value pairs
   * @param builder the builder to add the headers to
   * @return the same builder but with the given headers added
   */
  private HttpRequest.Builder addHeaders(String[] headers, HttpRequest.Builder builder) {
    // add each header to the request
    for (int i = 0; i < headers.length; i += 2) {
      String name = headers[i];
      String value = headers[i + 1];
      builder = builder.header(name, value);
    }
    return builder;
  }

  /**
   * Adds the given URL parameters to the url string and returns the new URI.
   * @param urlParams array of alternating names and values for url parameters
   * @return new URI to be requested
   */
  private URI addUrlParams(String[] urlParams) {
    StringBuilder urlBuilder = new StringBuilder(url + "?");
    for (int i = 0; i < urlParams.length; i += 2) {
      String name = urlParams[i];
      String value = urlParams[i + 1];
      urlBuilder.append(name).append("=").append(value).append("&");
    }
    // truncate final "&"
    String newUrl = urlBuilder.toString();
    newUrl = newUrl.substring(0, newUrl.length() - 1);
    return URI.create(newUrl);
  }

  /**
   * Builds a string that is formatted to be passed into a BodyPublishers.ofString() from
   * an array of alternating key-value pairs.
   * @param body array of alternating key-value pairs with even length.
   * @return string to use as a POST request body.
   */
  private String buildBodyString(String[] body) {
    StringBuilder bodyBuilder = new StringBuilder("{");
    for (int i = 0; i < body.length; i += 2) {
      String name = body[i];
      String value = body[i + 1];
      String entry = "\"" + name + "\": \"" + value + "\", ";
      bodyBuilder.append(entry);
    }
    // truncate final ", "
    String bodyString = bodyBuilder.toString();
    bodyString = bodyString.substring(0, bodyString.length() - 2);
    return bodyString + "}";
  }
}
