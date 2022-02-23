package edu.brown.cs.student.main.Builder;

import edu.brown.cs.student.main.KDNodes.StarNode;

import java.util.List;

/**
 * Class to build a StarNode of type KDNode that will eventually be inserted
 * into the KDTree.
 */
public class StarNodeBuilder implements CSVBuilder<StarNode> {
  @Override
  public StarNode build(List<String> fields) {
    try {
      if (fields.size() != 5) {
        System.err.println("ERROR: too few or many arguments in CSV line");
        return null;
      }
      StarNode newStar;
      int id = Integer.parseInt(fields.get(0));
      double x = Double.parseDouble(fields.get(2));
      double y = Double.parseDouble(fields.get(3));
      double z = Double.parseDouble(fields.get(4));
      newStar = new StarNode(id, x, y, z);
      return newStar;
    } catch (NumberFormatException ex) {
      System.err.println("ERROR: id, x, y, and z must be numerical.");
      return null;
    }
  }
  @Override
  public String getColumnTitles() {
    return "StarID,ProperName,X,Y,Z";
  }
}
