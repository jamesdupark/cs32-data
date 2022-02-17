package edu.brown.cs.student.main;

public class Student implements KDNode {
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
  private final String strengths;
  // the weaknesses of a student
  private final String weaknesses;
  // the skills of a student
  private final String skills;
  // the interests of a student
  private final String interests;

  /**
   * Constructor for a Student.
   * @param id the ID of a student
   * @param name name of a student
   * @param email email of a student
   * @param gender gender of a student
   * @param classYear class year of a student
   * @param nationality nationality of a student
   * @param race race of a student
   * @param yearsExp numbers of years of experience a student has
   * @param commStyle preferred communication style of a student
   * @param weeklyAvail weekly available hours a student has
   * @param meetingStyle preferred meeting style a student has
   * @param meetingTime preferred meeting time a student has
   * @param sweConfidence confidence that a student has in their software engineering skills
   * @param strengths strengths of a student
   * @param weaknesses weaknesses of a student
   * @param skills skills of a student
   * @param interests interests of a student
   */
  public Student(int id, String name, String email, String gender, String classYear,
                 String nationality, String race, double yearsExp, String commStyle,
                 double weeklyAvail, String meetingStyle, String meetingTime, double sweConfidence,
                 String strengths, String weaknesses, String skills, String interests) {
    this.id = id;
    this.name = name;
    this.email = email;
    this.gender = gender;
    this.classYear = classYear;
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


  /**
   * Method to return the ID of the KDNode that is used when
   * querying the nearest neighbors.
   *
   * @return the ID of the KDNode
   */
  @Override
  public int getID() {
    return this.id;
  }

  /**
   * Method to find the axis value for the value at a Node.
   * @param axis the axis to retrieve for the value
   * @return the axis value for the value at a Node
   * @throws RuntimeException if the axis is not integer 0, 1, or 2
   */
  @Override
  public double getAxisVal(int axis) throws RuntimeException {
    if (axis == 0) {
      return this.yearsExp;
    } else if (axis == 1) {
      return this.weeklyAvail;
    } else if (axis == 2) {
      return this.sweConfidence;
    } else {
      throw new RuntimeException("Attempt to get axis from student that does not exist");
    }
  }

  /**
   * Method to find the number of dimensions for a Student in the KDTree.
   * @return the number of dimensions for the KDTree, which in this case is 3
   */
  @Override
  public int getNumDimensions() {
    return 3;
  }

  public String toString() {
    return "(" + this.yearsExp + ", " + this.weeklyAvail + ", " + this.sweConfidence + ")";
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
  public String getStrengths() {
    return this.strengths;
  }
  public String getWeaknesses() {
    return this.weaknesses;
  }
  public String getSkills() {
    return this.skills;
  }
  public String getInterests() {
    return this.interests;
  }
}
