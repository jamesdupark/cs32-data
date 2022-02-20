package java.edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import edu.brown.cs.student.main.Coordinate;
import edu.brown.cs.student.main.Distances.EuclideanDistance;
import org.junit.Before;
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
    assertEquals(dist.getDistance(c1, c2), 4, 1);
  }
}
