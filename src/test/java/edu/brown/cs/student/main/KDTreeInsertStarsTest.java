//package edu.brown.cs.student.main;
//
//import edu.brown.cs.student.main.Builder.StarNodeBuilder;
//import edu.brown.cs.student.main.KDNodes.KDNode;
//import edu.brown.cs.student.main.KDNodes.StarNode;
//import org.junit.Before;
//import org.junit.Test;
//
//import java.io.IOException;
//import java.util.List;
//
//import static org.junit.Assert.assertEquals;
//
///**
// * Class to test the KDTree insert functionality for Stars.
// */
//public class KDTreeInsertStarsTest {
//  /** a KDTree for Stars that is loaded from data/stars/ten-star.csv. */
//  KDTree<KDNode> tenStars;
//
//  /**
//   * Method to initialize and populate the three KDTrees fields.
//   * @throws IOException if the CSVReader is unable to read the file path
//   */
//  @Before
//  public void setup() throws IOException {
//    // initialize stars kd-tree
//    tenStars = new KDTree<>();
//    CSVParser parser = new CSVParser(new StarNodeBuilder());
//    parser.load("data/stars/ten-star.csv");
//    List<KDNode> starsList = parser.getDataList();
//    tenStars.insertList(starsList, 0);
//  }
//
//  /**
//   * Method to test the KDTree Constructor and fields.
//   * @throws IOException if the CSVReader cannot read the filepath for smallStudent Tree
//   */
//  @Test
//  public void testConstructor() throws IOException {
//    setup();
//    // test number of nodes
//    assertEquals(tenStars.getNumNodes(), 10);
//
//    // test if root is inserted correctly
//    assertEquals(tenStars.getRoot().getVal().getAxisVal(0), -0.01729, 0);
//    assertEquals(tenStars.getRoot().getVal().getAxisVal(1), -1.81533, 0);
//    assertEquals(tenStars.getRoot().getVal().getAxisVal(2), 0.14824, 0);
//
//    // test num dimensions
//    assertEquals(tenStars.getRoot().getVal().getNumDimensions(), 3);
//
//    // test parent
//    // null case
//    assertEquals(tenStars.getRoot().getParent(), null);
//    // not null case
//    assertEquals(tenStars.getRoot().getLeft().getParent().getVal(),
//        tenStars.getRoot().getVal());
//    assertEquals(tenStars.getRoot().getRight().getLeft().getParent().getVal(),
//        tenStars.getRoot().getRight().getRight().getParent().getVal());
//
//    // test depth
//    assertEquals(tenStars.getRoot().getDepth(), 1);
//    assertEquals(tenStars.getRoot().getLeft().getDepth(), 2);
//  }
//
//  /**
//   * Test insert method for KDTree to see if one element can be added.
//   */
//  @Test
//  public void testInsert() {
//    KDTree<KDNode> testTree = new KDTree<>();
//    // insert into root
//    KDNode s = new StarNode(0, 0, 2, 4);
//    assertEquals(testTree.getNumNodes(), 0);
//    testTree.insert(testTree.getRoot(), s);
//    assertEquals(testTree.getRoot().getVal().getAxisVal(0), 0, 0);
//    assertEquals(testTree.getRoot().getVal().getAxisVal(1), 2, 0);
//    assertEquals(testTree.getRoot().getVal().getAxisVal(2), 4, 0);
//    assertEquals(testTree.getNumNodes(), 1);
//
//    // insert into left subtree from root
//    KDNode s2 = new StarNode(1, -5, 3, 1);
//    testTree.insert(testTree.getRoot(), s2);
//    assertEquals(testTree.getRoot().getLeft().getVal().getAxisVal(0), -5, 0);
//    assertEquals(testTree.getRoot().getLeft().getVal().getAxisVal(1), 3, 0);
//    assertEquals(testTree.getRoot().getLeft().getVal().getAxisVal(2), 1, 0);
//    assertEquals(testTree.getNumNodes(), 2);
//
//    // insert into right subtree from root
//    KDNode s3 = new StarNode(2, 8, 4, 0);
//    testTree.insert(testTree.getRoot(), s3);
//    assertEquals(testTree.getRoot().getRight().getVal().getAxisVal(0), 8, 0);
//    assertEquals(testTree.getRoot().getRight().getVal().getAxisVal(1), 4, 0);
//    assertEquals(testTree.getRoot().getRight().getVal().getAxisVal(2), 0, 0);
//    assertEquals(testTree.getNumNodes(), 3);
//
//    // insert into left subtree from left subtree from root
//    KDNode s4 = new StarNode(3, -1, 0, 20);
//    testTree.insert(testTree.getRoot(), s4);
//    assertEquals(testTree.getRoot().getLeft().getLeft().getVal().getAxisVal(0),
//        -1, 0);
//    assertEquals(testTree.getRoot().getLeft().getLeft().getVal().getAxisVal(1),
//        0, 0);
//    assertEquals(testTree.getRoot().getLeft().getLeft().getVal().getAxisVal(2),
//        20, 0);
//    assertEquals(testTree.getNumNodes(), 4);
//
//    // insert into right subtree from left subtree from root
//    KDNode s5 = new StarNode(4, -1, 3, 0);
//    testTree.insert(testTree.getRoot(), s5);
//    assertEquals(testTree.getRoot().getLeft().getRight().getVal().getAxisVal(0),
//        -1, 0);
//    assertEquals(testTree.getRoot().getLeft().getRight().getVal().getAxisVal(1),
//        3, 0);
//    assertEquals(testTree.getRoot().getLeft().getRight().getVal().getAxisVal(2),
//        0, 0);
//    assertEquals(testTree.getNumNodes(), 5);
//
//    // insert into left subtree from right subtree from root
//    KDNode s6 = new StarNode(5, 9.2, 3.5, 0.2);
//    testTree.insert(testTree.getRoot(), s6);
//    assertEquals(testTree.getRoot().getRight().getLeft().getVal().getAxisVal(0),
//        9.2, 0);
//    assertEquals(testTree.getRoot().getRight().getLeft().getVal().getAxisVal(1),
//        3.5, 0);
//    assertEquals(testTree.getRoot().getRight().getLeft().getVal().getAxisVal(2),
//        0.2, 0);
//    assertEquals(testTree.getNumNodes(), 6);
//
//    // insert into right subtree from right subtree from root
//    KDNode s7 = new StarNode(6, 1, 4, -2.023);
//    testTree.insert(testTree.getRoot(), s7);
//    assertEquals(testTree.getRoot().getRight().getRight().getVal().getAxisVal(0),
//        1, 0);
//    assertEquals(testTree.getRoot().getRight().getRight().getVal().getAxisVal(1),
//        4, 0);
//    assertEquals(testTree.getRoot().getRight().getRight().getVal().getAxisVal(2),
//        -2.023, 0);
//    assertEquals(testTree.getNumNodes(), 7);
//  }
//
//  /**
//   * Test getting an axis value that does not exist - should throw Runtime Exception
//   */
//  @Test(expected = RuntimeException.class)
//  public void testGetAxisValThatDoesNotExist() {
//    tenStars.getRoot().getVal().getAxisVal(4);
//  }
//}
