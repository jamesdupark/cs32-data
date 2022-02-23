package edu.brown.cs.student.main.Blooms.SimilarityMetrics;

import edu.brown.cs.student.main.Blooms.BloomFilter;

import java.util.Comparator;

/**
 * Interface for a comparator which calculates the similarity between a given
 * bloom filter and a base filter.
 * @author jamesdupark
 */
public interface BloomComparator extends Comparator<BloomFilter> {
  /**
   * Method that returns an integer representing the similarity between the
   * inputted filter and the comparator's base filter. First checks that the
   * number of hashes and bitset size are the same, then applies the desired
   * similarity algorithm.
   * @param compFilter filter to compare to the base filter
   * @return integer representing filter similarity metric
   */
  int similarity(BloomFilter compFilter);

  /**
   * Returns the bloom filter used as the base of this BloomComparator.
   * @return base filter for this comparator.
   */
  BloomFilter getBase();
}
