package edu.brown.cs.student.main;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * REPLCommands class that packages commands related to k-d tree.
 * @author andrew7li
 */
public class KDCommands implements REPLCommands {
  /**
   * List of strings representing the command keywords supported by this class.
   */
  private final List<String> commands =
      List.of("load_kd", "similar_kd");

  /**
   * the most recently created KDTree, able to be inserted into and queried.
   */
  private KDTree<KDNode> kdTree;

  /**
   * Takes in a tokenized array representing user input and executes the proper
   * command based on the input, if a corresponding command exists. Also handles
   * printing results of commands and error messages.
   *
   * @param cmd argv[0], the keyword indicating which command should be run
   * @param argv array of strings tokenized from user input
   * @param argc length of argv
   */
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
    } catch (IllegalArgumentException ex) {
      System.err.println(ex.getMessage());
    }
  }

  /**
   * Executes the "load_kd" command by creating a new KD Tree and then inserting
   * elements into the Tree.
   * attempting to create a new bloom filter
   * with the given parameters. If successful, updates the currFilter field and
   * prints the filter's bitset to stdout. Prints informative error message upon
   * failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void loadKDCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments."
          + "Expected 2 arguments but got" + argc);
    }

    try {
      // create new bloom filter and update currFilter field
      this.kdTree = new KDTree<>();
      CSVParser parser = new CSVParser();
      parser.parse(argv[1]);
      List<KDNode> studentList = parser.getData();
      this.kdTree.insertList(studentList);
      // print statement for inserting nodes
      System.out.println("Read " + this.kdTree.getNumNodes() + " students from " + argv[1]);
//      this.kdTree.printTree(this.kdTree.getRoot(), "");
//      System.out.println(this.kdTree.userIDToNode + "\n\n");
//
//      System.out.println("FINDING NEIGHBORS INSIDE KDCOMMANDS");
//      System.out.println(this.kdTree.findKNN(5, 3, this.kdTree.getRoot()));
    } catch (IllegalArgumentException ex) {
      System.err.println(ex.getMessage());
    } catch (IOException e) {
      System.out.println("ERROR:" + e);
    }
  }

  private void similarKDCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
//    System.out.println("Similar KD Cmd");
//    System.out.println(this.kdTree.userIDToNode + "\n\n");
//
//    System.out.println("FINDING NEIGHBORS INSIDE KDCOMMANDS");
    System.out.println(this.kdTree.findKNN(Integer.parseInt(argv[1]), Integer.parseInt(argv[2]), this.kdTree.getRoot()));
  }

  /**
   * Executes the "create_bf" command by attempting to create a new bloom filter
   * with the given parameters. If successful, updates the currFilter field and
   * prints the filter's bitset to stdout. Prints informative error message upon
   * failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  /*private void createBfCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 3) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments."
          + "Expected 3 arguments but got" + argc);
    }

    try {
      // parse numerical params
      double fpRate = Double.parseDouble(argv[1]);
      int maxElts = Integer.parseInt(argv[2]);

      // create new bloom filter and update currFilter field
      BloomFilter newFilter = new BloomFilter(fpRate, maxElts);
      currFilter = newFilter;
      System.out.println(currFilter);
    } catch (NumberFormatException ex) {
      System.err.println("ERROR: Incorrect numerical format for either "
          + "<r> or <n>.");
    } catch (IllegalArgumentException ex) {
      System.err.println(ex.getMessage());
    }
  }*/

  /**
   * Executes the "insert_bf" command by attempting to insert the given element
   * into the current bloom filter. If successful, prints the current filter's
   * updated bitset to stdout. Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  /*private void insertBfCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments."
          + "Expected 2 arguments but got" + argc);
    }

    if (currFilter != null) {
      currFilter.insert(argv[1]);
      System.out.println(currFilter);
    } else {
      System.out.println("ERROR: Must create a bloom filter with the "
          + "create_bf <r> <n> command before inserting.");
    }
  }*/

  /**
   * Executes the "query_bf" command by attempting to query the given element
   * into the current bloom filter. Prints the results of a successful query to
   * stdout. Prints informative error message upon failure.
//   * @param argv array of strings representing tokenized user input
//   * @param argc length of argv
   * @throws IllegalArgumentException if number of args is incorrect
   */
  /*private void queryBfCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments."
          + "Expected 2 arguments but got" + argc);
    }

    if (currFilter != null) {
      if (currFilter.query(argv[1])) {
        System.out.println("\"" + argv[1] + "\" might be in the set.");
      } else {
        System.out.println("\"" + argv[1] + "\" is definitely not in the set.");
      }
    } else {
      System.out.println("ERROR: Must create a bloom filter with the "
          + "create_bf <r> <n> command before inserting.");
    }
  }*/

  @Override
  public void addCmds(Map<String, REPLCommands> replCommandsMap) {
    for (String cmd : commands) {
      replCommandsMap.put(cmd, this);
    }
  }
}
