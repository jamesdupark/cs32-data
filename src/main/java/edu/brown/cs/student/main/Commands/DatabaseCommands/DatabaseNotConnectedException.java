package edu.brown.cs.student.main.Commands.DatabaseCommands;

/**
 * Class to throw an exception if the user tries to execute a SQL query without
 * connecting to the respective, correct Database first.
 */
public class DatabaseNotConnectedException extends Exception {
  /**
   * Constructor for DatabaseNotConnectedException.
   * @param errorMessage message containing the error.
   */
  public DatabaseNotConnectedException(String errorMessage) {
    super(errorMessage);
  }
}
