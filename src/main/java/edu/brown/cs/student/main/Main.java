package edu.brown.cs.student.main;

import edu.brown.cs.student.main.Commands.BloomCommands;
import edu.brown.cs.student.main.Commands.KDCommands;
import edu.brown.cs.student.main.Commands.REPLCommands;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import spark.Spark;

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

    // adding REPLCommands packages
    BloomCommands blooms = new BloomCommands();
    // TODO : UNCOMMENT LINE 54 AND 58
//    NightSkyCommands stars = new NightSkyCommands();
    KDCommands kdtree = new KDCommands();

    // creating list
//    List<REPLCommands> commandsList = List.of(blooms, stars, kdtree);
    List<REPLCommands> commandsList = List.of(blooms, kdtree);
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
