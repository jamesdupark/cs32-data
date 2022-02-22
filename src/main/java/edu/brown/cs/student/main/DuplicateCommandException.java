package edu.brown.cs.student.main;

/**
 * Exception class for when two command strings in a REPL's commandMap intersect
 * with each other.
 */
public class DuplicateCommandException extends Exception {
  /**
   * Constructor for DuplicateCommandException exceptions.
   * @param message exception message
   */
  public DuplicateCommandException(String message) {
    super(message);
  }
}
