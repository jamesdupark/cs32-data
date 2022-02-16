package edu.brown.cs.student.main;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class KDTreeTest {
  KDTree<KDNode> emptyTree, smallStudent, stdCoordinate;

  @Before
  public void setup() {
    emptyTree = new KDTree<>();
    smallStudent = new KDTree<>();
    stdCoordinate = new KDTree<>();
  }

  @Test
  public void testConstructor() {
    assertEquals(0, emptyTree.getNumNodes());
    assertEquals(20, smallStudent.getNumNodes());
    assertEquals("0".repeat(29), stdBloom.toString());

    // fprate out of bounds
    assertThrows(IllegalArgumentException.class, () -> new BloomFilter(0, 5));
    assertThrows(IllegalArgumentException.class, () -> new BloomFilter(-1, 5));
    assertThrows(IllegalArgumentException.class, () -> new BloomFilter(3, 5));
    assertThrows(IllegalArgumentException.class, () -> new BloomFilter(1, 5));

    // maxElts out of bounds
    assertThrows(IllegalArgumentException.class,
        () -> new BloomFilter(0.001, 2147483647));
    assertThrows(IllegalArgumentException.class, () -> new BloomFilter(0.1, -3));
    assertThrows(IllegalArgumentException.class, () -> new BloomFilter(0.1, 0));
  }
}
