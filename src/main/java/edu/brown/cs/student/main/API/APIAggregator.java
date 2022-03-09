package edu.brown.cs.student.main.API;

import edu.brown.cs.student.main.API.APIRequests.APIRequestBuilder;
import edu.brown.cs.student.main.API.APIRequests.APIRequestHandler;
import edu.brown.cs.student.main.API.APIRequests.BadStatusException;
import edu.brown.cs.student.main.API.json.JSONParser;
import edu.brown.cs.student.main.API.json.PartialStudent;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class for API aggregator objects which serve as a proxy and aggregator for
 * API requests.
 */
public class APIAggregator {
  /**
   * expected size of a complete aggregated dataset.
   */
  private static final int EXPECT_SIZE = 60;
  /**
   * type of the API aggregator (info or match) indicating which type of endpoint is being queried.
   */
  private final String type;
  /**
   * base URL of the APIAggregator class, used to create URLs for future API
   * requests.
   */
  private final String baseUrl;
  /**
   * handler for API requests made by the aggregator.
   */
  private final APIRequestHandler handler = new APIRequestHandler();
  /**
   * builds GET requests to find active APIs.
   */
  private final APIRequestBuilder activeBuilder;
  /**
   * map of endpoint names to HttpRequests. Requests are added as needed.
   */
  private final Map<String, HttpRequest> reqMap = new HashMap<>();

  /**
   * Constructor for the APIAggregator class.
   * @param type string representing the type of the aggregator to be created.
   *             can be either "info" or "match"
   * @throws IllegalArgumentException if type is not "info" or "match".
   */
  public APIAggregator(String type) throws IllegalArgumentException {
    try {
      assert type.equals("info") || type.equals("match")
          : "Type not supported.";
      this.type = type;
      baseUrl = "https://student" + type + "api.herokuapp.com";
      activeBuilder = new APIRequestBuilder(baseUrl + "/get-active");
    } catch (AssertionError ase) {
      throw new IllegalArgumentException("ERROR: " + ase.getMessage());
    }
  }

  /**
   * Makes a get request to the '/get-active' API endpoint for the aggregator's
   * base URL, and returns a list of Strings representing the currently active
   * API endpoints.
   * @return currently active API endpoints
   * @throws BadStatusException when the API request returns a bad status code
   */
  public List<String> getActiveClients() throws BadStatusException {
    HttpRequest activeRequest = activeBuilder.get(null, null);
    HttpResponse<String> response;
    try {
      response = handler.makeRequest(activeRequest);
    } catch (HttpTimeoutException htte) {
      final int status = 408;
      throw new BadStatusException("connection timed out.", status);
    }

    return JSONParser.toStringList(response.body());
  }

  /**
   * Initializes map of HTTPRequests by adding requests for currently active endpoints
   * if they are not in the map already.
   * @param active list of active endpoints
   */
  public void initReqMap(List<String> active) {
    String apiKey = ClientAuth.getApiKey();
    String auth = ClientAuth.getAuth();
    String[] urlAuth = new String[]{"auth", auth, "key", apiKey};
    String[] apiAuth = new String[]{"x-api-key", apiKey};
    String[] bodyAuth = new String[]{"auth", auth};

    for (String endpoint: active) {
      if (!reqMap.containsKey(endpoint)) {
        String endpointUrl = baseUrl + endpoint;
        APIRequestBuilder requestBuilder = new APIRequestBuilder(endpointUrl);
        HttpRequest req = null;

        // make the appropriate request
        switch (type) {
          case "info":
            req = requestBuilder.get(urlAuth, null);
            break;
          case "match":
            req = requestBuilder.post(apiAuth, bodyAuth);
            break;
          default:
            break;
        }

        reqMap.put(endpoint, req);
      }
    }
  }

  /**
   * Aggregates data from multiple API endpoints to create a complete data list. Cycles through
   * each endpoint until either a successful request or three consecutive failed requests, at which
   * point the active endpoints are updated.
   * @return list of type T
   * @param tClass class of the list to be returned. Must implement JSONable
   * @param <T> either studentMatch or studentInfo
   * @throws BadStatusException when active endpoints cannot be queried
   */
  public <T extends PartialStudent> List<T> aggregate(Class<T> tClass) throws BadStatusException {
    boolean dataComplete = false;
    List<T> dataset = new ArrayList<>();
    int retries = 0;
    while (!dataComplete) {
      List<String> active;
      try {
        active = getActiveClients();
        this.initReqMap(active);
      } catch (BadStatusException bse) { // cannot get active clients
        throw new BadStatusException("could not get active clients.", 404);
      }

      String currEndpoint = "";
      int consecutiveFails = 0;
      // continue until all endpoints have been successfully queried
      // if six queries fail consecutively, re-query active endpoints
      while (!active.isEmpty() && consecutiveFails < 6) {
        try {
          currEndpoint = active.remove(0);
          HttpRequest req = reqMap.get(currEndpoint);
          HttpResponse<String> response = handler.makeRequest(req);
          List<T> subset = JSONParser.getJsonObjectList(response.body(), tClass);
          dataset.addAll(subset);
          consecutiveFails = 0;
          // if the connection times out or fails, add endpoint to end of list to query again later
        } catch (BadStatusException | HttpTimeoutException ex) {
          active.add(currEndpoint);
          consecutiveFails++;
        }
      }

      if (consecutiveFails < 3) { // exit loop and return
        dataComplete = true;
        assert dataset.size() == EXPECT_SIZE : "Missing student data!";
      } else {
        dataset = new ArrayList<>(); // clear dataset and query again
        retries++;
        // more than 3 instances of 6 consecutive failures - probably a network problem
        if (retries > 3) {
          throw new BadStatusException("ERROR: endpoints unresponsive - check network.", 404);
        }
      }
    }

    return dataset;
  }
}
