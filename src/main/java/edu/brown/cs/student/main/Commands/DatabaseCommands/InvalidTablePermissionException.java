package edu.brown.cs.student.main.Commands.DatabaseCommands;

/**
 * Class to throw an exception if the user enters an invalid table permission that
 * is not of the following form: <R>, <W>, or <RW>.
 */
public class InvalidTablePermissionException extends Exception {
  /**
   * Constructor for InvalidTablePermissionException.
   * @param errorMessage message containing the error.
   */
  public InvalidTablePermissionException(String errorMessage) {
    super(errorMessage);
  }
}
