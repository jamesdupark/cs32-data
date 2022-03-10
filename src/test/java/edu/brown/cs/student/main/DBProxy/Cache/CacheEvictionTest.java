package edu.brown.cs.student.main.DBProxy.Cache;

import edu.brown.cs.student.main.Commands.DatabaseCommands.DataCommands;
import edu.brown.cs.student.main.Commands.DatabaseCommands.ZooCommands;
import org.junit.Test;

import java.sql.SQLException;
import java.util.concurrent.ExecutionException;

import static org.junit.Assert.assertEquals;

/**
 * Class to test that cache eviction works.
 */
public class CacheEvictionTest {
  /**
   * Method to test that SQL queries after the max size defined by the Cache are not
   * registered in the Cache as additional queries. That is, the queries will overwrite
   * old queries and the size of the Cache remains fixed at its max size.
   */
  @Test
  public void testCacheEvictionOnSize() throws SQLException, ExecutionException {
    ZooCommands zooCmd = new ZooCommands();
    String[] argv = new String[3];
    argv[0] = "connect_db_zoo";
    argv[1] = "data/recommendation/sql/zoo.sqlite3";
    argv[2] = "RW";
    zooCmd.executeCmds("connect_db_zoo", argv, 3);

    // execute SQL commands into Cache so that Cache reaches its limit
    String sqlQuery = "SELECT * FROM zoo WHERE id = \'1\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 1);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'2\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 2);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'3\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 3);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'4\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 4);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'5\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 5);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'6\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 6);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'7\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 7);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'8\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 8);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'9\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 9);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'10\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 10);

    // queries after cache has reached its max size to test eviction policy
    sqlQuery = "SELECT * FROM zoo WHERE id = \'11\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 10);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'12\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 10);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'13\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 10);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'30\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 10);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'54\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 10);
    sqlQuery = "SELECT * FROM zoo WHERE id = \'1\'";
    zooCmd.getProxy().cacheExec(sqlQuery);
    assertEquals(zooCmd.getProxy().getCache().size(), 10);
  }
}
