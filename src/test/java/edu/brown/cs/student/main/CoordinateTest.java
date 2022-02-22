package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import edu.brown.cs.student.main.Onboarding.Coordinate;
import org.junit.Before;
import org.junit.Test;

public class CoordinateTest {
  Coordinate origin, cOne, cTwo, cThree;

  @Before
  public void setup() {
    origin = new Coordinate(0, 0, 0);
    cOne = new Coordinate(3, 4, 0);
    cTwo = new Coordinate(3, 4, 0);
    cThree = new Coordinate(12, 3, 4);
  }

  @Test
  public void testDistance() {
    double delta = Double.MIN_VALUE;
    double selfDistance = origin.distance(origin);
    double identicalDistance = cOne.distance(cTwo);
    double pythagDistance = origin.distance(cOne);
    double reflexiveDistance = cOne.distance(origin);
    double threeDimDistance = cThree.distance(origin);
    double nonIntDistance = cThree.distance(cOne);
    assertEquals(selfDistance, 0, delta);
    assertEquals(identicalDistance, 0, delta);
    assertEquals(pythagDistance, 5, delta);
    assertEquals(reflexiveDistance, pythagDistance, delta);
    assertEquals(threeDimDistance, 13, delta);
    assertEquals(nonIntDistance, 9.8995, 0.00001);
  }

  @Test
  public void testEquals() {
    assertEquals(cOne, cTwo);
    assertNotEquals(origin, cOne);
  }

  @Test
  public void testHash() {
    int hashOne = cOne.hashCode();
    int hashTwo = cTwo.hashCode();
    assertEquals(hashOne, hashTwo);
    assertEquals(hashOne, hashOne);
  }

  @Test
  public void testGetters() {
    double delta = Double.MIN_VALUE;
    double x = cThree.getX();
    double y = cThree.getY();
    double z = cThree.getZ();

    assertEquals(x, 12, delta);
    assertEquals(y, 3, delta);
    assertEquals(z, 4, delta);
  }


}
