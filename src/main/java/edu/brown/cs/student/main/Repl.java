package edu.brown.cs.student.main;
import edu.brown.cs.student.main.Commands.DuplicateCommandException;
import edu.brown.cs.student.main.Commands.REPLCommands;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.List;

/**
 * REPL class for reading in, parsing, and executing commands from standard
 * input.
 */
public class Repl {
  /**
   * BufferedReader object that reads in input stream.
   */
  private final BufferedReader reader;

  /**
   * hashMap of command strings to their respective REPLCommands classes.
   */
  private final HashMap<String, REPLCommands> cmdMap;

  /**
   * Creates a new Repl object that reads from standard input.
   */
  Repl(List<REPLCommands> cmdList) {
    this.cmdMap = new HashMap<String, REPLCommands>();
    this.reader = new BufferedReader(new InputStreamReader(System.in));
    for (REPLCommands cmdPackage : cmdList) {
      try {
        cmdPackage.addCmds(this.cmdMap);
      } catch (DuplicateCommandException ex) {
        System.err.println(ex.getMessage());
      }
    }
  }

  /**
   * Runs a REPL that reads from standard input and attempts to parse commands.
   */
  public void run() {
    // initialize all commands
    try {
      String line = reader.readLine();
      while (line != null) { // start REPL
        ArrayList<String> matchList = new ArrayList<String>();
        Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"");
        Matcher regexMatcher = regex.matcher(line);
        while (regexMatcher.find()) {
          if (regexMatcher.group(1) != null) {
            matchList.add(regexMatcher.group(1));
          } else {
            matchList.add(regexMatcher.group());
          }
        }
        String[] argv = matchList.toArray(new String[0]);
        int argc = argv.length;
        if (argc == 0) { // no input
          line = reader.readLine(); // read next line
          continue;
        }
        String cmd = argv[0];
        if (cmdMap.containsKey(cmd)) {
          REPLCommands cmdPack = cmdMap.get(cmd);
          cmdPack.executeCmds(cmd, argv, argc);
        } else {
          System.out.println("ERROR: Unknown command");
        }
        line = reader.readLine(); // read next line
      }
      reader.close();

    } catch (IOException ex) { // catch IOexceptions
      System.err.println("ERROR: IOException encountered.");
    }
  }
}