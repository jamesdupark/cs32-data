package edu.brown.cs.student.main.DBParsing.DBItems;

/**
 * Class to represent an individual student in the Student Database. Upon instantiation,
 * all fields will evaluate to null, which will then be mutated through setters.
 */
public class DatabaseStudent {
  /** Integer representing the ID of the student. This serves as a unique
   * identifier for each student. */
  private Integer id;
  /** String representing the name of the student. */
  private String name;
  /** String representing the email of the student. */
  private String email;
  /** String representing whether the following trait is a strength or weakness
   * of the student. */
  private String attrType;
  /** String representing the trait of the student. */
  private String trait;
  /** String representing the skill of the student. */
  private String skill;
  /** String representing the interest of the student. */
  private String interest;

  /**
   * Constructor for the DatabaseStudent Class — initializes all fields to null.
   */
  public DatabaseStudent() {
    this.id = null;
    this.name = null;
    this.email = null;
    this.attrType = null;
    this.trait = null;
    this.skill = null;
    this.interest = null;
  }

  /**
   * Accessor method for the id field of the student.
   * @return the id field of the student.
   */
  public Integer getId() {
    return id;
  }

  /**
   * Mutator method for the id field of the student.
   * @param id Integer representing the new id of the student.
   */
  public void setId(Integer id) {
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
   * Accessor method for the attribute type field of the student.
   * @return the attrType field of the student.
   */
  public String getAttrType() {
    return attrType;
  }

  /**
   * Mutator method for the attribute type field of the student.
   * @param attrType String representing the new attribute type of the student.
   */
  public void setAttrType(String attrType) {
    this.attrType = attrType;
  }

  /**
   * Accessor method for the trait field of the student.
   * @return the trait field of the student.
   */
  public String getTrait() {
    return trait;
  }

  /**
   * Mutator method for the trait field of the student.
   * @param trait String representing the new trait of the student.
   */
  public void setTrait(String trait) {
    this.trait = trait;
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
   * Accessor method for the interest field of the student.
   * @return the interest field of the student.
   */
  public String getInterest() {
    return interest;
  }

  /**
   * Mutator method for the interest field of the student.
   * @param interest String representing the new interest of the student.
   */
  public void setInterest(String interest) {
    this.interest = interest;
  }
  @Override
  public String toString() {
    return "DatabaseStudent{" + "id=" + id + ", name='" + name + '\''
        + ", email='" + email + '\'' + ", attrType='" + attrType + '\''
        + ", trait='" + trait + '\'' + ", skill='" + skill + '\''
        + ", interest='" + interest + '\'' + '}';
  }
}
