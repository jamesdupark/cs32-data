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
//  private final HashMap<String, CSVBuilder<T>> builderMap;

  /**
   * Constructor for CSVReader.
   * @param builder fill
   */
  public CSVParser(CSVBuilder<T> builder) {
    this.dataList = new ArrayList<>();
    this.builder = builder;
//    this.builderMap = new HashMap<String, CSVBuilder<CSVDatum>>();
//    for (CSVBuilder<CSVDatum> builder : builderList) {
//      builderMap.put(builder.getColumnTitles(), builder);
//    }
  }
  public List<T> getDataList() {
    return this.dataList;
  }

  /**
   * Loads data from CSV file into list of CSVDatum.
   * Regex and matchlist design inspired by: https://stackoverflow.com/questions/366202/ +
   * regex-for-splitting-a-string-using-space-when-not-surrounded-by-single-or-double/366532#366532
   * @param filePath - String that is the file path to CSV file.
   */
  public void load(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line = reader.readLine();
      // checking for correct CSV column titles.
      if (!this.builder.getColumnTitles().equals(line)) {
        throw new IOException("ERROR: CSV column names does not match expected");
      } else {
        line = reader.readLine();
        int count = 0;
        // looping through each line in the csv file after the column names
        while (line != null) {
          List<String> matchList = new ArrayList<String>();
          Pattern regex = Pattern.compile("[^,\"]+|\"([^\"]*)\"");
          Matcher regexMatcher = regex.matcher(line);
          while (regexMatcher.find()) {
            matchList.add(regexMatcher.group());
          }
          this.dataList.add(builder.build(matchList));
          count++;
          line = reader.readLine();
        }
      }
    } catch (IOException e) {
      System.out.println("ERROR:" + e);
    }
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
