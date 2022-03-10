package edu.brown.cs.student.main.DBProxy.Cache;

import edu.brown.cs.student.main.Commands.DatabaseCommands.DataCommands;
import edu.brown.cs.student.main.Commands.DatabaseCommands.HoroscopeCommands;
import org.junit.Before;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Class to test that caching works.
 */
public class CacheTest {
  /**
   * HashMap mapping from String table name in the Horoscopes Database to its
   * corresponding and respective table permission.
   */
  private Map<String, String> horoTablePermissions;
  /**
   * HashMap mapping from String table name in the Student Database to its
   * corresponding and respective table permission.
   */
  private Map<String, String> studTablePermissions;
  /**
   * Method to initialize and populate the horoTablePermissions field.
   */
  @Before
  public void setupHoroTalblePerm() {
    // initialize horoscope table permissions
    horoTablePermissions = new HashMap<>();
    horoTablePermissions.put("sqlite_sequence", "RW");
    horoTablePermissions.put("horoscopes", "RW");
    horoTablePermissions.put("ta_horoscope", "RW");
    horoTablePermissions.put("tas", "RW");
  }
  /**
   * Method to initialize and populate the studTablePermissions field.
   */
  @Before
  public void setupStudTablePerm() {
    // initialize student table permissions
    studTablePermissions = new HashMap<>();
    studTablePermissions.put("names", "RW");
    studTablePermissions.put("traits", "RW");
    studTablePermissions.put("skills", "RW");
    studTablePermissions.put("interests", "RW");
  }

  /**
   * Method to test that repeated SQL queries are Cached.
   */
  @Test
  public void testRepeatedQueriesEnterCache() {
    DataCommands dataCmd = new DataCommands();
    String[] argv = new String[6];
    argv[0] = "connect_db_data";
    argv[1] = "data/recommendation/sql/data.sqlite3";
    argv[2] = "RW";
    argv[3] = "RW";
    argv[4] = "RW";
    argv[5] = "RW";
    dataCmd.executeCmds("connect_db_data", argv, 6);

    // run a sql query
    String[] argv2 = new String[1];
    argv2[0] = "select_all_data";
    dataCmd.executeCmds("select_all_data", argv2, 1);
    assertEquals(dataCmd.getProxy().getCache().size(), 1);

    // repeat sql query
    dataCmd.executeCmds("select_all_data", argv2, 1);
    assertEquals(dataCmd.getProxy().getCache().size(), 1);

    // run a new sql query
    String[] argv3 = new String[1];
    argv3[0] = "find_same_traits_and_skills_data";
    dataCmd.executeCmds("find_same_traits_and_skills_data", argv3, 1);
    assertEquals(dataCmd.getProxy().getCache().size(), 2);

    // run a new sql query
    String[] argv4 = new String[1];
    argv4[0] = "find_same_interests_data";
    dataCmd.executeCmds("find_same_interests_data", argv4, 1);
    assertEquals(dataCmd.getProxy().getCache().size(), 3);

    // repeat old queries
    dataCmd.executeCmds("find_same_interests_data", argv4, 1);
    dataCmd.executeCmds("find_same_traits_and_skills_data", argv3, 1);
    dataCmd.executeCmds("find_same_traits_and_skills_data", argv3, 1);
    dataCmd.executeCmds("select_all_data", argv2, 1);
    assertEquals(dataCmd.getProxy().getCache().size(), 3);
  }

  /**
   * Method to test that the Cache clears upon a Write command.
   * @throws SQLException if there is an error with the SQL query
   * @throws FileNotFoundException if the input filepath to the Proxy is invalid
   * @throws ClassNotFoundException if there is no definition for the class specified to the proxy
   */
  @Test
  public void testCacheClearsOnWriteCmd() {
    HoroscopeCommands horoCmd = new HoroscopeCommands();
    String[] argv = new String[6];
    argv[0] = "connect_db_horo";
    argv[1] = "data/recommendation/sql/horoscopes.sqlite3";
    argv[2] = "RW";
    argv[3] = "RW";
    argv[4] = "RW";
    argv[5] = "RW";
    horoCmd.executeCmds("connect_db_horo", argv, 6);

    // run a sql query that does not need write command
    String[] argv2 = new String[1];
    argv2[0] = "find_tas_w_horoscope_horo";
    horoCmd.executeCmds("find_tas_w_horoscope_horo", argv2, 1);
    assertEquals(horoCmd.getProxy().getCache().size(), 1);

    // run a sql query that uses write command
    String[] argv3 = new String[3];
    argv3[0] = "update_ta_role_horo";
    argv3[1] = "HTA";
    argv3[2] = "Dylan";
    horoCmd.executeCmds("update_ta_role_horo", argv3, 3);
    assertEquals(horoCmd.getProxy().getCache().size(), 0);
  }
}
