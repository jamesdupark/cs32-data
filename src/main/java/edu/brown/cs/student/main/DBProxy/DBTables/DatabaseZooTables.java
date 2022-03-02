package edu.brown.cs.student.main.DBProxy.DBTables;

import java.util.HashMap;
import java.util.Map;

/**
 * Class that contains the tables for the Zoo Database.
 */
public class DatabaseZooTables implements DatabaseTables {
  /**
   * Map that keys on an index to the corresponding table name in the database.
   * The order of the <key, value> pair is determined by the order of the
   * tables in the database.
   */
  private Map<Integer, String> indexTables = new HashMap<>() {{
      put(0, "zoo");
    }};
  @Override
  public Map<Integer, String> getIndexTables() {
    return indexTables;
  }
}
