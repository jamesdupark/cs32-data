package edu.brown.cs.student.main.KDimTree;

/**
 * Class to throw an exception if user ID is not found in CSV.
 */
public class KeyNotFoundException extends Exception {
  /**
   * Constructor for UserIDNotFoundException.
   * @param errorMessage message containing the error.
   */
  public KeyNotFoundException(String errorMessage) {
    super(errorMessage);
  }
}
