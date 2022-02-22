package edu.brown.cs.student.main.Commands;

import edu.brown.cs.student.main.BloomFilter.BloomComparator;
import edu.brown.cs.student.main.BloomFilter.BloomFilter;
import edu.brown.cs.student.main.BloomFilter.XNORSimilarity;
import edu.brown.cs.student.main.CSVData.CSVBuilder;
import edu.brown.cs.student.main.CSVData.StarBuilder;
import edu.brown.cs.student.main.CSVData.StudentBuilder;
import edu.brown.cs.student.main.KNNCalculator.BloomKNNCalculator;
import edu.brown.cs.student.main.CSVData.CSVDatum;
import edu.brown.cs.student.main.CSVData.CSVReader;
import edu.brown.cs.student.main.DuplicateCommandException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REPLCommands class that packages commands related to bloom filters.
 * @author jamesdupark
 */
public class BloomCommands implements REPLCommands {
  /**
   * List of strings representing the command keywords supported by this class.
   */
  private final List<String> commands =
      List.of("create_bf", "insert_bf", "query_bf", "load_bf", "similar_bf");

  /**
   * the most recently created BloomFilter, able to be inserted into and
   * queried.
   */
  private BloomFilter currFilter;

  /**
   * Map of all bloom filters from their ID to their respective filter.
   */
  private Map<Integer, BloomFilter> allFilters;

  /**
   * Maximum number of strings in the fields used by our current dataset.
   */
  private int maxInsert = 0;

  /**
   * Takes in a tokenized array representing user input and executes the proper
   * command based on the input, if a corresponding command exists. Also handles
   * printing results of commands and error messages.
   *
   * @param cmd argv[0], the keyword indicating which command should be run
   * @param argv array of strings tokenized from user input
   * @param argc length of argv
   */
  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    // verifying that command is a supported one; should never fail
    assert cmd.equals(argv[0]) && commands.contains(cmd);

    try {
      switch (cmd) {
        case "create_bf":
          this.createBfCmd(argv, argc);
          break;
        case "insert_bf":
          this.insertBfCmd(argv, argc);
          break;
        case "query_bf":
          this.queryBfCmd(argv, argc);
          break;
        case "load_bf":
          this.loadBfCmd(argc, argv);
          break;
        case "similar_bf":
          this.similarBfCmd(argc, argv);
          break;
        default:
          System.err.println("ERROR: Command not recognized.");
          break;
      }
    } catch (IllegalArgumentException ex) {
      System.err.println(ex.getMessage());
    }
  }

  /**
   * Executes the "create_bf" command by attempting to create a new bloom filter
   * with the given parameters. If successful, updates the currFilter field and
   * prints the filter's bitset to stdout. Prints informative error message upon
   * failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void createBfCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 3) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments."
          + " Expected 3 arguments but got " + argc);
    }

    try {
      // parse numerical params
      double fpRate = Double.parseDouble(argv[1]);
      int maxElts = Integer.parseInt(argv[2]);

      // create new bloom filter and update currFilter field
      BloomFilter newFilter = new BloomFilter(fpRate, maxElts);
      currFilter = newFilter;
      System.out.println(currFilter);
    } catch (NumberFormatException ex) {
      System.err.println("ERROR: Incorrect numerical format for either "
          + "<r> or <n>.");
    } catch (IllegalArgumentException ex) {
      System.err.println(ex.getMessage());
    }

  }

  /**
   * Executes the "insert_bf" command by attempting to insert the given element
   * into the current bloom filter. If successful, prints the current filter's
   * updated bitset to stdout. Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void insertBfCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments."
          + " Expected 2 arguments but got " + argc);
    }

    if (currFilter != null) {
      currFilter.insert(argv[1]);
      System.out.println(currFilter);
    } else {
      System.out.println("ERROR: Must create a bloom filter with the "
          + "create_bf <r> <n> command before inserting.");
    }
  }

  /**
   * Executes the "query_bf" command by attempting to query the given element
   * into the current bloom filter. Prints the results of a successful query to
   * stdout. Prints informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of args is incorrect
   */
  private void queryBfCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments."
          + " Expected 2 arguments but got " + argc);
    }

    if (currFilter != null) {
      if (currFilter.query(argv[1])) {
        System.out.println("\"" + argv[1] + "\" might be in the set.");
      } else {
        System.out.println("\"" + argv[1] + "\" is definitely not in the set.");
      }
    } else {
      System.out.println("ERROR: Must create a bloom filter with the "
          + "create_bf <r> <n> command before inserting.");
    }
  }

  /**
   * Executes the "load_bf" command by attempting read in bloom filters from the
   * given filepath. If successful, prints the number of filters read in. Prints
   * informative error message upon failure.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void loadBfCmd(int argc, String[] argv)
      throws IllegalArgumentException {
    if (argc != 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments."
          + " Expected 2 arguments but got " + argc);
    }

    CSVBuilder<CSVDatum> studentBuilder = new StudentBuilder();
    CSVBuilder<CSVDatum> starBuilder = new StarBuilder();
    List<CSVBuilder<CSVDatum>> builders = List.of(studentBuilder, starBuilder);
    CSVReader<CSVDatum> parser = new CSVReader<>(builders);
    String filepath = argv[1];
    if (!parser.load(filepath)) {
      return;
    }

    List<CSVDatum> data = parser.getDataList();
    System.out.println(data);
    for (CSVDatum datum : data) {
      if (datum.getMaxElts() > maxInsert) {
        maxInsert = datum.getMaxElts();
      }
    }

    Map<Integer, BloomFilter> newFilters = new HashMap<>();
    for (CSVDatum datum : data) {
      BloomFilter filter = datum.toBloomFilter(maxInsert);
      int id = filter.getId();
      newFilters.put(id, filter);
    }

    int size = newFilters.size();

    System.out.println("Read " + size + " " + parser.getDatumName() + "s from "
        + filepath);

    allFilters = newFilters;
  }

  /**
   * Executes the "similar_bf" command by attempting to query the studentFilters
   * database for the k most similar filters in the database, based on the given
   * BloomComparator metric.
   *
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of args is incorrect
   */
  private void similarBfCmd(int argc, String[] argv)
      throws IllegalArgumentException {
    if (argc != 3) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments."
          + " Expected 3 arguments but got " + argc);
    } else if (allFilters == null) {
      System.err.println("ERROR: please read in a dataset using the load_bf"
          + " command before querying similar_bf.");
    }

    int k, id;
    try { // attempt to parse
      k = Integer.parseInt(argv[1]);
      id = Integer.parseInt(argv[2]);
    } catch (NumberFormatException ex) {
      throw new IllegalArgumentException("ERROR: k and student id must be "
          + "integer values");
    }

    if (k < 0) {
      throw new IllegalArgumentException("ERROR: k cannot be negative.");
    } else if (k == 0) {
      return;
    } else {
      BloomFilter base = allFilters.get(id);
      if (base == null) {
        System.err.println("ERROR: given id not in database.");
        return;
      }
      BloomComparator defaultComparator = new XNORSimilarity(base);
      BloomKNNCalculator knnCalc =
          new BloomKNNCalculator(base, allFilters, defaultComparator);
      List<Integer> knnList = knnCalc.knn(k);
      for (int neighborID : knnList) {
        System.out.println(neighborID);
      }
    }
  }

  @Override
  public void addCmds(Map<String, REPLCommands> replCommandsMap)
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
