package edu.brown.cs.student.main.DBParsing;

public class DatabaseStudent {
  private Integer id;
  private String name;
  private String email;
  private String attrType;
  private String trait;
  private String skill;
  private String interest;

  public DatabaseStudent() {
    id = null;
    name = null;
    email = null;
    attrType = null;
    trait = null;
    skill = null;
    interest = null;
  }

  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getAttrType() {
    return attrType;
  }

  public void setAttrType(String attrType) {
    this.attrType = attrType;
  }

  public String getTrait() {
    return trait;
  }

  public void setTrait(String trait) {
    this.trait = trait;
  }

  public String getSkill() {
    return skill;
  }

  public void setSkill(String skill) {
    this.skill = skill;
  }

  public String getInterest() {
    return interest;
  }

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
