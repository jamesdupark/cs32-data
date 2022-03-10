package edu.brown.cs.student.main.DBProxy.Cache;

import edu.brown.cs.student.main.Commands.DatabaseCommands.ConnectDB;
import edu.brown.cs.student.main.Commands.DatabaseCommands.DataCommands;
import edu.brown.cs.student.main.Commands.DatabaseCommands.HoroscopeCommands;
import edu.brown.cs.student.main.DBProxy.Proxy;
import org.checkerframework.checker.units.qual.C;
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
  public void testRepeatedQueriesEnterCache() throws SQLException, FileNotFoundException,
      ClassNotFoundException {
    setupStudTablePerm();
    DataCommands dataCmd = new DataCommands();
    dataCmd.setProxy(dataCmd.connectProxy("data/recommendation/sql/data.sqlite3",
        studTablePermissions));
    dataCmd.setConn(dataCmd.getProxy().getConn());
    dataCmd.getProxy().connectDB();

    // run a sql query
    dataCmd.selectAllCmd(1);
    assertEquals(dataCmd.getProxy().getCache().size(), 1);

    // repeat sql query
    dataCmd.selectAllCmd(1);
    assertEquals(dataCmd.getProxy().getCache().size(), 1);

    // run a new sql query
    dataCmd.findTraitAndSkillsCmd(1);
    assertEquals(dataCmd.getProxy().getCache().size(), 2);

    // run a new sql query
    dataCmd.findSameInterestsCmd(1);
    assertEquals(dataCmd.getProxy().getCache().size(), 3);

    // repeat old queries
    dataCmd.findSameInterestsCmd(1);
    dataCmd.findTraitAndSkillsCmd(1);
    dataCmd.findTraitAndSkillsCmd(1);
    dataCmd.selectAllCmd(1);
    assertEquals(dataCmd.getProxy().getCache().size(), 3);
  }

  /**
   * Method to test that the Cache clears upon a Write command.
   * @throws SQLException if there is an error with the SQL query
   * @throws FileNotFoundException if the input filepath to the Proxy is invalid
   * @throws ClassNotFoundException if there is no definition for the class specified to the proxy
   */
  @Test
  public void testCacheClearsOnWriteCmd() throws SQLException, FileNotFoundException, ClassNotFoundException {
    setupHoroTalblePerm();
    HoroscopeCommands horoCmd = new HoroscopeCommands();
    horoCmd.setProxy(horoCmd.connectProxy("data/recommendation/sql/horoscopes.sqlite3",
        horoTablePermissions));
    horoCmd.setConn(horoCmd.getProxy().getConn());
    horoCmd.getProxy().connectDB();

    // run a sql query that does not need write command
    horoCmd.findTARoleForAHoroscopeCmd(1);
    assertEquals(horoCmd.getProxy().getCache().size(), 1);

    // run a sql query that uses write command
    String[] argv = new String[3];
    argv[0] = "data/recommendation/sql/horoscopes.sqlite3";
    argv[1] = "HTA";
    argv[2] = "Dylan";
    horoCmd.updateTARoleCmd(argv, 3);
    assertEquals(horoCmd.getProxy().getCache().size(), 0);
  }
}
