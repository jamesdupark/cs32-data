package edu.brown.cs.student.main.API.json;

import edu.brown.cs.student.main.Recommender.Stud.StudentMatch;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class StudentMatchTest {
  List<StudentMatch> twoMatches;
  StudentMatch matchOne, matchTwo;

  @Before
  public void init() {
    String matchFilePath = "data/recommendation/json/studentMatchTest.json";
    try {
      twoMatches = JSONParser.readJsonFile(matchFilePath, StudentMatch.class);
    } catch (IOException ignored) { }
    matchOne = new StudentMatch(1, 41, "Petr Dillingstone",
        "Female", "Egypt", "and Native Hawaiian or Other Pacific Islander");
    matchTwo = new StudentMatch(2, 76, "Gerri Enterle",
        "Female", "Saudi Arabia", "Black or African American");
  }

  @Test
  public void testGetQuantMap() {
    Map<String, Double> expectMap = new HashMap<>();
    expectMap.put("software_engn_confidence", 41.0);
    assertEquals(expectMap, matchOne.getQuantMap());
  }

  @Test
  public void testGetQualMap() {
    Map<String, String> expectMap = new HashMap<>();
    expectMap.put("id", "1");
    expectMap.put("name", "Petr Dillingstone");
    expectMap.put("gender", "Female");
    expectMap.put("nationality", "Egypt");
    expectMap.put("race", "and Native Hawaiian or Other Pacific Islander");
    assertEquals(expectMap, matchOne.getQualMap());
  }

  @Test
  public void testEquals() {
    assertEquals(matchOne, twoMatches.get(0));
    assertEquals(matchOne, matchOne);
    assertNotEquals(matchOne, twoMatches.get(1));
    assertEquals(matchTwo, twoMatches.get(1));
  }

  @Test
  public void testCompareTo() {
    assertEquals(Integer.compare(1, 2), matchOne.compareTo(matchTwo));
  }
}
