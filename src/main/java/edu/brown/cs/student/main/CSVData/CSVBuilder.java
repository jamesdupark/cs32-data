package edu.brown.cs.student.main.CSVData;

import java.util.List;

/**
 * Interface that
 * @param <T>
 */
public interface CSVBuilder<T extends CSVDatum> {
  /**
   * Adds class made from input line to list.
   * @param fields - List of Strings that is a line from CSV File.
   * @return - object to add to data list of field where this method is called.
   */
  T build(List<String> fields);

  /**
   * Acquires the expected column titles for input CSV line.
   * @return - String of the expected column titles of CSV file.
   */
  String getColumnTitles();
}
