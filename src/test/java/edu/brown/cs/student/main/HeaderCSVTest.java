package edu.brown.cs.student.main;

import edu.brown.cs.student.main.Commands.HeaderCommands;
import org.junit.Test;

import java.util.HashMap;

public class HeaderCSVTest {

  @Test
  public void testConstructor() {
    HeaderCommands cmd = new HeaderCommands(new HashMap<String, String>());
  }
}
