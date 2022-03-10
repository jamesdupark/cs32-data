package edu.brown.cs.student.main.Commands.DatabaseCommands;

/**
 * Class to throw an exception if the Database has an invalid table permission
 * pertaining to the level of access required from a SQL Command.
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
