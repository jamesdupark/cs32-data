package edu.brown.cs.student.main;

/**
 * class representing data about individual stars.
 */
public class Star {
  /** unique, non-null identifier for each star. */
  private final int id;

  /** unique identifier for each star. May be null. */
  private final String name;

  /** position of star in 3-dimensional space. May not be null. */
  private final Coordinate coord;

  /**
   * Constructor for stars when all 5 fields are given.
   *
   * @param id star's id
   * @param name star's name
   * @param x star's x-coordinate
   * @param y star's y-coordinate
   * @param z star's z coordinate
   */
  Star(int id, String name, double x, double y, double z) {
    this.id = id;
    this.name = name;
    this.coord = new Coordinate(x, y, z);
  }

  /**
   * Constructor for stars when no name is given. Name is initialized to null.
   *
   * @param id star's id
   * @param x star's x-coordinate
   * @param y star's y-coordinate
   * @param z star's z coordinate
   */
  Star(int id, double x, double y, double z) {
    this.id = id;
    this.name = null;
    this.coord = new Coordinate(x, y, z);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj instanceof Star) {
      Star oStar = (Star) obj;
      boolean nonNullFields = id == oStar.id && coord.equals(oStar.coord);

      if (!nonNullFields) {
        return false;
      } else if (name == null) {
        return true;
      } else {
        return name.equals(oStar.name);
      }
    } else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Integer.hashCode(id);
  }

  /**
   * Getter method for star's coordinate field.
   * @return this star's coordinate
   */
  public Coordinate getCoord() {
    return this.coord;
  }
}
