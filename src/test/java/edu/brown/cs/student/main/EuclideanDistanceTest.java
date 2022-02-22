package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;

import edu.brown.cs.student.main.Distances.EuclideanDistance;
import edu.brown.cs.student.main.Onboarding.Coordinate;
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
    Coordinate c1 = new Coordinate(1, 2, 3);
    Coordinate c2 = new Coordinate(1, 2, 3);

    // same coordinate
    assertEquals(dist.calcDistance(c1, c1), 0, 0);

    // different coordinate but distance of 0
    assertEquals(dist.calcDistance(c1, c2), 0, 0);

    // standard tests
    c2 = new Coordinate(5, 3, 1);
    // negative values
    Coordinate c3 = new Coordinate(-3, 41, 23.34);
    // large and small values
    Coordinate c4 = new Coordinate(42, 100000, 0.002);
    // decimals
    Coordinate c5 = new Coordinate(3420, 1.23, 91.2);
    // precise decimal values
    Coordinate c6 = new Coordinate(3.1415, 2.11232, -19);

    assertEquals(dist.calcDistance(c1, c2), 4.58, 0.1);
    assertEquals(dist.calcDistance(c1, c3), 44.166906, 0.1);
    assertEquals(dist.calcDistance(c1, c4), 99998.00845, 0.1);
    assertEquals(dist.calcDistance(c1, c5), 3420.137546, 0.1);
    assertEquals(dist.calcDistance(c1, c6), 22.104267, 0.1);

    assertEquals(dist.calcDistance(c2, c3), 44.800397, 0.1);
    assertEquals(dist.calcDistance(c2, c4), 99997.00685, 0.1);
    assertEquals(dist.calcDistance(c2, c5), 3416.191472, 0.1);
    assertEquals(dist.calcDistance(c2, c6), 20.10577, 0.1);

    assertEquals(dist.calcDistance(c3, c4), 99959.012854, 0.1);
    assertEquals(dist.calcDistance(c3, c5), 3423.903566, 0.1);
    assertEquals(dist.calcDistance(c3, c6), 57.815614, 0.1);

    assertEquals(dist.calcDistance(c4, c5), 100055.850417, 0.1);
    assertEquals(dist.calcDistance(c4, c6), 99997.897035, 0.1);

    assertEquals(dist.calcDistance(c5, c6), 3418.635229, 0.1);
  }
}
