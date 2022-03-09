package edu.brown.cs.student.main.Commands.DatabaseCommands;

import edu.brown.cs.student.main.Commands.REPLCommands;
import edu.brown.cs.student.main.DBProxy.DBItems.DatabaseStudent;
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
public class DataCommands extends ConnectDB implements REPLCommands {
  /**
   * List of strings representing the command keywords supported by this class.
   */
  private final List<String> commands =
      List.of("connect_db_data", "select_names_data",
          "find_same_interests_data", "find_same_traits_and_skills_data");
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

  private String database = null;

  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    // verifying that command is a supported one; should never fail
    assert cmd.equals(argv[0]) && commands.contains(cmd);
    try {
      switch (cmd) {
        case "connect_db_data":
          connectDBCmd(argv, argc);
          break;
        case "select_names_data":
          this.selectNamesCmd(argv, argc);
          break;
        case "find_same_interests_data":
          this.findSameInterestsCmd(argv, argc);
          break;
        case "find_same_traits_and_skills_data":
          this.findTraitAndSkillsCmd(argv, argc);
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

  public void connectDBCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc < 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments");
    }
    try {
      this.database = argv[1];
      // check correct number of args
      if (!getDbIndex().containsKey(database)) {
        System.out.println("ERROR: Database Proxy does not support commands for this database");
        return;
      }
      if (!this.database.equals("data/recommendation/sql/data.sqlite3")) {
        System.out.println("ERROR: Filepath does not correspond to database");
        return;
      }
      checkConnectionHasCorrectNumTablePerm(database, argc);
      Map<String, String> tablePermissions = setUpTablePerm(argv, database);
      proxy = new Proxy(database, tablePermissions);
      proxy.connectDB();
      conn = proxy.getConn();
      System.out.println("Successful connection made to " + database);
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
    if (conn == null || !database.equals("data/recommendation/sql/data.sqlite3")) {
      throw new RuntimeException("ERROR: Student database has not been connected!");
    }
    try {
      String sqlQuery = "SELECT name FROM names";
      // clear commandToTable and add to it
      commandToTable.clear();
      commandToTable.put("SELECT", "names");
      if (proxy.validateQuery(commandToTable)) {
        // valid query
        CachedRowSet rowset = proxy.cacheExec(sqlQuery);
        if (rowset == null) {
          System.out.println("Caller rowset null");
          return;
        }
        ResultSet rsForPrinting = rowset.createCopy();
        List<DatabaseStudent> dbStud = new ArrayList<>();
        while (rsForPrinting.next()) {
          DatabaseStudent student = new DatabaseStudent();
          String name = rsForPrinting.getString(1);
          student.setName(name);
          dbStud.add(student);
          System.out.println(name);
        }
        System.out.println("Cache size " + proxy.getCache().size());
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
    if (conn == null || !database.equals("data/recommendation/sql/data.sqlite3")) {
      throw new RuntimeException("ERROR: Student database has not been connected!");
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
        CachedRowSet rowSet = proxy.cacheExec(sqlQuery);
        ResultSet rsForPrinting = rowSet.createCopy();
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

  /**
   * Executes the "find_same_interests" command which queries off the Students Database
   * for students with the same interests as a target interest. If successful,
   * the ids, names, and interests of all the students should be printed.
   * Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void findTraitAndSkillsCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 1) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
          + "Expected 1 argument but got " + argc);
    }
    // check if connection to database has been made
    if (conn == null || !database.equals("data/recommendation/sql/data.sqlite3")) {
      throw new RuntimeException("ERROR: Student database has not been connected!");
    }
    try {
      final String searchTrait = "friendly";
      final String searchSkill = "algorithms";
      String sqlQuery = "SELECT name, LOWER(type_of_attribute) as type_attribute, trait, skill "
          + "FROM names, traits as t, skills as s WHERE t.id = s.id AND trait = \'"
          + searchTrait + "\' AND skill = \'" + searchSkill + "\' ORDER BY name;";
      System.out.println(sqlQuery);
      // clear commandToTable and add to it
      commandToTable.clear();
      commandToTable.put("SELECT", "names");
      commandToTable.put("SELECT", "traits");
      commandToTable.put("SELECT", "skills");
      commandToTable.put("JOIN", "traits");
      commandToTable.put("JOIN", "skills");
      if (proxy.validateQuery(commandToTable)) {
        // valid query
        CachedRowSet rowSet = proxy.cacheExec(sqlQuery);
        ResultSet rsForPrinting = rowSet.createCopy();
        List<DatabaseStudent> dbStud = new ArrayList<>();
        while (rsForPrinting.next()) {
          DatabaseStudent student = new DatabaseStudent();
          String name = rsForPrinting.getString(1);
          String attrType = rsForPrinting.getString(2);
          String trait = rsForPrinting.getString(3);
          String skill = rsForPrinting.getString(4);
          student.setName(name);
          student.setAttrType(attrType);
          student.setTrait(trait);
          student.setSkill(skill);
          dbStud.add(student);
          System.out.println(name + ", (" + attrType + ") " + trait + ", " + skill);
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
