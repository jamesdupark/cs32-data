package edu.brown.cs.student.main;

import edu.brown.cs.student.main.Blooms.BloomFilter;
import edu.brown.cs.student.main.Blooms.SimilarityMetrics.XNORSimilarity;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class XNORSimilarityTest {
  BloomFilter baseSet, compSet, simSet, emptySet, bigSet;
  XNORSimilarity comp;

  @Before
  public void init() {
    baseSet = new BloomFilter(0.5, 5);
    compSet = new BloomFilter(0.5, 5);
    simSet = new BloomFilter(0.5, 5);
    emptySet = new BloomFilter(0.5, 5);
    bigSet = new BloomFilter(0.1, 5);
    baseSet.insert("hello");
    baseSet.insert("hi");
    compSet.insert(3);
    simSet.insert("hi");


    comp = new XNORSimilarity(baseSet);
  }

  @Test
  public void testSimilarity() {
    assertEquals(5, comp.similarity(compSet));
    assertEquals(6, comp.similarity(emptySet));
    assertEquals(7, comp.similarity(simSet));
    assertThrows(AssertionError.class, () -> comp.similarity(bigSet));
  }

  @Test
  public void testCompare() {
    BloomFilter simCopy = new BloomFilter(0.5, 5);
    simCopy.insert("hi");

    assertEquals(1, comp.compare(compSet, emptySet));
    assertEquals(-1, comp.compare(emptySet, compSet)); // reflexive
    assertEquals(0, comp.compare(simSet, simSet)); // same object equal
    assertEquals(0, comp.compare(simSet, simCopy)); // copy equality
    assertEquals(1, comp.compare(compSet, simSet));
  }

  @Test
  public void testGetBase() {
    assertEquals(comp.getBase(), baseSet);
  }
}
