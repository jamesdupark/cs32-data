package edu.brown.cs.student.main.CSVParse.Builder;

import edu.brown.cs.student.main.Recommender.Stud.Student;

import java.util.HashMap;
import java.util.List;

public class StudentBuilder implements CSVBuilder<Student> {
  /**
   * Map from field name to data type of field.
   */
  private final HashMap<String, String> typeMap;

  /**
   * Constructor for new StudentBuilder class.
   * @param typeMap
   */
  public StudentBuilder(HashMap<String, String> typeMap) {
    this.typeMap = typeMap;
  }
  @Override
  public Student build(List<String> fields) {
    try {
      Student scholar = new Student();
      String[] fieldTitles = this.getColumnTitles().split(",");
      for (int i = 0; i < fieldTitles.length; i++) {
        String type = typeMap.get(fieldTitles[i]);
        if (type.equals("qualitative")) {
          scholar.addQual(fieldTitles[i], fields.get(i));
        } else {
          scholar.addQuan(fieldTitles[i], Double.parseDouble(fields.get(i)));
        }
      }
      return scholar;
    } catch (IllegalArgumentException ex) {
      System.err.println("ERROR: " + ex.getMessage());
      return null;
    }
  }
  @Override
  public String getColumnTitles() {
    return "id,name,email,gender,class_year,ssn,nationality,race,years_experience,"
        + "communication_style,weekly_avail_hours,meeting_style,meeting_time,"
        + "software_engn_confidence,strengths,weaknesses,skills,interests";
  }
}
