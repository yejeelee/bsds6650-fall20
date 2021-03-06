/*
 * Ski Data API for NEU Seattle distributed systems course
 * An API for an emulation of skier managment system for RFID tagged lift tickets. Basis for CS6650 Assignments for 2019
 *
 * OpenAPI spec version: 1.13
 *
 *
 * NOTE: This class is auto generated by the swagger code generator program.
 * https://github.com/swagger-api/swagger-codegen.git
 * Do not edit the class manually.
 */


import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import java.util.Objects;

/**
 * LiftRide
 */

@javax.annotation.Generated(value = "io.swagger.codegen.v3.generators.java.JavaClientCodegen", date = "2020-09-30T07:55:59.550Z[GMT]")
@DynamoDBTable(tableName="LiftRides")
public class LiftRide {
  private String resortID = null;
  private String dayID = null;
  private String skierID = null;
  private String time = null;
  private String liftID = null;
  private int vertical = 0;

  public LiftRide(String skierID, String resortID, String dayID, String time, String liftID) {
    this.skierID = skierID;
    this.resortID = resortID;
    this.dayID = dayID;
    this.time = time;
    this.liftID = liftID;

  }

  /**
   * Get resortID
   * @return resortID
   **/
  @DynamoDBHashKey(attributeName="Artist")
  public String getResortID() {
    return resortID;
  }

  public void setResortID(String resortID) {
    this.resortID = resortID;
  }

  /**
   * Get dayID
   * @return dayID
   **/
  public String getDayID() {
    return dayID;
  }

  public void setDayID(String dayID) {
    this.dayID = dayID;
  }


  /**
   * Get skierID
   * @return skierID
   **/
  public String getSkierID() {
    return skierID;
  }

  public void setSkierID(String skierID) {
    this.skierID = skierID;
  }

  /**
   * Get time
   * @return time
   **/
  public String getTime() {
    return time;
  }

  public void setTime(String time) {
    this.time = time;
  }

  /**
   * Get liftID
   * @return liftID
   **/
  public String getLiftID() {
    return liftID;
  }

  public void setLiftID(String liftID) {
    this.liftID = liftID;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LiftRide liftRide = (LiftRide) o;
    return Objects.equals(this.resortID, liftRide.resortID) &&
        Objects.equals(this.dayID, liftRide.dayID) &&
        Objects.equals(this.skierID, liftRide.skierID) &&
        Objects.equals(this.time, liftRide.time) &&
        Objects.equals(this.liftID, liftRide.liftID);
  }

  @Override
  public int hashCode() {
    return Objects.hash(resortID, dayID, skierID, time, liftID);
  }


  @Override
  public String toString() {
    StringBuilder sb = new StringBuilder();
    sb.append("class LiftRide {\n");

    sb.append("    resortID: ").append(toIndentedString(resortID)).append("\n");
    sb.append("    dayID: ").append(toIndentedString(dayID)).append("\n");
    sb.append("    skierID: ").append(toIndentedString(skierID)).append("\n");
    sb.append("    time: ").append(toIndentedString(time)).append("\n");
    sb.append("    liftID: ").append(toIndentedString(liftID)).append("\n");
    sb.append("}");
    return sb.toString();
  }

  /**
   * Convert the given object to string with each line indented by 4 spaces
   * (except the first line).
   */
  private String toIndentedString(Object o) {
    if (o == null) {
      return "null";
    }
    return o.toString().replace("\n", "\n    ");
  }

}
