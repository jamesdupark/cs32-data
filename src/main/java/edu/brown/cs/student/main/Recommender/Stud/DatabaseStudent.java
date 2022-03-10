package edu.brown.cs.student.main.Recommender.Stud;

import java.util.HashMap;
import java.util.Map;

/**
 * Class to represent an individual student in the Student Database. Upon instantiation,
 * all fields will evaluate to null, which will then be mutated through setters.
 */
public class DatabaseStudent implements PartialStudent, Comparable<DatabaseStudent> {
  /** String representing the ID of the student. This serves as a unique
   * identifier for each student. */
  private String id;
  /** String representing the name of the student. */
  private String name;
  /** String representing the email of the student. */
  private String email;
  /** String representing the traits of the student that are weaknesses. */
  private String weaknesses;
  /** String representing the traits of the student that are strengths. */
  private String strengths;
  /** String representing the skill of the student. */
  private String skill;
  /** String representing the interests of the student. */
  private String interests;

  /**
   * Constructor for the DatabaseStudent Class — initializes all fields to null.
   */
  public DatabaseStudent() {
    this.id = null;
    this.name = null;
    this.email = null;
    this.weaknesses = null;
    this.strengths = null;
    this.skill = null;
    this.interests = null;
  }

  /**
   * Returns a map of numerical attributes to their values.
   *
   * @return hashmap of attribute names to numerical values.
   */
  @Override
  public Map<String, Double> getQuantMap() {
    return new HashMap<>();
  }

  /**
   * Returns a map of qualitative attributes to their values.
   *
   * @return hashmap of attribute names to string values.
   */
  @Override
  public Map<String, String> getQualMap() {
    Map<String, String> qualMap = new HashMap<>();
    qualMap.put("id", this.id);
    qualMap.put("name", this.id);
    qualMap.put("email", this.id);
    qualMap.put("weaknesses", this.id);
    qualMap.put("strengths", this.id);
    qualMap.put("skill", this.id);
    qualMap.put("interests", this.id);
    return qualMap;
  }

  @Override
  public int getId() {
    return Integer.parseInt(id);
  }

  /**
   * Mutator method for the id field of the student.
   * @param id Integer representing the new id of the student.
   */
  public void setId(String id) {
    this.id = id;
  }

  /**
   * Accessor method for the name field of the student.
   * @return the name field of the student.
   */
  public String getName() {
    return name;
  }

  /**
   * Mutator method for the name field of the student.
   * @param name String representing the new name of the student.
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Accessor method for the email field of the student.
   * @return the email field of the student.
   */
  public String getEmail() {
    return email;
  }

  /**
   * Mutator method for the email field of the student.
   * @param email String representing the new email of the student.
   */
  public void setEmail(String email) {
    this.email = email;
  }

  /**
   * Accessor method for the weaknesses type field of the student.
   * @return the weaknesses field of the student.
   */
  public String getWeaknesses() {
    return this.weaknesses;
  }

  /**
   * Mutator method for the weaknesses type field of the student.
   * @param newWeaknesses String representing the new weakness of the student.
   */
  public void setWeaknesses(String newWeaknesses) {
    if (this.weaknesses == null) {
      this.weaknesses = newWeaknesses;
    } else {
      this.weaknesses += ", " + newWeaknesses;
    }
  }

  /**
   * Accessor method for the strengths type field of the student.
   * @return the strengths field of the student.
   */
  public String getStrengths() {
    return this.strengths;
  }

  /**
   * Mutator method for the weaknesses type field of the student.
   * @param newStrengths String representing the new strengths of the student.
   */
  public void setStrengths(String newStrengths) {
    if (this.strengths == null) {
      this.strengths = newStrengths;
    } else {
      this.strengths += ", " + newStrengths;
    }
  }

  /**
   * Accessor method for the skill field of the student.
   * @return the skill field of the student.
   */
  public String getSkill() {
    return skill;
  }

  /**
   * Mutator method for the skill field of the student.
   * @param skill String representing the new skill of the student.
   */
  public void setSkill(String skill) {
    this.skill = skill;
  }

  /**
   * Accessor method for the interests field of the student.
   * @return the interests field of the student.
   */
  public String getInterest() {
    return interests;
  }

  /**
   * Mutator method for the interest field of the student.
   * @param interest String representing the new interest of the student.
   */
  public void setInterest(String interest) {
    if (this.interests == null) {
      this.interests = interest;
    } else {
      this.interests += ", " + interest;
    }
  }
  @Override
  public String toString() {
    return "DatabaseStudent{" + "id=" + id + ", name='" + name + '\''
        + ", email='" + email + '\'' + ", weaknesses='" + weaknesses + '\''
        + ", strengths='" + strengths + '\'' + ", skill='" + skill + '\''
        + ", interest='" + interests + '\'' + '}';
  }
  @Override
  public int compareTo(DatabaseStudent o) {
    Integer id1 = this.getId();
    Integer id2 = o.getId();
    return id1.compareTo(id2);
  }
}
