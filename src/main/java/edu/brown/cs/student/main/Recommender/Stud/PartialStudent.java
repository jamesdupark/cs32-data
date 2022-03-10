package edu.brown.cs.student.main.Recommender.Stud;

import java.util.Map;

/**
 * JSONable interface for classes that represent part of the data about a student.
 */
public interface PartialStudent extends JSONable {
  /**
   * Returns a map of numerical attributes to their values.
   * @return hashmap of attribute names to numerical values.
   */
  Map<String, Double> getQuantMap();
  /**
   * Returns a map of qualitative attributes to their values.
   * @return hashmap of attribute names to string values.
   */
  Map<String, String> getQualMap();
  /**
   * gets the id of the partial student.
   * @return id of the partial student.
   */
  int getId();
}
