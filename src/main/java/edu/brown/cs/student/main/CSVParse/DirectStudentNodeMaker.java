package edu.brown.cs.student.main.CSVParse;

import edu.brown.cs.student.main.KDimTree.KDNodes.KDNode;
import edu.brown.cs.student.main.KDimTree.KDNodes.StudentNode;
import java.util.HashMap;

/**
 * StudentBloomList maker class that makes BloomList from Student class quanMap.
 */
public class DirectStudentNodeMaker {

  /**
   * Builds a KDNode.
   * @param id - The id of the student.
   * @param quanMap - A hashmap from quantitative field name to its field value.
   * @return - List of String to input for Bloom Filter
   */
  public KDNode build(int id, HashMap<String, Double> quanMap) {
    StudentNode newStudent = null;
    try {
      double yearsExp = quanMap.get("years_experience");
      double weeklyAvail = quanMap.get("weekly_avail_hours");
      double sweConfidence = quanMap.get("software_engn_confidence");
      newStudent = new StudentNode(id, yearsExp, weeklyAvail, sweConfidence);
    } catch (NullPointerException ex) {
      System.err.println("ERROR: input int or double field was null");
      return null;
    }
    return newStudent;
  }
}
