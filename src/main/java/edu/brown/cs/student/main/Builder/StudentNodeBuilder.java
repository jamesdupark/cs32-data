package edu.brown.cs.student.main.Builder;

import edu.brown.cs.student.main.KDNodes.KDNode;
import edu.brown.cs.student.main.KDNodes.StudentNode;

import java.util.List;

public class StudentNodeBuilder implements CSVBuilder<KDNode> {
  private final int idIndex = 0;
  private final int yearsExpIndex = 8;
  private final int weeklyAvailIndex = 10;
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
    } catch (NullPointerException ex) {
      System.err.println("ERROR: input int or double field was null");
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