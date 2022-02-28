package edu.brown.cs.student.main.CSVParse;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * A generic file parser. Taken from the cs32 API lab.
 */
public class FileParser {

  private BufferedReader bufRead = null;

  /**
   * A FP constructor.
   *
   * @param file - a String file path
   * @throws FileNotFoundException if the given file cannot be found.
   */
  public FileParser(String file) throws FileNotFoundException {
    this.bufRead = new BufferedReader(new FileReader(file));
  }

  /**
   * Reads a new line in the file and returns a String.
   *
   * @return a read String line
   */
  public String readNewLine() {
    if (bufRead != null) {
      try {
        String ln = bufRead.readLine();
        return ln;
      } catch (IOException e) {
        System.out.println("ERROR: Read");
        return null;
      }
    } else {
      return null;
    }
  }
}
