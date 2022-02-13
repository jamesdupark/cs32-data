package edu.brown.cs.student.main;

import java.util.List;
import java.util.Map;

/**
 * REPLCommands class that packages commands related to bloom filters.
 */
public class BloomCommands implements REPLCommands {
  /**
   * List of strings representing the command keywords supported by this class.
   */
  private final List<String> commands =
      List.of("create_bf", "insert_bf", "query_bf");

  private BloomFilter currFilter;

  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    // verifying that command is a supported one; should never fail
    assert cmd.equals(argv[0]) && commands.contains(cmd);

    try {
      switch (cmd) {
        case "create_bf":
          this.createBfCmd(argv, argc);
          break;
        case "insert_bf":
          System.out.println("Inserting into bloom filter");
          currFilter.insert("hello");
          System.out.println(currFilter);
          break;
        case "query_bf":
          System.out.println("Querying bloom filter");
          break;
        default:
          System.err.println("ERROR: Command not recognized.");
          break;
      }
    } catch (IllegalArgumentException ex) {
      System.err.println("ERROR: Incorrect number of arguments");
    }
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
  private void createBfCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 3) {
      throw new IllegalArgumentException();
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
      System.err.println("ERROR: Could not create bloom filter with"
          + "specified parameters");
    }

  }

  @Override
  public void addCmds(Map<String, REPLCommands> replCommandsMap) {
    for (String cmd : commands) {
      replCommandsMap.put(cmd, this);
    }
  }
}
