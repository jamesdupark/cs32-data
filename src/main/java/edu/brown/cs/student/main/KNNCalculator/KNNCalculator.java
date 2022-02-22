package edu.brown.cs.student.main.KNNCalculator;

import java.util.List;

/**
 * Interface for classes that calculate the K-nearest neighbors of a given
 * object. Objects being queried must implement KNNComparable
 * @param <T> class of object which implements KNNComparable
 * @author jamesdupark
 */
public interface KNNCalculator<T extends KNNComparable> {
  /**
   * calculates the K nearest neighbors of the given object.
   * @param k number of neighbors to return
   * @return list of ids of the k most similar neighbors
   */
  List<Integer> knn(int k);
}
