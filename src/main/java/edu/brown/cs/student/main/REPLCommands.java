package edu.brown.cs.student.main;

import java.util.Map;

/**
 * Interface for a package of related REPL Commands that rely on a common data
 * structure or information, which will also be stored in classes implementing
 * this interface.
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
   * Adds the string command keywords that this class supports to the given
   * hashmap as keys for the given REPLCommands object.
   *
   * @param replCommandsMap hashmap between all commands supported by a REPL and
   *                        the specific REPLCommands objects which support
   *                        each command.
   */
  void addCmds(Map<String, REPLCommands> replCommandsMap);
}
