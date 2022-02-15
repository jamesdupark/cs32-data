package edu.brown.cs.student.main;

public interface KDNode {
  /**
   * Method to find the axis value for the value at a Node.
   * @param axis the axis to retrieve for the value
   * @return the axis value for the value at a Node
   */
  double getAxisVal(int axis);

  /**
   * Method to find the number of dimensions for the KDTree.
   * @return the number of dimensions for the KDTree
   */
  int getNumDimensions();
}
