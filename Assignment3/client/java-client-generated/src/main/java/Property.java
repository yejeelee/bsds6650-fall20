public class Property {

  private Integer maxThreads;
  private Integer numSkiers;
  private Integer numLifts;
  private Integer day;
  private String resortID;

  public Property(Integer maxThreads, Integer numSkiers, Integer numLifts, Integer day, String resortID) {
    this.maxThreads = maxThreads;
    this.numSkiers = numSkiers;
    this.numLifts = numLifts;
    this.day = day;
    this.resortID = resortID;
  }

  public Integer getMaxThreads() {
    return this.maxThreads;
  }

  public Integer getNumSkiers() {
    return this.numSkiers;
  }

  public Integer getNumLifts() {
    return this.numLifts;
  }

  public Integer getDay() {
    return day;
  }

  public String getResortID() {
    return resortID;
  }
}
