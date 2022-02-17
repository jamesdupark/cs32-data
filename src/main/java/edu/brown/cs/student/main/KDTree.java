package edu.brown.cs.student.main;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
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
public class KDTree<T> {
  // the value of the current node
  private final KDNode val;
  // the root of the tree
  private KDTree<KDNode> root;
  // the parent of the node
  private KDTree<KDNode> parent;
  // the left node at the current node
  private KDTree<KDNode> left;
  // the right node at the current node
  private KDTree<KDNode> right;
  // the number of nodes in the tree
  private int numNodes;
  // the depth of the tree the node is at
  private int depth;

  public Map<Integer, KDNode> userIDToNode;
  public Map<Double, ArrayList<Integer>> distToUserID;
  public PriorityQueue<Double> distanceQueue;



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
   * Method to insert an input list of type KDNode into a KDTree.
   * @param inputList list of elements of type KDNode to be inserted into the tree
   */
  public void insertList(List<KDNode> inputList) {
    for (KDNode ele : inputList) {
      this.insert(this.root, ele);
    }
  }

  /**
   * Method to make a Node for a value and insert into the KDTree.
   * @param cursor Node on the KDTree for the input val Node to be inserted under
   * @param val value to be made into a Node and to be inserted
   */
  public void insert(KDTree<KDNode> cursor, KDNode val) {
    KDTree<KDNode> node = new KDTree<>(val);
//    System.out.println(node.val);
    userIDToNode.put(val.getID(), node.getVal());

    // empty tree -> node becomes root
    if (cursor == null) {
      this.numNodes++;
      cursor = node;
      cursor.root = cursor;
      cursor.depth = 1;
      this.root = cursor;
    } else {
      node.root = cursor.root;
      int axis = (cursor.depth - 1) % val.getNumDimensions();
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
          insert(cursor.left, val);
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
          insert(cursor.right, val);
        }
      }
    }
  }

  /**
   * Method to print and visualize the KDTree.
   * @param node node at which the Tree is to be printed at
   * @param prefix the spacing corresponding to the Node's depth in the tree to simulate layers
   */
  void printTree(KDTree<KDNode> node, String prefix) {
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

  public double findEuclideanDistance(KDNode origin, KDNode target) {
    System.out.println("inside euclidean distance" + origin);
//    System.out.println(this.getVal());
    double sum = 0;
    for (int i = 0; i < target.getNumDimensions(); i++) {
//      System.out.println("Target val " + target.getAxisVal(i));
//      System.out.println("Origin val " + origin.getAxisVal(i));
      double deltaAxis = (target.getAxisVal(i) - origin.getAxisVal(i));
//      System.out.println("DELTA ASIS" + deltaAxis);
      sum += Math.pow(deltaAxis, 2);
    }
    return Math.sqrt(sum);
  }

  public ArrayList<Integer> findKNN(int k, int targetID, KDTree<KDNode> cursor) {
    System.out.println("inside fnn");
    System.out.println(distanceQueue);
    System.out.println(distToUserID);
    if (distanceQueue.size() > 0) {
      double firstEle = distanceQueue.poll();
      System.out.println("FIRST ELEM" + firstEle);
      distanceQueue.add(firstEle);
    }
    if (userIDToNode.containsKey(targetID)) {
      // key exists
//      System.out.println("key exists");
      KDNode targetNode = userIDToNode.get(targetID);
//      System.out.println("key exists 2" + targetNode);
      double euclideanDist = findEuclideanDistance(cursor.val, targetNode);
//      System.out.println("key exists 3" + euclideanDist);

      // add user ID to the distance hashmap
      if (distToUserID.containsKey(euclideanDist)) {
        distToUserID.get(euclideanDist).add(cursor.val.getID());
      } else {
        ArrayList<Integer> temp = new ArrayList<>();
        temp.add(cursor.val.getID());
        distToUserID.put(euclideanDist, temp);
      }


      // check if we add this node to our queue
      if (distanceQueue.size() < k) {
        // our queue is not full yet
        distanceQueue.add(euclideanDist);
        // TODO : traverse both children
        if (cursor.left != null) {
          findKNN(k, targetID, cursor.left);
        }
        if (cursor.right != null) {
          findKNN(k, targetID, cursor.right);
        }
      } else {
        // check if current node's euclidean distance is closer than farthest distance
        if (euclideanDist <= distanceQueue.peek()) {
          distanceQueue.poll();
          distanceQueue.add(euclideanDist);
        }

        // compare axis distance to farthest euclidean distance in priority queue
        int axisDim = (cursor.getDepth() - 1) % cursor.val.getNumDimensions();
        double axisDistance = Math.abs(targetNode.getAxisVal(axisDim) - cursor.getVal().getAxisVal(axisDim));

        if (axisDistance  > distanceQueue.peek()) {
          // throw away a branch
          if (cursor.val.getAxisVal(axisDim) < targetNode.getAxisVal(axisDim)) {
            // throw away left
            // TODO : traverse right only
            if (cursor.right != null) {
              findKNN(k, targetID, cursor.right);
            }
          } else {
            // throw away right
            // TODO : traverse left only
            if (cursor.left != null) {
              findKNN(k, targetID, cursor.left);
            }
          }
          // I do not need to account for equals here because axisDistance == 0 which is
          // covered by next if branch
        } else if (axisDistance == distanceQueue.peek()) {
          // check if branch should be thrown away
          if (cursor.val.getAxisVal(axisDim) < targetNode.getAxisVal(axisDim)) {
            // throw away left
            // TODO : traverse right only
            if (cursor.right != null) {
              findKNN(k, targetID, cursor.right);
            }
          } else {
            // TODO : both children
            if (cursor.left != null) {
              findKNN(k, targetID, cursor.left);
            }
            if (cursor.right != null) {
              findKNN(k, targetID, cursor.right);
            }
          }
        } else {
          // i traverse both branches
          // TODO : travese both children
          if (cursor.left != null) {
            findKNN(k, targetID, cursor.left);
          }
          if (cursor.right != null) {
            findKNN(k, targetID, cursor.right);
          }
        }
      }
    } else {
      throw new RuntimeException("Key does not exist!");
    }

    ArrayList<Integer> IDList;
    ArrayList<Integer> retList = new ArrayList<>();

    List<Double> closestDistanceList = new ArrayList<>(distanceQueue);
    Collections.sort(closestDistanceList);
    for (double ele : closestDistanceList) {
      if (retList.size() >= k) {
        // already found k nearest neighbors
        break;
      } else {
        // add
        // find list of ids associated at key
        IDList = distToUserID.get(ele);
        if (IDList.size() > 1) {
          // need to randomize
          Collections.shuffle(IDList, new Random());
          for (int i = 0; i < IDList.size() && retList.size() < k; i++) {
            retList.add(IDList.get(i));
          }
        } else {
          // no need to randomize
          retList.add(IDList.get(0));
        }
      }
    }
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
