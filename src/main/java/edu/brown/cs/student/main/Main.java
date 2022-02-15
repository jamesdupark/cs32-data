package edu.brown.cs.student.main;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.Spark;

import java.util.ArrayList;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  // use port 4567 by default when running server
  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // set up parsing of command line flags
    OptionParser parser = new OptionParser();

    // "./run --gui" will start a web server
    parser.accepts("gui");

    // use "--port <n>" to specify what port on which the server runs
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
        .defaultsTo(DEFAULT_PORT);

    OptionSet options = parser.parse(args);
    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }

    
    System.out.println("HI");
    ArrayList<Coordinate> testList = new ArrayList<>();

    Coordinate c1 = new Coordinate(3, 2, 4);
    Coordinate c2 = new Coordinate(5, 10, 0);
    Coordinate c3 = new Coordinate(1, 5, -3);
    Coordinate c4 = new Coordinate(0, 3, 2);
    Coordinate c5 = new Coordinate(2, 4, 1);
    Coordinate c6 = new Coordinate(3, 1, 3);


    testList.add(c1);
    testList.add(c2);
    testList.add(c3);
    testList.add(c4);
    testList.add(c5);
    testList.add(c6);
    System.out.println(testList);

    KDTree<Coordinate> kdTree = new KDTree<>();
    System.out.println("Just initialized KD Tree");
    System.out.println(kdTree);
    System.out.println();
    kdTree.printTree(kdTree.root, "");

    System.out.println("Inserting one element");
    kdTree.insert(kdTree.root, c6);
    System.out.println(kdTree);
    System.out.println();
    kdTree.printTree(kdTree.root, "");

    System.out.println("Inserting second element");
    kdTree.insert(kdTree.root, c2);
    System.out.println(kdTree);
    System.out.println();
    kdTree.printTree(kdTree.root, "");

    System.out.println("Inserting third element");
    kdTree.insert(kdTree.root, c3);
    System.out.println(kdTree);
    System.out.println();
    kdTree.printTree(kdTree.root, "");

    System.out.println("Inserting fourth element");
    kdTree.insert(kdTree.root, c4);
    System.out.println(kdTree);
    System.out.println();
    kdTree.printTree(kdTree.root, "");

    kdTree.insert(kdTree.root, c5);
    kdTree.insert(kdTree.root, c6);
    kdTree.printTree(kdTree.root, "");
    System.out.println(kdTree.numNodes);
//    System.out.println("LOOK BELOW");
//    kdTree.printTree(kdTree.root, "");



//    System.out.println("Just initialized KD Tree");
//    System.out.println(kdTree);
//    System.out.println();
//    kdTree.printTree(kdTree.root, "");
//
//    System.out.println("Inserting one element");
//    kdTree.insert(c1);
//    System.out.println(kdTree);
//    System.out.println();
//    kdTree.printTree(kdTree.root, "");
//
//    System.out.println("Inserting second element");
//    kdTree.insert(c2);
//    System.out.println(kdTree);
//    System.out.println();
//    kdTree.printTree(kdTree.root, "");
//
//    System.out.println("Inserting third element");
//    kdTree.insert(c3);
//    System.out.println(kdTree);
//    System.out.println();
//    kdTree.printTree(kdTree.root, "");
//
//    System.out.println("Inserting fourth element");
//    kdTree.insert(c4);
//    System.out.println(kdTree);
//    System.out.println();
//
////    System.out.println("LOOK BELOW");
//    kdTree.printTree(kdTree.root, "");


//    Repl myRepl = new Repl();
//    myRepl.run();
  }

  private void runSparkServer(int port) {
    // set port to run the server on
    Spark.port(port);

    // specify location of static resources (HTML, CSS, JS, images, etc.)
    Spark.externalStaticFileLocation("src/main/resources/static");
  }
}
