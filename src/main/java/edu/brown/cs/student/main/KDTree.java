package edu.brown.cs.student.main;

import edu.brown.cs.student.main.Distances.Distances;
import edu.brown.cs.student.main.KDNodes.KDNode;
import org.checkerframework.checker.units.qual.A;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;

/**
 * Class representing a single KDTree of type KDNode. The KDTree will be
 * balanced according to the type's contract. This Class will primarily serve
 * two functionality: building a tree and finding close neighbors on the tree
 * governed by a extensible distance metric.
 * @param <T> the type of Nodes for the KDTree. The type implements KDNode
 *           interface and has the methods for finding the axis value
 *           and how many dimensions it has.
 * @author andrew7li
 */
public class KDTree<T extends KDNode> {
  /** the value of the current node. */
  private final KDNode val;
  /** the root of the tree. */
  private KDTree<KDNode> root;
  /** the parent of the node. */
  private KDTree<KDNode> parent;
  /** the left node at the current node. */
  private KDTree<KDNode> left;
  /** the right node at the current node. */
  private KDTree<KDNode> right;
  /** the number of nodes in the tree. */
  private int numNodes;
  /** the depth of the tree the node is at. */
  private int depth;
  /** hashmap mapping from user IDs to the node on the tree. */
  private Map<Integer, KDNode> userIDToNode;
  /** hashmap mapping from euclidean distance to a list of user IDs
   * associated with that distance. */
  private Map<Double, List<Integer>> distToUserID;
  /** priority queue that stores the k nearest euclidean distances. */
  private Queue<Double> distanceQueue;

  /**
   * Default constructor for a KDTree — instantiates tree as empty.
   */
  public KDTree() {
    this.val = null;
    this.root = null;
    this.parent = null;
    this.left = null;
    this.right = null;
    this.numNodes = 0;
    this.depth = 0;
    userIDToNode = new HashMap<>();
    distToUserID = new HashMap<>();
    distanceQueue = new PriorityQueue<>(Collections.reverseOrder());
  }

  /**
   * Constructor for a Node on the tree.
   * @param val the value associated at that Node
   */
  public KDTree(KDNode val) {
    this.val = val;
    this.root = null;
    this.left = null;
    this.right = null;
    this.parent = null;
  }

  /**
   * Method to insert an input list of type KDNode into a KDTree. The input
   * list will first be sorted on the axis corresponding to the depth of the
   * tree, then the middle element will be inserted into the tree. The left
   * sublist will then go on the left subtree and the right sublist will go
   * on the right subtree.
   * @param inputList list of elements of type KDNode to be inserted into the tree
   * @param axis the axis dimension to be sorted and compared on
   */
  public void insertList(List<KDNode> inputList, int axis) {
    // base case for recursion
    if (inputList.size() == 0) {
      return;
    }
    // deep copy the inputList
    List<KDNode> copyList = new ArrayList<>();
    for (KDNode ele : inputList) {
      copyList.add(ele);
    }

//    System.out.println(copyList);
    copyList.sort(Comparator.comparingDouble(ele -> ele.getAxisVal(axis)));
//    System.out.println(copyList);
//    System.out.println();
    int start = 0;
    int end = copyList.size() - 1;
    int mid = (end - start) / 2;
    // make sure element to be inserted is the first unique element in list with that value
    while (mid > start) {
      if (copyList.get(mid).getAxisVal(axis) > copyList.get(mid - 1).getAxisVal(axis)) {
        // element is unique
        break;
      } else {
        // find unique element
        mid--;
      }
    }
    this.insert(this.root, copyList.get(mid));
    List<KDNode> leftSublist = copyList.subList(start, mid);
    List<KDNode> rightSublist = copyList.subList(mid + 1, end + 1);
    insertList(leftSublist, (axis + 1) % copyList.get(mid).getNumDimensions());
    insertList(rightSublist, (axis + 1) % copyList.get(mid).getNumDimensions());
  }

