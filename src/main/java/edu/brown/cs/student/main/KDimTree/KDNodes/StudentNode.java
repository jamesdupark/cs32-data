package edu.brown.cs.student.main.KDimTree.KDNodes;

/**
 * Extensible class that takes a Student and extracts its relevant
 * fields for KDTree.
 */
public class StudentNode implements KDNode {
  /** ID of the Student. */
  private final int id;
  /** number of years of experience a student has with computer science. */
  private final double yearsExp;
  /** number of hours a student is available weekly. */
  private final double weeklyAvail;
  /** the student's confidence of themselves in software engineering. */
  private final double sweConfidence;

  /**
   * Constructor for my StudentNode Class.
   * @param id unique id of a student
   * @param yearsExp numbers of years of experience a student has
   * @param weeklyAvail number of weekly available hours a student has
   * @param sweConfidence software engineering confidence a student ranked themselves as
   */
  public StudentNode(int id, double yearsExp, double weeklyAvail, double sweConfidence) {
    this.id = id;
    this.yearsExp = yearsExp;
    this.weeklyAvail = weeklyAvail;
    this.sweConfidence = sweConfidence;
  }
  @Override
  public int getID() {
    return this.id;
  }
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
  @Override
  public int getNumDimensions() {
    return 3;
  }
  @Override
  public String toString() {
    return "(" + this.yearsExp + ", " + this.weeklyAvail + ", " + this.sweConfidence + ")";
  }
}
