package edu.brown.cs.student.main.API.APIRequests;

/**
 * Exception to be thrown when api requests return a bad status code.
 */
public class BadStatusException extends Exception {
  /**
   * Constructor for the bad status exception.
   * @param s exception message
   */
  public BadStatusException(String s) {
    super(s);
  }
}
