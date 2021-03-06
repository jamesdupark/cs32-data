package edu.brown.cs.student.main.Blooms;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class BloomFilterTest {
  BloomFilter stdBloom, tinyBloom, smallBloom, idFilter;

  @Before
  public void setup() {
    stdBloom = new BloomFilter(0.1, 5);
    tinyBloom = new BloomFilter(0.5, 1);
    smallBloom = new BloomFilter(0.5, 5);
    idFilter = new BloomFilter(0.5, 5, 3);
  }

  @Test
  public void testConstructor() {
    assertEquals(29, stdBloom.size());
    assertEquals(4, stdBloom.getNumHashes());
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

  @Test
  public void testInsert() {
    smallBloom.insert("hello");
    assertEquals("00000010", smallBloom.toString());

    // duplicate insertion
    smallBloom.insert("hello");
    assertEquals("00000010", smallBloom.toString());

    // unique insertion
    smallBloom.insert("hi");
    assertEquals("00010010", smallBloom.toString());

    // collision
    smallBloom.insert("2");
    assertEquals("00010010", smallBloom.toString());
  }

  @Test
  public void testQuery() {
    smallBloom.insert("hello");
    smallBloom.insert("hi");

    // maybe in the set
    assertTrue(smallBloom.query("hello"));
    assertTrue(smallBloom.query("hi"));

    // definitely not in the set
    assertFalse(smallBloom.query("3"));

    // false positive
    assertTrue(smallBloom.query("2"));
  }

  @Test
  public void testEquals() {
    BloomFilter smallCopy = new BloomFilter(0.5, 5);
    BloomFilter setA = new BloomFilter(0.5, 5);
    BloomFilter setB = new BloomFilter(0.5, 5);
    BloomFilter idCopy = new BloomFilter(0.5, 5, 3);
    BloomFilter idDiff = new BloomFilter(0.5, 5, 1);
    setA.insert("hello");
    setB.insert("hi");

    assertEquals(smallBloom, smallBloom); // reflexive equals
    assertEquals(smallBloom, smallCopy); // copy equals
    assertNotEquals(smallBloom, tinyBloom); // diff maxelts
    assertNotEquals(smallBloom, stdBloom); // diff fprate
    assertNotEquals(setA, setB); // diff contents
    assertEquals(idFilter, idCopy); // id same
    assertNotEquals(idFilter, idDiff); // id diff
  }

  @Test
  public void testGetId() {
    BloomFilter idFilter = new BloomFilter(0.5, 5, 3);

    assertEquals(3, idFilter.getId());
  }
}
