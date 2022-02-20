package edu.brown.cs.student.main.Distances;

//import edu.brown.cs.student.main.Distances.Distances;

import edu.brown.cs.student.main.KDNode;

public class EuclideanDistance implements Distances {
  @Override
  public double getDistance(KDNode origin, KDNode target) {
    double sum = 0;
    for (int i = 0; i < target.getNumDimensions(); i++) {
      double deltaAxis = (target.getAxisVal(i) - origin.getAxisVal(i));
      sum += Math.pow(deltaAxis, 2);
    }
    return Math.sqrt(sum);
  }
}
