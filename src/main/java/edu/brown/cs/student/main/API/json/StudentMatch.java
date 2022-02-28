package edu.brown.cs.student.main.API.json;

import com.google.gson.Gson;

import java.util.Objects;

/**
 * StudentMatch class for storing json information from student match API endpoints.
 */
public class StudentMatch implements JSONable {
  /**
   * integer parameters for student match - unique id and software engineering
   * confidence.
   */
  private int id, softwareEngnConfidence;
  /**
   * String parameters for student match - name, gender, nationality, and race.
   */
  private String name, gender, nationality, race;
  /**
   * Transient string parameters for student match to allow reading of jsons with
   * ssn field but never storing it.
   */
  private final transient String ssn = null;

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
}
