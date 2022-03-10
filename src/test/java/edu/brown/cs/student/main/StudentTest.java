package edu.brown.cs.student.main;

import edu.brown.cs.student.main.API.json.JSONParser;
import edu.brown.cs.student.main.Recommender.Stud.StudentInfo;
import edu.brown.cs.student.main.Recommender.Stud.StudentMatch;
import edu.brown.cs.student.main.Recommender.Stud.Student;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StudentTest {
  List<StudentInfo> twoInfos;
  List<StudentMatch> twoMatches;

  @Before
  public void init() {
    String infoFilePath = "data/recommendation/json/studentInfoTest.json";
    String matchFilePath = "data/recommendation/json/studentMatchTest.json";
    try {
      twoInfos = JSONParser.readJsonFile(infoFilePath, StudentInfo.class);
      twoMatches = JSONParser.readJsonFile(matchFilePath, StudentMatch.class);
    } catch (IOException ignored) { }
  }

  @Test
  public void testBuildFromPartial() {
    Student studentOne = new Student();
    studentOne.buildFromPartial(twoInfos.get(0);
    studentOne.buildFromPartial(twoMatches.get(0));

    Map<String, Double> oneQuan = studentOne.getQuanMap();
    Map<String, String> oneQual = studentOne.getQualMap();
    assertEquals(9, oneQual.size());
    assertEquals(3, oneQuan.size());
    assertEquals("Petr Dillingstone", oneQual.get("name"));
    assertEquals(Double.valueOf(41.0), oneQuan.get("software_engn_confidence"));
  }
}
