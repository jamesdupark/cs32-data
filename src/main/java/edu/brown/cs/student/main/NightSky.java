package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Class representing the "night sky," or a set of stars. Stars are read in
 * using the readCsv method and the id, name, and coordinates are stored in the
 * idMap and nameMap hashmaps.
 */
public class NightSky {
  /** Hashmap containing all stars mapped from id to Star. */
  private final Map<Integer, Star> idMap = new HashMap<>();

  /** Hashmap containing named stars mapped from name to Star. */
  private final Map<String, Star> nameMap = new HashMap<>();

  /**
   * Reads csv from the given file and populates the idMap and nameMap HashMaps.
   *
   * @param filename path to a .csv file containing all of and only StarID,
   *                 ProperName, X, Y, and Z fields. Has a header row
   *                 identifying these fields as well.
   * @return boolean indicating whether csv was read in without any problems
   * @throws IOException upon failure to open the given file
   */
  public boolean readCsv(String filename) throws IOException {
    BufferedReader csvReader;
    csvReader = new BufferedReader(new FileReader(filename));
    int starCount = -1;

    try {
      String line = csvReader.readLine();
      while (line != null) { // EOF not reached
        String[] entries = line.split(",", -1);
        this.addStar(entries, starCount);
        starCount++;
        line = csvReader.readLine();
      }
    } catch (AssertionError ex) {
      System.err.println("ERROR: Poorly formatted line encountered "
          + "while attempting to read csv.");
      return false;
    } catch (NumberFormatException ex) {
      System.err.println("ERROR: non-numerical coordinate value found while"
          + "attempting to read csv.");
      return false;
    }

    // finished reading, print informative message
    System.out.println("Read " + starCount + " stars from " + filename);
    return true;
  }

  /**
   * Parses a single line of the csv and adds the associated data into the
   * star class.
   * @param entries string array of single csv line split on commas
   * @param starCount counter for number of stars read in
   * @throws AssertionError when a poorly-formatted row is encountered
   * @throws NumberFormatException when id or coordinate value is non-numerical
   */
  private void addStar(String[] entries, int starCount)
      throws AssertionError, NumberFormatException {
    assert entries.length == 5;

    String idStr = entries[0];
    String name = entries[1];
    String xStr = entries[2];
    String yStr = entries[3];
    String zStr = entries[4];

    if (starCount == -1) { // header row
      boolean headerFormat = idStr.equals("StarID") && name.equals("ProperName")
          && xStr.equals("X") && yStr.equals("Y") && zStr.equals("Z");

      assert headerFormat; // AssertionError if poorly formatted header
    } else { // read in stars normally
      int id = Integer.parseInt(idStr);
      double x = Double.parseDouble(xStr);
      double y = Double.parseDouble(yStr);
      double z = Double.parseDouble(zStr);

      Star newStar;
      if (!name.equals("")) {
        newStar = new Star(id, name, x, y, z);
        nameMap.put(name, newStar);
      } else {
        newStar = new Star(id, x, y, z);
      }

      idMap.put(id, newStar);
    }
  }

  /**
   * Queries the nameMap with the given star name to get its corresponding
   * Star.
   *
   * @param starName name of the star to search for
   * @return Star object with the given name, if it exists
   */
  public Star nameToStar(String starName) {
    Star namedStar =  nameMap.get(starName);
    if (namedStar == null) { // star has no name
      System.err.println("ERROR: star " + starName + " not found.");
    }

    return namedStar;
  }

  /**
   * Queries the idMap with the given star ID to get its corresponding Star.
   *
   * @param id int id of the star to search for
   * @return Star object with the given id, if it exists
   */
  public Star idToStar(int id) {
    Star queriedStar =  idMap.get(id);
    if (queriedStar == null) { // star has no name
      System.err.println("ERROR: star " + id + " not found.");
    }

    return queriedStar;
  }

  /**
   * K-nearest neighbors algorithm based on euclidean distance between the given
   * coordinate and the stars in the database. Ties are broken randomly.
   * A star may be passed in to the mask parameter to be excluded from search
   * results.
   *
   * @param queryCoord coordinate to calculate distances to
   * @param k number of nearest neighbors to find.
   * @param mask star to mask off from search results
   * @return list of k nearest ids to queryCoord
   */
  public List<Integer> knn(Coordinate queryCoord, int k, Star mask) {
    TreeMap<Double, List<Integer>> distMap = new TreeMap<>();

    // traverse through all stars and calculate distances
    for (Map.Entry<Integer, Star> entry : idMap.entrySet()) {
      int id = entry.getKey();
      Star star = entry.getValue();
      double distance = queryCoord.distance(star.getCoord());
      if (star.equals(mask)) {
        assert distance == 0;
        continue;
      }

      if (distMap.containsKey(distance)) {
        List<Integer> distList = distMap.get(distance);
        distList.add(id);
      } else {
        List<Integer> distList = new ArrayList<>();
        distList.add(id);
        distMap.put(distance, distList);
      }
    }

    // get the first k elements from the TreeMap
    List<Integer> kList = new ArrayList<>();
    while (kList.size() < k && !distMap.isEmpty()) {
      double lowestDist = distMap.firstKey();
      List<Integer> bin = distMap.get(lowestDist);
      Collections.shuffle(bin); // randomize our bin of stars

      if (bin.size() + kList.size() <= k) { // still don't have enough elements
        kList.addAll(bin);
        distMap.remove(lowestDist);
      } else { // this bin will put us over k elements, choose k-n elements
        int numRequired = k - kList.size();
        List<Integer> subBin = bin.subList(0, numRequired);
        kList.addAll(subBin);
      }
    }

    return kList;
  }
}
