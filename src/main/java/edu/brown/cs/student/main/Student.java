package edu.brown.cs.student.main;

import java.util.HashMap;

public class Student {
  /**
   * A map from a quantitative field name to its value.
   */
  private final HashMap<String, Double> quanMap = new HashMap<>();
  /**
   * A map from a quantitative field name to its value.
   */
  private final HashMap<String, String> qualMap = new HashMap<>();

  /**
   * Method to store a quantitative field name and its value.
   * @param field - the field name of a field for Student.
   * @param value - the value of field for Student.
   */
  public void addQuan(String field, Double value) {
    this.quanMap.put(field, value);
  }

  /**
   * Method to store a qualitative field name and its value.
   * @param field - the field name of a field for Student.
   * @param value - the value of field for Student.
   */
  public void addQual(String field, String value) {
    this.qualMap.put(field, value);
  }

  /**
   * Getter for the qualMap field.
   * @return - the Hashmap with qualitative field name as key and field value as value.
   */
  public HashMap<String, String> getQualMap() {
    return this.qualMap;
  }

  /**
   * Getter for the quanMap field.
   * @return - the Hashmap with quantitative field name as key and field value as value.
   */
  public HashMap<String, Double> getQuanMap() {
    return this.quanMap;
  }
}
