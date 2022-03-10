package edu.brown.cs.student.main.DBProxy;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import javax.sql.rowset.CachedRowSet;
import javax.sql.rowset.RowSetFactory;
import javax.sql.rowset.RowSetProvider;
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
import java.util.Optional;
import java.util.concurrent.ExecutionException;

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
   * Guava Cache that maps a String SQL Query to an Optional CachedRowSet.
   */
  private LoadingCache<String, Optional<CachedRowSet>> cache;

  /**
   * Void method to instantiate the Guava Cache. When getUnchecked is called in cacheExec,
   * load() will call execQuery and obtain the resulting ResultSet pertaining to the SQL query.
   * From then, a copy of the ResultSet will be made using RowSetFactory and CachedRowSet,
   * and then cached.
   */
  public void makeCache() {
    // max size used for eviction
    final int maxCacheSize = 10;
    CacheLoader<String, Optional<CachedRowSet>> loader = new CacheLoader<>() {
      @Override
      public Optional<CachedRowSet> load(String key) throws SQLException {
        ResultSet result = execQuery(key);
        if (result == null) {
          return null;
        }
        RowSetFactory factory = RowSetProvider.newFactory();
        CachedRowSet rowSet = factory.createCachedRowSet();
        rowSet.populate(result);
        return Optional.ofNullable(rowSet);
      }
    };
    this.cache = CacheBuilder.newBuilder()
        .maximumSize(maxCacheSize)
        .weakKeys()
        .build(loader);
  }

  /**
   * Constructor for the Proxy class.
   * @param filepath String representing the filepath of the database
   * @param tablePermissions Map mapping tables to its permissions and access level
   *                         as determined by the user
   */
  public Proxy(String filepath, Map<String, String> tablePermissions) {
    this.filepath = filepath;
    this.tablePermissions = tablePermissions;
    this.sqlPermissions = new HashMap<>();
    this.cache = null;
    setupCommandPermissions();
  }

  /**
   * Method to populate sqlPermissions Map with the <key, value> pair being
   * the SQL Command to its permission level.
   */
  private void setupCommandPermissions() {
    sqlPermissions.put("SELECT", "R");
    sqlPermissions.put("INSERT", "W");
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
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filepath;
    conn = DriverManager.getConnection(urlToDB);
    // instruct database to enforce foreign keys during operations and should be present
    Statement stat = conn.createStatement();
    stat.executeUpdate("PRAGMA foreign_keys=ON;");
    makeCache();
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
   * Method to check whether a SQL Query needs Write (W) permission access.
   * @param sqlQuery string representing the SQL query that we are checking for write access
   * @return whether the string contains a command that needs Write (W) access
   */
  public boolean hasWriteCommand(String sqlQuery) {
    for (String key : sqlPermissions.keySet()) {
      String val = sqlPermissions.get(key);
      if (val.contains("W") && sqlQuery.contains(key)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Method to execute a SQL query after checking that the user has the necessary
   * permission to execute a query. This method is called from the GuavaCache load function,
   * which caches the ResultSet returned from this method for the String SQL query.
   * @param sqlQuery String representing the query to be executed
   * @return the table of data representing a database result set, which is generated
   * by executing the input sqlQuery from the connected database
   * @throws SQLException if it is an invalid query or if a database error occurs
   */
  public ResultSet execQuery(String sqlQuery) throws SQLException {
    PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
    boolean hasResultSet = preparedStatement.execute();
    if (hasResultSet) {
      return preparedStatement.getResultSet();
    } else {
      return null;
    }
  }

  /**
   * Method to execute a SQL query after checking that the user has the necessary
   * permission to execute a query. This method is called from the SQLCommands Class,
   * which will check the cache for the existing sql query as the key. If this is the case,
   * the value is retrieved; if not, the value is computed using the execQuery method called
   * from load. This method also checks if the input SQL Query contains any commands that
   * has write (W) level access — clearing the cache if so since the past cached items
   * could be out of date.
   * @param sqlQuery String representing the query to be executed
   * @return the table of data representing a database result set, which is generated
   * by executing the input sqlQuery from the connected database
   * @throws SQLException if it is an invalid query or if a database error occurs
   * @throws ExecutionException if getUnchecked results in an exception
   */
  public CachedRowSet cacheExec(String sqlQuery) throws SQLException, ExecutionException {
    // check if sql query has a Write command
    if (hasWriteCommand(sqlQuery)) {
      // clear cache and execute query
      cache.invalidateAll();
      execQuery(sqlQuery);
      return null;
    } else {
      // store query in cache
      Optional<CachedRowSet> rs = this.cache.getUnchecked(sqlQuery);
      return rs.orElse(null);
    }
  }

  /**
   * Accessor method for the conn field of the Proxy.
   * @return the conn field of the Proxy.
   */
  public Connection getConn() {
    return conn;
  }

  /**
   * Accessor method for the sql permissions table of the Proxy.
   * @return the sqlPermissions field of the Proxy.
   */
  public Map<String, String> getSqlPermissions() {
    return sqlPermissions;
  }

  /**
   * Accessor method for the Guava cache.
   * @return the Guava cache that maps a String SQL query to a CachedRowSet.
   */
  public LoadingCache<String, Optional<CachedRowSet>> getCache() {
    return cache;
  }
}
