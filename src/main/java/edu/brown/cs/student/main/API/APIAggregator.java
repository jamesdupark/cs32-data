package edu.brown.cs.student.main.API;

import edu.brown.cs.student.main.API.APIRequests.APIRequestBuilder;
import edu.brown.cs.student.main.API.APIRequests.APIRequestHandler;
import edu.brown.cs.student.main.API.APIRequests.BadStatusException;
import edu.brown.cs.student.main.API.json.JSONParser;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Comparator;
import java.util.List;

/**
 * Class for API aggregator objects which serve as a proxy and aggregator for
 * API requests.
 */
public class APIAggregator {
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
   * Constructor for the APIAggregator class.
   * @param type string representing the type of the aggregator to be created.
   *             can be either "info" or "match"
   * @throws IllegalArgumentException if type is not "info" or "match".
   */
  public APIAggregator(String type) throws IllegalArgumentException {
    try {
      assert type.equals("info") || type.equals("match")
          : "Type not supported.";
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
    HttpResponse<String> response = handler.makeRequest(activeRequest);

    return JSONParser.toStringList(response.body());
  }

  /**
   * Aggregates APIs by cycling through them based on the given ranking algorithm.
   * @param apiRanker a comparator for APIRequestBuilders that
   */
  public void aggregate(Comparator<APIRequestBuilder> apiRanker) {

  }
}
