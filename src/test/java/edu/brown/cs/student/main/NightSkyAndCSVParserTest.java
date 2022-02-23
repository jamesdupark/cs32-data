package edu.brown.cs.student.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import edu.brown.cs.student.main.Onboarding.Coordinate;
import edu.brown.cs.student.main.Onboarding.NightSky;
import edu.brown.cs.student.main.Onboarding.Star;
import org.junit.Before;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;

public class NightSkyAndCSVParserTest {
  NightSky tenStar, emptySky, knnSky;
  boolean readState;
  Coordinate origin;
  Star sol;

  @Before
  public void setup() {
    tenStar = new NightSky();
    readState = tenStar.parseCSV("data/stars/ten-star.csv");
    emptySky = new NightSky();
    knnSky = new NightSky();
    knnSky.parseCSV("data/test/knn-test.csv");
    origin = new Coordinate(0, 0, 0);
    sol = new Star(0, "Sol", 0, 0, 0);
  }

  @Test
  public void testReadCsvNormal() {
    boolean headerOnly = emptySky.parseCSV("data/test/header-only.csv");
    boolean badHeader = emptySky.parseCSV("data/test/bad-header.csv");
    boolean fewEntries = emptySky.parseCSV("data/test/4-entries.csv");
    boolean manyEntries = emptySky.parseCSV("data/test/6-entries.csv");
    boolean noID = emptySky.parseCSV("data/test/null-id.csv");
    boolean noX = emptySky.parseCSV("data/test/null-x.csv");
    boolean noY = emptySky.parseCSV("data/test/null-y.csv");
    boolean noZ = emptySky.parseCSV("data/test/null-z.csv");
    boolean nonNumX = emptySky.parseCSV("data/test/non-numerical-x.csv");
    boolean nonNumY = emptySky.parseCSV("data/test/non-numerical-y.csv");
    boolean nonNumZ = emptySky.parseCSV("data/test/non-numerical-z.csv");

    assertTrue(readState);
    assertTrue(headerOnly);
    assertFalse(badHeader);
    assertFalse(fewEntries);
    assertFalse(manyEntries);
    assertFalse(noID);
    assertFalse(noX);
    assertFalse(noY);
    assertFalse(noZ);
    assertFalse(nonNumX);
    assertFalse(nonNumY);
    assertFalse(nonNumZ);
  }

  @Test
  public void testNameToStar() {
    Star solQueried = tenStar.nameToStar("Sol");
    Star nonexistent = tenStar.nameToStar("hello");

    assertEquals(sol, solQueried);
    assertNull(nonexistent);
  }

  @Test
  public void testIdToStar() {
    Star solQueried = tenStar.idToStar(0);
    Star nonexistent = tenStar.idToStar(10);

    assertEquals(sol, solQueried);
    assertNull(nonexistent);
  }

  @Test
  public void testKnn() {
    // testing normal functionality
    List<Integer> tenSolQuery = tenStar.knn(origin, 1, null);
    List<Integer> solMaskedQuery = tenStar.knn(origin, 1, sol);
    List<Integer> zeroQuery = tenStar.knn(origin, 0, null);
    List<Integer> zeroMaskedQuery = tenStar.knn(origin, 0, sol);
    List<Integer> fiveQuery = tenStar.knn(origin, 5, sol);
    List<Integer> fiveExpected = List.of(70667, 71454, 71457, 87666, 118721);

    assertEquals(List.of(0), tenSolQuery);
    assertEquals(List.of(70667), solMaskedQuery);
    assertFalse(solMaskedQuery.contains(0));
    assertEquals(new ArrayList<Integer>(), zeroQuery);
    assertEquals(new ArrayList<Integer>(), zeroMaskedQuery);
    assertEquals(fiveExpected, fiveQuery);
  }

  @Test
  public void testKnnEdge() {
    // k > n
    List<Integer> knnOne = knnSky.knn(origin, 20, null);
    List<Integer> binOne = knnOne.subList(0, 2);
    List<Integer> binTwo = knnOne.subList(2, 6);
    List<Integer> binThree = knnOne.subList(6, 9);

    // bin distances are equal
    double distOne = this.getBinDistance(binOne, origin);
    double distTwo = this.getBinDistance(binTwo, origin);
    double distThree = this.getBinDistance(binThree, origin);

    // bin distances are ordered
    assertTrue(distOne < distTwo && distTwo < distThree);

    // correct ids in each bin
    assertTrue(binOne.containsAll(List.of(0, 1)));
    assertTrue(binTwo.containsAll(List.of(2, 3, 4, 5)));
    assertTrue(binThree.containsAll(List.of(6, 7, 8)));


    // ties broken randomly when ending in the middle
    List<Integer> knnTwo = knnSky.knn(origin, 4, sol);

    // first entry is sol1 (distance 0)
    assertEquals(knnTwo.get(0), 1, Double.MIN_VALUE);

    // bin of three has any three of but not all of 2, 3, 4, 5
    List<Integer> distFiveBin = knnTwo.subList(1, 4);
    List<Boolean> binContains = new ArrayList<>();
    binContains.add(distFiveBin.contains(2));
    binContains.add(distFiveBin.contains(3));
    binContains.add(distFiveBin.contains(4));
    binContains.add(distFiveBin.contains(5));
    binContains.removeIf((Boolean elt) -> !elt); // remove if false
    assertEquals(3, binContains.size());

    double distFive = this.getBinDistance(distFiveBin, origin);

    assertEquals(5, distFive, Double.MIN_VALUE);
  }

  /**
   * Iterates through a "bin" and checks whether the distances to a given coord
   * are all the same within a given bin. If they are, returns the bin's
   * distance
   *
   * @param idBin bin of star IDs to verify
   * @param coord Coordinate to check distance of stars against
   * @return distance of the bin to coord
   */
  private double getBinDistance(List<Integer> idBin, Coordinate coord) {
    int firstID = idBin.get(0);
    Star firstStar = knnSky.idToStar(firstID);
    Coordinate firstCoord = firstStar.getCoord();
    double distance = firstCoord.distance(coord);

    for (int id: idBin) {
      Star star = knnSky.idToStar(id);
      Coordinate starCoord = star.getCoord();
      assertEquals(distance, starCoord.distance(coord), Double.MIN_VALUE);
    }

    return distance;
  }
}
