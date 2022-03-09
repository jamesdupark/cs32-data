package edu.brown.cs.student.main.Commands;

import edu.brown.cs.student.main.Blooms.BloomFilter;
import edu.brown.cs.student.main.Blooms.SimilarityMetrics.BloomComparator;
import edu.brown.cs.student.main.Blooms.SimilarityMetrics.XNORSimilarity;
import edu.brown.cs.student.main.Blooms.StudentBloom;
import edu.brown.cs.student.main.CSVParse.Builder.StudentBuilder;
import edu.brown.cs.student.main.CSVParse.CSVParser;
import edu.brown.cs.student.main.CSVParse.DirectStudentBloomListMaker;
import edu.brown.cs.student.main.CSVParse.DirectStudentNodeMaker;
import edu.brown.cs.student.main.Distances.EuclideanDistance;
import edu.brown.cs.student.main.KDimTree.KDNodes.KDNode;
import edu.brown.cs.student.main.KDimTree.KDTree;
import edu.brown.cs.student.main.KDimTree.KIsNegativeException;
import edu.brown.cs.student.main.KDimTree.KeyNotFoundException;
import edu.brown.cs.student.main.KNNCalculator.BloomKNNCalculator;
import edu.brown.cs.student.main.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class RecommenderCommands implements REPLCommands {
  /**
   * List of strings representing the command keywords supported by this class.
   */
  private final List<String> commands = List.of("recsys_load", "recommend");
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
   * the number of students loaded from CSV file.
   */
  private int numStudents;
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
      } else if (cmd.equals("recommend")) {
        this.recommend(argc, argv);
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

    numStudents = studentList.size();
    System.out.println("Loaded Recommender with " + numStudents + " student(s)");
    allFilters = newFilters;
  }

  private void recommend(int argc, String[] argv) {

    int k = Integer.parseInt(argv[1]); // assert that this exists
    int id = Integer.parseInt(argv[2]); // assert that this exists
    BloomFilter base = allFilters.get(id);

    double bloomMin = Integer.MAX_VALUE;
    double bloomMax = Integer.MIN_VALUE;
    // get local copy of all students
    Map<Integer, BloomFilter> otherStudents = new HashMap<>(allFilters);
    // remove target students from potential recommendations
    otherStudents.remove(id);
    // assert that id not in otherFilters?
    BloomComparator studentComparator = new XNORSimilarity(base);
    Map<Integer, Integer> idToBloomDists = new HashMap<>();
    for (int studentID : otherStudents.keySet()) {
      BloomFilter filter = otherStudents.get(studentID);
      int dist = studentComparator.similarity(filter);
      idToBloomDists.put(studentID, dist);
      if (dist < bloomMin) {
        bloomMin = dist;
      }
      if (dist > bloomMax) {
        bloomMax = dist;
      }
    }
    Map<Integer, Double> idToNormalizedDist = new HashMap<>();
    for (int studentID : otherStudents.keySet()) {
      double distanceInDouble = idToBloomDists.get(studentID);
      double normalized = (distanceInDouble - bloomMin) / (bloomMax - bloomMin);
      idToNormalizedDist.put(studentID, normalized);
    }
    kdTree.cleanDataStructures();
    try {
      // loads DistanceQueue field with k length Queue from target student
      this.kdTree.findKSN(numStudents, id, this.kdTree.getRoot(), new EuclideanDistance());
      // find the min and max for normalization
      double kdMax = Double.MIN_VALUE;
      double kdMin = Double.MAX_VALUE;
      for (Double distance : this.kdTree.getDistanceQueue()) {
        if (distance > kdMax) {
          kdMax = distance;
        }
        if (distance < kdMin) {
          kdMin = distance;
        }
      }
      // loop through DistanceQueue to find normalized Distance for each node.
      for (Double distance : this.kdTree.getDistanceQueue()) {
        double normalizedDistance = (distance - kdMin) / (kdMax - kdMin);
        // loop through list of students with the same normalized distance to
        // combine their distance with bloom filter.
        for (Integer studentID : this.kdTree.getDistToUserID().get(distance)) {
          idToNormalizedDist.replace(studentID,
              idToNormalizedDist.get(studentID) + normalizedDistance);
        }
      }
      Map<Double, List<Integer>> distToIDs = new HashMap<>();
      Set<Double> distanceSet = new HashSet<>();
      idToNormalizedDist.forEach((studentID, normedDist) -> {
        if (!distanceSet.add(normedDist)) {
          distToIDs.get(normedDist).add(studentID);
        } else {
          List<Integer> lst = new ArrayList<>();
          lst.add(studentID);
          distToIDs.put(normedDist, lst);
        }
      });

      List<Double> sortedList = new ArrayList<>(distanceSet);
      Collections.sort(sortedList);
      // iterate through sorted list
      int idPrinted = 0;
      for (int p = 0; p < k; p++) {
        if (idPrinted >= numStudents) {
          break;
        }
        Double nextNearest = sortedList.get(p);
        // shuffle list to randomize ties
        Collections.shuffle(distToIDs.get(nextNearest));
        for (int j = 0; j < distToIDs.get(nextNearest).size(); j++) {
          // iterate through list value of map and print the starIDs
          if ((p + j) < k) {
            if (idPrinted < numStudents) {
              System.out.println(distToIDs.get(nextNearest).get(j));
              idPrinted++;
            }
          }
        }
      }
    } catch (KIsNegativeException ex) {
      System.err.println(ex.getMessage());
    } catch (KeyNotFoundException e) {
      System.err.println("ERROR: Key not found");
    }

  }
}
