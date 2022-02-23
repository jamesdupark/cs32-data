package edu.brown.cs.student.main.Blooms;

import java.util.List;

/**
 * Class representing bloom filters for students, which stores representations
 * of each student's race, class year, communication style, meeting style,
 * skills, and interests. Does not support inserting elements after
 * initialization.
 */
public class StudentBloom extends BloomFilter {
  /** default false positive rate for studentBloom filters. */
  private static final double DEFAULT_FP_RATE = 0.1;

  /** unique id of the student, contained as the first element of toInsert. */
  private final int id;

  /**
   * Constructor for StudentBloom objects - initializes the filter with the
   * given parameters and adds all strings in toInsert to the filter.
   * @param maxElts maximum number of elements in toInsert
   * @param toInsert list of strings to be inserted into the filter. The first
   *                 element of toInsert is the integer id of the filter.
   * @throws IllegalArgumentException when maxElts results in impossible filter
   * parameters
   */
  public StudentBloom(int maxElts, List<String> toInsert)
      throws IllegalArgumentException {
    super(DEFAULT_FP_RATE, maxElts); // create bloom filter
    assert maxElts >= toInsert.size(); // check filter can fit all elements

    // parse and set id
    int studentID;
    try {
      studentID = Integer.parseInt(toInsert.get(0));
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("ERROR: first element of strings to "
          + "insert should be student's integer ID.");
    }
    this.id = studentID;

    // insert all strings into filter
    for (String elt : toInsert) {
      super.insert(elt);
    }
  }

  @Override
  public void insert(Object obj) {
    throw new UnsupportedOperationException();
  }

  @Override
  public int getId() {
    return this.id;
  }
}
