package edu.brown.cs.student.main;

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
}
