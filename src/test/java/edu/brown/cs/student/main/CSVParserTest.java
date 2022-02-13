package edu.brown.cs.student.main;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class CSVParserTest {

  @Test
  public void parseDataTest() {
    CSVParser parser = new CSVParser();
    parser.parse("data/project1/proj1_small.csv");
    assertEquals(parser.getData().size(), 20, 0);
    assertEquals(parser.getData().get(0).getId(), 1, 0);
    assertEquals(parser.getData().get(0).getName(), "Stanton Swalough");
    assertEquals(parser.getData().get(0).getEmail(), "sswalough0@ask.com");
    assertEquals(parser.getData().get(0).getGender(), "Female");
    assertEquals(parser.getData().get(0).getClassYear(), "junior");
    assertEquals(parser.getData().get(0).getNationality(), "Russia");
    assertEquals(parser.getData().get(0).getRace(), "American Indian or Alaska Native");
    assertEquals(parser.getData().get(0).getYearsExp(), 18);
    assertEquals(parser.getData().get(0).getCommStyle(), "email");
    assertEquals(parser.getData().get(0).getWeeklyAvail(), 2);
    assertEquals(parser.getData().get(0).getMeetingStyle(), "in person");
    assertEquals(parser.getData().get(0).getMeetingTime(), "morning");
    assertEquals(parser.getData().get(0).getSweConfidence(), 2);
    assertEquals(parser.getData().get(0).getStrengths(),
        "\"quick learner, prepared, team player, early starter, friendly\"");
    assertEquals(parser.getData().get(0).getWeaknesses(), "\"cutthroat, unfriendly, late\"");
    assertEquals(parser.getData().get(0).getSkills(), "OOP");
    assertEquals(parser.getData().get(0).getInterests(),
        "\"mathematics, film/photography, politics\"");

  }
}
