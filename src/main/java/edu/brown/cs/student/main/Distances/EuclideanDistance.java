package edu.brown.cs.student.main.Distances;

import edu.brown.cs.student.main.KDNodes.KDNode;

/**
 * Class that implements the Distances Interface that defines the
 * distance as the Euclidean Distance.
 */
public class EuclideanDistance implements Distances {
  /**
   * Method to calculate the distance between two KDNodes based
   * on the Euclidean Distance.
   * @param origin the first KDNode
   * @param target the second KDNode
   * @return the Euclidean Distance between the two KDNodes
   */
  @Override
  public double calcDistance(KDNode origin, KDNode target) {
    double sum = 0;
    for (int i = 0; i < target.getNumDimensions(); i++) {
      double deltaAxis = target.getAxisVal(i) - origin.getAxisVal(i);
      sum += Math.pow(deltaAxis, 2);
    }
    return Math.sqrt(sum);
  }
}
