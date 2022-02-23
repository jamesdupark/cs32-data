package edu.brown.cs.student.main.Builder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Builder class for constructing StudentBloom filters. Hands a list of strings
 * to be inserted into the student bloom filter.
 * @author jamesdupark
 */
public class StudentBloomListBuilder implements CSVBuilder<List<String>> {
  /**
   * index of id field in parsed list of strings representing a csv line.
   */
  private final int idIdx = 0;
  /**
   * index of class yr field in parsed list of strings representing a csv line.
   */
  private final int classIdx = 4;
  /**
   * index of race field in parsed list of strings representing a csv line.
   */
  private final int raceIdx = 7;
  /**
   * index of communication style field in parsed list of strings representing a csv line.
   */
  private final int commStyleIdx = 9;
  /**
   * index of meeting style field in parsed list of strings representing a csv line.
   */
  private final int meetStyleIdx = 11;
  /**
   * index of strengths field in parsed list of strings representing a csv line.
   */
  private final int strIdx = 14;
  /**
   * index of skills field in parsed list of strings representing a csv line.
   */
  private final int skillIdx = 16;
  /**
   * index of interests field in parsed list of strings representing a csv line.
   */
  private final int interestIdx = 17;

  @Override
  public List<String> build(List<String> fields) {
    List<String> retList = new ArrayList<String>();
    // get strings from fields list
    String id = fields.get(idIdx);
    String classYr = fields.get(classIdx);
    String race = fields.get(raceIdx);
    String communicationStyle = fields.get(commStyleIdx);
    String meetingStyle = fields.get(meetStyleIdx);
    String strengths = fields.get(strIdx);
    String skills = fields.get(skillIdx);
    String interests = fields.get(interestIdx);

    // add strings to field list and parse multi-element fields
    List<String> singleFields = List.of(id, classYr, race, communicationStyle, meetingStyle);
    retList.addAll(singleFields);
    retList.addAll(parseField(strengths));
    retList.addAll(parseField(skills));
    retList.addAll(parseField(interests));
    return retList;
  }

  @Override
  public String getColumnTitles() {
    return "id,name,email,gender,class_year,ssn,nationality,race,years_experience,"
        + "communication_style,weekly_avail_hours,meeting_style,meeting_time,"
        + "software_engn_confidence,strengths,weaknesses,skills,interests";
  }

  /**
   * Parses a multi-element quote-enclosed field into a list of strings.
   * @param multField field of a csv that contains multiple elements
   * @return the same field as a list of strings with enclosing quotes removed
   */
  public List<String> parseField(String multField) {
    multField.replace("\"", "");
    return Arrays.asList(multField.split(", ").clone());
  }
}
