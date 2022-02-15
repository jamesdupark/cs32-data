package edu.brown.cs.student.main;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Arrays;
import java.util.HashMap;
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

  private final HashMap<String, REPLCommands> cmdMap;

  private final BloomCommands blooms = new BloomCommands();

  private final HashMap<String, REPLCommands> commandsMap = new HashMap<>();

  private NightSky mySky;

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
//    Repl(List<REPLCommands> cmdList) {
//      for (REPLCommands cmdPack : cmdList) {
//        cmdPack.addCmds(commandsMap);
//      }
//    }


  /**
   * Runs a REPL that reads from standard input and attempts to parse commands.
   */
  public void run() {
    // initialize all commands



    try {
      blooms.addCmds(commandsMap);
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
    } catch (DuplicateCommandException ex) {
      System.err.println(ex.getMessage());
    }
  }

  /**
   * Parses user input and executes commands based on given keywords.
   *
   * @param userInput Line read in from stdin, hopefully containing a command
   */
  private void parse(String userInput) {
    try {
      // check for quoted tokens by splitting on quotation marks
      String[] quoteSplit = userInput.split("\"", -1);

      // split things before quotes by whitespace
      String[] spaceSplit = quoteSplit[0].split("[\\s]+");
      String[] argv = new String[0];
      boolean quotedToken = false;

      // check whether quoted tokens are formatted correctly
      switch (quoteSplit.length) {
        case 1: // no quoted tokens
          argv = spaceSplit;
          break;
        case 2: // no closing quote
          System.err.println("ERROR: no closing quotation for quoted name.");
          return;
        case 3: // closing quote found
          if (quoteSplit[2].equals("")) { // no text following second quote
            List<String> argl = new ArrayList<>(Arrays.asList(spaceSplit));
            argl.add(quoteSplit[1]); // add our quote-enclosed string to the argl
            argv = argl.toArray(argv);
            quotedToken = true;
            break;
          } // fall-through for text following second quote
        default: // too many arguments
          throw new IllegalArgumentException();
      }

      // attempt to execute commands based on our argv
      int argc = argv.length;
      String cmd = argv[0];

      REPLCommands commandPack = commandsMap.get(cmd);

      if (commandPack != null) {
        commandPack.executeCmds(cmd, argv, argc);
        return;
      }

      switch (cmd) { // pattern matching for command strings
        case ("stars"): // stars: read in CSV
          this.starsCmd(argv, argc);
          break;

        case ("naive_neighbors"): // knn algorithm
          this.naiveNeighborsCmd(argv, argc, quotedToken);
          break;

        default: // not a recognized command
          System.err.println("ERROR: Command not recognized.");
          break;
      }
    } catch (IllegalArgumentException ex) {
      // incorrect no. of arguments
      System.err.println("ERROR: Incorrect number of arguments.");
    }
  }

  /**
   * Executes the "stars" command by attempting to read in star data from the
   * given .csv file.
   *
   * @param argv array of arguments read in from the REPL
   * @param argc number of elements in argv
   * @throws IllegalArgumentException if there are too many or too few arguments
   */
  private void starsCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    if (argc == 2) {
      String filepath = argv[1];

      try {
        NightSky newSky = new NightSky();
        boolean readSuccess = newSky.readCsv(filepath);
        if (readSuccess) { // file read from without encountering any errors
          this.mySky = newSky;
        }
      } catch (IOException ex) { // file not found
        System.err.println("ERROR: file not found.");
      }
    } else { // signal incorrect number of args
      throw new IllegalArgumentException();
    }
  }

  /**
   * Executes the "naive_neighbors" command by attempting to query the NightSky
   * database for the k nearest neighbors.
   *
   * @param argv String array containing parsed user input
   * @param argc int representing number of distinct inputs
   * @param quotedToken boolean representing whether a quoted token (name) was
   *                    present or not
   * @throws IllegalArgumentException when the number of arguments is incorrect
   */
  private void naiveNeighborsCmd(String[] argv, int argc, boolean quotedToken)
      throws IllegalArgumentException {
    if (mySky == null) { // no stars read in yet
      System.err.println("ERROR: star database empty. Please populate by "
          + "reading in a csv with the stars <filename> command.");
      return;
    }

    int k;
    double x, y, z;
    try {
      k = Integer.parseInt(argv[1]);
      assert k >= 0;
      Coordinate queryCoord;
      Star maskStar = null; // default: don't mask off any stars

      if (argc == 5) { // coordinate lookup
        x = Double.parseDouble(argv[2]);
        y = Double.parseDouble(argv[3]);
        z = Double.parseDouble(argv[4]);

        queryCoord = new Coordinate(x, y, z);
      } else if (argc == 3 && quotedToken) { // name lookup
        String starName = argv[2];

        maskStar = mySky.nameToStar(starName); // mask off star that was named
        if (maskStar == null) { // no star was found with that name
          return;
        }

        queryCoord = maskStar.getCoord();
      } else { // incorrect no. of args
        throw new IllegalArgumentException();
      }

      List<Integer> neighbors = mySky.knn(queryCoord, k, maskStar);
      for (int star : neighbors) {
        System.out.println(star);
      }
    } catch (NumberFormatException ex) { // k/x/y/z is not a number
      System.err.println("ERROR: non-numerical k or coordinate value.");
    } catch (AssertionError ex) { // k < 0;
      System.err.println("ERROR: k cannot be negative");
    }
  }
}
