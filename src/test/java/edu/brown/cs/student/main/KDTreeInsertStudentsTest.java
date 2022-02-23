package edu.brown.cs.student.main;

import edu.brown.cs.student.main.CSVParse.CSVParser;
import edu.brown.cs.student.main.CSVParse.Builder.StudentNodeBuilder;
import edu.brown.cs.student.main.KDimTree.KDNodes.KDNode;
import edu.brown.cs.student.main.KDimTree.KDNodes.StudentNode;
import edu.brown.cs.student.main.KDimTree.KDTree;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the KDTree insert functionality for Students.
 */
public class KDTreeInsertStudentsTest {
  /** an empty KDTree. */
  KDTree<KDNode> emptyTree;
  /** a KDTree for Students that is loaded from data/project1/proj1_small.csv */
  KDTree<KDNode> smallStudentTree;
  /** a KDTree for Students where all Students contain the same value. */
  KDTree<KDNode> sameStudentTree;
  /** a KDTree for Students where all Students contain the same axis value for a
   * particular axis. */
  KDTree<KDNode> sameAxisStudentTree;

  /**
   * Method to initialize and populate the three KDTrees fields.
   * @throws IOException if the CSVReader is unable to read the file path
   */
  @Before
  public void setup() throws IOException {
    // initialize empty tree
    emptyTree = new KDTree<>();

    // initialize small student tree
    smallStudentTree = new KDTree<>();
    CSVParser parser = new CSVParser(new StudentNodeBuilder());
    parser.load("data/project1/proj1_small.csv");
    List<KDNode> studentsList = parser.getDataList();
    smallStudentTree.insertList(studentsList, 0);

    // initialize sameStudent tree
    sameStudentTree = new KDTree<>();
    StudentNode stud = new StudentNode(1, 1, 2, 3);
    studentsList = new ArrayList<>();
    for (int i = 0; i < 15; i ++) {
      studentsList.add(stud);
    }
    sameStudentTree.insertList(studentsList, 0);

    // initialize sameAxisStudent tree
    sameAxisStudentTree = new KDTree<>();
    StudentNode s1 = new StudentNode(0, -0.5, 3, 1);
    StudentNode s2 = new StudentNode(1, -0.5, 5, 1);
    StudentNode s3 = new StudentNode(1, -0.5, 2, 1);
    StudentNode s4 = new StudentNode(1, -0.5, 4, 1);
    studentsList = new ArrayList<>();
    studentsList.add(s1);
    studentsList.add(s2);
    studentsList.add(s3);
    studentsList.add(s4);
    sameAxisStudentTree.insertList(studentsList, 0);
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
    assertEquals(smallStudentTree.getNumNodes(), 20);
    assertEquals(sameStudentTree.getNumNodes(), 15);
    assertEquals(sameAxisStudentTree.getNumNodes(), 4);

    // test if root is inserted correctly
    assertEquals(emptyTree.getRoot(), null);
    assertEquals(smallStudentTree.getRoot().getVal().getAxisVal(0), 10, 0);
    assertEquals(smallStudentTree.getRoot().getVal().getAxisVal(1), 16, 0);
    assertEquals(smallStudentTree.getRoot().getVal().getAxisVal(2), 32, 0);
    assertEquals(sameStudentTree.getRoot().getVal().getAxisVal(0), 1, 0);
    assertEquals(sameStudentTree.getRoot().getVal().getAxisVal(1), 2, 0);
    assertEquals(sameStudentTree.getRoot().getVal().getAxisVal(2), 3, 0);
    assertEquals(sameAxisStudentTree.getRoot().getVal().getAxisVal(0), -0.5, 0);
    assertEquals(sameAxisStudentTree.getRoot().getVal().getAxisVal(1), 3, 0);
    assertEquals(sameAxisStudentTree.getRoot().getVal().getAxisVal(2), 1, 0);

    // test num dimensions
    assertEquals(smallStudentTree.getRoot().getVal().getNumDimensions(), 3);
    assertEquals(sameStudentTree.getRoot().getVal().getNumDimensions(), 3);
    assertEquals(sameAxisStudentTree.getRoot().getVal().getNumDimensions(), 3);

    // test parent
    // null case
    assertEquals(smallStudentTree.getRoot().getParent(), null);
    assertEquals(sameStudentTree.getRoot().getParent(), null);
    assertEquals(sameAxisStudentTree.getRoot().getParent(), null);
    // not null case
    assertEquals(smallStudentTree.getRoot().getLeft().getParent().getVal(),
        smallStudentTree.getRoot().getVal());
    // check left and right children have same parent
    assertEquals(smallStudentTree.getRoot().getLeft().getParent().getVal(),
        smallStudentTree.getRoot().getRight().getParent().getVal());

    // test depth
    assertEquals(smallStudentTree.getRoot().getRight().getLeft().getDepth(), 3);
    assertEquals(sameStudentTree.getRoot().getRight().getRight().getRight().
        getRight().getRight().getRight().getRight().getDepth(), 8);
    assertEquals(sameAxisStudentTree.getRoot().getRight().getRight().getParent().
        getRight().getParent().getDepth(), 2);
  }

