package edu.brown.cs.student.main;

import java.util.List;
import java.util.Locale;

public class Header {
  /**
   * The name of the field / header.
   */
  private String fieldName;
  /**
   * The data type of the fieldName (qualitative or quantitative).
   */
  private String dataType;

  /**
   * Constructor for new Header Class.
   * @param fields - list of parsed strings from CSV file.
   */
  public Header(List<String> fields) {
    // name of the field
    this.fieldName = fields.get(0);
    // appending everything after comma in CSV file line.
    StringBuilder type = new StringBuilder();
    for (int i = 1; i < fields.size(); i++) {
      String[] splitBySpace = fields.get(i).split(" ");
      for (String s : splitBySpace) {
        type.append(s.trim());
      }
    }
    String cleanedType = type.toString().toLowerCase();
    if (!cleanedType.equals("quantitative") && !cleanedType.equals("qualitative")) {
      throw new IllegalArgumentException("ERROR: 'Header Description' column values must be either"
          + "'quantitative' or 'qualitative' after being cleaned.");
    }
    this.dataType = cleanedType;
  }

  /**
   * Getter for fieldName field.
   * @return - the fieldName field.
   */
  public String getFieldName() {
    return this.fieldName;
  }

  /**
   * Getter for dataType field.
   * @return - the dataType field.
   */
  public String getDataType() {
    return this.dataType;
  }
}
