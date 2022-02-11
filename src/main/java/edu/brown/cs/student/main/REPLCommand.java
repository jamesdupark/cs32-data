package edu.brown.cs.student.main;

public interface REPLCommand {
  void executeCmd(String[] argv, int argc);

  void checkArgs(String[] argv, int argc);
}
