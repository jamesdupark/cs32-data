package edu.brown.cs.student.main.KDimTree.KDNodes;

/**
 * Interface for a KDTree. Defines functionality that classes
 * of a KDTree must have.
 */
public interface KDNode {
  /**
   * Method to return the ID of the KDNode that is used when
   * querying the nearest neighbors.
   * @return the ID of the KDNode
   */
  int getID();

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
