package edu.brown.cs.student.main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
  Repl(ArrayList<REPLCommands> cmdList) {
    this.cmdMap = new HashMap<String, REPLCommands>();
    this.reader = new BufferedReader(new InputStreamReader(System.in));
    for (REPLCommands cmdPackage : cmdList) {
      cmdPackage.addCmds(this.cmdMap);
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
        Pattern regex = Pattern.compile("[^ \"]+|\"([^\"]*)\"");
        Matcher regexMatcher = regex.matcher(line);
        while (regexMatcher.find()) {
          matchList.add(regexMatcher.group());
        }
        String[] argv = matchList.toArray(new String[0]);
        String cmd = argv[0];
        int argc = argv.length;
        if (cmdMap.containsKey(cmd)) {
          REPLCommands cmdPack = cmdMap.get(cmd);
          cmdPack.executeCmds(cmd, argv, argc);
        } else {
          System.out.println("Unknown command");
        }
        line = reader.readLine();
      }
      reader.close();
    } catch (IOException ex) { // catch IOexceptions
      System.err.println("ERROR: IOEXception encountered.");
    }
  }
}
