package edu.brown.cs.student.main.Builder;

import java.util.List;
/**
 * Interface for a builder of class T.
 * @param <T> - the class that the class that implements CSVBuilder will create with build method.
 */
public interface CSVBuilder<T> {
  /**
   * Adds class made from input line to list.
   * @param fields - List of Strings that is a line from CSV File.
   * @return - object to add to data list of field where this method is called. In the
   * case of an error (such as a NumberFormat Exception), build will return null.
   */
  T build(List<String> fields);

  /**
   * Acquires the expected column titles for input CSV line.
   * @return - String of the expected column titles of CSV file.
   */
  String getColumnTitles();
}
