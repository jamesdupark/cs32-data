package edu.brown.cs.student.main;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class CSVParserTest {

  @Test
  public void parseDataTest() throws IOException {
    CSVParser parser = new CSVParser();
    parser.parse("data/project1/proj1_small.csv");
    assertEquals(parser.getData().size(), 20, 0);
    assertEquals(((Student)(parser.getData().get(0))).getId(), 1, 0);
    assertEquals(((Student)(parser.getData().get(0))).getName(), "Stanton Swalough");
    assertEquals(((Student)(parser.getData().get(0))).getEmail(), "sswalough0@ask.com");
    assertEquals(((Student)(parser.getData().get(0))).getGender(), "Female");
    assertEquals(((Student)(parser.getData().get(0))).getClassYear(), "junior");
    assertEquals(((Student)(parser.getData().get(0))).getNationality(), "Russia");
    assertEquals(((Student)(parser.getData().get(0))).getRace(), "American Indian or Alaska Native");
    assertEquals(((Student)(parser.getData().get(0))).getYearsExp(), 18, 0.1);
    assertEquals(((Student)(parser.getData().get(0))).getCommStyle(), "email");
    assertEquals(((Student)(parser.getData().get(0))).getWeeklyAvail(), 2, 0.1);
    assertEquals(((Student)(parser.getData().get(0))).getMeetingStyle(), "in person");
    assertEquals(((Student)(parser.getData().get(0))).getMeetingTime(), "morning");
    assertEquals(((Student)(parser.getData().get(0))).getSweConfidence(), 2, 0.1);
    assertEquals(((Student)(parser.getData().get(0))).getStrengths(),
        "\"quick learner, prepared, team player, early starter, friendly\"");
    assertEquals(((Student)(parser.getData().get(0))).getWeaknesses(), "\"cutthroat, unfriendly, late\"");
    assertEquals(((Student)(parser.getData().get(0))).getSkills(), "OOP");
    assertEquals(((Student)(parser.getData().get(0))).getInterests(),
        "\"mathematics, film/photography, politics\"");

  }
}
