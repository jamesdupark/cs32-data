package edu.brown.cs.student.main.DBProxy;

import edu.brown.cs.student.main.Commands.DatabaseCommands.InvalidTablePermissionException;
import edu.brown.cs.student.main.Recommender.Stud.DatabaseStudent;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class to establish a connection to the Student Database and retrieve a List of
 * DatabaseStudents that is passed to the Recommender System.
 */
public class DBStudentGenerator {
  /**
   * Connection used to establish a connection to the database through a filepath.
   */
  private static Connection conn = null;
  /**
   * Filepath of the database.
   */
  private String filepath;
  /**
   * Map that keys on table name in the student database to its corresponding
   * table permission in the database.
   */
  private final Map<String, String> indexTables = new HashMap<>() {{
      put("names", "R");
      put("traits", "R");
      put("skills", "R");
      put("interests", "R");
    }};
  /**
   * Map that keys on the SQL command used in the SQL Query that retrieves the
   * List of DatabaseStudents to its respective table name in the database.
   */
  private final Map<String, String> commandToTable = new HashMap<>() {{
      put("SELECT", "names");
      put("SELECT", "traits");
      put("SELECT", "skills");
      put("SELECT", "interests");
    }};

  /**
   * Constructor for the DBStudentGenerator.
   * @param filepath String representing the filepath of the Student Database.
   */
  public DBStudentGenerator(String filepath) {
    // validate filepath is to Student Database
    assert filepath.equals("data/recommendation/sql/data.sqlite3") : "Filepath must be"
        + "the filepath to the Student Database!";
    this.filepath = filepath;
  }

  /**
   * Method to retrieve a list of DatabaseStudents that is passed to the Recommender
   * System. This list is obtained from executing a SQL query that retrieves all the
   * fields from the Student Database.
   * @return a list of DatabaseStudents with all fields in the Database.
   */
  public List<DatabaseStudent> getDBStudents() {
    try {
      String urlToDB = "jdbc:sqlite:" + filepath;
      conn = DriverManager.getConnection(urlToDB);
      // instruct database to enforce foreign keys during operations and should be present
      Statement stat = conn.createStatement();
      stat.executeUpdate("PRAGMA foreign_keys=ON;");
      Proxy proxy = new Proxy(this.filepath, this.indexTables);
      proxy.connectDB();
      String sqlQuery = "SELECT names.id, names.name, email, LOWER(type_of_attribute), "
          + "trait, skill, interest FROM names JOIN traits ON names.id = traits.id JOIN "
          + "skills ON names.id = skills.id JOIN interests ON names.id = interests.id "
          + "ORDER BY names.id DESC;";
      if (proxy.validateQuery(this.commandToTable)) {
        // valid query
        ResultSet rs = proxy.execQuery(sqlQuery);
        return makeDatabaseStudentList(rs);
      } else {
        // sql table does not have this level of permission
        throw new InvalidTablePermissionException("ERROR: SQL Table does not "
            + "have the level of permission");
      }
    } catch (SQLException e) {
      System.err.println("ERROR: " + e.getMessage());
    } catch (InvalidTablePermissionException e) {
      System.err.println(e.getMessage());
    } catch (FileNotFoundException e) {
      System.err.println("ERROR: " + e.getMessage());
    } catch (ClassNotFoundException e) {
      System.err.println("ERROR: " + e.getMessage());
    }
    return null;
  }

  /**
   * Method to make a list of Database Students from a ResultSet.
   * @param rs the ResultSet containing all the students that is returned from executing
   *           the SQL Query
   * @return the list of Database Students from the ResultSet
   * @throws SQLException if there is an error related to the SQL query
   */
  public List<DatabaseStudent> makeDatabaseStudentList(ResultSet rs) throws SQLException {
    Map<String, DatabaseStudent> idToStud = new HashMap<>();
    while (rs.next()) {
      DatabaseStudent student = new DatabaseStudent();
      String id = rs.getString(1);
      String name = rs.getString(2);
      String email = rs.getString(3);
      String attrType = rs.getString(4);
      String trait = rs.getString(5);
      String skill = rs.getString(6);
      final int interestRSCol = 7;
      String interest = rs.getString(interestRSCol);
      if (idToStud.containsKey(id)) {
        // key exists so add to strengths, weaknesses, interests fields
        student = idToStud.get(id);
      } else {
        student.setId(id);
        student.setName(name);
        student.setEmail(email);
        student.setSkill(skill);
        student.setInterest(interest);
      }
      if (!student.getInterest().contains(interest)) {
        student.setInterest(interest);
      }
      if (attrType.equals("weaknesses") && (student.getWeaknesses() == null
          || !student.getWeaknesses().contains(trait))) {
        student.setWeaknesses(trait);
      } else if (attrType.equals("strengths") && (student.getStrengths() == null
          || !student.getStrengths().contains(trait))) {
        student.setStrengths(trait);
      }
      idToStud.put(id, student);
    }
    List<DatabaseStudent> dbStud = new ArrayList<>();
    idToStud.forEach((k, v) -> {
      dbStud.add(v);
    });
    Collections.sort(dbStud);
    return dbStud;
  }
}
