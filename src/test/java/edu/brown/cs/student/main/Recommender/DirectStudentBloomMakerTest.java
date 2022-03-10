package edu.brown.cs.student.main.Recommender;

import edu.brown.cs.student.main.API.APIRequests.APIRequestBuilder;
import edu.brown.cs.student.main.CSVParse.DirectStudentBloomListMaker;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class DirectStudentBloomMakerTest {
  private DirectStudentBloomListMaker bloomMaker;

  @Before
  public void init() {
    bloomMaker = new DirectStudentBloomListMaker();
  }

  @Test
  public void testBuild() {
    Map<String,String> fields = new HashMap<>();
    fields.put("id", "1");
    fields.put("class_year", "junior");
    fields.put("race", "junior");
    fields.put("communication_style", "junior");
    fields.put("meeting_style", "junior");
    fields.put("strengths", "junior");
    fields.put("skills", "junior");
    fields.put("interests", "junior");

  }
}
