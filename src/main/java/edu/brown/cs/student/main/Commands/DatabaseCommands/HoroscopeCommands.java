package edu.brown.cs.student.main.Commands.DatabaseCommands;

import edu.brown.cs.student.main.Commands.REPLCommands;
import edu.brown.cs.student.main.DBProxy.DBItems.DatabaseHoroscope;
import edu.brown.cs.student.main.DBProxy.Proxy;

import javax.sql.rowset.CachedRowSet;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
          this.connectDBCmd(argv, argc);
          break;
        case "find_tas_w_horoscope_horo":
          this.findTARoleForAHoroscopeCmd(argv, argc);
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
    } catch (RuntimeException e) {
      System.err.println(e.getMessage());
    }
  }

  @Override
  public void connectDBCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc < 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments");
    }
    try {
      // check correct number of args
      if (!getDbIndex().containsKey(argv[1])) {
        System.out.println("ERROR: Database Proxy does not support commands for this database");
        return;
      }
      if (!argv[1].equals(this.filepath)) {
        System.out.println("ERROR: Filepath does not correspond to database");
        return;
      }
      checkConnectionHasCorrectNumTablePerm(argv[1], argc);
      Map<String, String> tablePermissions = setUpTablePerm(argv, argv[1]);
      proxy = new Proxy(argv[1], tablePermissions);
      proxy.connectDB();
      conn = proxy.getConn();
      System.out.println("Successful connection made to " + argv[1]);
      System.out.println(proxy);
    } catch (FileNotFoundException e) {
      System.err.println("ERROR: " + argv[1] + " is an invalid filename");
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }
  /**
   * Executes the "find_tas_w_horoscope_horo" command which queries off the Horoscopes
   * Database for TAs with the same horoscope as a target horoscope. If successful,
   * the TA name, TA role, and horoscope of al the matches should be printed.
   * Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void findTARoleForAHoroscopeCmd(String[] argv, int argc) {
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
      // clear commandToTable and add to it
      commandToTable.clear();
      commandToTable.put("SELECT", "tas");
      commandToTable.put("JOIN", "ta_horoscope");
      commandToTable.put("JOIN", "horoscopes");
      if (proxy.validateQuery(commandToTable)) {
        CachedRowSet rowSet = proxy.cacheExec(sqlQuery);
        ResultSet rsForPrinting = rowSet.createCopy();
        List<DatabaseHoroscope> dbHoro = new ArrayList<>();
        while (rsForPrinting.next()) {
          DatabaseHoroscope horo = new DatabaseHoroscope();
          String name = rsForPrinting.getString(1);
          String role = rsForPrinting.getString(2);
          String horoscope = rsForPrinting.getString(3);
          horo.setTaName(name);
          horo.setTaRole(role);
          horo.setHoroscope(horoscope);
          dbHoro.add(horo);
          System.out.println(name + ", " + role + ", " + horoscope);
        }
        System.out.println(proxy.getCache().size());
      } else {
        // error: sql table does not have this level of permission
        System.out.println("ERROR: SQL Table does not have the level of permission");
      }
    } catch (SQLException e) {
      System.err.println("ERROR: " + e.getMessage());
    } catch (ExecutionException e) {
      e.printStackTrace();
    }
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
      // clear commandToTable and add to it
      commandToTable.clear();
      commandToTable.put("UPDATE", "tas");
      if (proxy.validateQuery(commandToTable)) {
        // valid query
        CachedRowSet rowSet = proxy.cacheExec(sqlQuery);
        if (rowSet == null) {
          System.out.println("Success: TA's role has been updated!");
        }
        System.out.println(proxy.getCache().size());
      } else {
        // error: sql table does not have this level of permission
        System.out.println("ERROR: SQL Table does not have the level of permission");
      }
    } catch (SQLException e) {
      System.err.println("ERROR: " + e.getMessage());
    } catch (ExecutionException e) {
      e.printStackTrace();
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
