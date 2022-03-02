//package edu.brown.cs.student.main.API;
//
//import static org.junit.Assert.assertThrows;
//import static org.junit.Assert.assertTrue;
//
//import edu.brown.cs.student.main.API.APIRequests.BadStatusException;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.util.List;
//
//public class APIAggregatorTest {
//  private APIAggregator infoAggregator, matchAggregator;
//  private List<String> infoActiveOne, infoActiveFour, matchActiveOne, matchActiveFour;
//
//  @Before
//  public void init() {
//    infoAggregator = new APIAggregator("info");
//    matchAggregator = new APIAggregator("match");
//    infoActiveOne = List.of("/info-one", "/info-two", "/info-three");
//    infoActiveFour = List.of("/info-four", "/info-five", "/info-six");
//    matchActiveOne = List.of("/match-one", "/match-two", "/match-three");
//    matchActiveFour = List.of("/match-four", "/match-five", "/match-six");
//  }
//
//  @Test
//  public void testConstructorException() {
//    // invalid type throws an exception
//    assertThrows(IllegalArgumentException.class, () -> new APIAggregator("hi"));
//  }
//
//  @Test
//  public void testGetActive() throws BadStatusException {
//    List<String> infoActive = infoAggregator.getActiveClients();
//    List<String> matchActive = matchAggregator.getActiveClients();
//
//    assertTrue(infoActive.equals(infoActiveOne) || infoActive.equals(infoActiveFour));
//    assertTrue(matchActive.equals(matchActiveOne) || matchActive.equals(matchActiveFour));
//  }
//}
