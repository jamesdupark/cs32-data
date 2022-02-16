package edu.brown.cs.student.main;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

public class KDTreeTest {
  KDTree<KDNode> emptyTree;
  KDTree<KDNode> smallStudent;
  KDTree<KDNode> stdCoordinate;

  @Before
  public void setup() throws IOException {
    // initialize empty tree
    emptyTree = new KDTree<>();

    // initialize small student tree
    smallStudent = new KDTree<>();
    CSVParser parser = new CSVParser();
    parser.parse("data/project1/proj1_small.csv");
    ArrayList<KDNode> studentsList = parser.getData();
    smallStudent.insertList(studentsList);

    // initialize standard coordinate tree
    stdCoordinate = new KDTree<>();
    ArrayList<Coordinate> cordList = new ArrayList<>();
    Coordinate c1 = new Coordinate(3, 2, 4);
    Coordinate c2 = new Coordinate(5, 10, 0);
    Coordinate c3 = new Coordinate(1, 5, -3);
    Coordinate c4 = new Coordinate(0, 3, 2);
    Coordinate c5 = new Coordinate(2, 4, 1);
    Coordinate c6 = new Coordinate(3, 1, 3);
    cordList.add(c1);
    cordList.add(c2);
    cordList.add(c3);
    cordList.add(c4);
    cordList.add(c5);
    cordList.add(c6);
    stdCoordinate.insertList();

  }

  @Test
  public void testConstructor() {
    assertEquals(0, emptyTree.getNumNodes());
    assertEquals(20, smallStudent.getNumNodes());
//    assertEquals("0".repeat(29), stdBloom.toString());

    // fprate out of bounds
//    assertThrows(IllegalArgumentException.class, () -> new BloomFilter(0, 5));
//    assertThrows(IllegalArgumentException.class, () -> new BloomFilter(-1, 5));
//    assertThrows(IllegalArgumentException.class, () -> new BloomFilter(3, 5));
//    assertThrows(IllegalArgumentException.class, () -> new BloomFilter(1, 5));
//
//    // maxElts out of bounds
//    assertThrows(IllegalArgumentException.class,
//        () -> new BloomFilter(0.001, 2147483647));
//    assertThrows(IllegalArgumentException.class, () -> new BloomFilter(0.1, -3));
//    assertThrows(IllegalArgumentException.class, () -> new BloomFilter(0.1, 0));
  }
}
