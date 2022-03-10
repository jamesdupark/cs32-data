package edu.brown.cs.student.main.API.APIRequests;

/**
 * Exception to be thrown when api requests return a bad status code.
 */
public class BadStatusException extends Exception {
  /**
   * status code of this exception.
   */
  private final int statusCode;
  /**
   * Constructor for the bad status exception.
   * @param s exception message
   * @param statusCode status code of the bad http request.
   */
  public BadStatusException(String s, int statusCode) {
    super(s);
    this.statusCode = statusCode;
  }

  /**
   * gets that status code associated with the bad http request.
   * @return this exception's status code.
   */
  public int getStatus() {
    return this.statusCode;
  }
}
