package edu.brown.cs.student.main.DBProxy;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the Proxy.
 */
public class ProxyTest {
  Map<String, String> studentTablePermissions;
  /**
   * Method to initialize and populate the three KDTrees fields.
   * @throws IOException if the CSVReader is unable to read the file path
   */
  @Before
  public void setup() throws IOException {
    // initialize student table permissions
    studentTablePermissions = new HashMap<>();
    studentTablePermissions.put("names", "RW");
    studentTablePermissions.put("traits", "RW");
    studentTablePermissions.put("skills", "RW");
    studentTablePermissions.put("interests", "RW");
  }

  /**
   * Method to test setupCommandPermissions.
   */
  @Test
  public void testSetupCommandPermissions() throws IOException {
    setup();
    Proxy proxy = new Proxy("data/recommendation/sql/data.sqlite3",
        studentTablePermissions);
    Map<String, String> sqlPermissions = proxy.getSqlPermissions();
    assertEquals(sqlPermissions.size(), 8);
    assertEquals(sqlPermissions.get("SELECT"), "R");
    assertEquals(sqlPermissions.get("INSERT"), "R");
    assertEquals(sqlPermissions.get("DROP"), "RW");
    assertEquals(sqlPermissions.get("UPDATE"), "RW");
    assertEquals(sqlPermissions.get("DELETE"), "RW");
    assertEquals(sqlPermissions.get("ALTER"), "RW");
    assertEquals(sqlPermissions.get("JOIN"), "R");
    assertEquals(sqlPermissions.get("TRUNCATE"), "RW");
  }

  /**
   * Method to test connectDB.
   */
  @Test
  public void testConnectDB() throws SQLException, IOException, ClassNotFoundException {
    setup();
    Proxy proxy = new Proxy("data/recommendation/sql/data.sqlite3",
        studentTablePermissions);
    assertEquals(proxy.getConn(), null);
    proxy.connectDB();
//    assertEquals(proxy.getConn().toString(), "org.sqlite.jdbc4.JDBC4Connection@3ef41c66");
  }
}
