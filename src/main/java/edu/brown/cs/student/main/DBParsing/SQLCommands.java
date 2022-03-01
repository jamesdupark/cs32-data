package edu.brown.cs.student.main.DBParsing;

import edu.brown.cs.student.main.CSVParse.Builder.StudentNodeBuilder;
import edu.brown.cs.student.main.CSVParse.CSVParser;
import edu.brown.cs.student.main.Commands.REPLCommands;
import edu.brown.cs.student.main.KDimTree.KDNodes.KDNode;
import edu.brown.cs.student.main.KDimTree.KDTree;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SQLCommands implements REPLCommands {
  /**
   * List of strings representing the command keywords supported by this class.
   */
  private final List<String> commands =
      List.of("connect_db", "select_names", "get_people_interests");
  private static Connection conn = null;
  private Proxy proxy = null;

  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    // verifying that command is a supported one; should never fail
    assert cmd.equals(argv[0]) && commands.contains(cmd);
    try {
      switch (cmd) {
        case "connect_db":
          this.connectDBCmd(argv, argc);
          break;
        case "select_names":
          this.selectNamesCmd(argv, argc);
          break;
//        case "get_people_interests":
//          this.similarKDCmd(argv, argc);
//          break;
        default:
          System.err.println("ERROR: Command not recognized.");
          break;
      }
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    } catch (RuntimeException e) {
      System.err.println(e.getMessage());
    }
  }


  private void connectDBCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc < 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments");
    }
    try {
      String filepath = argv[1];
      Map<String, String> tablePermissions = new HashMap<>();

      Map<Integer, String> indexTables = new HashMap<>();
      indexTables.put(0, "names");
      indexTables.put(1, "traits");
      indexTables.put(2, "skills");
      indexTables.put(3, "interests");

      for (int i = 0; i < argv.length - 2; i++) {
        tablePermissions.put(indexTables.get(i), argv[i + 2]);
      }
      proxy = new Proxy(filepath, tablePermissions);
      proxy.connectDB();
      conn = proxy.getConn();
      System.out.println("Connection made to " + conn);
    } catch (IllegalArgumentException e) {
      System.err.println(e.getMessage());
    } catch (SQLException e) {
      e.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  private void selectNamesCmd(String[] argv, int argc)
      throws IllegalArgumentException {
    // check correct number of args
    if (argc != 1) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
        + "Expected 1 argument but got " + argc);
    }
    // check if connection to database has been made
    if (conn == null) {
      throw new RuntimeException("ERROR: Database has not been contacted!");
    }
    try {
      String sql = "SELECT name FROM names";
      Map<String, String> commandToTable = new HashMap<>();
      commandToTable.put("SELECT", "names");
      if (proxy.validateQuery(commandToTable)) {
        // valid query
        System.out.println("Valid query!");
        List<String> tas = new ArrayList<>();
        //      String sql = "SELECT name FROM tas WHERE role = ?";
        PreparedStatement rolefinder = conn.prepareStatement(sql);
        //      rolefinder.setString(1, role);
        ResultSet rs = rolefinder.executeQuery();
        while (rs.next()) {
          System.out.println(rs.getString(1));
        }
      } else {
        // error: sql table does not have this level of permission
        System.out.println("ERROR: SQL Table does not have the level of permission");
      }
    } catch (SQLException e) {
      System.err.println("ERROR: " + e.getMessage());
    }
  }
//      // create new kd tree and insert elements into tree
//      this.kdTree = new KDTree<>();
//      CSVParser<KDNode> reader = new CSVParser(new StudentNodeBuilder());
//      reader.load(argv[1]);
//      List<KDNode> nodesList = reader.getDataList();
//      this.kdTree.insertList(nodesList, 0);
//      System.out.println("Read " + this.kdTree.getNumNodes() + " students from " + argv[1]);
//    } catch (IllegalArgumentException e) {
//      System.err.println(e.getMessage());
//    } catch (RuntimeException e) {
//      System.err.println(e.getMessage());
//    }
  // }

//  /**
//   * Executes the "load_kd" command by creating a new KD Tree and then inserting
//   * elements into the Tree that is parsed from a CSV. If successful, updates
//   * the KDTree field and prints how many entries were inserted successfully to stdout.
//   * Prints informative error message upon failure.
//   * @param argv array of strings representing tokenized user input
//   * @param argc length of argv
//   * @throws IllegalArgumentException if number of arguments is incorrect
//   */
//  private void loadKDCmd(String[] argv, int argc)
//      throws IllegalArgumentException {
//    // check correct number of args
//    if (argc != 2) {
//      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
//          + "Expected 2 arguments but got " + argc);
//    }
//    try {
//      // create new kd tree and insert elements into tree
//      this.kdTree = new KDTree<>();
//      CSVParser<KDNode> reader = new CSVParser(new StudentNodeBuilder());
//      reader.load(argv[1]);
//      List<KDNode> nodesList = reader.getDataList();
//      this.kdTree.insertList(nodesList, 0);
//      System.out.println("Read " + this.kdTree.getNumNodes() + " students from " + argv[1]);
//    } catch (IllegalArgumentException e) {
//      System.err.println(e.getMessage());
//    } catch (RuntimeException e) {
//      System.err.println(e.getMessage());
//    }
//  }
//
//  /**
//   * Executes the "similar_kd" command by querying for the k most similar
//   * values on the tree by some defined distance criteria that implements Distances
//   * interface. If successful, prints out the IDs of the k most similar values to stdout.
//   * Prints informative error message upon failure.
//   * @param argv array of strings representing tokenized user input
//   * @param argc length of argv
//   * @throws IllegalArgumentException if number of arguments is incorrect
//   * @throws RuntimeException if the KDTree is empty
//   */
//  private void similarKDCmd(String[] argv, int argc)
//      throws IllegalArgumentException, RuntimeException {
//    // check correct number of args
//    if (argc != 3) {
//      throw new IllegalArgumentException("ERROR: Incorrect number of arguments. "
//          + "Expected 3 arguments but got " + argc);
//    }
//    if (this.kdTree == null) {
//      throw new RuntimeException("ERROR: Can't query! There is no data in the KDTree");
//    }
//    try {
//      kdTree.cleanDataStructures();
//      List<Integer> retList = this.kdTree.findKSN(Integer.parseInt(argv[1]),
//          Integer.parseInt(argv[2]), this.kdTree.getRoot(), new EuclideanDistance());
//      for (Integer id : retList) {
//        System.out.println(id);
//      }
//    } catch (NumberFormatException e) {
//      System.err.println("ERROR: Number format exception " + e.getMessage());
//    } catch (IllegalArgumentException e) {
//      System.err.println(e.getMessage());
//    } catch (KIsNegativeException e) {
//      System.err.println(e.getMessage());
//    } catch (KeyNotFoundException e) {
//      System.err.println(e.getMessage());
//    }
//  }

  @Override
  public List<String> getCommandsList() {
    return this.commands;
  }
}
