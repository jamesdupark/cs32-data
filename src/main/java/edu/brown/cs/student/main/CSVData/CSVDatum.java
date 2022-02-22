package edu.brown.cs.student.main.CSVData;

import edu.brown.cs.student.main.KDNodes.KDNode;

/**
 * Interface that defines the properties of a datum inside the CSV.
 * Each datum inside the CSV must be able to become a KDNode, which
 * is later inserted into the KDTree.
 */
public interface CSVDatum {
  KDNode toKDNode();
}
