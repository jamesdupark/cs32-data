package edu.brown.cs.student.main;

import edu.brown.cs.student.main.Builder.CSVBuilder;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to Read CSV File and load its content into list of chosen classes.
 * @param <T> - the datatype of objects we make list of from CSV file.
 */
public class CSVParser<T> {
  private final List<T> dataList;
  private CSVBuilder<T> builder;

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
          List<String> matchList = new ArrayList<>();
          Pattern regex = Pattern.compile("[^,\"]+|\"([^\"]*)\"");
          Matcher regexMatcher = regex.matcher(line);
          while (regexMatcher.find()) {
            matchList.add(regexMatcher.group());
          }
          T item = builder.build(matchList);
          if (item != null) {
            this.dataList.add(builder.build(matchList));
          } else {
            for (T object : this.dataList) {
              this.dataList.remove(object);
            }
            return false;
          }
          line = reader.readLine();
        }
      }
    } catch (IOException e) {
      System.out.println("ERROR: " + e);
      return false;
    }
    return true;
  }


  /**
   * Gets the string representing the name of the objects being represented by
   * this datum.
   * @return string name of the CSVDatum class.
   */
  public String getDatumName() {
    return null;
  }
}
