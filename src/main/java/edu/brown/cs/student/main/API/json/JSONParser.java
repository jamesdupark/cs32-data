package edu.brown.cs.student.main.API.json;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

/**
 * JSONParser class to learn about JSONs in Java.
 *
 */
public class JSONParser {

  /**
   * Extracts the message parameter from a JSON object and stores it into a Message object.
   * Then, it prints the stored message in the Message object.
   * @param jsonObject stores the message.
   * @return message string from a json object
   * @throws JsonSyntaxException if the json object given is not properly formatted.
   */
  public static String getMessage(String jsonObject) throws JsonSyntaxException {
    Gson parser = new Gson();
    Message myMessage = parser.fromJson(jsonObject, Message.class);
    return myMessage.getMessage();
  }
}
