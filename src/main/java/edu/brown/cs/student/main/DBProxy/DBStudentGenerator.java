package edu.brown.cs.student.main.DBProxy;

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

public class DBStudentGenerator {
  /**
   * Connection used to establish a connection to the database through a filepath.
   */
  private static Connection conn = null;
  private String filepath = null;
  /**
   * Map that keys on an index to the corresponding table name in the database.
   * The order of the <key, value> pair is determined by the order of the
   * tables in the database.
   */
  private final Map<String, String> indexTables = new HashMap<>() {{
      put("names", "R");
      put("traits", "R");
      put("skills", "R");
      put("interests", "R");
    }};
  private final Map<String, String> commandToTable = new HashMap<>() {{
      put("SELECT", "names");
      put("SELECT", "traits");
      put("SELECT", "skills");
      put("SELECT", "interests");
    }};

  public DBStudentGenerator(String filepath) {
    this.filepath = filepath;
  }
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
          if (attrType.equals("weaknesses")) {
            if (student.getWeaknesses() == null || !student.getWeaknesses().contains(trait)) {
              student.setWeaknesses(trait);
            }
          } else if (attrType.equals("strengths")) {
            if (student.getStrengths() == null || !student.getStrengths().contains(trait)) {
              student.setStrengths(trait);
            }
          }
          idToStud.put(id, student);
        }
        List<DatabaseStudent> dbStud = new ArrayList<>();
        idToStud.forEach((k, v) -> {
          dbStud.add(v);
        });
        Collections.sort(dbStud);
        return dbStud;
      } else {
        // error: sql table does not have this level of permission
        System.out.println("ERROR: SQL Table does not have the level of permission");
        return null;
      }
    } catch (SQLException e) {
      System.err.println("ERROR: " + e.getMessage());
    } catch (FileNotFoundException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return null;
  }
}
