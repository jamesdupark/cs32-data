package edu.brown.cs.student.main.API.json;

/**
 * Simple Message class for interacting with JSON parsers.
 *
 */
public class Message {

  private String message;

  /**
   * Simple constructor.
   * @param message the message extracted from the JSON object.
   */
  public Message(String message) {
    this.message = message;
  }

  /**
   * Returns the extracted message.
   * @return the extracted message.
   */
  public String getMessage() {
    return this.message;
  }
}
