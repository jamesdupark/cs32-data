package edu.brown.cs.student.main.DBParsing.DBItems;

/**
 * Class to represent an individual animals in the Zoo Database. Upon instantiation,
 * all fields will evaluate to null, which will then be mutated through setters.
 */
public class DatabaseAnimal {
  /** Integer representing the ID of the animal. This serves as a unique
   * identifier for each animal. */
  private Integer id;
  /** String representing the type and species of the animal. */
  private String animalType;
  /** Integer representing the age of the animal. */
  private Integer age;
  /** Double representing the height of the animal. */
  private Double height;

  /**
   * Constructor for the DatabaseAnimal Class — initializes all fields to null.
   */
  public DatabaseAnimal() {
    this.id = null;
    this.animalType = null;
    this.age = null;
    this.height = null;
  }
}
