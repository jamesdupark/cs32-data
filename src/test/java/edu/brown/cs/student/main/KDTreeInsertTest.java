package edu.brown.cs.student.main;

import edu.brown.cs.student.main.Builder.StudentNodeBuilder;
import edu.brown.cs.student.main.KDNodes.KDNode;
import edu.brown.cs.student.main.Onboarding.Coordinate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the KDTree insert functionality.
 */
public class KDTreeInsertTest {
  /** an empty KDTree. */
  KDTree<KDNode> emptyTree;
  /** a KDTree for Students that is loaded from data/project1/proj1_small.csv */
  KDTree<KDNode> smallStudent;
  /** a KDTree for Coordinates. */
  KDTree<KDNode> stdCoordinate;

  /**
   * Method to initialize and populate the three KDTrees fields.
   * @throws IOException if the CSVReader is unable to read the file path
   */
  @Before
  public void setup() throws IOException {
    // initialize empty tree
    emptyTree = new KDTree<>();

    // initialize small student tree
    smallStudent = new KDTree<>();
    CSVParser parser = new CSVParser(new StudentNodeBuilder());
    parser.load("data/project1/proj1_small.csv");
    List<KDNode> studentsList = parser.getDataList();
    smallStudent.insertList(studentsList, 0);
    smallStudent.printTree(smallStudent.getRoot(), "");

    // initialize standard coordinate tree
    stdCoordinate = new KDTree<>();
    List<KDNode> cordList = new ArrayList<>();
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
    stdCoordinate.insertList(cordList, 0);
    stdCoordinate.printTree(stdCoordinate.getRoot(), "");
  }

  /**
   * Method to test the KDTree Constructor and fields.
   * @throws IOException if the CSVReader cannot read the filepath for smallStudent Tree
   */
  @Test
  public void testConstructor() throws IOException {
    setup();
    // test number of nodes
    assertEquals(emptyTree.getNumNodes(), 0);
    assertEquals(smallStudent.getNumNodes(), 20);
    assertEquals(stdCoordinate.getNumNodes(), 6);

    // test if root is inserted correctly
    assertEquals(emptyTree.getRoot(), null);
    assertEquals(smallStudent.getRoot().getVal().getAxisVal(0), 10, 0);
    assertEquals(smallStudent.getRoot().getVal().getAxisVal(1), 16, 0);
    assertEquals(smallStudent.getRoot().getVal().getAxisVal(2), 32, 0);
    assertEquals(stdCoordinate.getRoot().getVal().getAxisVal(0), 2, 0);
    assertEquals(stdCoordinate.getRoot().getVal().getAxisVal(1), 4, 0);
    assertEquals(stdCoordinate.getRoot().getVal().getAxisVal(2), 1, 0);

    // test num dimensions
    assertEquals(smallStudent.getRoot().getVal().getNumDimensions(), 3);
    assertEquals(stdCoordinate.getRoot().getVal().getNumDimensions(), 3);

    // test parent
    // null case
    assertEquals(stdCoordinate.getRoot().getParent(), null);
    // not null case
    assertEquals(smallStudent.getRoot().getLeft().getParent().getVal(),
        smallStudent.getRoot().getVal());
    assertEquals(stdCoordinate.getRoot().getRight().getLeft().getParent().getVal(),
        stdCoordinate.getRoot().getRight().getRight().getParent().getVal());

    // test depth
    assertEquals(stdCoordinate.getRoot().getDepth(), 1);
    assertEquals(stdCoordinate.getRoot().getLeft().getDepth(), 2);
    assertEquals(smallStudent.getRoot().getRight().getLeft().getDepth(), 3);
  }

