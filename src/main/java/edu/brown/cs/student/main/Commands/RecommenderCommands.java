package edu.brown.cs.student.main.Commands;

import edu.brown.cs.student.main.API.APIStudentGenerator;
import edu.brown.cs.student.main.Blooms.BloomFilter;
import edu.brown.cs.student.main.Blooms.SimilarityMetrics.BloomComparator;
import edu.brown.cs.student.main.Blooms.SimilarityMetrics.XNORSimilarity;
import edu.brown.cs.student.main.Blooms.StudentBloom;
import edu.brown.cs.student.main.CSVParse.Builder.StudentBuilder;
import edu.brown.cs.student.main.CSVParse.CSVParser;
import edu.brown.cs.student.main.CSVParse.Maker.DirectStudentBloomListMaker;
import edu.brown.cs.student.main.CSVParse.Maker.DirectStudentNodeMaker;
import edu.brown.cs.student.main.DBProxy.DBStudentGenerator;
import edu.brown.cs.student.main.Distances.EuclideanDistance;
import edu.brown.cs.student.main.KDimTree.KDNodes.KDNode;
import edu.brown.cs.student.main.KDimTree.KDTree;
import edu.brown.cs.student.main.KDimTree.KIsNegativeException;
import edu.brown.cs.student.main.KDimTree.KeyNotFoundException;
import edu.brown.cs.student.main.Recommender.Stud.DatabaseStudent;
import edu.brown.cs.student.main.Recommender.Stud.Student;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * REPLCommands package for commands related to the Recommender commands.
 * @author tkato1
 */
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
  private int numStudents = 0;
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
    if (argc != 3) {
      throw new IllegalArgumentException("Incorrect number of arguments."
          + " Expected 3 arguments but got " + argc);
    } else if (typeMap.isEmpty()) {
      throw new IllegalArgumentException("Need to call headers_load before calling recsys_load");
    }
    try {
      List<Student> studentList = new ArrayList<>();
      if (argv[1].equals("CSV")) {
        CSVParser<Student> reader = new CSVParser<>(new StudentBuilder(typeMap));
        reader.load(argv[2]);
        studentList = reader.getDataList();
        if (studentList.size() == 0) {
          throw new IllegalArgumentException("ERROR: no file found");
        }
      } else if (argv[1].equals("API-DB")) {
        DBStudentGenerator a = new DBStudentGenerator(argv[2]);
        APIStudentGenerator gen = new APIStudentGenerator();
        List<DatabaseStudent> dbStudents = a.getDBStudents();
        List<Student> apiStudents = gen.studentsFromAPI();
        for (int i = 0; i < apiStudents.size(); i++) {
          Student currStudent = apiStudents.get(i);
          DatabaseStudent dbStudent = dbStudents.get(i);
          assert Integer.parseInt(currStudent.getQualMap().get("id")) == dbStudent.getId()
              : "Ids must correspond";
          currStudent.buildFromPartial(dbStudent);
          studentList.add(currStudent);
        }
      } else {
        throw new IllegalArgumentException("second argument should either be CSV or API-DB");
      }
      // make StudentNodes from studentList
      loadKD(studentList);
      // make HashMap that will become allFilters from studentList
      Map<Integer, BloomFilter> newFilters = getBloomFilterMap(studentList);
      if (newFilters == null) {
        return;
      }
      allFilters = newFilters;
      numStudents = studentList.size();
      System.out.println("Loaded Recommender with " + numStudents + " student(s)");
      allFilters = newFilters;
    } catch (AssertionError ex) {
      System.err.println("ERROR: input sql file path does not exist");
    } catch (IllegalArgumentException ex) {
      // purposely blank
    }
  }

  /**
   * Load the List of Students as StudentNodes in the kd Tree.
   * @param studentList - List of Student classes.
   */
  private void loadKD(List<Student> studentList) {
    List<KDNode> nodesList = new ArrayList<>();
    DirectStudentNodeMaker nodeMaker = new DirectStudentNodeMaker();
    // interate through Student list and make StudentNodes.
    for (Student scholar : studentList) {
      int id = Integer.parseInt(scholar.getQualMap().get("id"));
      nodesList.add(nodeMaker.build(id, scholar.getQuanMap()));
    }
    this.kdTree = new KDTree<>();
    this.kdTree.insertList(nodesList, 0);
  }

  /**
   * Does the loading of Bloom Filters for recsys_load call.
   * @param studentList - List of students to make Bloom Filters with.
   * @return - A Hashmap from student id to Bloom Filter.
   */
  private Map<Integer, BloomFilter> getBloomFilterMap(List<Student> studentList) {
    int maxInsert = 0;
    List<List<String>> bloomData = new ArrayList<>();
    DirectStudentBloomListMaker bloomMaker = new DirectStudentBloomListMaker();
    for (Student scholar : studentList) {
      // extracting StudentBloomList and finding maxInsert
      List<String> bloomList = bloomMaker.build(scholar.getQualMap());
      bloomData.add(bloomList);
      if (bloomList.size() > maxInsert) {
        maxInsert = bloomList.size();
      }
    }
    Map<Integer, BloomFilter> newFilters = new HashMap<>();
    for (List<String> toInsert : bloomData) {
      try {
        BloomFilter filter = new StudentBloom(maxInsert, toInsert);
        int id = filter.getId();
        newFilters.put(id, filter);
      } catch (IllegalArgumentException ex) {
        System.err.println(ex.getMessage());
        return null;
      }
    }
    return newFilters;
  }

  /**
   * Prints the k (argv[1]) most similar students from student with the input id.
   * @param argc - the length of the argv.
   * @param argv - array of strings representing tokenized user input.
   */
  private void recommend(int argc, String[] argv) {
    if (numStudents == 0) {
      throw new IllegalArgumentException("must call recsys_load before calling recommend");
    } else if (argc != 3) {
      throw new IllegalArgumentException("Incorrect number of args, command should be:"
          + " recommend [k] [id]");
    }
    int k, id;
    id = -1;
    try {
      k = Integer.parseInt(argv[1]);
      id = Integer.parseInt(argv[2]);
      // get id to normalized bloom similarity map.
      Map<Integer, Double> idToNormalizedDist = getBloomNormalizedMap(k, id);
      // add normalized kd similarity to idToNormalizedDist map
      addNormalizedKdSimilarity(id, idToNormalizedDist);
      // set and hashmap of lists method to randomize ties
      Map<Double, List<Integer>> distToIDs = new HashMap<>();
      Set<Double> distanceSet = new HashSet<>();
      // adding each id and distance into distance set and hashmap.
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
        if (idPrinted >= numStudents - 1) {
          break;
        }
        Double nextNearest = sortedList.get(p);
        // shuffle list to randomize ties
        Collections.shuffle(distToIDs.get(nextNearest));
        for (int j = 0; j < distToIDs.get(nextNearest).size(); j++) {
          // iterate through list value of map and print the Student ids
          if ((p + j) < k) {
            if (idPrinted < numStudents - 1) {
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
    } catch (NumberFormatException ex) {
      System.err.println("k (arg 1) and id (arg 2) must be an integer");
    } catch (NullPointerException e) {
      System.err.println("ERROR: input student id does not exist");
    }
  }

  /**
   * Adds the normalized kd similarity score to idToNormalizedDist.
   * @param id - the id of the student to find similar students to
   * @param idToNormalizedDist - hashmap from student id to normalized score in similarity
   * @throws KIsNegativeException - when k is negative
   * @throws KeyNotFoundException - when student id does not exist
   */
  private void addNormalizedKdSimilarity(int id, Map<Integer, Double> idToNormalizedDist)
      throws KIsNegativeException, KeyNotFoundException {
    kdTree.cleanDataStructures();
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
  }

  /**
   * Outputs a map from student id to normalized bloom similarity score.
   * @param k - the number of similar students to find.
   * @param id - the id of the student to find similar students to
   * @return - a hashmap from student id to normalized bloom similarity score
   */
  private Map<Integer, Double> getBloomNormalizedMap(int k, int id) {
    BloomFilter base = allFilters.get(id);
    if (k < 0) {
      throw new IllegalArgumentException("k cannot be negative");
    }

    double bloomMin = Integer.MAX_VALUE;
    double bloomMax = Integer.MIN_VALUE;
    // get local copy of all students
    Map<Integer, BloomFilter> otherStudents = new HashMap<>(allFilters);
    // remove target students from potential recommendations
    otherStudents.remove(id);
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
    return idToNormalizedDist;
  }
}
