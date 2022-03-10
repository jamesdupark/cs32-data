package edu.brown.cs.student.main.Blooms.SimilarityMetrics;

import edu.brown.cs.student.main.Blooms.BloomFilter;

import java.util.BitSet;

/**
 * BloomComparator class that compares using the XNOR similarity metric.
 * @author jamesdupark
 */
public class XNORSimilarity implements BloomComparator {
  /** the base filter that other filters are compared against. */
  private final BloomFilter baseFilter;

  /**
   * Constructor for XNORSimilarity comparators.
   * @param baseFilter base filter for this comparator.
   */
  public XNORSimilarity(BloomFilter baseFilter) {
    this.baseFilter = baseFilter;
  }

  @Override
  public int compare(BloomFilter o1, BloomFilter o2) {
    // "more similar" -> higher similarity score
    return Integer.compare(similarity(o1), similarity(o2));
  }

  @Override
  public int similarity(BloomFilter compFilter) throws AssertionError {
    assert baseFilter.getNumHashes() == compFilter.getNumHashes()
        && baseFilter.size() == compFilter.size()
        : "Filters do not have matching parameters.";

    if (baseFilter.equals(compFilter)) { // filters are equal so all bits same
      return baseFilter.size();
    }

    BitSet baseSet = baseFilter.getFilter();
    BitSet compSet = compFilter.getFilter();
    compSet.xor(baseSet); // modifies compSet to the xor of itself and baseSet
    compSet.flip(0, baseFilter.size()); // xnor = complement of xor
    return baseFilter.size() - compSet.cardinality();
  }

  @Override
  public BloomFilter getBase() {
    return baseFilter;
  }
}
