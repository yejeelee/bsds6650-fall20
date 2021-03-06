package io.swagger.client;

import io.swagger.client.api.SkiersApi;
import io.swagger.client.model.LiftRide;
import io.swagger.client.model.SkierVertical;
import java.io.IOException;
import java.util.Properties;
import java.util.*;
import java.util.concurrent.CountDownLatch;

public class Phase implements Runnable {
  SkiersApi apiInstance = new SkiersApi();
  Random rand = new Random();
  RequestCounter totalSuccessfulReq;
  CountDownLatch countDown;
  CsvFile file;
  int maxThreads;
  int numThreads;
  int numSkiers;
  int startSkierID;
  int endSkierID;
  int numLifts;
  int startTime;
  int endTime;
  int day;
  String resortID;
  int postReqNum;
  int getReqNum;
  int successfulReq = 0;
  String name;

  Phase (String name, Properties prop, String basePath, int startSkierID, int endSkierID, int startTime, int endTime,
      int postReqNum, int getReqNum, RequestCounter totalSuccessfulReq, CountDownLatch countDown, CsvFile file) {
    this.name = name;
    this.totalSuccessfulReq = totalSuccessfulReq;
    this.maxThreads = Integer.parseInt(prop.getProperty("maxThreads"));
    this.numThreads = maxThreads / 4;
    this.numSkiers = Integer.parseInt(prop.getProperty("numSkiers"));
    this.startSkierID = startSkierID;
    this.endSkierID =  endSkierID;
    this.numLifts = Integer.parseInt(prop.getProperty("numLifts"));
    this.day = Integer.parseInt(prop.getProperty("day"));
    this.resortID = prop.getProperty("resortID");
    this.postReqNum = postReqNum;
    this.getReqNum = getReqNum;
    this.startTime = startTime;
    this.endTime = endTime;
    this.countDown = countDown;
    this.file = file;
    ApiClient client = apiInstance.getApiClient();
    client.setBasePath(basePath);
  }

  public void run() {
    for (int post = 0; post < this.postReqNum; post++) {
      RowComponent row = new RowComponent();
      int randomSkierID = rand.nextInt((endSkierID - startSkierID) + 1) + startSkierID;
      int randomLiftID = rand.nextInt(numLifts + 1) + 1;
      int randomTime = rand.nextInt((endTime - startTime) + 1) + 1;
      LiftRide newLift = new LiftRide();
      newLift.setResortID(resortID);
      newLift.setDayID(Integer.toString(day));
      newLift.setSkierID(Integer.toString(randomSkierID));
      newLift.setTime(Integer.toString(randomTime));
      newLift.setLiftID(Integer.toString(randomLiftID));
      long start = System.currentTimeMillis();
      row.setStartTime(String.valueOf(start));
      row.setRequestType("POST");
      long end;
      int statusCode = 500;
      try {
        ApiResponse response = this.apiInstance.writeNewLiftRideWithHttpInfo(newLift);
        end = System.currentTimeMillis();
        if(response.getStatusCode() == 200) {
          statusCode = 200;
          this.successfulReq++;
        }
      } catch (ApiException e) {
        statusCode = e.getCode();
        end = System.currentTimeMillis();
        System.err.println("Exception when calling SkiersApi#writeNewLiftRide");
        e.printStackTrace();
      }
      row.setLatency(String.valueOf(end - start));
      row.setResponseCode(String.valueOf(statusCode));
      try {
        file.write(row.getRow());
      } catch(IOException e) {
        System.out.println("unable to write to " + this.name + "_result.csv");
      }
    }
    for (int get = 0; get < this.getReqNum; get++) {
      RowComponent row = new RowComponent();
      int randomSkierID = rand.nextInt((endSkierID - startSkierID) + 1) + startSkierID;
      long start = System.currentTimeMillis();
      row.setStartTime(String.valueOf(start));
      row.setRequestType("GET");
      long end;
      int statusCode = 500;
      try {
        ApiResponse<SkierVertical> result = this.apiInstance
            .getSkierDayVerticalWithHttpInfo(resortID, Integer.toString(day),
                Integer.toString(randomSkierID));
        end = System.currentTimeMillis();
        if(result.getStatusCode() == 200) {
          statusCode = result.getStatusCode();
          this.successfulReq++;
        }
      } catch (ApiException e) {
        end = System.currentTimeMillis();
        statusCode = e.getCode();
        System.err.println("Exception when calling SkiersApi#getSkierDayVertical");
        e.printStackTrace();
      }
      row.setLatency(String.valueOf(end - start));
      row.setResponseCode(String.valueOf(statusCode));
      try {
        file.write(row.getRow());
      } catch(IOException e) {
        System.out.println("unable to write to " + this.name + "_result.csv");
      }
    }
    totalSuccessfulReq.incBySpecificAmount(this.successfulReq);
    countDown.countDown();
  }
}
