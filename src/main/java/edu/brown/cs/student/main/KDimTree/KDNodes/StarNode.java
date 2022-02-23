package edu.brown.cs.student.main.KDimTree.KDNodes;

/**
 * Extensible class that takes a Star and extracts its relevant
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
   * @param id the unique id of a Star
   * @param x value corresponding to the x-axis of a Star
   * @param y value corresponding to the y-axis of a Star
   * @param z value corresponding to the z-axis of a Star
   */
  public StarNode(int id, double x, double y, double z) {
    this.id = id;
    this.x = x;
    this.y = y;
    this.z = z;
  }
  @Override
  public int getID() {
    return this.id;
  }
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
  @Override
  public int getNumDimensions() {
    return 3;
  }
  @Override
  public String toString() {
    return "(" + x + ", " + y + ", " + z + ")";
  }
  @Override
  public int hashCode() {
    return Integer.hashCode(id);
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj instanceof StarNode) {
      StarNode oStarNode = (StarNode) obj;
      return id == oStarNode.id && x == oStarNode.x
          && y == oStarNode.y && z == oStarNode.z;

    } else {
      return false;
    }
  }
}
