package edu.brown.cs.student.main;

import edu.brown.cs.student.main.Builder.StudentNodeBuilder;
import edu.brown.cs.student.main.Distances.EuclideanDistance;
import edu.brown.cs.student.main.KDNodes.KDNode;
import edu.brown.cs.student.main.KDNodes.StudentNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import static org.junit.Assert.assertEquals;

/**
 * Class to test the KDTree k most similar neighbors functionality for Students.
 */
public class KDTreeFindNeighborsTest {
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
   * Method to test inserting an ID into the distToUserID HashMap.
   */
  @Test
  public void testInsertingIDIntoMap() {
    assertEquals(smallStudentTree.getDistToUserID().size(), 0);

    // insert values not associated with a key in the map
    smallStudentTree.insertIDIntoMap(3, 0);
    smallStudentTree.insertIDIntoMap(2, 1);
    List<Integer> lst1 = new ArrayList<>();
    lst1.add(0);
    List<Integer> lst2 = new ArrayList<>();
    lst2.add(1);
    assertEquals(smallStudentTree.getDistToUserID().get(3.0), lst1);
    assertEquals(smallStudentTree.getDistToUserID().get(2.0), lst2);
    assertEquals(smallStudentTree.getDistToUserID().size(), 2);

    // insert a value that is associated with a key in the map
    List<Integer> lst3 = new ArrayList<>();
    lst3.add(4);
    lst3.add(9);
    smallStudentTree.insertIDIntoMap(6.2, 4);
    smallStudentTree.insertIDIntoMap(6.2, 9);
    assertEquals(smallStudentTree.getDistToUserID().get(6.2), lst3);
    assertEquals(smallStudentTree.getDistToUserID().size(), 3);
  }

  /**
   * Test cleanDataStructures method to see if distToUserID and distanceQueue
   * are cleared.
   */
  @Test
  public void testCleanDataStructures() {
    // check that data structure is cleared
    assertEquals(smallStudentTree.getDistToUserID().size(), 0);
    assertEquals(smallStudentTree.getDistanceQueue().size(), 0);

    // insert into data structures
    smallStudentTree.insertIDIntoMap(3, 0);
    smallStudentTree.insertIDIntoMap(2.5, 1);
    smallStudentTree.getDistanceQueue().add(2.50);
    smallStudentTree.getDistanceQueue().add(0.50);
    smallStudentTree.getDistanceQueue().add(1.23);
    smallStudentTree.getDistanceQueue().add(0.0);
    assertEquals(smallStudentTree.getDistToUserID().size(), 2);
    assertEquals(smallStudentTree.getDistanceQueue().size(), 4);

    // test clearing data structures
    smallStudentTree.cleanDataStructures();
    assertEquals(smallStudentTree.getDistToUserID().size(), 0);
    assertEquals(smallStudentTree.getDistanceQueue().size(), 0);
  }

  /**
   * Method to test finding neighbors if k is negative.
   */
  @Test(expected = KIsNegativeException.class)
  public void testKIsNegative() throws KIsNegativeException, KeyNotFoundException {
    smallStudentTree.findKSN(-1, 0,
        smallStudentTree.getRoot(), new EuclideanDistance());
  }

  /**
   * Method to test finding neighbors if k is equal to zero.
   */
  @Test
  public void testKIsZero() throws KIsNegativeException, KeyNotFoundException {
    assertEquals(smallStudentTree.findKSN(0, 5,
        smallStudentTree.getRoot(), new EuclideanDistance()), new ArrayList<>());
    assertEquals(sameStudentTree.findKSN(0, 2,
        sameStudentTree.getRoot(), new EuclideanDistance()), new ArrayList<>());
    assertEquals(sameAxisStudentTree.findKSN(0, 4,
        sameAxisStudentTree.getRoot(), new EuclideanDistance()), new ArrayList<>());
  }

  /**
   * Method to test finding neighbors if k is greater than the number of nodes
   * in the tree.
   */
  @Test
  public void testKIsGreaterThanNumberOfNodes() throws KIsNegativeException, KeyNotFoundException {
    assertEquals(smallStudentTree.findKSN(20, 5,
        smallStudentTree.getRoot(), new EuclideanDistance()).size(), 20);
    assertEquals(smallStudentTree.findKSN(31, 5,
        smallStudentTree.getRoot(), new EuclideanDistance()).size(), 31);
//    assertEquals(sameStudentTree.findKSN(35, 1,
//        sameStudentTree.getRoot(), new EuclideanDistance()).size(), 35);
//    assertEquals(sameAxisStudentTree.findKSN(109, 0,
//        sameAxisStudentTree.getRoot(), new EuclideanDistance()).size(), 109);
  }

  /**
   * Method to test finding the k most similar neighbors.
   */
  @Test
  public void testFindKSN() {

  }

}
