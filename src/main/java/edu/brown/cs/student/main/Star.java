package edu.brown.cs.student.main;

import edu.brown.cs.student.main.CSVData.CSVDatum;
import edu.brown.cs.student.main.KDNodes.KDNode;

/**
 * class representing data about individual stars.
 */
public class Star implements CSVDatum {
  /** unique, non-null identifier for each star. */
  private final int id;
  /** unique identifier for each star. May be null. */
  private final String name;
  /** position of star in 3-dimensional space. May not be null. */
  private final Coordinate cord;

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
    this.cord = new Coordinate(x, y, z);
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
    this.cord = new Coordinate(x, y, z);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }

    if (obj instanceof Star) {
      Star oStar = (Star) obj;
      boolean nonNullFields = id == oStar.id && cord.equals(oStar.cord);

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
  public Coordinate getCord() {
    return this.cord;
  }

  @Override
  public KDNode toKDNode() {
    return null;
  }
}
