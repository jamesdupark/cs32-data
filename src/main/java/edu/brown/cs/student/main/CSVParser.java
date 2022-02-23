package edu.brown.cs.student.main;

import edu.brown.cs.student.main.Builder.CSVBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to Read CSV File and load its content into list of chosen classes.
 * @param <T> - the datatype of objects we make list of from CSV file.
 */
public class CSVParser<T> {
  private final List<T> dataList;
  private final CSVBuilder<T> builder;

  /**
   * Constructor for CSVReader.
   * @param builder fill
   */
  public CSVParser(CSVBuilder<T> builder) {
    this.dataList = new ArrayList<>();
    this.builder = builder;
  }

  public List<T> getDataList() {
    return this.dataList;
  }

  /**
   * Loads data from CSV file into list of CSVDatum.
   * Regex and matchlist design inspired by: https://stackoverflow.com/questions/366202/ +
   * regex-for-splitting-a-string-using-space-when-not-surrounded-by-single-or-double/366532#366532
   * @param filePath - String that is the file path to CSV file.
   * @return - boolean of whether loading in CSV file information succeeded or not.
   */
  public boolean load(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line = reader.readLine();
      // checking for correct CSV column titles.
      if (!this.builder.getColumnTitles().equals(line)) {
        throw new IOException("CSV column names does not match expected");
      } else {
        line = reader.readLine();
        // looping through each line in the csv file after the column names
        while (line != null) {
          String[] quoteSplit = line.split("\"", -1);
          List<String> parseList = new ArrayList<>();

          for (int i = 0; i < quoteSplit.length; i++) {
            String token = quoteSplit[i];

            if (i % 2 == 0) { // odd numbered element, not in quotes
              String[] commaSplit = token.split(",", -1);
              parseList.addAll(Arrays.asList(commaSplit));
            } else { // even numbered element, between quotes
              token = "\"" + token + "\"";
              parseList.add(token);
            }
          }
          T item = builder.build(parseList);
          if (item != null) {
            this.dataList.add(builder.build(parseList));
          } else {
            for (T object : this.dataList) {
              this.dataList.remove(object);
            }
            return false;
          }
          line = reader.readLine();
        }
        return true;
      }
    } catch (IOException e) {
      System.out.println("ERROR: " + e.getMessage());
      return false;
    }
  }
}
