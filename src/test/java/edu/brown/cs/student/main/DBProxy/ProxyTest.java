package edu.brown.cs.student.main.DBProxy;

import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * Class to test the Proxy.
 */
public class ProxyTest {
  /**
   * HashMap mapping from String table name in the Student Database to its
   * corresponding and respective table permission.
   */
  private Map<String, String> studentTablePermissions;
  /**
   * Method to initialize and populate the studentTablePermissions field.
   */
  @Before
  public void setup() {
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
    assertEquals(sqlPermissions.get("INSERT"), "W");
    assertEquals(sqlPermissions.get("DROP"), "RW");
    assertEquals(sqlPermissions.get("UPDATE"), "RW");
    assertEquals(sqlPermissions.get("DELETE"), "RW");
    assertEquals(sqlPermissions.get("ALTER"), "RW");
    assertEquals(sqlPermissions.get("JOIN"), "R");
    assertEquals(sqlPermissions.get("TRUNCATE"), "RW");
  }

  /**
   * Method to test connectDB throws an exception for incorrect file path.
   */
  @Test(expected = FileNotFoundException.class)
  public void testConnectDBThrowsException() throws SQLException, IOException, ClassNotFoundException {
    setup();
    Proxy proxy = new Proxy("invalid/filepath.sqlite3",
        studentTablePermissions);
    proxy.connectDB();
  }

  /**
   * Method to test connectDB.
   */
  @Test
  public void testConnectDB() throws SQLException, IOException, ClassNotFoundException {
    setup();
    Proxy proxy = new Proxy("data/recommendation/sql/data.sqlite3",
        studentTablePermissions);
    proxy.connectDB();
    assertNotNull(proxy.getConn());
  }

  /**
   * Method to test hasWriteCommand.
   */
  @Test
  public void testHasWriteCommand() throws IOException {
    String sqlQueryNoWrite = "SELECT name, email, interest FROM names as n JOIN interests as i"
        + " on n.id = i.id WHERE i.interest = \'music\' ORDER BY name;";
    String sqlQueryWithWrite = "UPDATE tas SET role = \'HTA\' WHERE name = \'Dylan\';";
    String sqlQueryWithWrite2 = "DROP ALL";
    setup();
    Proxy proxy = new Proxy("data/recommendation/sql/data.sqlite3",
        studentTablePermissions);

    assertEquals(proxy.hasWriteCommand(sqlQueryNoWrite), false);
    assertEquals(proxy.hasWriteCommand(sqlQueryWithWrite), true);
    assertEquals(proxy.hasWriteCommand(sqlQueryWithWrite2), true);
  }

  /**
   * Method to test validateQuery throws exception for an empty table permission input.
   */
  @Test(expected = RuntimeException.class)
  public void testValidateQueryThrowsRuntimeEx() {
    Proxy proxy = new Proxy("data/recommendation/sql/data.sqlite3",
        new HashMap<>());
    proxy.validateQuery(new HashMap<>());
  }

  /**
   * Method to set up a command table for validateQuery test.
   */
  private void setupFindSameInterestsCommandTable(Map<String, String> commandToTable) {
    commandToTable.clear();
    commandToTable.put("SELECT", "names");
    commandToTable.put("JOIN", "interests");
  }

  /**
   * Method to test validateQuery.
   */
  @Test
  public void testValidateQuery() {
    Proxy proxy = new Proxy("data/recommendation/sql/data.sqlite3",
        studentTablePermissions);
    Map<String, String> commandToTable = new HashMap<>();
    setupFindSameInterestsCommandTable(commandToTable);
    assertEquals(proxy.validateQuery(commandToTable), true);
  }
}
