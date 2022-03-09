package edu.brown.cs.student.main.Commands;

import edu.brown.cs.student.main.DBProxy.DBItems.DatabaseStudent;
import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseHoroscopesTables;
import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseStudentTables;
import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseTables;
import edu.brown.cs.student.main.DBProxy.DBTables.DatabaseZooTables;
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
 * REPLCommands class that packages commands related to SQL queries.
 */
public class SQLCommands implements REPLCommands {
  /**
   * List of strings representing the command keywords supported by this class.
   */
  private final List<String> commands =
      List.of("connect_db", "select_names", "find_same_interests");
  /**
   * HashMap mapping from the database filepath supported by this class to an index.
   */
  private final Map<String, Integer> dbIndex = new HashMap<>() {{
      put("data/recommendation/sql/data.sqlite3", 0);
      put("data/recommendation/sql/horoscopes.sqlite3", 1);
      put("data/recommendation/sql/zoo.sqlite3", 2);
    }};
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

  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    // verifying that command is a supported one; should never fail
    assert cmd.equals(argv[0]) && commands.contains(cmd);
    try {
      switch (cmd) {
        case "connect_db":
          this.connectDBCmd(argv, argc);
          break;
        case "select_names":
//          this.selectNamesCmd(argv, argc);
          break;
        case "find_same_interests":
          this.findSameInterestsCmd(argv, argc);
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

  /**
   * Method to make the DatabaseTables corresponding to the filepath of the database in
   * order to retrieve the Map of the index to the corresponding table name. This information
   * is then used for the "connect_db" command.
   * @param filepath String representing the filepath of the database to establish a
   *                 connection with
   * @return the DatabaseTables corresponding to the filepath of the database
   */
  public DatabaseTables getDBTables(String filepath) {
    int index = dbIndex.get(filepath);
    switch (index) {
      case 0:
        return new DatabaseStudentTables();
      case 1:
        return new DatabaseHoroscopesTables();
      case 2:
        return new DatabaseZooTables();
      default:
        throw new RuntimeException("ERROR: Connection to this DB is not supported.");
    }
  }

  /**
   * Executes the "connect_db" command by creating a new Proxy that will be used to
   * connect to the database when given the database filepath. If successful, updates
   * the Connection and Proxy fields and prints the connection made to the database.
   * Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void connectDBCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc < 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments");
    }
    try {
      String filepath = argv[1];
      // check correct number of args
      if (!dbIndex.containsKey(argv[1])) {
        System.out.println("ERROR: Database Proxy does not support commands for this database");
        return;
      }
      DatabaseTables dbTables = this.getDBTables(argv[1]);
      Map<Integer, String> dbIndexTables = dbTables.getIndexTables();
      if (dbIndexTables.size() != argc - 2) {
        throw new IllegalArgumentException("ERROR: Incorrect number of arguments");
      }

      Map<String, String> tablePermissions = new HashMap<>();

      for (int i = 2; i < argv.length; i++) {
        if (!argv[i].equals("R") && !argv[i].equals("W") && !argv[i].equals("RW")) {
          throw new RuntimeException("ERROR: permission is not <R> or <W> or <RW>");
        }
        tablePermissions.put(dbIndexTables.get(i - 2), argv[i]);
      }
      proxy = new Proxy(filepath, tablePermissions);
      proxy.connectDB();
      conn = proxy.getConn();
      System.out.println("Successful connection made to " + filepath);
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
   * Executes the "select_names" command which queries off the Students Database. If successful,
   * the names of all the students should be printed.
   * Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void selectNamesCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 1) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
          + "Expected 1 argument but got " + argc);
    }

    // check if connection to database has been made
    if (conn == null) {
      throw new RuntimeException("ERROR: Database has not been connected!");
    }
    try {
      String sqlQuery = "SELECT name FROM names";
      // clear commandToTable and add to it
      commandToTable.clear();
      commandToTable.put("SELECT", "names");
      if (proxy.validateQuery(commandToTable)) {
        // valid query
        System.out.println("ABOVE RS");
        CachedRowSet rowset = proxy.cacheExec(sqlQuery);
        if (rowset == null) {
          System.out.println("Caller rowset null");
          return;
        }
        ResultSet rsForPrinting = rowset.createCopy();
        System.out.println("Get here?!!!!");
        System.out.println(rsForPrinting);
        System.out.println("BELOW RS");
        List<DatabaseStudent> dbStud = new ArrayList<>();
        while (rsForPrinting.next()) {
          System.out.println("inside while");
          DatabaseStudent student = new DatabaseStudent();
          String name = rsForPrinting.getString(1);
          student.setName(name);
          dbStud.add(student);
          System.out.println(name);
        }
        System.out.println("Before put " + proxy.getCache().size());
        System.out.println("reached end");
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
   * Executes the "find_same_interests" command which queries off the Students Database
   * for students with the same interests as a target interest. If successful,
   * the ids, names, and interests of all the students should be printed.
   * Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void findSameInterestsCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 1) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
          + "Expected 1 argument but got " + argc);
    }
    // check if connection to database has been made
    if (conn == null) {
      throw new RuntimeException("ERROR: Database has not been connected!");
    }
    try {
      final String lookFor = "music";
      String sqlQuery = "SELECT name, email, interest FROM names as n JOIN interests as i"
          + " on n.id = i.id WHERE i.interest = \'" + lookFor + "\' ORDER BY name;";
      // clear commandToTable and add to it
      commandToTable.clear();
      commandToTable.put("SELECT", "names");
      commandToTable.put("JOIN", "interests");
      if (proxy.validateQuery(commandToTable)) {
        // valid query
        System.out.println("ABOVE RS");
        CachedRowSet rowSet = proxy.cacheExec(sqlQuery);
        ResultSet rsForPrinting = rowSet.createCopy();
        System.out.println("BELOW RS");
        List<DatabaseStudent> dbStud = new ArrayList<>();
        while (rsForPrinting.next()) {
          DatabaseStudent student = new DatabaseStudent();
          String name = rsForPrinting.getString(1);
          String email = rsForPrinting.getString(2);
          String interest = rsForPrinting.getString(3);
          student.setName(name);
          student.setEmail(email);
          student.setInterest(interest);
          dbStud.add(student);
          System.out.println(name + " " + email + " " + interest);
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
  public List<String> getCommandsList() {
    return this.commands;
  }
}
