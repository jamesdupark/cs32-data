package edu.brown.cs.student.main.Commands;

import edu.brown.cs.student.main.CSVData.CSVDatum;
import edu.brown.cs.student.main.CSVData.CSVReader;
import edu.brown.cs.student.main.Distances.EuclideanDistance;
import edu.brown.cs.student.main.DuplicateCommandException;
import edu.brown.cs.student.main.KDNodes.KDNode;
import edu.brown.cs.student.main.KDTree;
import edu.brown.cs.student.main.KIsNegativeException;
import edu.brown.cs.student.main.KeyNotFoundException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * REPLCommands class that packages commands related to k-d tree.
 * @author andrew7li
 */
public class KDCommands implements REPLCommands {
  /** List of strings representing the command keywords supported by this class. */
  private final List<String> commands = List.of("load_kd", "similar_kd");
  /** the most recently created KDTree, able to be inserted into and queried. */
  private KDTree<KDNode> kdTree;

  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    // verifying that command is a supported one; should never fail
    assert cmd.equals(argv[0]) && commands.contains(cmd);
    try {
      switch (cmd) {
        case "load_kd":
          this.loadKDCmd(argv, argc);
          break;
        case "similar_kd":
          this.similarKDCmd(argv, argc);
          break;
        default:
          System.err.println("ERROR: Command not recognized.");
          break;
      }
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    } catch (RuntimeException e) {
      System.err.println(e.getMessage());
    }
  }

  /**
   * Executes the "load_kd" command by creating a new KD Tree and then inserting
   * elements into the Tree that is parsed from a CSV. If successful, updates
   * the KDTree field and prints how many entries were inserted successfully to stdout.
   * Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void loadKDCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
          + "Expected 2 arguments but got " + argc);
    }
    try {
      // create new kd tree and insert elements into tree
      this.kdTree = new KDTree<>();
      CSVReader<CSVDatum> reader = new CSVReader();
      reader.load(argv[1]);
      List<CSVDatum> studentCSVList = reader.getDataList();

      // turn my CSVDatum list into a list of KDNodes
      List<KDNode> nodesList = new ArrayList<>();
      for (CSVDatum stud : studentCSVList) {
        nodesList.add(stud.toKDNode());
      }
      this.kdTree.insertList(nodesList, 0);
      System.out.println("Read " + this.kdTree.getNumNodes() + " students from " + argv[1]);
      this.kdTree.printTree(this.kdTree.getRoot(), "");
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    } catch (RuntimeException e) {
      System.err.println(e.getMessage());
    }
  }

  /**
   * Executes the "similar_kd" command by querying for the k most similar
   * values on the tree by some defined distance criteria that implements Distances
   * interface. If successful, prints out the IDs of the k most similar values to stdout.
   * Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   * @throws RuntimeException if the KDTree is empty
   */
  private void similarKDCmd(String[] argv, int argc)
      throws IllegalArgumentException, RuntimeException {
    // check correct number of args
    if (argc != 3) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
          + "Expected 3 arguments but got " + argc);
    }
    if (this.kdTree == null) {
      throw new RuntimeException("ERROR: Can't query! There is no data in the KDTree");
    }
    try {
      kdTree.cleanDataStructures();
      List<Integer> retList = this.kdTree.findKSN(Integer.parseInt(argv[1]),
          Integer.parseInt(argv[2]), this.kdTree.getRoot(), new EuclideanDistance());
      for (Integer id : retList) {
        System.out.println(id);
      }
    } catch (NumberFormatException e) {
      System.err.println("ERROR: Number format exception " + e.getMessage());
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    } catch (KIsNegativeException e) {
      System.err.println(e.getMessage());
    } catch (KeyNotFoundException e) {
      System.err.println(e.getMessage());
    }
  }

  @Override
  public void addCmds(Map<String, REPLCommands> replCommandsMap)
      throws DuplicateCommandException {
    for (int i = 0; i < commands.size(); i++) {
      // check for duplicate commands
      String cmd = commands.get(i);
      REPLCommands dupPack = replCommandsMap.get(cmd);
      if (dupPack != null) {
        // if a duplicate value is found
        for (int j = 0; j < i; j++) { // remove all previously added keys
          String cmdToRemove = commands.get(j);
          replCommandsMap.remove(cmdToRemove);
        }
        throw new DuplicateCommandException("ERROR: command " + cmd
            + " already in this REPL's commandsMap");
      } else { // no duplicates found, can put command in safely
        replCommandsMap.put(cmd, this);
      }
    }
  }
}
