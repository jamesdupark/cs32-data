package edu.brown.cs.student.main.API.json;

import java.util.Objects;

/**
 * Simple Message class for interacting with JSON parsers. Taken from the cs32
 * API lab.
 *
 */
public class Message {
  /**
   * Message stored by the json object.
   */
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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Message message1 = (Message) o;
    return Objects.equals(getMessage(), message1.getMessage());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getMessage());
  }
}
