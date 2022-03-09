package edu.brown.cs.student.main.Commands.DatabaseCommands;

import edu.brown.cs.student.main.Commands.REPLCommands;
import edu.brown.cs.student.main.DBProxy.Proxy;

import javax.sql.rowset.CachedRowSet;
import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutionException;

/**
 * REPLCommands class that packages commands related to SQL queries pertaining to the
 * Zoo Database.
 */
public class ZooCommands extends ConnectDB implements REPLCommands {
  /**
   * List of strings representing the command keywords supported by this class.
   */
  private final List<String> commands =
      List.of("connect_db_zoo", "insert_animal_zoo", "count_animal_zoo");
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
   * String representing the filepath of the Zoo database.
   */
  private final String filepath = "data/recommendation/sql/zoo.sqlite3";

  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    // verifying that command is a supported one; should never fail
    assert cmd.equals(argv[0]) && commands.contains(cmd);
    try {
      switch (cmd) {
        case "connect_db_zoo":
          connectDBCmd(argv, argc);
          break;
        case "insert_animal_zoo":
          this.insertAnimalCmd(argv, argc);
          break;
        case "count_animal_zoo":
          this.countAnimalCmd(argv, argc);
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
   * Executes the "insert_animal_zoo" command which inserts an animal into the
   * Zoo Database. If successful, a success message informing the client that
   * the animal has been added to the database will be printed.
   * Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void insertAnimalCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 5) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
          + "Expected 5 argument but got " + argc);
    }
    // check if connection to database has been made
    checkDatabaseConnected();
    try {
      int id = Integer.parseInt(argv[1]);
      String name = argv[2];
      int age = Integer.parseInt(argv[3]);
      int height = Integer.parseInt(argv[4]);
      String sqlQuery = "INSERT INTO zoo VALUES (\'" + id + "\', + \'" + name + "\', + \'"
          + age + "\', + \'" + height + "\');";
      // clear commandToTable and add to it
      commandToTable.clear();
      commandToTable.put("INSERT", "zoo");
      if (proxy.validateQuery(commandToTable)) {
        // valid query
        CachedRowSet rowSet = proxy.cacheExec(sqlQuery);
        if (rowSet == null) {
          System.out.println("Success: animal has been added!");
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
   * Executes the "count_animal_zoo" command which counts the number of animals in
   * the Zoo Database. If successful, the number of animals should be printed.
   * Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void countAnimalCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 1) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
          + "Expected 1 argument but got " + argc);
    }
    // check if connection to database has been made
    checkDatabaseConnected();
    try {
      String sqlQuery = "SELECT COUNT(*) FROM zoo;";
      // clear commandToTable and add to it
      commandToTable.clear();
      commandToTable.put("SELECT", "zoo");
      if (proxy.validateQuery(commandToTable)) {
        // valid query
        CachedRowSet rowSet = proxy.cacheExec(sqlQuery);
        if (rowSet == null) {
          System.out.println("Caller rowset null");
          return;
        }
        ResultSet rsForPrinting = rowSet.createCopy();
        rsForPrinting.next();
        String count = rsForPrinting.getString(1);
        System.out.println("Number of animals is " + count);
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
  public void checkDatabaseConnected() {
    if (conn == null || !this.filepath.equals("data/recommendation/sql/zoo.sqlite3")) {
      throw new RuntimeException("ERROR: Zoo database has not been connected!");
    }
  }
  @Override
  public List<String> getCommandsList() {
    return this.commands;
  }
}
