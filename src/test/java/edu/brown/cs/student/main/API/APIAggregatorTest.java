package edu.brown.cs.student.main.API;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import edu.brown.cs.student.main.API.APIRequests.BadStatusException;
import edu.brown.cs.student.main.API.json.JSONParser;
import edu.brown.cs.student.main.Recommender.Stud.StudentInfo;
import edu.brown.cs.student.main.Recommender.Stud.StudentMatch;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class APIAggregatorTest {
  private APIAggregator infoAggregator, matchAggregator;
  private List<String> infoActiveOne, infoActiveFour, matchActiveOne, matchActiveFour;

  @Before
  public void init() {
    infoAggregator = new APIAggregator("info");
    matchAggregator = new APIAggregator("match");
    infoActiveOne = List.of("/info-one", "/info-two", "/info-three");
    infoActiveFour = List.of("/info-four", "/info-five", "/info-six");
    matchActiveOne = List.of("/match-one", "/match-two", "/match-three");
    matchActiveFour = List.of("/match-four", "/match-five", "/match-six");
  }

  @Test
  public void testConstructorException() {
    // invalid type throws an exception
    assertThrows(IllegalArgumentException.class, () -> new APIAggregator("hi"));
  }

  @Test
  public void testGetActive() throws BadStatusException {
    List<String> infoActive = infoAggregator.getActiveClients();
    List<String> matchActive = matchAggregator.getActiveClients();

    assertTrue(infoActive.equals(infoActiveOne) || infoActive.equals(infoActiveFour));
    assertTrue(matchActive.equals(matchActiveOne) || matchActive.equals(matchActiveFour));
  }

  @Test
  public void testAggregate() throws BadStatusException, IOException {
    List<StudentInfo> studentInfoList = infoAggregator.aggregate(StudentInfo.class);
    List<StudentMatch> studentMatchList = matchAggregator.aggregate(StudentMatch.class);

    // test size
    assertEquals(60, studentInfoList.size());
    assertEquals(60, studentMatchList.size());

    String infoFilePath = "data/recommendation/json/studentInfoGet.json";
    String matchFilePath = "data/recommendation/json/studentMatchPost.json";
    Set<StudentInfo> infoExpect =
        new HashSet<>(JSONParser.readJsonFile(infoFilePath, StudentInfo.class));
    Set<StudentMatch> matchExpect =
        new HashSet<>(JSONParser.readJsonFile(matchFilePath, StudentMatch.class));

    // test contents
    Set<StudentInfo> infoSet = new HashSet<>(studentInfoList);
    Set<StudentMatch> matchSet = new HashSet<>(studentMatchList);
    assertEquals(infoExpect, infoSet);
    assertEquals(matchExpect, matchSet);
  }

  @Test
  public void testAggregateReliability() throws BadStatusException {
    for (int i = 0; i < 60; i++) {
      infoAggregator.aggregate(StudentInfo.class);
      matchAggregator.aggregate(StudentMatch.class);
    }
  }
}
