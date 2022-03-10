package edu.brown.cs.student.main.Recommender.Stud;

import edu.brown.cs.student.main.API.json.JSONable;

import java.util.HashMap;
import java.util.Objects;

public class Student implements JSONable {
  /**
   * A map from a quantitative field name to its value.
   */
  private final HashMap<String, Double> quanMap = new HashMap<>();
  /**
   * A map from a quantitative field name to its value.
   */
  private final HashMap<String, String> qualMap = new HashMap<>();

  /**
   * Method for initializing a student from two complementary PartialStudent
   * objects
   * (StudentInfo & StudentMatches). The two PartialStudent objects must be of the
   * same ID.
   * 
   * @param part PartialStudent object to add information from
   * @throws IllegalArgumentException if IDs of PartialStudent objects don't match
   *                                  up
   */
  public void buildFromPartial(PartialStudent part)
      throws IllegalArgumentException {
    try {
      assert this.getId() == null || this.getId().equals(part.getId()) : "IDs must correspond";
      // add info from the partial student
      quanMap.putAll(part.getQuantMap());
      qualMap.putAll(part.getQualMap());
    } catch (AssertionError ase) {
      throw new IllegalArgumentException("ERROR: " + ase.getMessage());
    }
  }

  /**
   * gets the id of the current student.
   * 
   * @return student's id
   */
  private Integer getId() {
    if (qualMap.containsKey("id")) {
      return Integer.parseInt(qualMap.get("id"));
    }
    return null;
  }

  /**
   * Method to store a quantitative field name and its value.
   * 
   * @param field - the field name of a field for Student.
   * @param value - the value of field for Student.
   */
  public void addQuan(String field, Double value) {
    this.quanMap.put(field, value);
  }

  /**
   * Method to store a qualitative field name and its value.
   * 
   * @param field - the field name of a field for Student.
   * @param value - the value of field for Student.
   */
  public void addQual(String field, String value) {
    this.qualMap.put(field, value);
  }

  /**
   * Getter for the qualMap field.
   * 
   * @return - the Hashmap with qualitative field name as key and field value as
   *         value.
   */
  public HashMap<String, String> getQualMap() {
    return this.qualMap;
  }

  /**
   * Getter for the quanMap field.
   * 
   * @return - the Hashmap with quantitative field name as key and field value as
   *         value.
   */
  public HashMap<String, Double> getQuanMap() {
    return this.quanMap;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Student student = (Student) o;
    return Objects.equals(getQuanMap(), student.getQuanMap())
        && Objects.equals(getQualMap(), student.getQualMap());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getQuanMap(), getQualMap());
  }

  @Override
  public String toString() {
    return "Student " + qualMap.get("id")
        + " with keys: " + qualMap.keySet() + ", " +  quanMap.keySet();
  }
}
