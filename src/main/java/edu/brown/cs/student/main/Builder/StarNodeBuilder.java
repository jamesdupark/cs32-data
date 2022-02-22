package edu.brown.cs.student.main.Builder;

import edu.brown.cs.student.main.KDNodes.KDNode;
import edu.brown.cs.student.main.KDNodes.StarNode;

import java.util.List;

public class StarNodeBuilder implements CSVBuilder<KDNode> {
  @Override
  public KDNode build(List<String> fields) {
    StarNode newStar;
    int id = Integer.parseInt(fields.get(0));
    double x = Double.parseDouble(fields.get(2));
    double y = Double.parseDouble(fields.get(3));
    double z = Double.parseDouble(fields.get(4));
    newStar = new StarNode(id, x, y, z);
    return newStar;
  }

  @Override
  public String getColumnTitles() {
    return "StarID,ProperName,X,Y,Z";
  }
}
