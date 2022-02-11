package edu.brown.cs.student.main;

/**
 * Class that represents coordinates of stars within 3-dimensional space.
 */
public class Coordinate {
  /** double representing a star's x-coord. */
  private final double x;

  /** double representing a star's y-coord. */
  private final double y;

  /** double representing a star's z-coord. */
  private final double z;

  /**
   * Constructor for new Coordinate objects.
   *
   * @param x - the x-coord of the new Coordinate object
   * @param y - the y-coord of the new Coordinate object
   * @param z - the z-coord of the new Coordinate object
   */
  Coordinate(double x, double y, double z) {
    this.x = x;
    this.y = y;
    this.z = z;
  }

  /**
   * Calculates the euclidean distance between the current and given Coordinate
   * objects.
   *
   * @param coord - the Coordinate to calculate the distance to
   * @return - double representing the distance from current coordinate to the
   * given one
   */
  public double distance(Coordinate coord) {
    double x2 = coord.getX();
    double y2 = coord.getY();
    double z2 = coord.getZ();

    double sumSquare =
        Math.pow(x - x2, 2) + Math.pow(y - y2, 2) + Math.pow(z - z2, 2);
    return Math.sqrt(sumSquare);
  }

  /**
   * Getter method for x-coord.
   * @return - this Coordinate's x-coord
   */
  public double getX() {
    return this.x;
  }

  /**
   * Getter method for y-coord.
   * @return - this Coordinate's x-coord
   */
  public double getY() {
    return this.y;
  }

  /**
   * Getter method for z-coord.
   * @return - this Coordinate's x-coord
   */
  public double getZ() {
    return this.z;
  }
  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj instanceof Coordinate) {
      Coordinate oCoord = (Coordinate) obj;
      return x == oCoord.getX() && y == oCoord.getY() && z == oCoord.getZ();
    } else {
      return false;
    }
  }
  @Override
  public int hashCode() {
    return Double.hashCode(x + y + z);
  }
}
