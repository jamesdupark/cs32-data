package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import edu.brown.cs.student.main.CSVParse.Builder.StarBuilder;
import edu.brown.cs.student.main.Onboarding.Star;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;

public class StarBuilderTest {

  @Test
  public void testBuilder() {
    StarBuilder builder = new StarBuilder();

    List<String> lonelyData = Arrays.asList("0", "", "0.1", "0", "0");
    Star expected = new Star(0, 0.1, 0, 0);
    List<String> withName = Arrays.asList("0", "Lonely Star", "0", "0", "0");
    Star nameExpected = new Star(0, "Lonely Star", 0, 0, 0);
    Star lonely = builder.build(lonelyData);
    Star lonelyWithName = builder.build(withName);
    assertEquals(expected, lonely);
    assertEquals(nameExpected, lonelyWithName);
  }

  @Test
  public void testIncorrectBuilder() {
    StarBuilder builder = new StarBuilder();

    List<String> notNum = Arrays.asList("0", "", "A", "0", "0");
    List<String> notNumMult = Arrays.asList("A", "", "A", "B", "C");
    List<String> emptyData = Arrays.asList();
    List<String> withName = Arrays.asList("A", "Lonely Star", "0", "0", "0");
    List<String> tooManyData = Arrays.asList("A", "Lonely Star", "0", "0", "0", "0");

    Star notNumber = builder.build(notNum);
    Star notNumMultiple = builder.build(notNumMult);
    Star lonelyWithName = builder.build(withName);
    Star empty = builder.build(emptyData);
    Star tooMany = builder.build(tooManyData);

    assertNull(notNumber);
    assertNull(notNumMultiple);
    assertNull(lonelyWithName);
    assertNull(empty);
    assertNull(tooMany);
  }
}