  /**
  * Method to make a Node for a value and insert into the KDTree.
  * @param cursor Node on the KDTree for the input inputVal Node to be inserted under
  * @param inputVal value to be made into a Node and to be inserted
  */
  public void insert(KDTree<KDNode> cursor, KDNode inputVal) {
    KDTree<KDNode> node = new KDTree<>(inputVal);
    userIDToNode.put(inputVal.getID(), node.getVal());
    // empty tree -> node becomes root
    if (cursor == null) {
      this.numNodes++;
      cursor = node;
      cursor.root = cursor;
      cursor.depth = 1;
      this.root = cursor;
    } else {
      node.root = cursor.root;
      int axis = (cursor.depth - 1) % inputVal.getNumDimensions();
      if (node.val.getAxisVal(axis) < cursor.val.getAxisVal(axis)) {
        // insert left
        if (cursor.left == null) {
          // node does not exist so insert here
          this.numNodes++;
          cursor.left = node;
          node.parent = cursor;
          node.depth = cursor.depth + 1;
        } else {
          // node exists so recursive call
          insert(cursor.left, inputVal);
        }
      } else {
        // insert right
        if (cursor.right == null) {
          // node does not exist so insert here
          this.numNodes++;
          cursor.right = node;
          node.parent = cursor;
          node.depth = cursor.depth + 1;
        } else {
          // node exists so recursive call
          insert(cursor.right, inputVal);
        }
      }
    }
  }

  /**
   * Method to clear the data structures distToUserID and distanceQueue.
   */
  public void cleanDataStructures() {
    distToUserID.clear();
    distanceQueue.clear();
  }

  /**
   * Method to print and visualize the KDTree at any Node on the tree.
   * @param node node at which the Tree is to be printed at
   * @param prefix the spacing corresponding to the Node's depth in the tree to simulate layers
   */
  public void printTree(KDTree<KDNode> node, String prefix) {
    if (node == null) {
      return;
    }
    // print left subtree above current node
    printTree(node.left, prefix + " ");
    // print current node along with its depth and parent
    String nodeString = prefix + " + " + node.val + " depth: " + node.depth;
    if (node.parent != null) {
      nodeString += " Node's parent: " + node.parent.val;
    }
    System.out.println(nodeString);
    // print right subtree below current node
    printTree(node.right, prefix + " ");
  }

  /**
   * Method to insert a user ID into the distToUserID Hashmap.
   * @param key the distance corresponding to the key in the map
   * @param value the user ID to be added
   */
  private void insertIDIntoMap(double key, int value) {
    if (distToUserID.containsKey(key)) {
      distToUserID.get(key).add(value);
    } else {
      ArrayList<Integer> temp = new ArrayList<>();
      temp.add(value);
      distToUserID.put(key, temp);
    }
  }

  /**
   * Method that traverses a tree when finding the nearest neighbors.
   * @param traverseLeft boolean indicating whether we should traverse left subtree from cursor
   * @param traverseRight boolean indicating whether we should traverse right subtree from cursor
   * @param k the number of nearest neighbors to find
   * @param targetID the ID of the target node
   * @param cursor pointer pointing to the current node on the tree
   * @param distMetric the distance metric used to compare values
   * @throws KIsNegativeException if k is negative
   * @throws KeyNotFoundException if the user ID is not found
   */
  public void traverseTree(boolean traverseLeft, boolean traverseRight, int k,
                           int targetID, KDTree<KDNode> cursor, Distances distMetric)
      throws KIsNegativeException, KeyNotFoundException {
    if (traverseLeft && cursor.left != null) {
      findKSN(k, targetID, cursor.left, distMetric);
    }
    if (traverseRight && cursor.right != null) {
      findKSN(k, targetID, cursor.right, distMetric);
    }
  }

  /**
   * Method to find the user IDs of the k most similar neighbors from the
   * distanceQueue and distToUserID fields.
   * @param k the number of user IDs to return
   * @return the user IDs of the k most similar neighbors
   */
  public List<Integer> getUserIDOfKSN(int k) {
    List<Integer> idList;
    List<Integer> retList = new ArrayList<>();
    List<Double> closestDistanceList = new ArrayList<>(distanceQueue);
    Collections.sort(closestDistanceList);
    for (double ele : closestDistanceList) {
      if (retList.size() >= k) {
        // already found k nearest neighbors
        break;
      } else {
        // find list of ids associated at key
        idList = distToUserID.get(ele);
        if (idList.size() > 1) {
          // need to randomize
          Collections.shuffle(idList, new Random());
          for (int i = 0; i < idList.size() && retList.size() < k; i++) {
            retList.add(idList.get(i));
          }
        } else {
          // no need to randomize
          retList.add(idList.get(0));
        }
      }
    }
    return retList;
  }

