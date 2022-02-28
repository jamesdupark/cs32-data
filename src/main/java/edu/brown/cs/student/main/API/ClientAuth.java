package edu.brown.cs.student.main.API;

import edu.brown.cs.student.main.CSVParse.FileParser;

/**
 * This simple class is for reading the API Key from the secret file. Taken from the cs32
 * API lab.
 */
public final class ClientAuth {
  /**
   * Constructor for ClientAuth - never called.
   */
  private ClientAuth() {
    // never called
  }
  /**
   * Reads the API Key from the secret text file where we have stored it.
   *
   * @return a String of the api key.
   */
  public static String getApiKey() {
    FileParser parser = new FileParser("config/secret/apikey.txt");
    return parser.readNewLine();
  }
}
