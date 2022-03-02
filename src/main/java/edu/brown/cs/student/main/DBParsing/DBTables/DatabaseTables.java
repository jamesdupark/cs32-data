package edu.brown.cs.student.main.DBParsing.DBTables;

import java.util.Map;

/**
 * Interface for the tables inside each database that defines an accessor
 * method to retrieve a Map of the index to the corresponding table name.
 * This information will then be used by SQLCommands Class when connecting
 * to a database.
 */
public interface DatabaseTables {
  /**
   * Method that returns the Map of the index to the corresponding table name in a
   * database. The index of the table is determined by the order of the tables
   * present in the database.
   * @return the Map of the index to the corresponding table name for a database
   */
  Map<Integer, String> getIndexTables();
}
