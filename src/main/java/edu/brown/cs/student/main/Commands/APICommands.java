package edu.brown.cs.student.main.Commands;

import edu.brown.cs.student.main.API.APIAggregator;
import edu.brown.cs.student.main.API.APIRequests.APIRequestBuilder;
import edu.brown.cs.student.main.API.APIRequests.APIRequestHandler;
import edu.brown.cs.student.main.API.APIRequests.BadStatusException;
import edu.brown.cs.student.main.API.json.JSONParser;
import edu.brown.cs.student.main.API.json.StudentInfo;
import edu.brown.cs.student.main.API.json.StudentMatch;

import java.io.IOException;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REPLCommands package for commands related to the API aggregator.
 * @author jamesdupark
 */
public class APICommands implements REPLCommands {
  /**
   * List of commands supported by APICommands, to be retrieved by the
   * getCommandsList() method.
   */
  private final List<String> commands = List.of("active", "api", "api_aggregate", "load_json");
  /**
   * APIRequestHandler for executing single API requests.
   */
  private final APIRequestHandler handler = new APIRequestHandler();
  /**
   * APIAggregator for studentinfo API endpoints.
   */
  private final APIAggregator infoAggregator = new APIAggregator("info");
  /**
   * APIAggregator for studentmatch API endpoints.
   */
  private final APIAggregator matchAggregator = new APIAggregator("match");
  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    // verifying that command is a supported one; should never fail
    assert cmd.equals(argv[0]) && commands.contains(cmd);
    try {
      switch (cmd) {
        case "active":
          this.activeCmd(argc, argv);
          break;
        case "api":
          this.apiCmd(argc, argv);
          break;
        case "api_aggregate":
          this.aggregateCmd(argc, argv);
          break;
        case "load_json":
          this.loadJsonCmd(argc, argv);
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
   * Executes the "active" command to query the given API type (either "info" or
   * "match") for currently active API endpoints and prints the resultant list of
   * currently active endpoints.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void activeCmd(int argc, String[] argv)
      throws IllegalArgumentException {
    if (argc != 2) {
      throw new IllegalArgumentException("ERROR: incorrect number of args.");
    }
    String type = argv[1];
    List<String> activeList = new ArrayList<>();
    try {
      switch (type) {
        case "info":
          activeList = infoAggregator.getActiveClients();
          break;
        case "match":
          activeList = matchAggregator.getActiveClients();
          break;
        default:
          throw new IllegalArgumentException("ERROR: type not recognized.");
      }
    } catch (BadStatusException bse) {
      System.err.println("ERROR: " + bse.getMessage());
    }

    System.out.println("active " + type + " clients: " + activeList);
  }

  /**
   * Executes the "api" command to make a request of the given type with the
   * given params to the API endpoint at the given URL. Attempts to read the resultant
   * json into a list of StudentInfo or StudentMatch, and prints response body upon
   * failure to read into json. Prints informative error message upon failure to make
   * request.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void apiCmd(int argc, String[] argv) throws IllegalArgumentException {
    if (argc != 4 && argc != 3) {
      throw new IllegalArgumentException("ERROR: incorrect number of args.");
    }

    String type = argv[1];
    String url = argv[2];
    // check if params were provided
    String[] header = null, body = null, urlParams = null;
    if (argc == 4) {
      String params = argv[3];
      // split params
      Map<String, String[]> paramsMap = parseParams(params);
      if (paramsMap.containsKey("url")) {
        urlParams = paramsMap.get("url");
      }
      if (paramsMap.containsKey("header")) {
        header = paramsMap.get("header");
      }
      if (paramsMap.containsKey("body")) {
        body = paramsMap.get("body");
      }
    }

    APIRequestBuilder builder = new APIRequestBuilder(url);
    HttpRequest request;
    switch (type) {
      case "GET":
        request = builder.get(urlParams, header); // body has no meaning to GET request
        break;
      case "POST":
        request = builder.post(header, body); // auth should be provided in body
        break;
      default:
        throw new IllegalArgumentException("ERROR: type not recognized.");
    }

    // get response
    HttpResponse<String> response;
    try {
      response = handler.makeRequest(request); // will abort if request fails
    } catch (BadStatusException | HttpTimeoutException ex) {
      System.err.println("ERROR: " + ex.getMessage());
      return;
    }
    System.out.println(response.body());
  }

  /**
   * Executes the "api_aggregate" command to make a request of the given type to all active
   * endpoints of that type and prints resulting set of 60 partial students. Prints informative
   * error message upon failure to make request.
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void aggregateCmd(int argc, String[] argv) throws IllegalArgumentException {
    if (argc != 2) {
      throw new IllegalArgumentException("ERROR: incorrect number of args.");
    }

    String type = argv[1];
    try {
      switch (type) {
        case "info":
          List<StudentInfo> studentInfoData = infoAggregator.aggregate(StudentInfo.class);
          System.out.println(studentInfoData);
          break;
        case "match":
          List<StudentMatch> studentMatchData = matchAggregator.aggregate(StudentMatch.class);
          System.out.println(studentMatchData);
          break;
        default:
          throw new IllegalArgumentException("ERROR: Type must be either \"info\" or \"match\"");
      }
    } catch (BadStatusException bse) {
      System.err.println("ERROR: " + bse.getMessage());
    }
  }

  /**
   * Reads in info from a local json file and displays the result. DEMO ONLY
   * @param argc number of args in argv
   * @param argv string array of parsed user input.
   */
  private void loadJsonCmd(int argc, String[] argv) {
    if (argc != 2) {
      throw new IllegalArgumentException("ERROR: incorrect number of args.");
    }
    // load file
    String filepath = argv[1];
    try {
      List<StudentMatch> list = JSONParser.readJsonFile(filepath, StudentMatch.class);
      System.out.println(list);
    } catch (IOException iox) {
      System.err.println(iox.getMessage());
    }
  }

  /**
   * Parses a user-inputted param string of the format
   * "type1:name1:value1:name2:value2;type2:name3:value3" by placing each set of
   * parameters into a HashMap keyed on the parameter type.
   * @param params user-inputted param string
   * @return map of each parameter type to the respective parameters.
   * @throws IllegalArgumentException upon encountering an ill-formatted params string
   */
  private Map<String, String[]> parseParams(String params) throws IllegalArgumentException {
    try {
      String[] paramsArray = params.split(";");
      Map<String, String[]> paramsMap = new HashMap<>();
      for (String singleParam : paramsArray) {
        // split only on the first colon
        String[] paramTypeValSplit = singleParam.split(":", 2);
        assert paramTypeValSplit.length == 2 : "No param type indicator";
        assert !paramTypeValSplit[1].equals("") : "No param values given.";
        String paramType = paramTypeValSplit[0];
        String[] paramVals = paramTypeValSplit[1].split(":", -1);
        paramsMap.put(paramType, paramVals);
      }

      return paramsMap;
    } catch (AssertionError ae) {
      throw new IllegalArgumentException("ERROR: " + ae.getMessage());
    }
  }

  @Override
  public List<String> getCommandsList() {
    return commands;
  }
}
