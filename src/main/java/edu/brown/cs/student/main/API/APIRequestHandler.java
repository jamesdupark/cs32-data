package edu.brown.cs.student.main.API;

import java.io.IOException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * Class that handles executing HTTPRequests objects created by
 * APIRequestBuilders. Adapted from the APIClient class from cs0320's lab 2.
 */
public class APIRequestHandler {
  /**
   * HTTPClient that makes the API requests.
   */
  private HttpClient client;
  /**
   * max duration in seconds to wait before a request expires.
   */
  private static final int TIMEOUT = 60;

  /**
   * Constructor for APIRequestHandlers.
   */
  public APIRequestHandler() {
    HttpClient apiClient = HttpClient.newBuilder()
        .version(HttpClient.Version.HTTP_2)
        .connectTimeout(Duration.ofSeconds(TIMEOUT))
        .build();

    this.client = apiClient;
  }

  /**
   * Attempts to make the given HTTPRequest.
   * @param req HTTPRequest object representing the request to be made.
   * @return HttpResponse object for the API's response
   * @throws IllegalArgumentException when the request fails to be made for some reason
   */
  public HttpResponse<String> makeRequest(HttpRequest req) throws IllegalArgumentException {
    try {
      HttpResponse<String> apiResponse = client.send(req, HttpResponse.BodyHandlers.ofString());
      System.out.println("Status " + apiResponse.statusCode());
      System.out.println(apiResponse.body());

      return apiResponse;
    } catch (IOException ioe) {
      throw new IllegalArgumentException("ERROR: An I/O error occurred while "
          + "sending or receiving data.");

    } catch (InterruptedException ie) {
      throw new IllegalArgumentException("ERROR: The operation was interrupted.");

    } catch (IllegalArgumentException iae) {
      throw new IllegalArgumentException("ERROR: The request argument was invalid.");

    } catch (SecurityException se) {
      throw new IllegalArgumentException("ERROR: There was a security configuration error.");
    }
  }
}
