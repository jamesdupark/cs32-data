package edu.brown.cs.student.main.DBParsing.DBItems;

/**
 * Class to represent an individual horoscope in the Horoscopes Database. Upon instantiation,
 * all fields will evaluate to null, which will then be mutated through setters.
 */
public class DatabaseHoroscope {
  /** Integer representing the ID of the horoscope. This serves as a unique
   * identifier for each horoscope. */
  private Integer horoscopeID;
  /** String representing the name of the horoscope. */
  private String horoscope;
  /** Integer representing the ID of the TA. This serves as a unique
   * identifier for each TA. */
  private Integer taID;
  /** String representing the name of the TA. */
  private String taName;
  /** String representing the role of the TA. */
  private String taRole;

  /**
   * Constructor for the DatabaseHoroscope Class — initializes all fields to null.
   */
  public DatabaseHoroscope() {
    this.horoscopeID = null;
    this.horoscope = null;
    this.taID = null;
    this.taName = null;
    this.taRole = null;
  }

  /**
   * Accessor method for the horoscopeID field of the horoscope.
   * @return the horoscopeID field of the horoscope.
   */
  public Integer getHoroscopeID() {
    return horoscopeID;
  }

  /**
   * Mutator method for the horoscopeID field of the horoscope.
   * @param horoscopeID Integer representing the new id of the horoscope.
   */
  public void setHoroscopeID(Integer horoscopeID) {
    this.horoscopeID = horoscopeID;
  }

  /**
   * Accessor method for the horoscope field of the horoscope.
   * @return the horoscope field of the horoscope.
   */
  public String getHoroscope() {
    return horoscope;
  }

  /**
   * Mutator method for the horoscope field of the horoscope.
   * @param horoscope String representing the new name of the horoscope.
   */
  public void setHoroscope(String horoscope) {
    this.horoscope = horoscope;
  }

  /**
   * Accessor method for the taID field of the TA.
   * @return the taID field of the TA.
   */
  public Integer getTaID() {
    return taID;
  }

  /**
   * Mutator method for the taID field of the TA.
   * @param taID Integer representing the new id of the TA.
   */
  public void setTaID(Integer taID) {
    this.taID = taID;
  }

  /**
   * Accessor method for the taName field of the TA.
   * @return the taName field of the TA.
   */
  public String getTaName() {
    return taName;
  }

  /**
   * Mutator method for the taName field of the TA.
   * @param taName String representing the new name of the TA.
   */
  public void setTaName(String taName) {
    this.taName = taName;
  }

  /**
   * Accessor method for the taRole field of the TA.
   * @return the taRole field of the TA.
   */
  public String getTaRole() {
    return taRole;
  }

  /**
   * Mutator method for the taRole field of the TA.
   * @param taRole String representing the new role of the TA.
   */
  public void setTaRole(String taRole) {
    this.taRole = taRole;
  }
  @Override
  public String toString() {
    return "DatabaseHoroscope{" + "horoscopeID=" + horoscopeID + ", horoscope='"
        + horoscope + '\'' + ", taID=" + taID + ", taName='" + taName + '\''
        + ", taRole='" + taRole + '\'' + '}';
  }
}
