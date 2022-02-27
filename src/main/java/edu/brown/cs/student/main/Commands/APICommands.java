package edu.brown.cs.student.main.Commands;

import com.google.gson.JsonSyntaxException;
import edu.brown.cs.student.main.API.APIRequestBuilder;
import edu.brown.cs.student.main.API.APIRequestHandler;
import edu.brown.cs.student.main.API.json.JSONParser;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * REPLCommands package for commands related to the API aggregator.
 * @author jamesdupark
 */
public class APICommands implements REPLCommands {
  /**
   * APIRequestHandler for executing single API requests.
   */
  private APIRequestHandler handler = new APIRequestHandler();
  /**
   * List of commands supported by APICommands, to be retrieved by the
   * getCommandsList() method.
   */
  private final List<String> commands = List.of("active", "api");
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
   * "match") for currently active API endpoints. TODO: add more about i/o
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void activeCmd(int argc, String[] argv)
      throws IllegalArgumentException {
    if (argc != 2) {
      throw new IllegalArgumentException("ERROR: incorrect number of args.");
    }
  }

  /**
   * Executes the "api" command to make a request of the given type with the
   * given params to the API endpoint at the given URL. TODO: add more about i/o
   * @param argv array of strings representing tokenized user input
   * @param argc length of argv
   * @throws IllegalArgumentException if number of arguments is incorrect
   */
  private void apiCmd(int argc, String[] argv) throws IllegalArgumentException {
    if (argc != 4) {
      throw new IllegalArgumentException("ERROR: incorrect number of args.");
    }

    String type = argv[1];
    String url = argv[2];
    String params = argv[3];
    // split params
    Map<String, String[]> paramsMap = parseParams(params);
    // try to grab header and body
    String[] header = new String[0];
    String[] body = new String[0];
    String[] urlParams = new String[0];
    if (paramsMap.containsKey("url")) {
      urlParams = paramsMap.get("url");
    }
    if (paramsMap.containsKey("header")) {
      header = paramsMap.get("header");
    }
    if (paramsMap.containsKey("body")) {
      body = paramsMap.get("body");
    }

    APIRequestBuilder builder = new APIRequestBuilder(url);
    HttpRequest request;
    switch (type) {
      case "GET":
        request = builder.get(urlParams, header); // body has no meaning to GET request
        break;
      case "POST":
        request = builder.post(header, body);
        break;
      default:
        throw new IllegalArgumentException("ERROR: type not recognized.");
    }

    // TODO: execute command
    HttpResponse<String> response = handler.makeRequest(request); // will abort if request fails
    int status = response.statusCode();
    final int placeVal = 100; // place value to round the status code to
    int codeType = status - status % placeVal; // rounded down to the nearest 100
    try {
      if (codeType == 200) {
        // parse student

      } else {
        String statusMessage = JSONParser.getMessage(response.body());
        System.err.println("ERROR: status " + status + " code received: " + statusMessage);
      }
    } catch (JsonSyntaxException jse) {
      System.err.println("ERROR: request status " + status
          + ": error while attempting to parse json.");
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
