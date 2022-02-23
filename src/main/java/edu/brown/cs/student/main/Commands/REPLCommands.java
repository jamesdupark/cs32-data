package edu.brown.cs.student.main.Commands;

import java.util.List;
import java.util.Map;

/**
 * Interface for a package of related REPL Commands that rely on a common data
 * structure or information, which will also be stored in classes implementing
 * this interface.
 * @author jamesdupark
 */
public interface REPLCommands {
  /**
   * Takes in a tokenized array representing user input and executes the proper
   * command based on the input, if a corresponding command exists. Also handles
   * printing results of commands and error messages.
   *
   * @param cmd argv[0], the keyword indicating which command should be run
   * @param argv array of strings tokenized from user input
   * @param argc length of argv
   */
  void executeCmds(String cmd, String[] argv, int argc);
  /**
   * Gets a list of strings representing the commands supported by this
   * REPLCommands object.
   * @return Commands supported by this REPLCommands object.
   */
  List<String> getCommandsList();

  /**
   * Adds the string command keywords that this class supports to the given
   * hashmap as keys for the given REPLCommands object. If a duplicate command
   * is found to already be in the hashmap, none of the commands for the given
   * REPLCommands class will be added and an exception will be thrown.
   *
   * @param commands list of commands this package supports, accessed via the
   *                 .getCommandsList() method.
   * @param replCommandsMap hashmap between all commands supported by a REPL and
   *                        the specific REPLCommands objects which support
   *                        each command.
   * @throws DuplicateCommandException if one of the commands that belongs to
   * the class is already a key for a different commands package.
   */
  default void addCmds(List<String> commands, Map<String, REPLCommands> replCommandsMap)
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
