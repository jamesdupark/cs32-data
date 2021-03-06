package edu.brown.cs.student.main.Blooms;

import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

import java.util.List;

public class StudentBloomTest {
  @Test
  public void testConstructor() {
    BloomFilter student1 = new StudentBloom(5, List.of("0", "hello", "hi"));

    assertTrue(student1.query("hello"));
    assertTrue(student1.query("hi"));
    assertFalse(student1.query(3));
  }

  @Test
  public void testInsertException() {
    BloomFilter student1 = new StudentBloom(5, List.of("0"));

    assertThrows(UnsupportedOperationException.class, () -> student1.insert("hi"));
  }
}
