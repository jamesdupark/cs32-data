package edu.brown.cs.student.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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

  private final HashMap<String, REPLCommands> map;

  /**
   * Creates a new Repl object that reads from standard input.
   */
  Repl() {
    this.reader = new BufferedReader(new InputStreamReader(System.in));
    this.map = new HashMap<String, REPLCommands>();
  }

  /**
   * Runs a REPL that reads from standard input and attempts to parse commands.
   */
  public void run() {

    try {
      String line = reader.readLine();
      while (line != null) { // start REPL
        String[] cmds = line.split(" ");
        if (map.containsKey(cmds[0])) {
          map.get(cmds[0]).executeCmds(cmds[0],
              Arrays.copyOfRange(cmds, 1,  cmds.length - 1), cmds.length - 1);
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
