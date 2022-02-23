package edu.brown.cs.student.main.CSVParse.Builder;

import edu.brown.cs.student.main.KDimTree.KDNodes.KDNode;
import edu.brown.cs.student.main.KDimTree.KDNodes.StudentNode;

import java.util.List;

/**
 * Class to build a StudentNode of type KDNode that will eventually be inserted
 * into the KDTree.
 */
public class StudentNodeBuilder implements CSVBuilder<KDNode> {
  /** the index corresponding to the ID field in the student CSV Data. */
  private final int idIndex = 0;
  /** the index corresponding to the years of experience field in the student CSV Data. */
  private final int yearsExpIndex = 8;
  /** the index corresponding to the weekly available hour field in the student CSV Data. */
  private final int weeklyAvailIndex = 10;
  /** the index corresponding to the software confidence field in the student CSV Data. */
  private final int sweConfidenceIndex = 13;

  @Override
  public KDNode build(List<String> fields) {
    StudentNode newStudent = null;
    try {
      int id = Integer.parseInt(fields.get(idIndex));
      double yearsExp = Double.parseDouble(fields.get(yearsExpIndex));
      double weeklyAvail = Double.parseDouble(fields.get(weeklyAvailIndex));
      double sweConfidence = Double.parseDouble(fields.get(sweConfidenceIndex));
      newStudent = new StudentNode(id, yearsExp, weeklyAvail, sweConfidence);
    } catch (NumberFormatException ex) {
      System.err.println("ERROR: input int or double field unable to be converted from string");
      return null;
    } catch (NullPointerException ex) {
      System.err.println("ERROR: input int or double field was null");
      return null;
    }
    return newStudent;
  }

  @Override
  public String getColumnTitles() {
    return "id,name,email,gender,class_year,ssn,nationality,race,years_experience,"
        + "communication_style,weekly_avail_hours,meeting_style,meeting_time,"
        + "software_engn_confidence,strengths,weaknesses,skills,interests";
  }
}
