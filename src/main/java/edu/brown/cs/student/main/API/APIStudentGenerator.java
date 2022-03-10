package edu.brown.cs.student.main.API;

import edu.brown.cs.student.main.API.APIRequests.BadStatusException;
import edu.brown.cs.student.main.Recommender.Stud.Student;
import edu.brown.cs.student.main.Recommender.Stud.StudentInfo;
import edu.brown.cs.student.main.Recommender.Stud.StudentMatch;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Class for Recommender that uses API aggregators to load a full database of
 * students.
 */
public class APIStudentGenerator {
  /**
   * APIAggregator for studentinfo API endpoints.
   */
  private final APIAggregator infoAggregator = new APIAggregator("info");
  /**
   * APIAggregator for studentmatch API endpoints.
   */
  private final APIAggregator matchAggregator = new APIAggregator("match");

  /**
   * Uses API aggregators to get two halves of a student dataset, then returns the
   * full student
   * dataset, sorted in ascending order by student ID.
   * 
   * @return list of students with studentInfo and studentMatch data added.
   * @throws IllegalArgumentException
   */
  public List<Student> studentsFromAPI() throws IllegalArgumentException {
    List<StudentInfo> infoList;
    List<StudentMatch> matchList;
    try { // get dataset halves
      infoList = infoAggregator.aggregate(StudentInfo.class);
      matchList = infoAggregator.aggregate(StudentMatch.class);
      // sort by id
      Collections.sort(infoList);
      Collections.sort(matchList);
      final int expectSize = 60;
      assert infoList.size() == expectSize && matchList.size() == expectSize
          : "Datasets incomplete or different sizes.";
    } catch (BadStatusException | AssertionError ex) {
      System.err.println("ERROR: " + ex.getMessage());
      return null;
    }
    // combine into students
    List<Student> dataset = new ArrayList<>();
    for (int i = 0; i < infoList.size(); i++) {
      Student toAdd = new Student();
      StudentInfo info = infoList.get(i);
      StudentMatch match = matchList.get(i);
      // throws IllegalArgumentException if args do not correspond
      toAdd.buildFromPartial(info);
      toAdd.buildFromPartial(match);
      dataset.add(toAdd);
    }

    return dataset;
  }
}
