package edu.brown.cs.student.main;

import edu.brown.cs.student.main.BloomFilter.BloomComparator;
import edu.brown.cs.student.main.BloomFilter.BloomFilter;

import java.util.ArrayList;
import java.util.Collections;
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
    this.filters = new HashMap<Integer, BloomFilter>(filters);
    this.comp = comp;

    // remove base from students so it doesn't appear in our search results
    BloomFilter removed = this.filters.remove(base.getId());
    assert base.equals(removed) && !this.filters.containsKey(base.getId());
    assert base.equals(comp.getBase())
        : "Given comparator does not match base filter";
  }

  @Override
  public List<Integer> knn(int k) {
    assert k > 0; // we check for negative and 0 values before calling
    assert !filters.containsValue(comp.getBase()); // should be removed

    // enqueue all elements
    PriorityQueue<BloomFilter> queue = new PriorityQueue<>(comp);
    queue.addAll(filters.values());

    // get the first k elements from the queue
    List<Integer> retList = new ArrayList<>();
    while (retList.size() < k && !queue.isEmpty()) {
      // create bin list
      List<Integer> bin = new ArrayList<>();
      int binDist = comp.similarity(queue.peek());
      while (queue.peek() != null && comp.similarity(queue.peek()) == binDist) {
        BloomFilter closest = queue.poll();
        assert closest != null; // closest should never be null
        bin.add(closest.getId());
      }
      Collections.shuffle(bin); // randomize our bin of filters

      if (bin.size() + retList.size() <= k) { // still don't have enough filters
        retList.addAll(bin);
      } else { // this bin will put us over k elements, choose k-n elements
        int numRequired = k - retList.size();
        List<Integer> subBin = bin.subList(0, numRequired);
        retList.addAll(subBin);
      }
    }
    return retList;
  }
}
