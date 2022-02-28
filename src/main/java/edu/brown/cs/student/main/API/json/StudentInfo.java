package edu.brown.cs.student.main.API.json;

import com.google.gson.Gson;

import java.util.List;

/**
 * StudentInfo class for storing json information from student info API endpoints.
 */
public class StudentInfo implements JSONable {
  /**
   * integer parameters for student info - unique id, years of experience, and
   * weekly available hours.
   */
  private int id, yearsExperience, weeklyAvailHours;
  /**
   * String parameters for student info - name, class year, communication style,
   * meeting style, and meeting time.
   */
  private String name, classYear, communicationStyle, meetingStyle, meetingTime;

  /**
   * Constructor for manually creating StudentInfo objects (for testing).
   * @param numericalList list of numerical params in the order id, yearsExperience,
   *                      weeklyAvailHours
   * @param stringList list of string params in the order name, classYear, communicationStyle,
   *                   meetingStyle, meetingTime
   */
  public StudentInfo(List<Integer> numericalList, List<String> stringList) {
    this.id = numericalList.get(0);
    this.yearsExperience = numericalList.get(1);
    this.weeklyAvailHours = numericalList.get(2);
    this.name = stringList.get(0);
    this.classYear = stringList.get(1);
    this.communicationStyle = stringList.get(2);
    this.meetingStyle = stringList.get(3);
    this.meetingTime = stringList.get(4);
  }

  @Override
  public String toString() {
    Gson parser = new Gson();
    return parser.toJson(this);
  }
}
