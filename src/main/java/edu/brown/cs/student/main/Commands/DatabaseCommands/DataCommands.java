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
 * to the Student Database.
 */
public class DataCommands extends ConnectDB implements REPLCommands {
  /**
   * List of strings representing the command keywords supported by this class.
   */
  private final List<String> commands = List.of("connect_db_data", "select_all_data",
      "find_same_interests_data", "find_same_traits_and_skills_data");
  /**
   * Connection used to establish a connection to the database through a filepath.
   */
  private static Connection conn = null;
  /**
   * Proxy that sits between the client and the database used to add a layer of
   * abstraction in the program so that users do not have to directly interact
   * with the SQL database.
   */
  private Proxy proxy = null;
  /**
   * Map mapping from a SQL Command to the corresponding database table that it
   * applies to. This Map is used when checking whether a table supports the level
   * of access to execute the SQL Command.
   */
  private Map<String, String> commandToTable = new HashMap<>();
  /**
   * String representing the filepath of the Student database.
   */
  private final String filepath = "data/recommendation/sql/data.sqlite3";
  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    // verifying that command is a supported one; should never fail
    assert cmd.equals(argv[0]) && commands.contains(cmd);
    try {
      switch (cmd) {
        case "connect_db_data":
          // check correct number of args
          if (argc < 2) {
            throw new IllegalArgumentException("ERROR: Incorrect number of arguments");
          }
          this.proxy = connectProxy(argv[1], setUpTablePerm(argv, argv[1]));
          connectDBCmd(argv, argc, this.filepath);
          conn = this.proxy.getConn();
          break;
        case "select_all_data":
          this.selectAllCmd(argc);
          break;
        case "find_same_interests_data":
          this.findSameInterestsCmd(argc);
          break;
        case "find_same_traits_and_skills_data":
          this.findTraitAndSkillsCmd(argc);
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
   * Method to set up the command table for the "select_all_data" command. That is,
   * the SQL Commands in the SQL for the command will be mapped to the respective
   * table name.
   */
  private void setupSelectAllCommandTable() {
    commandToTable.clear();
    commandToTable.put("SELECT", "names");
    commandToTable.put("SELECT", "traits");
    commandToTable.put("SELECT", "skills");
    commandToTable.put("SELECT", "interests");
  }
  /**
   * Executes the "select_all_data" command which queries off the Students Database.
   * If successful, the names of all the students should be printed.
   * Prints informative error message upon failure.
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void selectAllCmd(int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 1) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
          + "Expected 1 argument but got " + argc);
    }
    // check if connection to database has been made
    checkDatabaseConnected();
    try {
      String sqlQuery = "SELECT names.id, names.name, email, LOWER(type_of_attribute), "
          + "trait, skill, interest FROM names JOIN traits ON names.id = traits.id JOIN "
          + "skills ON names.id = skills.id JOIN interests ON names.id = interests.id "
          + "ORDER BY names.id DESC;";
      setupSelectAllCommandTable();
      if (proxy.validateQuery(this.commandToTable)) {
        // valid query
        CachedRowSet rowSet = proxy.cacheExec(sqlQuery);
        printResultSet(rowSet, 6);
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
   * Method to set up the command table for the "find_same_interests_data" command. That is,
   * the SQL Commands in the SQL for the command will be mapped to the respective
   * table name.
   */
  private void setupFindSameInterestsCommandTable() {
    commandToTable.clear();
    commandToTable.put("SELECT", "names");
    commandToTable.put("JOIN", "interests");
  }
  /**
   * Executes the "find_same_interests_data" command which queries off the Students
   * Database for students with the same interests as a target interest. If successful,
   * the ids, names, and interests of all the students should be printed.
   * Prints informative error message upon failure.
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void findSameInterestsCmd(int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 1) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
          + "Expected 1 argument but got " + argc);
    }
    // check if connection to database has been made
    checkDatabaseConnected();
    try {
      final String lookFor = "music";
      String sqlQuery = "SELECT name, email, interest FROM names as n JOIN interests as i"
          + " on n.id = i.id WHERE i.interest = \'" + lookFor + "\' ORDER BY name;";
      setupFindSameInterestsCommandTable();
      if (proxy.validateQuery(commandToTable)) {
        // valid query
        CachedRowSet rowSet = proxy.cacheExec(sqlQuery);
        printResultSet(rowSet, 3);
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
   * Method to set up the command table for the "find_same_traits_and_skills_data" command.
   * That is, the SQL Commands in the SQL for the command will be mapped to the respective
   * table name.
   */
  private void setupFindSameTraitsAndSkilsCommandTable() {
    commandToTable.clear();
    commandToTable.put("SELECT", "names");
    commandToTable.put("SELECT", "traits");
    commandToTable.put("SELECT", "skills");
    commandToTable.put("JOIN", "traits");
    commandToTable.put("JOIN", "skills");
  }
  /**
   * Executes the "find_same_traits_and_skills_data" command which queries off the Students
   * Database for students with the same traits and skills as a target trait and skill
   * respectively. If successful, the names, attribute types, traits, and skills of all
   * the students should be printed.
   * Prints informative error message upon failure.
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void findTraitAndSkillsCmd(int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 1) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
          + "Expected 1 argument but got " + argc);
    }
    // check if connection to database has been made
    checkDatabaseConnected();
    try {
      final String searchTrait = "friendly";
      final String searchSkill = "algorithms";
      String sqlQuery = "SELECT name, LOWER(type_of_attribute) as type_attribute, trait, skill "
          + "FROM names, traits as t, skills as s WHERE t.id = s.id AND trait = \'"
          + searchTrait + "\' AND skill = \'" + searchSkill + "\' ORDER BY name;";
      setupFindSameTraitsAndSkilsCommandTable();
      if (proxy.validateQuery(this.commandToTable)) {
        // valid query
        CachedRowSet rowSet = proxy.cacheExec(sqlQuery);
        printResultSet(rowSet, 4);
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
    if (conn == null || !this.filepath.equals("data/recommendation/sql/data.sqlite3")) {
      throw new RuntimeException("ERROR: Student database has not been connected!");
    }
  }
  @Override
  public List<String> getCommandsList() {
    return this.commands;
  }
}
