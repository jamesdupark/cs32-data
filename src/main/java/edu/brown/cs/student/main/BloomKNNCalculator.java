package edu.brown.cs.student.main;

import edu.brown.cs.student.main.BloomFilter.BloomComparator;
import edu.brown.cs.student.main.BloomFilter.BloomFilter;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

/**
 * KNN calculator class for bloom filters.
 * @author jamesdupark
 */
public class BloomKNNCalculator implements KNNCalculator<BloomFilter> {
  /**
   * Base filter to find k-nearest neighbors for.
   */
  private final BloomFilter base;

  /**
   * Map of all student ids to their bloom filters.
   */
  private final Map<Integer, BloomFilter> filters;

  /**
   * Comparator for bloom filters based on the base filter.
   */
  private final BloomComparator comp;

  /**
   * Constructor for a BloomKNNCalculator. Sets fields appropriately, creating
   * a copy of the students Map and removing the base filter from it.
   * @param base the BloomFilter to calculate k-nearest neighbors from
   * @param filters Map of ids to bloom filters to be compared.
   * @param comp a BloomComparator initialized using base
   */
  public BloomKNNCalculator(BloomFilter base, Map<Integer, BloomFilter> filters,
                            BloomComparator comp) {
    this.base = base;
    this.filters = new HashMap<Integer, BloomFilter>(filters);
    this.comp = comp;

    // remove base from students so it doesn't appear in our search results
    BloomFilter removed = filters.remove(base.getId());
    assert base.equals(removed) && base.equals(comp.getBase());
  }

  @Override
  public List<Integer> knn(int k) {
    PriorityQueue<BloomFilter> queue = new PriorityQueue<>(comp);
    return null;
  }
}
