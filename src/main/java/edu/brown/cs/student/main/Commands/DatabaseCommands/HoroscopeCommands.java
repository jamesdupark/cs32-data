package edu.brown.cs.student.main.Commands.DatabaseCommands;

import edu.brown.cs.student.main.Commands.REPLCommands;
import edu.brown.cs.student.main.DBProxy.Proxy;

import javax.sql.rowset.CachedRowSet;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * REPLCommands class that packages commands related to SQL queries pertaining
 * to the Horoscopes Database.
 */
public class HoroscopeCommands extends ConnectDB implements REPLCommands {
  /**
   * List of strings representing the command keywords supported by this class.
   */
  private final List<String> commands =
      List.of("connect_db_horo", "find_tas_w_horoscope_horo", "update_ta_role_horo");
  /** Connection used to establish a connection to the database through a filepath. */
  private static Connection conn = null;
  /** Proxy that sits between the client and the database used to add a layer of
   * abstraction in the program so that users do not have to directly interact
   * with the SQL database. */
  private Proxy proxy = null;
  /**
   * Map mapping from a SQL Command to the corresponding database table that it applies to.
   * This Map is used when checking whether a table supports the level of access to
   * execute the SQL Command.
   */
  private Map<String, String> commandToTable = new HashMap<>();
  /**
   * String representing the filepath of the Student database.
   */
  private String filepath = "data/recommendation/sql/horoscopes.sqlite3";

  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    // verifying that command is a supported one; should never fail
    assert cmd.equals(argv[0]) && commands.contains(cmd);
    try {
      switch (cmd) {
        case "connect_db_horo":
          // check correct number of args
          if (argc < 2) {
            throw new IllegalArgumentException("ERROR: Incorrect number of arguments");
          }
          this.proxy = connectProxy(argv[1], setUpTablePerm(argv, argv[1]));
          connectDBCmd(argv, argc, this.filepath);
          conn = this.proxy.getConn();
          break;
        case "find_tas_w_horoscope_horo":
          this.findTARoleForAHoroscopeCmd(argc);
          break;
        case "update_ta_role_horo":
          this.updateTARoleCmd(argv, argc);
          break;
        default:
          System.err.println("ERROR: Command not recognized.");
          break;
      }
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    }
  }
  /**
   * Method to set up the command table for the "find_tas_w_horoscope_horo" command. That is,
   * the SQL Commands in the SQL for the command will be mapped to the respective
   * table name.
   */
  private void setupFindTasWHoroscopeCommandTable() {
    commandToTable.clear();
    commandToTable.put("SELECT", "tas");
    commandToTable.put("JOIN", "ta_horoscope");
    commandToTable.put("JOIN", "horoscopes");
  }
  /**
   * Executes the "find_tas_w_horoscope_horo" command which queries off the Horoscopes
   * Database for TAs with the same horoscope as a target horoscope. If successful,
   * the TA name, TA role, and horoscope of al the matches should be printed.
   * Prints informative error message upon failure.
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void findTARoleForAHoroscopeCmd(int argc) {
    // check correct number of args
    if (argc != 1) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
          + "Expected 1 argument but got " + argc);
    }
    // check if connection to database has been made
    checkDatabaseConnected();
    try {
      final String horoscopeMatch = "Cancer";
      String sqlQuery = "SELECT name, role, horoscope FROM tas JOIN ta_horoscope AS tah "
          + "ON tas.id = tah.ta_id JOIN horoscopes AS h ON h.horoscope_id = tah.horoscope_id "
          + "WHERE horoscope = \'" + horoscopeMatch + "\';";
      setupFindTasWHoroscopeCommandTable();
      if (proxy.validateQuery(commandToTable)) {
        // valid query
        CachedRowSet rowSet = proxy.cacheExec(sqlQuery);
        printResultSet(rowSet, 3);
      } else {
        // sql table does not have this level of permission
        throw new InvalidTablePermissionException("ERROR: SQL Table does not "
            + "have the level of permission");
      }
    } catch (SQLException e) {
      System.err.println("ERROR: " + e.getMessage());
    } catch (ExecutionException e) {
      System.err.println("ERROR: " + e.getMessage());
    } catch (InvalidTablePermissionException e) {
      System.err.println(e.getMessage());
    }
  }
  /**
   * Method to set up the command table for the "update_ta_role_horo" command. That is,
   * the SQL Commands in the SQL for the command will be mapped to the respective
   * table name.
   */
  private void setupUpdateTaRoleCommandTable() {
    commandToTable.clear();
    commandToTable.put("UPDATE", "tas");
  }
  /**
   * Executes the "update_ta_role_horo" command which queries off the Horoscopes
   * Database for TAs with the same name as an input name (argv[2]). For all matches,
   * the role field will then be updated with the input role (argv[1]).
   * Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void updateTARoleCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 3) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
          + "Expected 3 argument but got " + argc);
    }
    // check if connection to database has been made
    checkDatabaseConnected();
    try {
      String newRole = argv[1];
      String name = argv[2];
      String sqlQuery = "UPDATE tas SET role = \'" + newRole + "\' WHERE name = \'" + name + "\';";
      setupUpdateTaRoleCommandTable();
      if (proxy.validateQuery(commandToTable)) {
        // valid query
        CachedRowSet rowSet = proxy.cacheExec(sqlQuery);
        if (rowSet == null) {
          System.out.println("Success: TA's role has been updated!");
        }
      } else {
        // sql table does not have this level of permission
        throw new InvalidTablePermissionException("ERROR: SQL Table does not "
            + "have the level of permission");
      }
    } catch (SQLException e) {
      System.err.println("ERROR: " + e.getMessage());
    } catch (ExecutionException e) {
      System.err.println("ERROR :" + e.getMessage());
    } catch (InvalidTablePermissionException e) {
      System.err.println(e.getMessage());
    }
  }
  @Override
  protected void checkDatabaseConnected() {
    if (conn == null || !this.filepath.equals("data/recommendation/sql/horoscopes.sqlite3")) {
      throw new RuntimeException("ERROR: Horoscopes database has not been connected!");
    }
  }
  @Override
  public List<String> getCommandsList() {
    return this.commands;
  }
}