  /**
   * Method to find the K most similar neighbors to the target node. Similarity between
   * two nodes is determined by the distMetric input.
   * @param k the number of nearest neighbors to find
   * @param targetID the ID of the target node
   * @param cursor pointer pointing to the current node on the tree
   * @param distMetric the distance metric used to compare values when determining similarity
   * @return an array list of the most similar user IDs
   * @throws KIsNegativeException if k is negative
   * @throws KeyNotFoundException if the target ID is not found
   */
  public List<Integer> findKSN(int k, int targetID, KDTree<KDNode> cursor,
                                    Distances distMetric)
      throws KIsNegativeException, KeyNotFoundException {
    if (k < 0) {
      throw new KIsNegativeException("ERROR: K is negative!");
    }
    if (k == 0) {
      return new ArrayList<>();
    }
    if (userIDToNode.containsKey(targetID)) {
      // key exists
      KDNode targetNode = userIDToNode.get(targetID);
      double distance = distMetric.calcDistance(cursor.val, targetNode);
      insertIDIntoMap(distance, cursor.getVal().getID());

      // check if we add this node to our distance priority queue
      if (distanceQueue.size() < k && targetID != cursor.val.getID()) {
        // our queue is not full yet
        distanceQueue.add(distance);
        // traverse both children
        traverseTree(true, true, k, targetID, cursor, distMetric);
      } else {
        // check if current node's euclidean distance is closer than farthest distance
        if (distance <= distanceQueue.peek() && targetID != cursor.val.getID()) {
          distanceQueue.poll();
          distanceQueue.add(distance);
        }

        // traversal logic
        // compare axis distance to farthest euclidean distance in priority queue
        int axisDim = (cursor.getDepth() - 1) % cursor.val.getNumDimensions();
        double axisDistance = Math.abs(targetNode.getAxisVal(axisDim)
            - cursor.getVal().getAxisVal(axisDim));
        if (axisDistance  > distanceQueue.peek()) {
          // throw away one of the two branches
          if (cursor.val.getAxisVal(axisDim) < targetNode.getAxisVal(axisDim)) {
            traverseTree(false, true, k, targetID, cursor, distMetric);
          } else {
            traverseTree(true, false, k, targetID, cursor, distMetric);
          }
        } else if (axisDistance == distanceQueue.peek()) {
          // may throw away left branch
          if (cursor.val.getAxisVal(axisDim) < targetNode.getAxisVal(axisDim)) {
            traverseTree(false, true, k, targetID, cursor, distMetric);
          } else {
            traverseTree(true, true, k, targetID, cursor, distMetric);
          }
        } else {
          // traverse both children
          traverseTree(true, true, k, targetID, cursor, distMetric);
        }
      }
    } else {
      throw new KeyNotFoundException("ERROR: Key does not exist!");
    }
    // find user IDs associated from distanceQueue and distToUserID Map
    List<Integer> retList = getUserIDOfKSN(k);
    return retList;
  }

  /**
   * Accessor method for getting the value at a Node.
   * @return value at a Node for the KDTree.
   */
  public KDNode getVal() {
    return val;
  }

  /**
   * Accessor method for getting the root of the KDTree.
   * @return the root of the KDTree
   */
  public KDTree<KDNode> getRoot() {
    return root;
  }

  /**
   * Accessor method for getting the parent of a Node on the KDTree.
   * @return the parent of the current Node on the KDTree
   */
  public KDTree<KDNode> getParent() {
    return parent;
  }

  /**
   * Accessor method for getting the left subtree for a Node on the KDTree.
   * @return the left subtree for a Node on the KDTree
   */
  public KDTree<KDNode> getLeft() {
    return left;
  }

  /**
   * Accessor method for getting the right subtree for a Node on the KDTree.
   * @return the right subtree for a Node on the KDTree
   */
  public KDTree<KDNode> getRight() {
    return right;
  }

  /**
   * Accessor method for getting the number of nodes on the KDTree.
   * @return the number of nodes on the KDTree
   */
  public int getNumNodes() {
    return numNodes;
  }

  /**
   * Accessor method for getting the depth of the current Node on the KDTree.
   * @return the depth of the current Node on the KDTree
   */
  public int getDepth() {
    return depth;
  }
}
