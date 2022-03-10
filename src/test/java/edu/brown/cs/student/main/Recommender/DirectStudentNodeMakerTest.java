package edu.brown.cs.student.main.Recommender;

import edu.brown.cs.student.main.CSVParse.Maker.DirectStudentBloomListMaker;
import edu.brown.cs.student.main.CSVParse.Maker.DirectStudentNodeMaker;
import edu.brown.cs.student.main.KDimTree.KDNodes.KDNode;
import edu.brown.cs.student.main.KDimTree.KDNodes.StudentNode;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class DirectStudentNodeMakerTest {
  private DirectStudentNodeMaker nodeMaker;

  @Before
  public void init() {
    nodeMaker = new DirectStudentNodeMaker();
  }

  @Test
  public void testBuild() {
    HashMap<String,Double> fields = new HashMap<>();
    fields.put("years_experience", 1.0);
    fields.put("weekly_avail_hours", 2.0);
    fields.put("software_engn_confidence", 3.0);

    KDNode output = nodeMaker.build(1, fields);
    assertEquals(output.getAxisVal(0), 1.0, 0);
    assertEquals(output.getAxisVal(1), 2.0, 0);
    assertEquals(output.getAxisVal(2), 3.0, 0);
  }
}
