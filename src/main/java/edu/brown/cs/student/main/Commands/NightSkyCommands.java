package edu.brown.cs.student.main.Commands;

import edu.brown.cs.student.main.Commands.REPLCommands;
import edu.brown.cs.student.main.Coordinate;
import edu.brown.cs.student.main.NightSky;
import edu.brown.cs.student.main.Star;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class NightSkyCommands implements REPLCommands {
  private NightSky sky = new NightSky();
  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    switch (cmd) { // pattern matching for command strings
      case ("stars"): // stars: read in CSV
        this.starsCmd(argv, argc);
        break;

      case ("naive_neighbors"): // knn algorithm
        this.naiveNeighborsCmd(argv, argc);
        break;
      default: // not a recognized command
        System.err.println("ERROR: Command not recognized.");
        break;
    }

  }

  @Override
  public void addCmds(Map<String, REPLCommands> replCommandsMap) {
    replCommandsMap.put("stars", this);
    replCommandsMap.put("naive_neighbors", this);
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
          this.sky = newSky;
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
   * @throws IllegalArgumentException when the number of arguments is incorrect
   */
  private void naiveNeighborsCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    if (sky == null) { // no stars read in yet
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
      } else if (argc == 3) { // name lookup
        String starName = argv[2];

        maskStar = sky.nameToStar(starName); // mask off star that was named
        if (maskStar == null) { // no star was found with that name
          return;
        }

        queryCoord = maskStar.getCoord();
      } else { // incorrect no. of args
        throw new IllegalArgumentException();
      }

      List<Integer> neighbors = sky.knn(queryCoord, k, maskStar);
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
