package edu.brown.cs.student.main.CSVData;

import edu.brown.cs.student.main.BloomFilter;
import edu.brown.cs.student.main.KDNodes.KDNode;
import edu.brown.cs.student.main.KDNodes.StudentNode;

import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;

public class Student {
  // index of id of input list for Student constructor
  private static final int ID_INDEX = 0;
  // index of name of input list for Student constructor
  private static final int NAME_INDEX = 1;
  // index of email of input list for Student constructor
  private static final int EMAIL_INDEX = 2;
  // index of gender of input list for Student constructor
  private static final int GENDER_INDEX = 3;
  // index of class year of input list for Student constructor
  private static final int CLASS_YEAR_INDEX = 4;
  // index of nationality of input list for Student constructor
  private static final int NATIONALITY_INDEX = 6;
  // index of race of input list for Student constructor
  private static final int RACE_INDEX = 7;
  // index of years experience of input list for Student constructor
  private static final int YEARS_EXP_INDEX = 8;
  // index of communication style of input list for Student constructor
  private static final int COMM_STYLE_INDEX = 9;
  // index of weekly available hours of input list for Student constructor
  private static final int WEEKLY_AVAIL_INDEX = 10;
  // index of meeting style of input list for Student constructor
  private static final int MEETING_STYLE_INDEX = 11;
  // index of meeting time of input list for Student constructor
  private static final int MEETING_TIME_INDEX = 12;
  // index of swe confidence of input list for Student constructor
  private static final int SWE_CONFIDENCE_INDEX = 13;
  // index of strenths of input list for Student constructor
  private static final int STRENGTHS_INDEX = 14;
  // index of weaknesses of input list for Student constructor
  private static final int WEAKNESSES_INDEX = 15;
  // index of skills of input list for Student constructor
  private static final int SKILLS_INDEX = 16;
  // index of interests of input list for Student constructor
  private static final int INTERESTS_INDEX = 17;
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
  public Student(List<String> studentInfo) throws NumberFormatException {
    this.id = Integer.parseInt(studentInfo.get(ID_INDEX));
    this.name = studentInfo.get(NAME_INDEX);
    this.email = studentInfo.get(EMAIL_INDEX);
    this.gender = studentInfo.get(GENDER_INDEX);
    this.classYear = studentInfo.get(CLASS_YEAR_INDEX);
    this.nationality = studentInfo.get(NATIONALITY_INDEX);
    this.race = studentInfo.get(RACE_INDEX);
    this.yearsExp = Double.parseDouble(studentInfo.get(YEARS_EXP_INDEX));
    this.commStyle = studentInfo.get(COMM_STYLE_INDEX);
    this.weeklyAvail = Double.parseDouble(studentInfo.get(WEEKLY_AVAIL_INDEX));
    this.meetingStyle = studentInfo.get(MEETING_STYLE_INDEX);
    this.meetingTime = studentInfo.get(MEETING_TIME_INDEX);
    this.sweConfidence = Double.parseDouble(studentInfo.get(SWE_CONFIDENCE_INDEX));
    this.strengths = Arrays.asList(studentInfo.get(STRENGTHS_INDEX).split(", "));
    this.weaknesses = Arrays.asList(studentInfo.get(WEAKNESSES_INDEX).split(", "));
    this.skills = Arrays.asList(studentInfo.get(SKILLS_INDEX).split(", "));
    this.interests = Arrays.asList(studentInfo.get(INTERESTS_INDEX).split(", "));
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
  public CSVBuilder getBuilder() {
    return new StudentBuilder();
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
