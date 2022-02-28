package edu.brown.cs.student.main.API.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

/**
 * JSONParser class to learn about JSONs in Java. Adapted from the cs32 API lab.
 *
 */
public final class JSONParser {
  /**
   * constructor for JSONParser - never called since JSONParser is a utility class.
   */
  private JSONParser() {
    // not called
  }

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

  /**
   * Deserializes a json that just represents a list of strings. Conversion method taken from:
   * https://www.baeldung.com/gson-list.
   * @param jsonObject json string representing a list of strings
   * @return list of strings
   * @throws JsonSyntaxException upon encountering ill-formatted json string.
   */
  public static List<String> toStringList(String jsonObject) throws JsonSyntaxException {
    Gson parser = new Gson();
    Type stringListType = new TypeToken<List<String>>() { } .getType();
    return parser.fromJson(jsonObject, stringListType);
  }

  /**
   * Deserializes a json that represents a list of objects of the given class.
   * Conversion method taken from: https://www.baeldung.com/gson-list.
   * camelCase matching method from: https://stackoverflow.com/questions/2370745/
   * convert-json-style-properties-names-to-java-camelcase-names-with-gson
   * @param jsonObject json object storing the studentInfo information
   * @param <T> type of object to be read from the json
   * @param tClass class to be read from the json
   * @return list of StudentInfo
   * @throws JsonSyntaxException upon encountering ill-formatted
   */
  public static <T extends JSONable> List<T> getJsonObjectList(String jsonObject, Class<T> tClass)
      throws JsonSyntaxException {
    Gson parser = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
    Type jsonObjectListType = new TypeToken<List<T>>() { } .getType();
    return parser.fromJson(jsonObject, jsonObjectListType);
  }

  /**
   * Converts a json representing a single JSONable object to its respective object.
   * camelCase matching method from: https://stackoverflow.com/questions/2370745/
   * convert-json-style-properties-names-to-java-camelcase-names-with-gson
   * @param jsonObject json object storing the studentInfo information
   * @param <T> type of object to be read from the json
   * @param tClass class to be read from the json
   * @return list of StudentInfo
   * @throws JsonSyntaxException upon encountering ill-formatted
   */
  public static <T extends JSONable> T getJsonObject(String jsonObject, Class<T> tClass)
      throws JsonSyntaxException {
    Gson parser = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
    return parser.fromJson(jsonObject, tClass);
  }
}
