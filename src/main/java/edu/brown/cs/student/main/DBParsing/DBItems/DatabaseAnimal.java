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

  /**
   * Accessor method for the id field of the animal.
   * @return the id field of the animal
   */
  public Integer getId() {
    return id;
  }

  /**
   * Mutator method for the id field of the animal.
   * @param id Integer representing the new id of the animal.
   */
  public void setId(Integer id) {
    this.id = id;
  }

  /**
   * Accessor method for the animal type field of the animal.
   * @return the animalType field of the animal.
   */
  public String getAnimalType() {
    return animalType;
  }

  /**
   * Mutator method for the animal type field of the animal.
   * @param animalType String representing the new animal type of the animal.
   */
  public void setAnimalType(String animalType) {
    this.animalType = animalType;
  }

  /**
   * Accessor method for the age field of the animal.
   * @return the age field of the animal.
   */
  public Integer getAge() {
    return age;
  }

  /**
   * Mutator method for the age field of the animal.
   * @param age Integer representing the new age of the animal.
   */
  public void setAge(Integer age) {
    this.age = age;
  }

  /**
   * Accessor method for the height field of the animal.
   * @return the height field of the animal.
   */
  public Double getHeight() {
    return height;
  }

  /**
   * Mutator method for the height field of the animal.
   * @param height Integer representing the new height of the animal.
   */
  public void setHeight(Double height) {
    this.height = height;
  }
  @Override
  public String toString() {
    return "DatabaseAnimal{" + "id=" + id + ", animalType='" + animalType
        + '\'' + ", age=" + age + ", height=" + height + '}';
  }
}
