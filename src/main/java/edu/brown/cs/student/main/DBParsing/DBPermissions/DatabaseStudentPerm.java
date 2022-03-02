package edu.brown.cs.student.main.DBParsing.DBPermissions;

import java.util.HashMap;
import java.util.Map;

public class DatabaseStudentPerm implements DBPerm {
  private Map<Integer, String> indexTables = new HashMap<>() {{
      put(0, "names");
      put(1, "traits");
      put(2, "skills");
      put(3, "interests");
    }};

  @Override
  public Map<Integer, String> getIndexTable() {
    return indexTables;
  }
}
