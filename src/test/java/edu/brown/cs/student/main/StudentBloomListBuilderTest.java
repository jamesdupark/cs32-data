package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;

import edu.brown.cs.student.main.CSVParse.Builder.StudentBloomListBuilder;
import org.junit.Test;

import java.util.List;

public class StudentBloomListBuilderTest {
  @Test
  public void testParse() {
    List<String> parseLine = List.of("0", "a", "b", "c", "jr", "d", "e", "as",
        "f", "em", "g", "zm", "h", "i", "\"j, k, l\"", "\"m, n, o\"", "\"p, q\"",
        "\"r, s, t, u\"");

    List<String> parsed = List.of("0", "jr", "as", "em", "zm", "j", "k", "l",
        "p", "q", "r", "s", "t", "u");

    StudentBloomListBuilder builder = new StudentBloomListBuilder();
    assertEquals(parsed, builder.build(parseLine));
  }

}
