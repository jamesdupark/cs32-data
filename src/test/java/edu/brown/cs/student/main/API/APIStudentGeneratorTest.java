package edu.brown.cs.student.main.API;

import edu.brown.cs.student.main.API.APIRequests.BadStatusException;
import edu.brown.cs.student.main.API.json.JSONParser;
import edu.brown.cs.student.main.API.json.StudentInfo;
import edu.brown.cs.student.main.API.json.StudentMatch;
import edu.brown.cs.student.main.Recommender.Student;
import org.junit.Test;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.Assert.assertEquals;

public class APIStudentGeneratorTest {

  @Test
  public void testAggregate() throws IOException {
    List<Student> studentList = new APIStudentGenerator().studentsFromAPI();

    // test size
    assertEquals(60, studentList.size());

    String infoFilePath = "data/recommendation/json/studentInfoGet.json";
    String matchFilePath = "data/recommendation/json/studentMatchPost.json";
    List<StudentInfo> infoExpect = JSONParser.readJsonFile(infoFilePath, StudentInfo.class);
    List<StudentMatch> matchExpect = JSONParser.readJsonFile(matchFilePath, StudentMatch.class);
    Set<Student> studentExpect = new HashSet<>();
    for (int i = 0; i < infoExpect.size(); i++) {
      Student toAdd = new Student();
      toAdd.buildFromPartial(infoExpect.get(i));
      toAdd.buildFromPartial(matchExpect.get(i));
      studentExpect.add(toAdd);
    }

    // test contents
    Set<Student> studentSet = new HashSet<>(studentList);
    assertEquals(studentExpect, studentSet);
    System.out.println(studentExpect);
  }
}
