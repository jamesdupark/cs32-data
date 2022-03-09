package edu.brown.cs.student.main.Commands.DatabaseCommands;

import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseHoroscopesTables;
import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseStudentTables;
import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseTables;
import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseZooTables;

import java.util.HashMap;
import java.util.Map;

/**
 * Abstract Class that defines the connect_db command and other common methods that are
 * shared by classes in the DatabaseCommands package.
 */
public abstract class ConnectDB {
  /**
   * HashMap mapping from the database filepath supported by this application to an index.
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
  /**
   * Defines the abstract method that the subclasses will implement. This method will
   * establish a connection from the subclass to its respective database by creating a new
   * Proxy with the database filepath. If successful, updates the Connection and Proxy fields
   * and prints the connection made to the database.
   * Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  abstract void connectDBCmd(String[] argv, int argc)
      throws IllegalArgumentException;

  /**
   * Method to check whether the client has entered the correct number of table
   * permissions into the command line when establishing connection to a database.
   * @param database String filepath corresponding to the connected database
   * @param argc the number of arguments the client entered into the command line
   */
  public void checkConnectionHasCorrectNumTablePerm(String database, int argc) {
    DatabaseTables dbTables = getDBTables(database);
    Map<Integer, String> dbIndexTables = dbTables.getIndexTables();
    if (dbIndexTables.size() != argc - 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments");
    }
  }
  /**
   * Method to connect the database tables with their respective table permission. That is,
   * the tables in the database will be assigned R, W, or RW access defined by the client.
   * The order of the table permissions is determined by the order of the tables in the
   * database and the order of the permissions entered by the client.
   * @param argv array of strings representing tokenized user input
   * @param database String filepath corresponding to the connected database
   * @return a HashMap mapping from the table in the database to its respective table permission.
   */
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
  /**
   * Defines the abstract method that subclasses will implement to check whether a connection
   * has been established to their respective database before executing any SQL query and command.
   */
  abstract void checkDatabaseConnected();
  /**
   * Accessor method for the dbIndex HashMap.
   * @return the dbIndex HashMap.
   */
  public Map<String, Integer> getDbIndex() {
    return dbIndex;
  }
}
