package edu.brown.cs.student.main.KDimTree;

/**
 * Class to throw an exception if input k is negative.
 */
public class KIsNegativeException extends Exception {
  /**
   * Constructor for KIsNegativeException.
   * @param errorMessage message containing the error.
   */
  public KIsNegativeException(String errorMessage) {
    super(errorMessage);
  }
}
