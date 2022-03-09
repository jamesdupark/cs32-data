package edu.brown.cs.student.main.Commands.DatabaseCommands;

import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseHoroscopesTables;
import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseStudentTables;
import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseTables;
import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseZooTables;

import java.util.HashMap;
import java.util.Map;

public abstract class ConnectDB {
  /**
   * HashMap mapping from the database filepath supported by this class to an index.
   */
  private final Map<String, Integer> dbIndex = new HashMap<>() {{
      put("data/recommendation/sql/data.sqlite3", 0);
      put("data/recommendation/sql/horoscopes.sqlite3", 1);
      put("data/recommendation/sql/zoo.sqlite3", 2);
    }};
  /**
   * Method to make the DatabaseTables corresponding to the filepath of the database in
   * order to retrieve the Map of the index to the corresponding table name. This information
   * is then used for the "connect_db" command.
   * @param filepath String representing the filepath of the database to establish a
   *                 connection with
   * @return the DatabaseTables corresponding to the filepath of the database
   */
  public DatabaseTables getDBTables(String filepath) {
    int index = dbIndex.get(filepath);
    switch (index) {
      case 0:
        return new DatabaseStudentTables();
      case 1:
        return new DatabaseHoroscopesTables();
      case 2:
        return new DatabaseZooTables();
      default:
        throw new RuntimeException("ERROR: Connection to this DB is not supported.");
    }
  }
  abstract void connectDBCmd(String[] argv, int argc)
      throws IllegalArgumentException;
  public Map<String, Integer> getDbIndex() {
    return dbIndex;
  }
  public void checkConnectionHasCorrectNumTablePerm(String database, int argc) {
    DatabaseTables dbTables = getDBTables(database);
    Map<Integer, String> dbIndexTables = dbTables.getIndexTables();
    if (dbIndexTables.size() != argc - 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments");
    }
  }

  public Map<String, String> setUpTablePerm(String[] argv, String database) {
    DatabaseTables dbTables = getDBTables(database);
    Map<Integer, String> dbIndexTables = dbTables.getIndexTables();
    Map<String, String> tablePermissions = new HashMap<>();
    for (int i = 2; i < argv.length; i++) {
      if (!argv[i].equals("R") && !argv[i].equals("W") && !argv[i].equals("RW")) {
        throw new RuntimeException("ERROR: permission is not <R> or <W> or <RW>");
      }
      tablePermissions.put(dbIndexTables.get(i - 2), argv[i]);
    }
    return tablePermissions;
  }
}
