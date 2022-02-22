package edu.brown.cs.student.main;

import edu.brown.cs.student.main.CSVData.CSVBuilder;

import java.util.List;

public class StarBuilder implements CSVBuilder<Star> {
  @Override
  public Star build(List<String> fields) {
    Star newStar;
    if (fields.size() == 5) {
      newStar = new Star(Integer.parseInt(fields.get(0)), fields.get(1),
          Double.parseDouble(fields.get(2)), Double.parseDouble(fields.get(3)),
          Double.parseDouble(fields.get(4)));
    } else {
      newStar = new Star(Integer.parseInt(fields.get(0)), "",
          Double.parseDouble(fields.get(1)), Double.parseDouble(fields.get(2)),
          Double.parseDouble(fields.get(3)));
    }
    return newStar;
  }

  @Override
  public String getColumnTitles() {
    return "StarID,ProperName,X,Y,Z";
  }
}
