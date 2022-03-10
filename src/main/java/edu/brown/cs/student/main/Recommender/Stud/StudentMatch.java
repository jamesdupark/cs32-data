package edu.brown.cs.student.main.Recommender.Stud;

import com.google.gson.Gson;
import edu.brown.cs.student.main.API.json.JSONable;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * StudentMatch class for storing json information from student match API endpoints.
 */
public class StudentMatch implements PartialStudent, Comparable<StudentMatch> {
  /**
   * integer parameters for student match - unique id and software engineering
   * confidence.
   */
  private final int id, softwareEngnConfidence;
  /**
   * String parameters for student match - name, gender, nationality, and race.
   */
  private final String name, gender, nationality, race;

  /**
   * Constructor for manually creating studentMatch objects (for testing).
   * @param id student's id
   * @param softwareEngnConfidence student's software engineering confidence.
   * @param name student's name
   * @param gender student's gender
   * @param nationality student's nationality
   * @param race student's race
   */
  public StudentMatch(int id, int softwareEngnConfidence, String name, String gender,
                      String nationality, String race) {
    this.id = id;
    this.softwareEngnConfidence = softwareEngnConfidence;
    this.name = name;
    this.gender = gender;
    this.nationality = nationality;
    this.race = race;
  }

  @Override
  public Map<String, Double> getQuantMap() {
    Map<String, Double> quantMap = new HashMap<>();
    quantMap.put("software_engn_confidence", (double) softwareEngnConfidence);

    return quantMap;
  }

  @Override
  public Map<String, String> getQualMap() {
    Map<String, String> qualMap = new HashMap<>();
    qualMap.put("id", String.valueOf(id));
    qualMap.put("name", name);
    qualMap.put("gender", gender);
    qualMap.put("nationality", nationality);
    qualMap.put("race", race);
    return qualMap;
  }

  @Override
  public int getId() {
    return this.id;
  }

  @Override
  public String toString() {
    Gson parser = new Gson();
    return parser.toJson(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    StudentMatch that = (StudentMatch) o;
    return id == that.id && softwareEngnConfidence == that.softwareEngnConfidence
        && Objects.equals(name, that.name) && Objects.equals(gender, that.gender)
        && Objects.equals(nationality, that.nationality)
        && Objects.equals(race, that.race);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public int compareTo(StudentMatch o) {
    Integer id1 = this.id;
    Integer id2 = o.getId();
    return id1.compareTo(id2);
  }
}
