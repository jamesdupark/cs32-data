package edu.brown.cs.student.main;

import edu.brown.cs.student.main.Commands.APICommands;
import edu.brown.cs.student.main.Commands.BloomCommands;
import edu.brown.cs.student.main.Commands.DatabaseCommands.DataCommands;
import edu.brown.cs.student.main.Commands.DatabaseCommands.HoroscopeCommands;
import edu.brown.cs.student.main.Commands.DatabaseCommands.ZooCommands;
import edu.brown.cs.student.main.Commands.HeaderCommands;
import edu.brown.cs.student.main.Commands.KDCommands;
import edu.brown.cs.student.main.Commands.NightSkyCommands;
import edu.brown.cs.student.main.Commands.REPLCommands;
import edu.brown.cs.student.main.Commands.RecommenderCommands;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.Spark;

import java.util.HashMap;
import java.util.List;

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

  /**
   * Method that will execute the application.
   */
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
    //map from field to data type
    HashMap<String, String> typeMap = new HashMap<>();

    // adding REPLCommands packages
    BloomCommands blooms = new BloomCommands(typeMap);
    NightSkyCommands stars = new NightSkyCommands(typeMap);
    KDCommands kdTree = new KDCommands(typeMap);
    HeaderCommands header = new HeaderCommands(typeMap);
    RecommenderCommands recommender = new RecommenderCommands(typeMap);
    APICommands api = new APICommands();
    DataCommands data = new DataCommands();
    ZooCommands zoo = new ZooCommands();
    HoroscopeCommands horo = new HoroscopeCommands();
    // creating list
    List<REPLCommands> commandsList = List.of(blooms, stars, kdTree, header, recommender,
        api, data, zoo, horo);
    Repl myRepl = new Repl(commandsList);
    myRepl.run();
  }

  private void runSparkServer(int port) {
    // set port to run the server on
    Spark.port(port);

    // specify location of static resources (HTML, CSS, JS, images, etc.)
    Spark.externalStaticFileLocation("src/main/resources/static");
  }
}
