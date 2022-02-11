package edu.brown.cs.student.main;

public class Student {
  private final int id;

  /** unique identifier for each star. May be null. */
  private final String name;

  /** position of star in 3-dimensional space. May not be null. */
  private final String email;

  private final String gender;

  private final int classYear;

  private final int SSN;

  private final String nationality;

  private final String race;

  private final int yearsExp;

  private final String commStyle;

  private final int weeklyAvail;

  private final String meetingStyle;

  private final String meetingTime;

  private final int sweConfidence;

  private final String strengths;

  private final String weaknesses;

  private final String skills;

  private final String interests;

  public Student(int id, String name, String email, String gender, int classYear,
                 int SSN, String nationality, String race, int yearsExp, String commStyle,
                 int weeklyAvail, String meetingStyle, String meetingTime, int sweConfidence,
                 String strengths, String weaknesses, String skills, String interests) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.gender = gender;
    this.classYear = classYear;
    this.SSN = SSN;
    this.nationality = nationality;
    this.race = race;
    this.yearsExp = yearsExp;
    this.commStyle = commStyle;
    this.weeklyAvail = weeklyAvail;
    this.meetingStyle = meetingStyle;
    this.meetingTime = meetingTime;
    this.sweConfidence = sweConfidence;
    this.strengths = strengths;
    this.weaknesses = weaknesses;
    this.skills = skills;
    this.interests = interests;
  }
}
