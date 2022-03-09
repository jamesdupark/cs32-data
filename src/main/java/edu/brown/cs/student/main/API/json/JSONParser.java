package edu.brown.cs.student.main.API.json;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.reflect.TypeToken;
import edu.brown.cs.student.main.CSVParse.FileParser;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
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
   * Generic parametrized list typing from: https://stackoverflow.com/questions/20773850/
   * gson-typetoken-with-dynamic-arraylist-item-type
   * @param jsonObject json object storing the information about type T
   * @param <T> type of object to be read from the json
   * @param tClass class to be read from the json
   * @return list of type T
   * @throws JsonSyntaxException upon encountering ill-formatted json
   */
  public static <T extends JSONable> List<T> getJsonObjectList(String jsonObject, Class<T> tClass)
      throws JsonSyntaxException {
    Gson parser = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
    Type jsonObjectListType = TypeToken.getParameterized(List.class, tClass).getType();
    return parser.fromJson(jsonObject, jsonObjectListType);
  }

  /**
   * Converts a json representing a single JSONable object to its respective object.
   * camelCase matching method from: https://stackoverflow.com/questions/2370745/
   * convert-json-style-properties-names-to-java-camelcase-names-with-gson
   * @param jsonObject json object storing the information about type T
   * @param <T> type of object to be read from the json
   * @param tClass class to be read from the json
   * @return object of type T
   * @throws JsonSyntaxException upon encountering ill-formatted json
   */
  public static <T extends JSONable> T getJsonObject(String jsonObject, Class<T> tClass)
      throws JsonSyntaxException {
    Gson parser = new GsonBuilder()
        .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
        .create();
    return parser.fromJson(jsonObject, tClass);
  }

  /**
   * Reads a json file into a list of objects of type T. Each line of the json file
   * represents a single json object.
   * @param filepath filepath to the .json file.
   * @param tClass class of T
   * @param <T> type of the json object to be returned
   * @return list of type T
   * @throws IOException upon encountering an issue with json formatting or reading the file.
   */
  public static <T extends JSONable> List<T> readJsonFile(String filepath, Class<T> tClass)
      throws IOException {
    try {
      FileParser parser = new FileParser(filepath);
      List<T> jsonList = new ArrayList<>();
      String line = parser.readNewLine();
      while (line != null) {
        // find json object within line (ignoring extraneous vals like "," and "[")
        int openInd = line.indexOf("{");
        int closeInd = line.indexOf("}");
        assert openInd >= 0 && closeInd >= 0 : "json object lacks enclosing {}";
        String jsonString = line.substring(openInd, closeInd + 1);
        // convert jsonString to jsonObject
        T jsonObject = JSONParser.getJsonObject(jsonString, tClass);
        jsonList.add(jsonObject);
        line = parser.readNewLine();
      }
      return jsonList;
    } catch (FileNotFoundException fe) {
      throw new IOException("ERROR: file " + filepath + " not found.");
    } catch (JsonSyntaxException jse) {
      throw new IOException("ERROR: invalid json syntax.");
    } catch (AssertionError ase) {
      throw new IOException("ERROR" + ase.getMessage());
    }
  }
}