  /**
   * Test insert() method for KDTree to see if one element can be added.
   */
  @Test
  public void testInsert() {
    KDTree<KDNode> testTree = new KDTree<>();
    // insert into root
    Coordinate c = new Coordinate(0, 2, 4);
    assertEquals(testTree.getNumNodes(), 0);
    testTree.insert(testTree.getRoot(), c);
    assertEquals(testTree.getRoot().getVal().getAxisVal(0), 0, 0);
    assertEquals(testTree.getRoot().getVal().getAxisVal(1), 2, 0);
    assertEquals(testTree.getRoot().getVal().getAxisVal(2), 4, 0);
    assertEquals(testTree.getNumNodes(), 1);

    // insert into left subtree from root
    Coordinate c2 = new Coordinate(-5, 3, 1);
    testTree.insert(testTree.getRoot(), c2);
    assertEquals(testTree.getRoot().getLeft().getVal().getAxisVal(0), -5, 0);
    assertEquals(testTree.getRoot().getLeft().getVal().getAxisVal(1), 3, 0);
    assertEquals(testTree.getRoot().getLeft().getVal().getAxisVal(2), 1, 0);
    assertEquals(testTree.getNumNodes(), 2);

    // insert into right subtree from root
    Coordinate c3 = new Coordinate(8, 4, 0);
    testTree.insert(testTree.getRoot(), c3);
    assertEquals(testTree.getRoot().getRight().getVal().getAxisVal(0), 8, 0);
    assertEquals(testTree.getRoot().getRight().getVal().getAxisVal(1), 4, 0);
    assertEquals(testTree.getRoot().getRight().getVal().getAxisVal(2), 0, 0);
    assertEquals(testTree.getNumNodes(), 3);

    // insert into left subtree from left subtree from root
    Coordinate c4 = new Coordinate(-1, 0, 20);
    testTree.insert(testTree.getRoot(), c4);
    assertEquals(testTree.getRoot().getLeft().getLeft().getVal().getAxisVal(0), -1, 0);
    assertEquals(testTree.getRoot().getLeft().getLeft().getVal().getAxisVal(1), 0, 0);
    assertEquals(testTree.getRoot().getLeft().getLeft().getVal().getAxisVal(2), 20, 0);
    assertEquals(testTree.getNumNodes(), 4);

    // insert into right subtree from left subtree from root
    Coordinate c5 = new Coordinate(-1, 3, 0);
    testTree.insert(testTree.getRoot(), c5);
    assertEquals(testTree.getRoot().getLeft().getRight().getVal().getAxisVal(0), -1, 0);
    assertEquals(testTree.getRoot().getLeft().getRight().getVal().getAxisVal(1), 3, 0);
    assertEquals(testTree.getRoot().getLeft().getRight().getVal().getAxisVal(2), 0, 0);
    assertEquals(testTree.getNumNodes(), 5);

    // insert into left subtree from right subtree from root
    Coordinate c6 = new Coordinate(9.2, 3.5, 0.2);
    testTree.insert(testTree.getRoot(), c6);
    assertEquals(testTree.getRoot().getRight().getLeft().getVal().getAxisVal(0), 9.2, 0);
    assertEquals(testTree.getRoot().getRight().getLeft().getVal().getAxisVal(1), 3.5, 0);
    assertEquals(testTree.getRoot().getRight().getLeft().getVal().getAxisVal(2), 0.2, 0);
    assertEquals(testTree.getNumNodes(), 6);

    // insert into right subtree from right subtree from root
    Coordinate c7 = new Coordinate(1, 4, -2.023);
    testTree.insert(testTree.getRoot(), c7);
    assertEquals(testTree.getRoot().getRight().getRight().getVal().getAxisVal(0), 1, 0);
    assertEquals(testTree.getRoot().getRight().getRight().getVal().getAxisVal(1), 4, 0);
    assertEquals(testTree.getRoot().getRight().getRight().getVal().getAxisVal(2), -2.023, 0);
    assertEquals(testTree.getNumNodes(), 7);
  }

  /**
   * Test getting an axis value that does not exist - should throw Runtime Exception
   */
  @Test(expected = RuntimeException.class)
  public void testGetAxisValThatDoesNotExist() {
    smallStudent.getRoot().getVal().getAxisVal(-1);
    smallStudent.getRoot().getVal().getAxisVal(4);
  }
}
