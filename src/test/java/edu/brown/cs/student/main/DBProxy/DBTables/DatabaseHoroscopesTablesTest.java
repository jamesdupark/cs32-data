package edu.brown.cs.student.main.DBProxy.DBTables;

import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Class to test DatabaseHoroscopesTable.
 */
public class DatabaseHoroscopesTablesTest {
  /**
   * Method to test for a correct indexTable for the Horoscopes Database.
   */
  @Test
  public void testGetIndexTables() {
    DatabaseHoroscopesTables dbHoroTab = new DatabaseHoroscopesTables();
    Map<Integer, String> indexTables = dbHoroTab.getIndexTables();

    assertEquals(indexTables.size(), 4);
    assertEquals(indexTables.get(0), "sqlite_sequence");
    assertEquals(indexTables.get(1), "horoscopes");
    assertEquals(indexTables.get(2), "ta_horoscope");
    assertEquals(indexTables.get(3), "tas");
  }
}
