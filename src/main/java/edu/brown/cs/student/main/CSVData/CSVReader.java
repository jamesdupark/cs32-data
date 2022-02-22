package edu.brown.cs.student.main.CSVData;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class to Read CSV File and load its content into list of chosen classes.
 * @param <T> - the datatype of objects we make list of from CSV file.
 */
public class CSVReader<T extends CSVDatum> {
  private final List<CSVDatum> dataList;
  private final HashMap<String, CSVBuilder<CSVDatum>> builderMap;

  /**
   * Constructor for CSVReader.
   * @param builderList - A list of classes that implements the CSVBuilder interface.
   */
  public CSVReader(List<CSVBuilder<CSVDatum>> builderList) {
    this.dataList = new ArrayList<CSVDatum>();
    this.builderMap = new HashMap<String, CSVBuilder<CSVDatum>>();
    for (CSVBuilder<CSVDatum> builder : builderList) {
      builderMap.put(builder.getColumnTitles(), builder);
    }
  }
  public List<CSVDatum> getDataList() {
    return this.dataList;
  }

  public void load(String filePath) {
    try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
      String line = reader.readLine();
      // checking for correct CSV column titles.
      if (!this.builderMap.containsKey(line)) {
        System.out.println("ERROR: CSV column names does not match expected");
      } else {
        CSVBuilder<CSVDatum> builder = this.builderMap.get(line);
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
