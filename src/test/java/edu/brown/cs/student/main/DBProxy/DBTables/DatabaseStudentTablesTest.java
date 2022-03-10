package edu.brown.cs.student.main.DBProxy.DBTables;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Class to test DatabaseStudentTable.
 */
public class DatabaseStudentTablesTest {
  /**
   * Method to test for a correct indexTable for the Student Database.
   */
  @Test
  public void testGetIndexTables() {
    DatabaseStudentTables dbStudTab = new DatabaseStudentTables();
    Map<Integer, String> indexTables = dbStudTab.getIndexTables();

    assertEquals(indexTables.size(), 4);
    assertEquals(indexTables.get(0), "names");
    assertEquals(indexTables.get(1), "traits");
    assertEquals(indexTables.get(2), "skills");
    assertEquals(indexTables.get(3), "interests");
  }
}
