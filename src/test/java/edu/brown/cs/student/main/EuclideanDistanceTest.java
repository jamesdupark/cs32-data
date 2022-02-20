package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;

import edu.brown.cs.student.main.Distances.EuclideanDistance;
import org.junit.Test;
public class EuclideanDistanceTest {
  @Test
  public void testEuclideanDistance() {
    EuclideanDistance dist = new EuclideanDistance();
    Coordinate c1 = new Coordinate(1, 2, 3);
    Coordinate c2 = new Coordinate(1, 2, 3);

    // same coordinate
    assertEquals(dist.getDistance(c1, c1), 0, 0);

    // different coordinate but distance of 0
    assertEquals(dist.getDistance(c1, c2), 0, 0);

    // standard tests
    c2 = new Coordinate(5, 3, 1);
    Coordinate c3 = new Coordinate(-3, 41, 23.34);
    Coordinate c4 = new Coordinate(42, 100000, 0.002);
    Coordinate c5 = new Coordinate(3420, 1.23, 91.2);
    Coordinate c6 = new Coordinate(3.1415, 2.11232, 19);

    assertEquals(dist.getDistance(c1, c2), 4.58, 0.1);
    assertEquals(dist.getDistance(c1, c3), 4.24, 0.1);
    assertEquals(dist.getDistance(c1, c4), 4.24, 0.1);
    assertEquals(dist.getDistance(c1, c5), 4.24, 0.1);
    assertEquals(dist.getDistance(c1, c6), 4.24, 0.1);

    assertEquals(dist.getDistance(c2, c3), 4.24, 0.1);
    assertEquals(dist.getDistance(c2, c4), 4.24, 0.1);
    assertEquals(dist.getDistance(c2, c5), 4.24, 0.1);
    assertEquals(dist.getDistance(c2, c6), 4.24, 0.1);

    assertEquals(dist.getDistance(c3, c4), 4.24, 0.1);
    assertEquals(dist.getDistance(c3, c5), 4.24, 0.1);
    assertEquals(dist.getDistance(c3, c6), 4.24, 0.1);

    assertEquals(dist.getDistance(c4, c5), 4.24, 0.1);
    assertEquals(dist.getDistance(c4, c6), 4.24, 0.1);

    assertEquals(dist.getDistance(c5, c6), 4.24, 0.1);
  }
}
