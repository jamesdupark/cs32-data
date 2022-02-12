package edu.brown.cs.student.main;

import java.util.List;
import java.util.Map;

/**
 * REPLCommands class that packages commands related to bloom filters
 */
public class BloomCommands implements REPLCommands {
  /**
   * List of strings representing the command keywords supported by this class.
   */
  List<String> commands = List.of("create_bf", "insert_bf", "query_bf");

  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    switch (cmd) {
      case "create_bf":
        System.out.println("Creating bloom filter");
        break;
      case "insert_bf":
        System.out.println("Inserting into bloom filter");
        break;
      case "query_bf":
        System.out.println("Querying bloom filter");
        break;
      default:
        System.err.println("ERROR: Command not recognized.");
        break;
    }
  }

  @Override
  public void addCmds(Map<String, REPLCommands> replCommandsMap) {
    for (String cmd : commands) {
      replCommandsMap.put(cmd, this);
    }
  }
}
