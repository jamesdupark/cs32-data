package edu.brown.cs.student.main.DBParsing;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

/**
 * Proxy class that sits between the client and the database used to add a layer of
 * abstraction in the program so that users do not have to directly interact with
 * the SQL database.
 */
public class Proxy {
  /** Connection used to establish a connection to the database through a filepath. */
  private static Connection conn = null;
  /** String representing the filepath. */
  private final String filepath;
  /**
   * Map mapping tables to its permissions and access level as determined by the user.
   * All tables in the database will be included and permissions will be one of
   * <R>, <W>, or <RW>.
   */
  private Map<String, String> tablePermissions;
  /**
   * Map mapping a SQL Command to its corresponding permission level, which will be one of
   * <R>, <W>, or <RW>.
   */
  private final Map<String, String> sqlPermissions;

  /**
   * Constructor for the Proxy class.
   * @param filepath String representing the filepath of the database
   * @param tablePermissions Map mapping tables to its permissions and access level
   *                         as determined by the user
   */
  public Proxy(String filepath, Map<String, String> tablePermissions) {
    this.filepath = filepath;
    this.tablePermissions = tablePermissions;
    sqlPermissions = new HashMap<>();
    setupCommandPermissions();
  }

  /**
   * Method to populate sqlPermissions Map with the <key, value> pair being
   * the SQL Command to its permission level.
   */
  private void setupCommandPermissions() {
    sqlPermissions.put("SELECT", "R");
    sqlPermissions.put("INSERT", "R");
    sqlPermissions.put("DROP", "RW");
    sqlPermissions.put("UPDATE", "RW");
    sqlPermissions.put("DELETE", "RW");
    sqlPermissions.put("ALTER", "RW");
    sqlPermissions.put("JOIN", "R");
    sqlPermissions.put("TRUNCATE", "RW");
  }

  /**
   * Method to establish a connection to the Database, which is given through the filepath.
   * @throws ClassNotFoundException if the class cannot be located
   * @throws SQLException if a database access error occurs or the url is nul
   * @throws FileNotFoundException if the filepath is invalid
   */
  public void connectDB() throws ClassNotFoundException, SQLException, FileNotFoundException {
    new FileReader(filepath);
    System.out.println("Inside connect DB Proxy!");
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filepath;
    conn = DriverManager.getConnection(urlToDB);
    // tell the database to enforce foreign keys during operations and should be present
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
  }

  /**
   * Method to check whether a user has the necessary permission to execute a SQL query.
   * @param commandToTable Map mapping from the SQL command for the query to the
   *                       corresponding table that the command is intended for
   * @return whether the user can execute the query, which is determined by the
   * table permissions matching the sql command permissions
   */
  public boolean validateQuery(Map<String, String> commandToTable) {
    if (tablePermissions.isEmpty()) {
      throw new RuntimeException("ERROR: Table permissions is empty!");
    }
    for (Map.Entry<String, String> entry : commandToTable.entrySet()) {
      String command = entry.getKey();
      String dbTable = entry.getValue();
      String sqlPerm = sqlPermissions.get(command);
      if (!tablePermissions.get(dbTable).contains(sqlPerm)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Method to execute a SQL query after checking that the user has the necessary
   * permission to execute a query.
   * @param sqlQuery String representing the query to be executed
   * @return the table of data representing a database result set, which is generated
   * by executing the input sqlQuery from the connected database
   * @throws SQLException if it is an invalid query or if a database error occurs
   */
  public ResultSet execQuery(String sqlQuery) throws SQLException {
    PreparedStatement rolefinder = conn.prepareStatement(sqlQuery);
    ResultSet rs = rolefinder.executeQuery();
    return rs;
  }

  /**
   * Accessor method for the conn field of the Proxy.
   * @return the conn field of the Proxy.
   */
  public Connection getConn() {
    return conn;
  }
}
