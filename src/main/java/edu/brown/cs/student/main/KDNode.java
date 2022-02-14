package edu.brown.cs.student.main;

public class KDNode<KDTree> {
  // the value of the current node
  KDTree val;
  // the root of the tree
  public KDNode<KDTree> root;
  // the parent of the node
  public KDNode<KDTree> parent;
  // the left node at the current node
  public KDNode<KDTree> left;
  // the right node at the current node
  public KDNode<KDTree> right;
  // the number of nodes in the tree
  int numNodes;
  // the depth of the tree the node is at
  int depth;

  public KDNode() {
    this.val = null;
    this.root = null;
    this.parent = null;
    this.left = null;
    this.right = null;
    this.numNodes = 0;
    this.depth = 0;
  }

  public KDNode(KDTree val) {
    this.val = val;
    this.root = null;
    this.left = null;
    this.right = null;
    this.parent = null;
  }

  public double getAxisVal(KDTree val, int axis) {
    if (axis == 0) {
      return ((Coordinate)val).getFirstAxis();
    } else if (axis == 1) {
      return ((Coordinate)val).getSecondAxis();
    } else {
      return ((Coordinate)val).getThirdAxis();
    }
  }
//  Map<Integer, Runnable> commands = new HashMap<>();
//
//  // Populate commands map
//      commands.put(0, () -> ((Coordinate)val).getFirstAxis();
//      commands.put(1, () -> ((Coordinate)val).getSecondAxis();
//      commands.put(2, () -> ((Coordinate)val).getThirdAxis();

  /**
   *
   * @param treeNode node to be inserted under
   * @param val
   */
  public void insert(KDNode<KDTree> treeNode, KDTree val) {
    KDNode<KDTree> node = new KDNode<>(val);

    System.out.println("Node to be inserted: " + node.val);
    // empty tree -> node becomes root
    if (treeNode == null) {
      this.numNodes++;
      treeNode = node;
      treeNode.root = treeNode;
      treeNode.depth = 1;
      this.root = treeNode;
      System.out.println("Root node val: " + this.root.val);
    } else {
      node.root = treeNode.root;
      System.out.println("treeNode root val: " + treeNode.root.val);
      System.out.println("Node's root val: " + node.root.val);
      int axis = (treeNode.depth - 1) % 3;


      if (getAxisVal(node.val, axis) < getAxisVal(treeNode.val, axis)) {
        // insert left
        if (treeNode.left == null) {
          // node does not exist so insert here
          this.numNodes++;
          treeNode.left = node;
          node.parent = treeNode;
          node.depth = treeNode.depth + 1;
        } else {
          // node exists so recursive call
          insert(treeNode.left, val);
        }
      } else {
        // insert right
        System.out.println("Inserting right!");
        if (treeNode.right == null) {
          // node does not exist so insert here
          this.numNodes++;
          System.out.println("Inserting here!");
          treeNode.right = node;
          node.parent = treeNode;
          node.depth = treeNode.depth + 1;
          System.out.println(node.depth);
          System.out.println("here: " + node.root.val);
//          System.out.println("here: " + node.root.right.val);
        } else {
          // node exists so recursive call
          insert(treeNode.right, val);
        }
      }
    }
  }

  void printTree(KDNode<KDTree> node, String prefix) {

    if(node == null) return;

    printTree(node.left, prefix + " ");

    String nodeString = prefix + " + " + node.val + " depth: " + node.depth;
    if (node.parent != null) {
      nodeString += " Node's parent: " + node.parent.val;
    }
    System.out.println(nodeString);

    printTree(node.right , prefix + " ");
  }
}
