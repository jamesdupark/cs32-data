package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import edu.brown.cs.student.main.BloomFilter.BloomFilter;
import edu.brown.cs.student.main.BloomFilter.XNORSimilarity;
import edu.brown.cs.student.main.KNNCalculator.BloomKNNCalculator;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BloomKNNCalculatorTest {
  BloomFilter baseSet, compSet, simSet, emptySet, bigSet;
  XNORSimilarity comp;
  BloomKNNCalculator baseKNN;
  Map<Integer, BloomFilter> filters;

  @Before
  public void init() {
    baseSet = new BloomFilter(0.5, 5, 0);
    compSet = new BloomFilter(0.5, 5, 1);
    simSet = new BloomFilter(0.5, 5, 2);
    emptySet = new BloomFilter(0.5, 5, 3);
    bigSet = new BloomFilter(0.1, 5);

    // insert into bloom filters
    baseSet.insert("hello");
    baseSet.insert("hi");
    compSet.insert(3);
    simSet.insert("hi");

    // add filters to map
    filters = new HashMap<>();
    filters.put(0, baseSet);
    filters.put(1, compSet);
    filters.put(2, simSet);
    filters.put(3, emptySet);

    comp = new XNORSimilarity(baseSet);
    baseKNN = new BloomKNNCalculator(baseSet, filters, comp);
  }

  @Test
  public void testConstructorExceptions() {
    assertThrows(AssertionError.class,
        () -> new BloomKNNCalculator(compSet, filters, comp));
  }

  @Test
  public void testKNNBasic() {
    List<Integer> expect1 = List.of(2);
    List<Integer> expect2 = List.of(2, 3);
    List<Integer> expect3 = List.of(2, 3, 1);

    assertThrows(AssertionError.class, () -> baseKNN.knn(-3));
    assertThrows(AssertionError.class, () -> baseKNN.knn(0));
    System.out.println(baseKNN.knn(5));
    assertEquals(expect1, baseKNN.knn(1));
    assertEquals(expect2, baseKNN.knn(2));
    assertEquals(expect3, baseKNN.knn(3));
    assertEquals(expect3, baseKNN.knn(5));
  }

  @Test
  public void testKNNBinning() {
    BloomFilter simSet2 = new BloomFilter(0.5, 5, 4);
    simSet2.insert("hello");
    filters.put(4, simSet2);

    BloomKNNCalculator binKNN = new BloomKNNCalculator(baseSet, filters, comp);
    List<Integer> randomSet = binKNN.knn(3);
    List<Integer> firstBin = randomSet.subList(0, 2);
    assertTrue(firstBin.containsAll(List.of(4, 2)));
    assertEquals(3, (long) randomSet.get(2));
    assertFalse(firstBin.contains(3));
  }
}
