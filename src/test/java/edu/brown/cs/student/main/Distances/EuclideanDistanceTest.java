package edu.brown.cs.student.main.Distances;

import static org.junit.Assert.assertEquals;

import edu.brown.cs.student.main.KDimTree.KDNodes.KDNode;
import edu.brown.cs.student.main.KDimTree.KDNodes.StarNode;
import org.junit.Test;

/**
 * Class to test the EuclideanDistance Class.
 */
public class EuclideanDistanceTest {
  /**
   * Method to test the euclidean distance.
   */
  @Test
  public void testEuclideanDistance() {
    EuclideanDistance dist = new EuclideanDistance();
    KDNode s1 = new StarNode(0, 1, 2, 3);
    KDNode s2 = new StarNode(1, 1, 2, 3);

    // same coordinate
    assertEquals(dist.calcDistance(s1, s1), 0, 0);

    // different coordinate but distance of 0
    assertEquals(dist.calcDistance(s1, s2), 0, 0);

    // standard tests
    s2 = new StarNode(2, 5, 3, 1);
    // negative values
    KDNode s3 = new StarNode(3, -3, 41, 23.34);
    // large and small values
    KDNode s4 = new StarNode(4, 42, 100000, 0.002);
    // decimals
    KDNode s5 = new StarNode(5, 3420, 1.23, 91.2);
    // precise decimal values
    KDNode s6 = new StarNode(6, 3.1415, 2.11232, -19);

    assertEquals(dist.calcDistance(s1, s2), 4.58, 0.1);
    assertEquals(dist.calcDistance(s1, s3), 44.166906, 0.1);
    assertEquals(dist.calcDistance(s1, s4), 99998.00845, 0.1);
    assertEquals(dist.calcDistance(s1, s5), 3420.137546, 0.1);
    assertEquals(dist.calcDistance(s1, s6), 22.104267, 0.1);

    assertEquals(dist.calcDistance(s2, s3), 44.800397, 0.1);
    assertEquals(dist.calcDistance(s2, s4), 99997.00685, 0.1);
    assertEquals(dist.calcDistance(s2, s5), 3416.191472, 0.1);
    assertEquals(dist.calcDistance(s2, s6), 20.10577, 0.1);

    assertEquals(dist.calcDistance(s3, s4), 99959.012854, 0.1);
    assertEquals(dist.calcDistance(s3, s5), 3423.903566, 0.1);
    assertEquals(dist.calcDistance(s3, s6), 57.815614, 0.1);

    assertEquals(dist.calcDistance(s4, s5), 100055.850417, 0.1);
    assertEquals(dist.calcDistance(s4, s6), 99997.897035, 0.1);

    assertEquals(dist.calcDistance(s5, s6), 3418.635229, 0.1);
  }
}
