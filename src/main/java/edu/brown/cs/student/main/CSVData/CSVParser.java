package edu.brown.cs.student.main.CSVData;

import edu.brown.cs.student.main.CSVData.CSVDatum;
import edu.brown.cs.student.main.CSVData.Student;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CSVParser<T extends CSVDatum> {
  private List<CSVDatum> data;
  private String header;
  public CSVParser() {
    this.data = new ArrayList<>();
  }
  public List<CSVDatum> getData() {
    return this.data;
  }

  public void parse(String path) throws IOException {
    BufferedReader reader = new BufferedReader(new FileReader(path));
    String line = reader.readLine();
    // checking for correct CSV column titles.
    header = line;
    if (!line.equals("id,name,email,gender,class_year,ssn,nationality,race,years_experience,"
        + "communication_style,weekly_avail_hours,meeting_style,meeting_time,"
        + "software_engn_confidence,strengths,weaknesses,skills,interests")) {
      System.out.println("ERROR: CSV column names does not match expected");
    } else {
      line = reader.readLine();
      int count = 0;
      // looping through each line in the csv file after the column names
      while (line != null) {
        ArrayList<String> matchList = new ArrayList<String>();
        Pattern regex = Pattern.compile("[^,\"]+|\"([^\"]*)\"");
        Matcher regexMatcher = regex.matcher(line);
        while (regexMatcher.find()) {
          matchList.add(regexMatcher.group());
        }
        // adding new Student to list of students
        Student newStudent = new Student(matchList);
        this.data.add(newStudent);
        count++;
        line = reader.readLine();
      }
    }
  }
}
