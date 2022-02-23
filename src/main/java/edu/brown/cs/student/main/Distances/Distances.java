package edu.brown.cs.student.main.Distances;

import edu.brown.cs.student.main.KDimTree.KDNodes.KDNode;

/**
 * Interface for a package of related Distances that is used to compare
 * KDNodes in the KDTree and determine similarity.
 */
public interface Distances {
  /**
   * Method to calculate the distance between two KDNodes based on
   * a specific distance metric.
   * @param origin the first KDNode
   * @param target the second KDNode
   * @return the distance between the two KDNodes based on the distance metric
   */
  double calcDistance(KDNode origin, KDNode target);
}
