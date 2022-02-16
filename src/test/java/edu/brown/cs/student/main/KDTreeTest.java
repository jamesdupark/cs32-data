package edu.brown.cs.student.main;

import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;

import static org.junit.Assert.assertEquals;

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
    ArrayList<KDNode> cordList = new ArrayList<>();
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
    stdCoordinate.insertList(cordList);
  }

  @Test
  public void testConstructor() throws IOException {
    setup();
    // test number of nodes
    assertEquals(emptyTree.getNumNodes(), 1);
    assertEquals(smallStudent.getNumNodes(), 20);
    assertEquals(stdCoordinate.getNumNodes(), 6);

    // test if root is inserted correctly
    assertEquals(emptyTree.getRoot(), null);
    assertEquals(smallStudent.getRoot().getVal().getAxisVal(0), 18, 0);
    assertEquals(smallStudent.getRoot().getVal().getAxisVal(1), 2, 0);
    assertEquals(smallStudent.getRoot().getVal().getAxisVal(2), 2, 0);
    assertEquals(stdCoordinate.getRoot().getVal().getAxisVal(0), 3, 0);
    assertEquals(stdCoordinate.getRoot().getVal().getAxisVal(1), 2, 0);
    assertEquals(stdCoordinate.getRoot().getVal().getAxisVal(2), 4, 0);

    // test num dimensions
    assertEquals(smallStudent.getVal().getNumDimensions(), 3);
    assertEquals(stdCoordinate.getVal().getNumDimensions(), 3);
  }
}
