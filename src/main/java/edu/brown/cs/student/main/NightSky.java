package edu.brown.cs.student.main;

import edu.brown.cs.student.main.CSVData.CSVBuilder;
import edu.brown.cs.student.main.CSVData.CSVDatum;
import edu.brown.cs.student.main.CSVData.CSVReader;
import edu.brown.cs.student.main.CSVData.Star;
import edu.brown.cs.student.main.CSVData.StarBuilder;

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
   * Parses a single line of the csv and adds the associated data into the
   * star class.
   * @param filePath - the file path the CSV file to load data from.
   */
  public boolean parseCSV(String filePath) {
    ArrayList<CSVBuilder<CSVDatum>> builderList = new ArrayList<CSVBuilder<CSVDatum>>();
    CSVBuilder<CSVDatum> builder = new StarBuilder();
    builderList.add(builder);
    CSVReader<Star> reader = new CSVReader<Star>(builderList);
    reader.load(filePath);
    List<Star> starList = reader.getDataList();
    for (CSVDatum star : reader.getDataList()) {
      Star st = (Star) star;
      idMap.put(st.getId(), st);
      nameMap.put(st.getName(), st);
    }
    System.out.println("Read " + reader.getDataList().size() + " stars from " + filePath);
    return true;
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
      double distance = queryCoord.distance(star.getCord());
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
