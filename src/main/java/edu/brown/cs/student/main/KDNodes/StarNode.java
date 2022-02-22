package edu.brown.cs.student.main.KDNodes;

import edu.brown.cs.student.main.CSVData.Star;

/**
 * Extensible class that takes a Student and extracts its relevant
 * fields for KDTree.
 */
public class StarNode implements KDNode {
  /** unique, non-null identifier for each star. */
  private int id;
  /** double representing a star's x-cord. */
  private double x;
  /** double representing a star's y-cord. */
  private double y;
  /** double representing a star's z-cord. */
  private double z;

  /**
   * Constructor for my StarNode Class.
   * @param star the star that we will be extracting the relevant
   *             fields from for the KDTree
   */
  public StarNode(Star star) {
    id = star.getCord().getID();
    x = star.getCord().getX();
    y = star.getCord().getY();
    z = star.getCord().getZ();
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
      return this.x;
    } else if (axis == 1) {
      return this.y;
    } else if (axis == 2) {
      return this.z;
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
    return "(" + x + ", " + y + ", " + z + ")";
  }
}
