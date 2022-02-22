package edu.brown.cs.student.main.CSVData;

import java.util.List;

public class StudentBuilder implements CSVBuilder<CSVDatum> {
  @Override
  public Student build(List<String> fields) {
    Student newStudent = null;
    try {
      newStudent = new Student(fields);
    } catch (NumberFormatException ex) {
      System.err.println("ERROR: input int or double field unable to be converted from string");
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
