package edu.brown.cs.student.main.DBProxy.DBTables;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Class to test DatabaseZooTable.
 */
public class DatabaseZooTablesTest {
  /**
   * Method to test for a correct indexTable for the Zoo Database.
   */
  @Test
  public void testGetIndexTables() {
    DatabaseZooTables dbZooTab = new DatabaseZooTables();
    Map<Integer, String> indexTables = dbZooTab.getIndexTables();

    assertEquals(indexTables.size(), 1);
    assertEquals(indexTables.get(0), "zoo");
  }
}

