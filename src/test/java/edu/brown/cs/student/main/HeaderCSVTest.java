package edu.brown.cs.student.main;

import edu.brown.cs.student.main.Blooms.BloomFilter;
import edu.brown.cs.student.main.Commands.HeaderCommands;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class HeaderCSVTest {

  @Before
  public void setup() {
  }
  @Test
  public void testConstructor() {
    assertThrows(IllegalArgumentException.class, () -> new Header(List.of("hi", "non")));
    assertThrows(IllegalArgumentException.class, () -> new Header(List.of("hi", "qualitatives")));
  }

  @Test
  public void testGetter() {
    Header head = new Header(List.of("name", "qualitative"));
    Header head2 = new Header(List.of("name", "q u a n t i t a t i v e"));
    assertEquals(head.getFieldName(), "name");
    assertEquals(head.getDataType(), "qualitative");
    assertEquals(head2.getDataType(), "quantitative");
  }

  @Test
  public void testTypeMap() {
    HeaderCommands cmd = new HeaderCommands(new HashMap<String, String>());
    String[] argv = {"headers_load", "data/recommendation/csv/header_types.csv"};
    cmd.executeCmds("headers_load", argv, 2);
    assertEquals(cmd.getType("id"), "qualitative");
    assertEquals(cmd.getType("name"), "qualitative");
    assertEquals(cmd.getType("email"), "qualitative");
    assertEquals(cmd.getType("gender"), "qualitative");
    assertEquals(cmd.getType("class_year"), "qualitative");
    assertEquals(cmd.getType("ssn"), "qualitative");
    assertEquals(cmd.getType("nationality"), "qualitative");
    assertEquals(cmd.getType("race"), "qualitative");
    assertEquals(cmd.getType("years_experience"), "quantitative");
    assertEquals(cmd.getType("communication_style"), "qualitative");
    assertEquals(cmd.getType("weekly_avail_hours"), "quantitative");
    assertEquals(cmd.getType("meeting_style"), "qualitative");
    assertEquals(cmd.getType("meeting_time"), "qualitative");
    assertEquals(cmd.getType("software_engn_confidence"), "quantitative");
    assertEquals(cmd.getType("strengths"), "qualitative");
    assertEquals(cmd.getType("weaknesses"), "qualitative");
    assertEquals(cmd.getType("skills"), "qualitative");
    assertEquals(cmd.getType("interests"), "qualitative");
  }
}
