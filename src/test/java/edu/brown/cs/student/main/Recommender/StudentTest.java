package edu.brown.cs.student.main.Recommender;

import edu.brown.cs.student.main.API.json.JSONParser;
import edu.brown.cs.student.main.CSVParse.Builder.StudentBuilder;
import edu.brown.cs.student.main.Recommender.Stud.Student;
import edu.brown.cs.student.main.Recommender.Stud.StudentInfo;
import edu.brown.cs.student.main.Recommender.Stud.StudentMatch;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;

public class StudentTest {
  StudentBuilder builder;
  List<String> input;

  @Before
  public void init() {
    HashMap<String,String> typeMap = new HashMap<>();
    typeMap.put("id", "qualitative");
    typeMap.put("name", "qualitative");
    typeMap.put("email", "qualitative");
    typeMap.put("gender", "qualitative");
    typeMap.put("class_year", "qualitative");
    typeMap.put("ssn", "qualitative");
    typeMap.put("nationality", "qualitative");
    typeMap.put("race", "qualitative");
    typeMap.put("years_experience", "quantitative");
    typeMap.put("communication_style", "qualitative");
    typeMap.put("weekly_avail_hours", "quantitative");
    typeMap.put("meeting_style", "qualitative");
    typeMap.put("meeting_time", "qualitative");
    typeMap.put("software_engn_confidence", "quantitative");
    typeMap.put("strengths", "qualitative");
    typeMap.put("weaknesses", "qualitative");
    typeMap.put("skills", "qualitative");
    typeMap.put("interests", "qualitative");
    builder = new StudentBuilder(typeMap);
    input = List.of("1", "Stanton Swalough", "sswalough0@ask.com", "Female", "junior",
        "375-75-3870", "Russia", "American Indian or Alaska Native", "18", "email", "2",
        "in person", "morning", "2",
        "\"quick learner, prepared, team player, early starter, friendly\"",
        "\"cutthroat, unfriendly, late\"", "OOP", "\"mathematics, film/photography, politics\"");
  }

  @Test
  public void testBuilder() {
    Student output = builder.build(input);
    assertEquals(output.getQualMap().get("id"), "1");
    assertEquals(output.getQualMap().get("name"), "Stanton Swalough");
    assertEquals(output.getQualMap().get("email"), "sswalough0@ask.com");
    assertEquals(output.getQualMap().get("gender"), "Female");
    assertEquals(output.getQualMap().get("class_year"), "junior");
    assertEquals(output.getQualMap().get("ssn"), "375-75-3870");
    assertEquals(output.getQualMap().get("nationality"), "Russia");
    assertEquals(output.getQualMap().get("race"), "American Indian or Alaska Native");
    assertEquals(output.getQuanMap().get("years_experience"), 18, 0);
    assertEquals(output.getQualMap().get("communication_style"), "email");
    assertEquals(output.getQuanMap().get("weekly_avail_hours"), 2, 0);
    assertEquals(output.getQualMap().get("meeting_style"), "in person");
    assertEquals(output.getQualMap().get("meeting_time"), "morning");
    assertEquals(output.getQuanMap().get("software_engn_confidence"), 2, 0);
    assertEquals(output.getQualMap().get("strengths"),
        "\"quick learner, prepared, team player, early starter, friendly\"");
    assertEquals(output.getQualMap().get("weaknesses"), "\"cutthroat, unfriendly, late\"");
    assertEquals(output.getQualMap().get("skills"),
        "OOP");
    assertEquals(output.getQualMap().get("interests"),
        "\"mathematics, film/photography, politics\"");
  }

  @Test
  public void testAdding() {
    Student scholar = builder.build(input);
    scholar.addQual("favorite_gum", "mint");
    scholar.addQuan("favorite_double", 0.01);
    assertEquals(scholar.getQualMap().get("favorite_gum"), "mint");
    assertEquals(scholar.getQuanMap().get("favorite_double"), 0.01, 0);

  }
}
