package edu.brown.cs.student.main.API;

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
    } catch (AssertionError ase) {
      throw new IllegalArgumentException("ERROR: " + ase.getMessage());
    }
  }

  /**
   * Makes a get request to the '/get-active' API endpoint for the aggregator's
   * base URL, and returns a list of Strings representing the currently active
   * API endpoints.
   * @return currently active API endpoints
   */
  public List<String> getActiveClients() {
    // TODO: fill
    return List.of();
  }

  /**
   * Aggregates APIs by cycling through them based on the given ranking algorithms.
   * @param apiRanker
   */
  public void aggregate(Comparator<APIRequestBuilder> apiRanker) {

  }
}
