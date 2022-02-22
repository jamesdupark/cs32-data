package edu.brown.cs.student.main.CSVData;

import edu.brown.cs.student.main.BloomFilter.BloomFilter;
import edu.brown.cs.student.main.KDNodes.KDNode;
import edu.brown.cs.student.main.KDNodes.StudentNode;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Student implements CSVDatum {
  // id of a student
  private final int id;
  // name of a student
  private final String name;
  // email of a student
  private final String email;
  // gender of a student
  private final String gender;
  // class year of a student
  private final String classYear;
  // nationality of a student
  private final String nationality;
  // race of a student
  private final String race;
  // how many years of experience a student has with computer science
  private final double yearsExp;
  // the preferred communication style of the student
  private final String commStyle;
  // how many hours a student is available per week
  private final double weeklyAvail;
  // the meeting style that the student prefers
  private final String meetingStyle;
  // the time that a student prefers to meet
  private final String meetingTime;
  // the student's confidence of themselves in software engineering
  private final double sweConfidence;
  // the strengths of a student
  private final List<String> strengths;
  // the weaknesses of a student
  private final List<String> weaknesses;
  // the skills of a student
  private final List<String> skills;
  // the interests of a student
  private final List<String> interests;

  /**
   * Constructor for a Student.
   * @param studentInfo - A list of strings to be converted into different data types.
   */
  public Student(List<String> studentInfo) {
    this.id = Integer.parseInt(studentInfo.get(0));
    this.name = studentInfo.get(1);
    this.email = studentInfo.get(2);
    this.gender = studentInfo.get(3);
    this.classYear = studentInfo.get(4);
    this.nationality = studentInfo.get(6);
    this.race = studentInfo.get(7);
    this.yearsExp = Double.parseDouble(studentInfo.get(8));
    this.commStyle = studentInfo.get(9);
    this.weeklyAvail = Double.parseDouble(studentInfo.get(10));
    this.meetingStyle = studentInfo.get(11);
    this.meetingTime = studentInfo.get(12);
    this.sweConfidence = Double.parseDouble(studentInfo.get(13));
    this.strengths = Arrays.asList(studentInfo.get(14).split(", "));
    this.weaknesses = Arrays.asList(studentInfo.get(15).split(", "));
    this.skills = Arrays.asList(studentInfo.get(16).split(", "));
    this.interests = Arrays.asList(studentInfo.get(17).split(", "));
  }

  /**
   * @return id of student
   */
  public int getId() {
    return this.id;
  }
  public String getName() {
    return this.name;
  }
  public String getEmail() {
    return this.email;
  }
  public String getGender() {
    return this.gender;
  }
  public String getClassYear() {
    return this.classYear;
  }
  public String getNationality() {
    return this.nationality;
  }
  public String getRace() {
    return this.race;
  }
  public double getYearsExp() {
    return this.yearsExp;
  }
  public String getCommStyle() {
    return this.commStyle;
  }
  public double getWeeklyAvail() {
    return this.weeklyAvail;
  }
  public String getMeetingStyle() {
    return this.meetingStyle;
  }
  public String getMeetingTime() {
    return this.meetingTime;
  }
  public double getSweConfidence() {
    return this.sweConfidence;
  }
  public List<String> getStrengths() {
    return this.strengths;
  }
  public List<String> getWeaknesses() {
    return this.weaknesses;
  }
  public List<String> getSkills() {
    return this.skills;
  }
  public List<String> getInterests() {
    return this.interests;
  }

  @Override
  public KDNode toKDNode() {
    return new StudentNode(this);
  }

  @Override
  public BloomFilter toBloomFilter(int maxElts) {
    // class year, race, communication style, meeting style, meeting time, skills, interests
    List<String> toAdd = new ArrayList<>();
    toAdd.add(this.classYear);
    toAdd.add(this.race);
    toAdd.add(this.commStyle);
    toAdd.add(this.meetingStyle);
    toAdd.addAll(this.skills);
    toAdd.addAll(this.interests);

    BloomFilter filter = new BloomFilter(0.1, maxElts);
    for (String input : toAdd) {
      filter.insert(input);
    }

    return filter;
  }

  @Override
  public int getMaxElts() {
    return 4 + skills.size() + interests.size();
  }
}
