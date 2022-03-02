package edu.brown.cs.student.main.Commands;

import edu.brown.cs.student.main.CSVParse.Builder.HeaderBuilder;
import edu.brown.cs.student.main.CSVParse.CSVParser;
import edu.brown.cs.student.main.Header;
import java.util.HashMap;
import java.util.List;

public class HeaderCommands implements REPLCommands {
  /**
   * List of strings representing the command keywords supported by this class.
   */
  private final List<String> commands = List.of("headers_load");
  /**
   * Map from characteristic name to characteristic type (qualitative or quantitative).
   */
  private final HashMap<String, String> typeMap;

  /**
   * Constructor for a new HeaderCommands.
   * @param map - Hashmap from column name to data type.
   */
  public HeaderCommands(HashMap<String, String> map) {
    this.typeMap = map;
  }

  @Override
  public void executeCmds(String cmd, String[] argv, int argc) {
    try {
      if (cmd.equals("headers_load")) {
        this.parseFile(argv, argc);
      } else {
        System.err.println("ERROR: Command not recognized.");
      }
    } catch (IllegalArgumentException ex) {
      System.err.println("ERROR: " + ex.getMessage());
    }
  }

  private void parseFile(String[] argv, int argc) throws IllegalArgumentException {
    //check correct number of args
    if (argc != 2) {
      throw new IllegalArgumentException("ERROR: Incorrect number of arguments."
          + " Expected 2 arguments but got " + argc);
    }
    // clears typeMap to restart type data information
    typeMap.clear();
    CSVParser<Header> reader = new CSVParser<>(new HeaderBuilder());
    reader.load(argv[1]);
    List<Header> headersList = reader.getDataList();

    for (Header header : headersList) {
      typeMap.put(header.getFieldName(), header.getDataType());
    }
    System.out.println("Loaded header types.");
  }

  @Override
  public List<String> getCommandsList() {
    return this.commands;
  }

  /**
   * Getter for a field type in typeMap. (For testing)
   * @param field - the field name that is the key to typeMap.
   * @return - the value, type of the field.
   */
  public String getType(String field) {
    return typeMap.get(field);
  }
}
