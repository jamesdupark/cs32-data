package edu.brown.cs.student.main.API.APIRequests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import edu.brown.cs.student.main.API.APIAggregator;
import edu.brown.cs.student.main.API.ClientAuth;
import edu.brown.cs.student.main.API.json.JSONParser;
import org.junit.Before;
import org.junit.Test;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

public class APIRequestHandlerTest {
  private final APIRequestHandler handler = new APIRequestHandler();
  private APIRequestBuilder activeBuilder, infoBuilder, matchBuilder;
  private final APIAggregator infoAggregator = new APIAggregator("info");
  private final APIAggregator matchAggregator = new APIAggregator("match");
  private String apiKey, auth;

  @Before
  public void init() throws BadStatusException {
    String activeUrl = "https://studentinfoapi.herokuapp.com/get-active";
    activeBuilder = new APIRequestBuilder(activeUrl);
    // get active endpoints and create builders for them
    List<String> activeInfo = infoAggregator.getActiveClients();
    List<String> activeMatch = matchAggregator.getActiveClients();
    String infoUrl = "https://studentinfoapi.herokuapp.com" + activeInfo.get(0);
    String matchUrl = "https://studentmatchapi.herokuapp.com" + activeMatch.get(0);
    infoBuilder = new APIRequestBuilder(infoUrl);
    matchBuilder = new APIRequestBuilder(matchUrl);
    // retrieve api key and auth token
    apiKey = ClientAuth.getApiKey();
    auth = "jpark236";
  }

  @Test
  public void testMakeRequest() throws BadStatusException {
    // prep authentication
    String[] urlAuth = new String[]{"auth", auth, "key", apiKey};
    String[] badUrlAuth = new String[]{"auth", "username", "key", "password"};
    String[] postHead = new String[]{"x-api-key", apiKey};
    String[] badHead = new String[]{"x-api-key", "apiKey"};
    String[] postBody = new String[]{"auth", auth};
    String[] badBody = new String[]{"auth", "me"};

    // basic get request with no additional params
    HttpRequest basicGet = activeBuilder.get(null, null);
    HttpResponse<String> getResponse = handler.makeRequest(basicGet);
    List<String> activeOne = List.of("/info-one", "/info-two", "/info-three");
    List<String> activeThree = List.of("/info-four", "/info-five", "/info-six");
    List<String> responseList = JSONParser.toStringList(getResponse.body());
    assertTrue(responseList.equals(activeOne)
        || responseList.equals(activeThree));

    // authenticated get request + failed authentication + no authentication
    HttpRequest authGet = infoBuilder.get(urlAuth, null);
    HttpRequest failedAuthGet = infoBuilder.get(badUrlAuth, null);
    HttpRequest noAuthGet = infoBuilder.get(null, null);
    boolean done = false;
    do {
      try { // retry until request goes through
        HttpResponse<String> authResponse = handler.makeRequest(authGet);
        assertEquals(200, authResponse.statusCode());
        // TODO: convert to students
        done = true;
      } catch (BadStatusException ignored) { }
    } while (!done);
    assertThrows(BadStatusException.class, () -> handler.makeRequest(failedAuthGet));
    assertThrows(BadStatusException.class, () -> handler.makeRequest(noAuthGet));

    // authenticated post request + failed authentication + no authentication
    HttpRequest authPost = matchBuilder.post(postHead, postBody);
    HttpRequest failedAuth = matchBuilder.post(badHead, badBody);
    HttpRequest noAuth = matchBuilder.post(null, null);
    boolean postDone = false;
    do {
      try { // retry until request goes through
        HttpResponse<String> authResponse = handler.makeRequest(authPost);
        assertEquals(200, authResponse.statusCode());
        // TODO: convert to students
        postDone = true;
      } catch (BadStatusException ignored) { }
    } while (!postDone);
    assertThrows(BadStatusException.class, () -> handler.makeRequest(failedAuth));
    assertThrows(BadStatusException.class, () -> handler.makeRequest(noAuth));
    
    // get to post-only and post to get-only
    HttpRequest badGet = matchBuilder.get(urlAuth, null);
    HttpRequest badPost = infoBuilder.post(postHead, postBody);
    assertThrows(IllegalArgumentException.class, () -> handler.makeRequest(badGet));
    assertThrows(IllegalArgumentException.class, () -> handler.makeRequest(badPost));
  }
}
