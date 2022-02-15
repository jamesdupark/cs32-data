package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVParser {
  private ArrayList<Student> data;
  CSVParser() {
    this.data = new ArrayList<Student>();
  }
  public ArrayList<Student> getData() {
    return this.data;
  }

  public void parse(String path) {
    try (BufferedReader reader = new BufferedReader(new FileReader(path))) {
      String line = reader.readLine();
      // checking for correct CSV column titles.
      if (!line.equals("id,name,email,gender,class_year,ssn,nationality,race,years_experience,"
          + "communication_style,weekly_avail_hours,meeting_style,meeting_time,"
          + "software_engn_confidence,strengths,weaknesses,skills,interests")) {
        System.out.println("ERROR: CSV column names does not match expected");
      } else {
        line = reader.readLine();
        int count = 1;
        // looping through each line in the csv file after the column names
        while (line != null) {
          ArrayList<String> matchList = new ArrayList<String>();
          Pattern regex = Pattern.compile("[^,\"]+|\"([^\"]*)\"");
          Matcher regexMatcher = regex.matcher(line);
          while (regexMatcher.find()) {
            matchList.add(regexMatcher.group());
          }
          // adding new Star to data field LinkedList
          Student newStudent = new Student(Integer.parseInt(matchList.get(0)), matchList.get(1),
              matchList.get(2), matchList.get(3), matchList.get(4), matchList.get(6),
              matchList.get(7), Double.parseDouble(matchList.get(8)), matchList.get(9),
              Double.parseDouble(matchList.get(10)), matchList.get(11), matchList.get(12),
              Double.parseDouble(matchList.get(13)), matchList.get(14), matchList.get(15),
              matchList.get(16), matchList.get(17));
          this.data.add(newStudent);
          count++;
          line = reader.readLine();
        }
        // prints total number of stars added/read from csv file
        System.out.println("Read " + count + " students from " + path);
      }
    } catch (IOException e) {
      System.out.println("ERROR:" + e);
    }
  }
}
