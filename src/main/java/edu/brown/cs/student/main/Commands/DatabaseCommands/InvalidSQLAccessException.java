package edu.brown.cs.student.main.Commands.DatabaseCommands;

/**
 * Class to throw an exception if the Database has an invalid table permission
 * pertaining to the level of access required from a SQL Command. That is, a user
 * inputted a SQL command that is not supported by the level of assess as defined
 * according to the table permission.
 */
public class InvalidSQLAccessException extends Exception {
  /**
   * Constructor for InvalidSQLAccessException.
   * @param errorMessage message containing the error.
   */
  public InvalidSQLAccessException(String errorMessage) {
    super(errorMessage);
  }
}
