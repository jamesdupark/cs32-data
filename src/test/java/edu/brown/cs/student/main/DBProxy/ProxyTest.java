package edu.brown.cs.student.main.DBProxy;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the Proxy.
 */
public class ProxyTest {
  /**
   * Method to test setupCommandPermissions.
   */
  @Test
  public void testSetupCommandPermissions() {
    Map<String, String> tablePermissions = new HashMap<>();
    tablePermissions.put("names", "RW");
    tablePermissions.put("traits", "RW");
    tablePermissions.put("skills", "RW");
    tablePermissions.put("interests", "RW");

//    Proxy proxy = new Proxy("../../data/recommendation/sql/data.sqlite3", tablePermissions);
//    Map<String, String> sqlPermissions = proxy.getSqlPermissions();
//    assertEquals(sqlPermissions.size(), 8);
//    assertEquals(sqlPermissions.get("SELECT"), "R");
//    assertEquals(sqlPermissions.get("INSERT"), "R");
//    assertEquals(sqlPermissions.get("DROP"), "RW");
//    assertEquals(sqlPermissions.get("UPDATE"), "RW");
//    assertEquals(sqlPermissions.get("DELETE"), "RW");
//    assertEquals(sqlPermissions.get("ALTER"), "RW");
//    assertEquals(sqlPermissions.get("JOIN"), "R");
//    assertEquals(sqlPermissions.get("TRUNCATE"), "RW");
  }
}
