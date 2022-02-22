package edu.brown.cs.student.main.KDNodes;

import edu.brown.cs.student.main.CSVData.Student;

/**
 * Extensible class that takes a Student and extracts its relevant
 * fields for KDTree.
 */
public class StudentNode implements KDNode {
  /** ID of the Student. */
  private int id;
  /** number of years of experience a student has with computer science. */
  private double yearsExp;
  /** number of hours a student is available weekly. */
  private double weeklyAvail;
  /** the student's confidence of themselves in software engineering. */
  private double sweConfidence;

  /**
   * Constructor for my StudentNode Class.
   * @param stud the student that we will be extracting the relevant
   *             fields from for the KDTree
   */
  public StudentNode(Student stud) {
    id = stud.getId();
    yearsExp = stud.getYearsExp();
    weeklyAvail = stud.getWeeklyAvail();
    sweConfidence = stud.getSweConfidence();
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
}
