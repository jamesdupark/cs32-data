package edu.brown.cs.student.main;

import edu.brown.cs.student.main.CSVParse.Builder.StarNodeBuilder;
import edu.brown.cs.student.main.KDimTree.KDNodes.StarNode;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StarNodeBuilderTest {

  @Test
  public void testBuilder() {
    StarNodeBuilder builder = new StarNodeBuilder();

    List<String> lonelyData = Arrays.asList("0", "", "0", "0", "0");
    StarNode expected = new StarNode(0, 0, 0, 0);
    List<String> nonZero = Arrays.asList("0", "Hi", "1", "0.3", "0");
    StarNode nonZ = new StarNode(0,  1, 0.3, 0);
    StarNode lonely = builder.build(lonelyData);
    StarNode nonZeroNode = builder.build(nonZero);
    assertEquals(expected, lonely);
    assertEquals(nonZ, nonZeroNode);
  }

  @Test
  public void testIncorrectBuilder() {
    StarNodeBuilder builder = new StarNodeBuilder();

    List<String> notNum = Arrays.asList("0", "", "A", "0", "0");
    List<String> notNumMult = Arrays.asList("A", "", "A", "B", "C");
    List<String> emptyData = Arrays.asList();
    List<String> tooFew = Arrays.asList( "0", "0", "0");
    List<String> tooManyData = Arrays.asList("0", "", "0", "0", "0", "0");
    StarNode notNumber = builder.build(notNum);
    StarNode notNumMultiple = builder.build(notNumMult);
    StarNode tooFewArg = builder.build(tooFew);
    StarNode empty = builder.build(emptyData);
    StarNode tooMany = builder.build(tooManyData);

    assertNull(notNumber);
    assertNull(notNumMultiple);
    assertNull(tooFewArg);
    assertNull(empty);
    assertNull(tooMany);
  }
}
