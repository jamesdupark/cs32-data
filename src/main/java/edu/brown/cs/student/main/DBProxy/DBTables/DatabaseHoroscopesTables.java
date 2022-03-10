package edu.brown.cs.student.main.DBProxy.DBTables;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains the tables for the Horoscopes Database.
 */
public class DatabaseHoroscopesTables implements DatabaseTables {
  /**
   * Map that keys on an index to the corresponding table name in the database.
   * The order of the <key, value> pair is determined by the order of the
   * tables in the database.
   */
  private final Map<Integer, String> indexTables = new HashMap<>() {{
      put(0, "sqlite_sequence");
      put(1, "horoscopes");
      put(2, "ta_horoscope");
      put(3, "tas");
    }};
  @Override
  public Map<Integer, String> getIndexTables() {
    return indexTables;
  }
}