  /**
   * Test insert() method for KDTree to see if one element can be added.
   */
  @Test
  public void testInsert() {
    KDTree<KDNode> testTree = new KDTree<>();
    // insert into root
    KDNode s = new StudentNode(0, 0, 0, 0);
    assertEquals(testTree.getNumNodes(), 0);
    testTree.insert(testTree.getRoot(), s);
    assertEquals(testTree.getRoot().getVal().getAxisVal(0), 0, 0);
    assertEquals(testTree.getRoot().getVal().getAxisVal(1), 0, 0);
    assertEquals(testTree.getRoot().getVal().getAxisVal(2), 0, 0);
    assertEquals(testTree.getNumNodes(), 1);

    // insert into left subtree from root
    KDNode s2 = new StudentNode(1, -1.23, 342, 3);
    testTree.insert(testTree.getRoot(), s2);
    assertEquals(testTree.getRoot().getLeft().getVal().getAxisVal(0), -1.23, 0);
    assertEquals(testTree.getRoot().getLeft().getVal().getAxisVal(1), 342, 0);
    assertEquals(testTree.getRoot().getLeft().getVal().getAxisVal(2), 3, 0);
    assertEquals(testTree.getNumNodes(), 2);

    // insert into right subtree from left subtree from root
    KDNode s3 = new StudentNode(2, -1.23, 342, 0);
    testTree.insert(testTree.getRoot(), s3);
    assertEquals(testTree.getRoot().getLeft().getRight().getVal().getAxisVal(0),
        -1.23, 0);
    assertEquals(testTree.getRoot().getLeft().getRight().getVal().getAxisVal(1),
        342, 0);
    assertEquals(testTree.getRoot().getLeft().getRight().getVal().getAxisVal(2),
        0, 0);
    assertEquals(testTree.getNumNodes(), 3);

    // insert into left subtree from left subtree from root
    KDNode s4 = new StudentNode(3, -10, -10, 20);
    testTree.insert(testTree.getRoot(), s4);
    assertEquals(testTree.getRoot().getLeft().getLeft().getVal().getAxisVal(0),
        -10, 0);
    assertEquals(testTree.getRoot().getLeft().getLeft().getVal().getAxisVal(1),
        -10, 0);
    assertEquals(testTree.getRoot().getLeft().getLeft().getVal().getAxisVal(2),
        20, 0);
    assertEquals(testTree.getNumNodes(), 4);

    // insert into right subtree from root
    KDNode s5 = new StudentNode(4, 1, 3, 0.3);
    testTree.insert(testTree.getRoot(), s5);
    assertEquals(testTree.getRoot().getRight().getVal().getAxisVal(0), 1, 0);
    assertEquals(testTree.getRoot().getRight().getVal().getAxisVal(1), 3, 0);
    assertEquals(testTree.getRoot().getRight().getVal().getAxisVal(2), 0.3, 0);
    assertEquals(testTree.getNumNodes(), 5);

    // insert into left subtree from left subtree from left subtree from root
    KDNode s6 = new StudentNode(5, -10, -11, 7.2);
    testTree.insert(testTree.getRoot(), s6);
    assertEquals(testTree.getRoot().getLeft().getLeft().getLeft().getVal().getAxisVal(0),
        -10, 0);
    assertEquals(testTree.getRoot().getLeft().getLeft().getLeft().getVal().getAxisVal(1),
        -11, 0);
    assertEquals(testTree.getRoot().getLeft().getLeft().getLeft().getVal().getAxisVal(2),
        7.2, 0);
    assertEquals(testTree.getNumNodes(), 6);

    // insert into left subtree from left subtree from left subtree from left subtree from root
    KDNode s7 = new StudentNode(6, -99, -99, -2.023);
    testTree.insert(testTree.getRoot(), s7);
    assertEquals(testTree.getRoot().getLeft().getLeft().getLeft().getLeft().getVal().getAxisVal(0),
        -99, 0);
    assertEquals(testTree.getRoot().getLeft().getLeft().getLeft().getLeft().getVal().getAxisVal(1),
        -99, 0);
    assertEquals(testTree.getRoot().getLeft().getLeft().getLeft().getLeft().getVal().getAxisVal(2),
        -2.023, 0);
    assertEquals(testTree.getNumNodes(), 7);
  }

  /**
   * Test getting an axis value that does not exist - should throw Runtime Exception
   */
  @Test(expected = RuntimeException.class)
  public void testGetAxisValThatDoesNotExist() {
    smallStudentTree.getRoot().getVal().getAxisVal(-1);
    smallStudentTree.getRoot().getVal().getAxisVal(4);
  }
}
