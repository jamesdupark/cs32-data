package edu.brown.cs.student.main.Recommender;

import edu.brown.cs.student.main.CSVParse.Maker.DirectStudentBloomListMaker;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DirectStudentBloomMakerTest {
  private DirectStudentBloomListMaker bloomMaker;

  @Before
  public void init() {
    bloomMaker = new DirectStudentBloomListMaker();
  }

  @Test
  public void testBuild() {
    HashMap<String,String> fields = new HashMap<>();
    fields.put("id", "1");
    fields.put("class_year", "junior");
    fields.put("race", "asian");
    fields.put("communication_style", "in person");
    fields.put("meeting_style", "in person");
    fields.put("strengths", "\"quick learner, friendly\"");
    fields.put("skills", "\"cutthroat, unfriendly\"");
    fields.put("interests", "\"mathematics, politics\"");

    List<String> output = List.of("1", "junior", "asian", "in person", "in person", "quick learner",
        "friendly", "cutthroat", "unfriendly", "mathematics", "politics");
    List<String> hashOutput = bloomMaker.build(fields);
    for (int i = 0; i < output.size(); i++) {
      assertEquals(output.get(i), hashOutput.get(i));
    }
  }

}
