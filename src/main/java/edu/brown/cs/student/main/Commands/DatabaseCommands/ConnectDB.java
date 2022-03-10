package edu.brown.cs.student.main.Commands.DatabaseCommands;

import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseHoroscopesTables;
import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseStudentTables;
import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseTables;
import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseZooTables;
import edu.brown.cs.student.main.DBProxy.Proxy;

import javax.sql.rowset.CachedRowSet;
import java.io.FileNotFoundException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Abstract Class that defines the connect_db command and other common behavior that are
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
   * @param filepath String representing the filepath of the database that is being
   * connected to. This field will be used to check whether user input (argv[1])
   * matches the database filepath
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  public void connectDBCmd(String[] argv, int argc, String filepath)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc < 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments");
    }
    try {
      // check correct number of args
      if (!getDbIndex().containsKey(argv[1])) {
        System.out.println("ERROR: Database Proxy does not support commands for this database");
        return;
      }
      if (!argv[1].equals(filepath)) {
        System.out.println("ERROR: Filepath does not correspond to database");
        return;
      }
      checkConnectionHasCorrectNumTablePerm(argv[1], argc);
      System.out.println("Successful connection made to " + argv[1]);
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    }
  }

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
  public Map<String, String> setUpTablePerm(String[] argv, String database)
      throws InvalidTablePermissionException {
    if (!dbIndex.containsKey(database)) {
      throw new IllegalArgumentException("ERROR: Database path is not recognized");
    }
    DatabaseTables dbTables = getDBTables(database);
    Map<Integer, String> dbIndexTables = dbTables.getIndexTables();
    Map<String, String> tablePermissions = new HashMap<>();
    for (int i = 2; i < argv.length; i++) {
      if (!argv[i].equals("R") && !argv[i].equals("W") && !argv[i].equals("RW")) {
        throw new InvalidTablePermissionException("ERROR: permission is not <R> or <W> or <RW>");
      }
      tablePermissions.put(dbIndexTables.get(i - 2), argv[i]);
    }
    return tablePermissions;
  }
  /**
   * Defines the abstract method that subclasses will implement to check whether a connection
   * has been established to their respective database before executing any SQL query and command.
   */
  abstract void checkDatabaseConnected() throws DatabaseNotConnectedException;
  /**
   * Accessor method for the dbIndex HashMap.
   * @return the dbIndex HashMap.
   */
  public Map<String, Integer> getDbIndex() {
    return dbIndex;
  }

  /**
   * List that stores all Proxy objects from the DatabaseCommands classes.
   */
  private List<Proxy> proxyList = new ArrayList<>();

  /**
   * Method to close any and all existing connections from the list of Proxy. This
   * ensures that the program is bug-safe since an existing connection could
   * write to the database with another active connection, which would result in
   * out-of-date results.
   */
  private void closeExistingConnection() {
    try {
      for (Proxy p : proxyList) {
        p.getConn().close();
      }
    } catch (SQLException e) {
      System.out.println("ERROR: " + e.getMessage());
    }
  }

  /**
   * Method to connect the caller of the Proxy with the Proxy by creating a Proxy
   * object. The caller of the Proxy will be a DatabaseCommands class.
   * @param filename String representing the filename of the database that the
   *                 caller is trying to establish connection to
   * @param tablePerm the table permissions for the database that determines the
   *                  level of SQL command access
   * @return the Proxy that connects the caller of the proxy with the proxy
   */
  public Proxy connectProxy(String filename, Map<String, String> tablePerm) {
    Proxy proxy = new Proxy(filename, tablePerm);
    closeExistingConnection();
    proxyList.add(proxy);
    try {
      proxy.connectDB();
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: " + e.getMessage());
    } catch (SQLException e) {
      System.out.println("ERROR: " + e.getMessage());
    } catch (FileNotFoundException e) {
      System.out.println("ERROR: " + e.getMessage());
    }
    return proxy;
  }
  /**
   * Method that prints the values from a ResultSet.
   * @param rowSet CachedRowSet corresponding to the ResultSet form a SQL Query
   * @param fieldsPresent the number of fields that should be retrieved from the ResultSet
   * @throws SQLException if there is an error with the ResultSet
   */
  public void printResultSet(CachedRowSet rowSet, int fieldsPresent)
      throws SQLException {
    ResultSet rs = rowSet.createCopy();
    while (rs.next()) {
      String res = "";
      for (int i = 1; i <= fieldsPresent; i++) {
        res += rs.getString(i) + ", ";
      }
      res = res.substring(0, res.length() - 2);
      System.out.println(res);
    }
  }
}
