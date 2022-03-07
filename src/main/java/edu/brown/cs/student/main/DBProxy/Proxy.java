package edu.brown.cs.student.main.DBProxy;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import edu.brown.cs.student.main.DBProxy.DBItems.DatabaseStudent;

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


  private LoadingCache<String, Optional<ResultSet>> cache;

  public void makeCache() {
    final int maxCacheSize = 10;
    CacheLoader<String, Optional<ResultSet>> loader = new CacheLoader<>() {
      @Override
      public Optional<ResultSet> load(String key) throws SQLException, ExecutionException {
        return Optional.ofNullable(execQuery(key));
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
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filepath;
    conn = DriverManager.getConnection(urlToDB);
    // tell the database to enforce foreign keys during operations and should be present
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
   * Method to execute a SQL query after checking that the user has the necessary
   * permission to execute a query.
   * @param sqlQuery String representing the query to be executed
   * @return the table of data representing a database result set, which is generated
   * by executing the input sqlQuery from the connected database
   * @throws SQLException if it is an invalid query or if a database error occurs
   */
  public ResultSet execQuery(String sqlQuery) throws SQLException, ExecutionException {
    PreparedStatement preparedStatement = conn.prepareStatement(sqlQuery);
    boolean hasResultSet = preparedStatement.execute();
    if (hasResultSet) {
//      ResultSet rs = preparedStatement.getResultSet();
//      RowSetFactory factory = RowSetProvider.newFactory();
//      CachedRowSet rowset = factory.createCachedRowSet();
//      rowset.populate(rs);
//      ResultSet rsForPrinting = rowset.createCopy();
//      return rsForPrinting;
      return preparedStatement.getResultSet();
    } else {
      return null;
    }
//    ResultSet rs = rolefinder.executeQuery();
//    while (rs.next()) {
//      System.out.println("YE");
//    }
//    return rs;
    // check if sql query is in my cache
//    if (cache.getIfPresent(sqlQuery) != null) {
//      System.out.println("I got my sql Query from cache!");
//      System.out.println(cache.size());
//      cache.get(sqlQuery);
//      System.out.println(cache.size());
//      cache.get(sqlQuery);
//      System.out.println(cache.size());
////      System.out.println(cache.get(sqlQuery).getString(1));
//      System.out.println("before");
//      ResultSet rs = this.cache.getUnchecked(sqlQuery).get()  ;
//      System.out.println("after");
//      while (rs.next()) {
//        System.out.println("Inside while loop");
//        String name = rs.getString(1);
//        System.out.println(name);
//      }
//      return rs;
//    } else {
//      PreparedStatement rolefinder = conn.prepareStatement(sqlQuery);
//      ResultSet rs = rolefinder.executeQuery();
//      cache.put(sqlQuery, rs);
////      cache.getUnchecked(sqlQuery);
//      return rs;
//    }
//    System.out.println(cache.size());
//    System.out.println(cache.get(sqlQuery));
  }

  public ResultSet cacheExec(String sqlQuery) throws SQLException, ExecutionException {
//    if (cache.getIfPresent(sqlQuery) != null) {
//      System.out.println(cache.size());
//      return this.cache.getUnchecked(sqlQuery);
//    } else {
//      return null;
//    }
//    connect_db data/recommendation/sql/data.sqlite3 RW RW RW RW
//    if (cache.getIfPresent(sqlQuery) != null) {
//      return cache.get(sqlQuery).get();
//    } else {
//      return cache.getUnchecked(sqlQuery).get();
//    }
//    System.out.println(cache.size());
//    Optional<ResultSet> t = cache.getIfPresent(sqlQuery);
//    if (t == null) {
//      System.out.println("NULL CASE!");
//    } else {
//      System.out.println("NOT NULL!");
//      return t.get();
//    }
//
    System.out.println("cache: " + cache.asMap());
    Optional<ResultSet> rs = this.cache.getUnchecked(sqlQuery);

    System.out.println(rs.orElse(null).next());
    System.out.println("cache: " + cache.asMap());
    return rs.orElse(null);


//    ResultSet rs2 = rs.get();
////
//    RowSetFactory factory = RowSetProvider.newFactory();
//    CachedRowSet rowset = factory.createCachedRowSet();
//    rowset.populate(rs2);
//    ResultSet rsForPrinting = rowset.createCopy();
//    return rsForPrinting;
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

  public LoadingCache<String, Optional<ResultSet>> getCache() {
    return cache;
  }
}
