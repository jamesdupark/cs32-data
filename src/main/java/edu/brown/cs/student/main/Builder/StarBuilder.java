package edu.brown.cs.student.main.Builder;

import edu.brown.cs.student.main.Onboarding.Star;

import java.util.List;

/**
 * Builder that makes class Star that will be used in NightSky.
 */
public class StarBuilder implements CSVBuilder<Star> {
  @Override
  public Star build(List<String> fields) {
    try {
      if (fields.size() != 5) {
        System.err.println("ERROR: too few or many inputs in CSV line");
        return null;
      }
      if (!fields.get(1).equals("")) { // null name
        int id = Integer.parseInt(fields.get(0));
        double x = Double.parseDouble((fields.get(2)));
        double y = Double.parseDouble((fields.get(3)));
        double z = Double.parseDouble((fields.get(4)));
        return new Star(id, fields.get(1), x, y, z);
      } else {
        int id = Integer.parseInt(fields.get(0));
        double x = Double.parseDouble((fields.get(2)));
        double y = Double.parseDouble((fields.get(3)));
        double z = Double.parseDouble((fields.get(4)));
        return new Star(id, x, y, z);
      }
    } catch (NumberFormatException ex) {
      System.err.println("ERROR: int or double field from CSV file "
          + "unable to be converted from String");
      return null;
    }
  }
  @Override
  public String getColumnTitles() {
    return "StarID,ProperName,X,Y,Z";
  }
}
