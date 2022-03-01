package edu.brown.cs.student.main.DBParsing;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Proxy {
  private static Connection conn = null;
  private final String filepath;
  private Map<String, String> tablePermissions;
  private final Map<String, String> sqlPermissions;

  public Proxy(String filepath, Map<String, String> tablePermissions) {
    this.filepath = filepath;
    this.tablePermissions = tablePermissions;
    sqlPermissions = new HashMap<>();
    setupCommandPermissions();
  }

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

  public void connectDB() throws ClassNotFoundException, SQLException, FileNotFoundException {
    BufferedReader reader = new BufferedReader(new FileReader(filepath));
    System.out.println("Inside connect DB Proxy!");
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filepath;
    conn = DriverManager.getConnection(urlToDB);
  }

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

  public ResultSet execQuery(String sqlQuery) throws SQLException {
    PreparedStatement rolefinder = conn.prepareStatement(sqlQuery);
    ResultSet rs = rolefinder.executeQuery();
    return rs;
  }

  public Connection getConn() {
    return conn;
  }
}
