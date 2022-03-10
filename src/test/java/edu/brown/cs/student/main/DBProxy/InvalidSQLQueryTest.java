package edu.brown.cs.student.main.DBProxy;

import edu.brown.cs.student.main.Commands.DatabaseCommands.DataCommands;
import org.junit.Test;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

/**
 * Class to test that invalid SQL queries are handled correctly by the Proxy and Caller.
 */
public class InvalidSQLQueryTest {
  /**
   * Method to test that invalid SQL queries are handled correctly and result in an error.
   */
  @Test(expected = RuntimeException.class)
  public void testValidateQueryThrowsRuntimeEx() throws SQLException, ExecutionException {
    DataCommands dataCmd = new DataCommands();
    String[] argv = new String[6];
    argv[0] = "connect_db_data";
    argv[1] = "data/recommendation/sql/data.sqlite3";
    argv[2] = "RW";
    argv[3] = "RW";
    argv[4] = "RW";
    argv[5] = "RW";
    dataCmd.executeCmds("connect_db_data", argv, 6);

    // execute invalid sql query
    String sqlQuery = "";
    dataCmd.getProxy().cacheExec(sqlQuery);
  }
}
