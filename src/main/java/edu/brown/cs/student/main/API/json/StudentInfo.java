package edu.brown.cs.student.main.API.json;

import com.google.gson.Gson;

import java.util.List;
import java.util.Objects;

/**
 * StudentInfo class for storing json information from student info API endpoints.
 */
public class StudentInfo implements PartialStudent {
  /**
   * integer parameters for student info - unique id, years of experience, and
   * weekly available hours.
   */
  private final int id, yearsExperience, weeklyAvailHours;
  /**
   * String parameters for student info - name, class year, communication style,
   * meeting style, and meeting time.
   */
  private final String name, classYear, communicationStyle, meetingStyle, meetingTime;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StudentInfo that = (StudentInfo) o;
    return id == that.id && yearsExperience == that.yearsExperience
        && weeklyAvailHours == that.weeklyAvailHours && Objects.equals(name, that.name)
        && Objects.equals(classYear, that.classYear)
        && Objects.equals(communicationStyle, that.communicationStyle)
        && Objects.equals(meetingStyle, that.meetingStyle)
        && Objects.equals(meetingTime, that.meetingTime);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }
}
