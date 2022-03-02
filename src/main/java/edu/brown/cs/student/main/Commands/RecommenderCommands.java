package edu.brown.cs.student.main.Commands;

import edu.brown.cs.student.main.Blooms.BloomFilter;
import edu.brown.cs.student.main.Blooms.StudentBloom;
import edu.brown.cs.student.main.CSVParse.Builder.StudentBuilder;
import edu.brown.cs.student.main.CSVParse.CSVParser;
import edu.brown.cs.student.main.CSVParse.DirectStudentBloomListMaker;
import edu.brown.cs.student.main.CSVParse.DirectStudentNodeMaker;
import edu.brown.cs.student.main.KDimTree.KDNodes.KDNode;
import edu.brown.cs.student.main.KDimTree.KDTree;
import edu.brown.cs.student.main.Student;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RecommenderCommands implements REPLCommands {
  /**
   * List of strings representing the command keywords supported by this class.
   */
  private final List<String> commands = List.of("recsys_load");
  /**
   * Map from characteristic name to characteristic type (qualitative or quantitative).
   */
  private final HashMap<String, String> typeMap;

  /**
   * Map of all bloom filters from their ID to their respective filter.
   */
  private Map<Integer, BloomFilter> allFilters;
  /**
   * the most recently created KDTree, able to be inserted into and queried.
   * */
  private KDTree<KDNode> kdTree;

  /**
   * Constructor for a new HeaderCommands.
   * @param map - Hashmap from column name to data type.
   */
  public RecommenderCommands(HashMap<String, String> map) {
    this.typeMap = map;
  }

  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    try {
      if (cmd.equals("recsys_load")) {
        this.load(argv, argc);
      } else {
        System.err.println("ERROR: Command not recognized.");
      }
    } catch (IllegalArgumentException ex) {
      System.err.println("ERROR: " + ex.getMessage());
    }
  }

  @Override
  public List<String> getCommandsList() {
    return this.commands;
  }

  /**
   * Loads the Students in CSV File to Bloom Filters and Student Nodes.
   * @param argv - array of strings representing tokenized user input.
   * @param argc - length of argv.
   */
  public void load(String[] argv, int argc) {
    //check correct number of args
    if (argc != 2) {
      throw new IllegalArgumentException("Incorrect number of arguments."
          + " Expected 2 arguments but got " + argc);
    } else if (typeMap.isEmpty()) {
      throw new IllegalArgumentException("Need to call headers_load before calling recsys_load");
    }
    CSVParser<Student> reader = new CSVParser<>(new StudentBuilder(typeMap));
    reader.load(argv[1]);
    List<Student> studentList = reader.getDataList();
    // list of bloom list or student nodes.
    List<List<String>> bloomData = new ArrayList<>();
    List<KDNode> nodesList = new ArrayList<>();
    int maxInsert = 0;
    // maker for each Bloom List and Student Node
    DirectStudentBloomListMaker bloomMaker = new DirectStudentBloomListMaker();
    DirectStudentNodeMaker nodeMaker = new DirectStudentNodeMaker();
    for (Student scholar : studentList) {
      // extracting StudentBloomList and finding maxInsert
      List<String> bloomList = bloomMaker.build(scholar.getQualMap());
      bloomData.add(bloomList);
      if (bloomList.size() > maxInsert) {
        maxInsert = bloomList.size();
      }
      int id = Integer.parseInt(scholar.getQualMap().get("id"));
      nodesList.add(nodeMaker.build(id, scholar.getQuanMap()));
    }

    // for bloom
    Map<Integer, BloomFilter> newFilters = new HashMap<>();
    for (List<String> toInsert : bloomData) {
      try {
        BloomFilter filter = new StudentBloom(maxInsert, toInsert);
        int id = filter.getId();
        newFilters.put(id, filter);
      } catch (IllegalArgumentException ex) {
        System.err.println(ex.getMessage());
        return;
      }
    }
    // for kdTree
    this.kdTree = new KDTree<>();
    this.kdTree.insertList(nodesList, 0);

    int size = studentList.size();
    System.out.println("Loaded Recommender with " + size + " student(s)");
    allFilters = newFilters;

  }
}
