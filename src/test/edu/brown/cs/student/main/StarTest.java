package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Test;

public class StarTest {

  @Test
  public void testEqualsWithName() {
    Star starZero = new Star(0, "zero", 0, 0, 0);
    Star starZeroCopy = new Star(0, "zero", 0, 0, 0);
    Star starIDDiff = new Star(1, "zero", 0, 0, 0);
    Star starNameDiff = new Star(0, "one", 0, 0, 0);
    Star starXDiff = new Star(0, "zero", 1, 0, 0);
    Star starYDiff = new Star(0, "zero", 0, 1, 0);
    Star starZDiff = new Star(0, "zero", 0, 0, 1);

    assertEquals(starZero, starZero);
    assertEquals(starZero, starZeroCopy);
    assertNotEquals(starZero, starIDDiff);
    assertNotEquals(starZero, starNameDiff);
    assertNotEquals(starZero, starXDiff);
    assertNotEquals(starZero, starYDiff);
    assertNotEquals(starZero, starZDiff);
  }

  @Test
  public void testEqualsWithoutName() {
    Star starZero = new Star(0, 0, 0, 0);
    Star starZeroCopy = new Star(0, 0, 0, 0);
    Star starIDDiff = new Star(1, 0, 0, 0);
    Star starXDiff = new Star(0,1, 0, 0);
    Star starYDiff = new Star(0, 0, 1, 0);
    Star starZDiff = new Star(0, 0, 0, 1);

    assertEquals(starZero, starZero);
    assertEquals(starZero, starZeroCopy);
    assertNotEquals(starZero, starIDDiff);
    assertNotEquals(starZero, starXDiff);
    assertNotEquals(starZero, starYDiff);
    assertNotEquals(starZero, starZDiff);
  }

  @Test
  public void testHash() {
    Star starZero = new Star(0, "zero", 0, 0, 0);
    Star starZeroCopy = new Star(0, "zero", 0, 0, 0);
    int hashZero = starZero.hashCode();
    int hashZeroCopy = starZeroCopy.hashCode();
    assertEquals(hashZero, hashZeroCopy);
    assertEquals(hashZero, hashZero);
  }

  @Test
  public void testGetters() {
    Star starZero = new Star(0, "zero", 0, 0, 0);
    Coordinate origin = new Coordinate(0, 0, 0);

    assertEquals(origin, starZero.getCoord());
  }
}
