package edu.brown.cs.student.main.DBProxy;

import edu.brown.cs.student.main.Recommender.Stud.DatabaseStudent;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the DBStudentGenerator Class.
 */
public class DBStudentGeneratorTest {
  /**
   * Method to test getDBStudents.
   */
  @Test
  public void testGetDBStudents() {
    DBStudentGenerator dbStudGen = new DBStudentGenerator("data/recommendation/sql/data.sqlite3");
    List<DatabaseStudent> dbStudList = dbStudGen.getDBStudents();

    // check properties of dbStudList
    assertEquals(dbStudList.size(), 60);

    // check one specific value inside dbStudList
    assertEquals(dbStudList.get(0).getQuantMap(), new HashMap<>());

    Map<String, String> qualMap = new HashMap<>();
    qualMap.put("skills", "front-end");
    qualMap.put("strengths", "team player, prepared");
    qualMap.put("weaknesses", "unfriendly, late, dishonest");
    qualMap.put("name", "Petr Dillingstone");
    qualMap.put("id", "1");
    qualMap.put("interests", "film/photography, mathematics, politics, theatre");
    qualMap.put("email", "pdillingstone0@nationalgeographic.com");
    assertEquals(dbStudList.get(0).getQualMap().size(), 7);
    assertEquals(dbStudList.get(0).getQualMap(), qualMap);
  }
}
