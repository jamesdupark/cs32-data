package edu.brown.cs.student.main.CSVData;

import edu.brown.cs.student.main.BloomFilter.BloomFilter;
import edu.brown.cs.student.main.KDNodes.KDNode;

/**
 * Interface that defines the properties of a datum inside the CSV.
 * Each datum inside the CSV must be able to become a KDNode, which
 * is later inserted into the KDTree.
 */
public interface CSVDatum {
  KDNode toKDNode();

  /**
   * Converts a CSVDatum object into a bloom filter by inserting the appropriate
   * fields into a bloom filter.
   * @param maxElts max number of elements in the filter
   * @return bloom filter representing the selected fields
   */
  BloomFilter toBloomFilter(int maxElts);

  /**
   * Returns the maximum number of strings that could be inserted into a bloom
   * filter for this CSVDatum object.
   * @return max number of possible strings for a bloom filter for this object
   */
  int getMaxElts();
}
