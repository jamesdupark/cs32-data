package edu.brown.cs.student.main.CSVParse.Builder;

import edu.brown.cs.student.main.Recommender.Header;

import java.util.List;

public class HeaderBuilder implements CSVBuilder<Header> {
  @Override
  public Header build(List<String> fields) {
    try {
      return new Header(fields);
    } catch (IllegalArgumentException ex) {
      System.err.println(ex.getMessage());
      return null;
    }
  }
  @Override
  public String getColumnTitles() {
    return "Header Name,Header Description";
  }
}
