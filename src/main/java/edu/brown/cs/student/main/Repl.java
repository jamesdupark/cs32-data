package edu.brown.cs.student.main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;

/**
 * REPL class for reading in, parsing, and executing commands from standard
 * input.
 */
public class Repl {
  /**
   * BufferedReader object that reads in input stream.
   */
  private final BufferedReader reader;

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

    try {
      String line = reader.readLine();
      while (line != null) { // start REPL
        String[] cmds = line.split(" ");
        if (cmdMap.containsKey(cmds[0])) {
          cmdMap.get(cmds[0]).executeCmds(cmds[0], cmds, cmds.length);
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
