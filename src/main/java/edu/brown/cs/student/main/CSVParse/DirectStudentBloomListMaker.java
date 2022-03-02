package edu.brown.cs.student.main.CSVParse;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * StudentBloomList maker class that makes BloomList from Student class qualMap.
 */
public class DirectStudentBloomListMaker {

  /**
   * Builds a List of String to input for Bloom Filter.
   * @param fields - A hashmap from qualitative field name to its field value.
   * @return - List of String to input for Bloom Filter
   */
  public List<String> build(HashMap<String, String> fields) {
    List<String> retList = new ArrayList<>();
    // get strings from fields list
    String id = fields.get("id");
    String classYr = fields.get("class_year");
    String race = fields.get("race");
    String communicationStyle = fields.get("communication_style");
    String meetingStyle = fields.get("meeting_style");
    String strengths = fields.get("strengths");
    String skills = fields.get("skills");
    String interests = fields.get("interests");

    // add strings to field list and parse multi-element fields
    List<String> singleFields = List.of(id, classYr, race, communicationStyle, meetingStyle);
    retList.addAll(singleFields);
    retList.addAll(parseField(strengths));
    retList.addAll(parseField(skills));
    retList.addAll(parseField(interests));
    return retList;
  }

  /**
   * Parses a multi-element quote-enclosed field into a list of strings.
   * @param multField field of a csv that contains multiple elements
   * @return the same field as a list of strings with enclosing quotes removed
   */
  private List<String> parseField(String multField) {
    String quoteRemoved = multField.replace("\"", "");
    return Arrays.asList(quoteRemoved.split(", ").clone());
  }
}
