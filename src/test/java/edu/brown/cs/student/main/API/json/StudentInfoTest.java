package edu.brown.cs.student.main.API.json;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

public class StudentInfoTest {
  List<StudentInfo> twoInfos;
  StudentInfo infoOne, infoTwo;

  @Before
  public void init() {
    String infoFilePath = "data/recommendation/json/studentInfoTest.json";
    try {
      twoInfos = JSONParser.readJsonFile(infoFilePath, StudentInfo.class);
    } catch (IOException ignored) { }
    infoOne = new StudentInfo(List.of(1, 8, 13),
        List.of("Petr Dillingstone", "sophomore", "email", "in person", "afternoon"));
    infoTwo = new StudentInfo(List.of(2, 19, 19),
        List.of("Gerri Enterle", "sophomore", "zoom", "in person", "morning"));
  }

  @Test
  public void testGetQuantMap() {
    Map<String, Double> expectMap = new HashMap<>();
    expectMap.put("years_experience", 8.0);
    expectMap.put("weekly_avail_hours", 13.0);
    assertEquals(expectMap, infoOne.getQuantMap());
  }

  @Test
  public void testGetQualMap() {
    Map<String, String> expectMap = new HashMap<>();
    expectMap.put("id", "1");
    expectMap.put("name", "Petr Dillingstone");
    expectMap.put("class_year", "sophomore");
    expectMap.put("communication_style", "email");
    expectMap.put("meeting_style", "in person");
    expectMap.put("meeting_time", "afternoon");
    assertEquals(expectMap, infoOne.getQualMap());
  }

  @Test
  public void testEquals() {
    assertEquals(infoOne, twoInfos.get(0));
    assertEquals(infoOne, infoOne);
    assertNotEquals(infoOne, twoInfos.get(1));
    assertEquals(infoTwo, twoInfos.get(1));
  }

  @Test
  public void testCompareTo() {
    assertEquals(Integer.compare(1, 2), infoOne.compareTo(infoTwo));
  }
}
