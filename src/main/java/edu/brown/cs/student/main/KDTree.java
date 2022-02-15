package edu.brown.cs.student.main;

public class KDTree<T> {
  // the value of the current node
  KDNode val;
  // the root of the tree
  public KDTree<KDNode> root;
  // the parent of the node
  public KDTree<KDNode> parent;
  // the left node at the current node
  public KDTree<KDNode> left;
  // the right node at the current node
  public KDTree<KDNode> right;
  // the number of nodes in the tree
  int numNodes;
  // the depth of the tree the node is at
  int depth;

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
   * Method to make a Node for a value and insert into the KDTree.
   * @param treeNode Node on the KDTree for the input val Node to be inserted under
   * @param val value to be made into a Node and to be inserted
   */
  public void insert(KDTree<KDNode> treeNode, KDNode val) {
    KDTree<KDNode> node = new KDTree<>(val);

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
      int axis = (treeNode.depth - 1) % val.getNumDimensions();

      if (node.val.getAxisVal(axis) < treeNode.val.getAxisVal(axis)) {
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
//  public void insert(KDNode<KDTree> treeNode, KDTree val) {
//    KDNode<KDTree> node = new KDNode<>(val);
//    System.out.println("Inside insert!");
//    System.out.println((Coordinate)val);
//    System.out.println("Node to be inserted: " + node.val);
//    // empty tree -> node becomes root
//    if (treeNode == null) {
//      this.numNodes++;
//      treeNode = node;
//      treeNode.root = treeNode;
//      treeNode.depth = 1;
//      this.root = treeNode;
//      System.out.println("Root node val: " + this.root.val);
//    } else {
//      node.root = treeNode.root;
//      System.out.println("treeNode root val: " + treeNode.root.val);
//      System.out.println("Node's root val: " + node.root.val);
//      int axis = (treeNode.depth - 1) % 3;
//
//      KDNode<KDTree> childToTraverse = null;
//      if (node.val.getAxisVal(axis) < treeNode.val.getAxisVal(axis)) {
//        // insert left
//        childToTraverse = treeNode.left;
//        System.out.println("Child is inserting left!");
////        System.out.println(childToTraverse.val);
//      } else {
//        childToTraverse = treeNode.right;
//        System.out.println("Child is inserting right!");
////        System.out.println(childToTraverse.val);
//      }
//      if (childToTraverse == null) {
//        // node does not exist so insert here
//        this.numNodes++;
//        node.parent = treeNode;
//        node.depth = treeNode.depth + 1;
//      } else {
//        // node exists so recursive call
//        insert(childToTraverse, val);
//      }
//    }
//  }

  /**
   * Method to print and visualize the KDTree.
   * @param node node at which the Tree is to be printed at
   * @param prefix the spacing corresponding to the Node's depth in the tree to simulate layers
   */
  void printTree(KDTree<KDNode> node, String prefix) {
    if(node == null) return;

    // print left subtree above current node
    printTree(node.left, prefix + " ");

    // print current node along with its depth and parent
    String nodeString = prefix + " + " + node.val + " depth: " + node.depth;
    if (node.parent != null) {
      nodeString += " Node's parent: " + node.parent.val;
    }
    System.out.println(nodeString);

    // print right subtree below current node
    printTree(node.right , prefix + " ");
  }
}
