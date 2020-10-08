package io.swagger.client;
import java.util.concurrent.CountDownLatch;
import java.io.*;
import java.util.*;

public class SkiersApiMultiThread {

  final RequestCounter successfulReqNum;
  final Properties prop;
  final CsvFile file;

  //private String basePath = "http://54.236.50.254:8080/hw1_war";
  public String basePath = "http://localhost:8080/hw1_war_exploded";


  SkiersApiMultiThread() throws IOException {
    this.prop = ReadPropertiesFile.readPropertiesFile("/Users/yejeelee/Desktop/java-client-generated/src/main/java/io/swagger/client/param.properties");
    this.successfulReqNum = new RequestCounter();
    this.file = new CsvFile();
  }

   public void start() throws IOException, InterruptedException {
    int totalReqNum = 0;
    int numSkiers = Integer.parseInt(this.prop.getProperty("numSkiers"));

    int phase1_numThreads = (int) Integer.parseInt(this.prop.getProperty("maxThreads")) / 4;
    int phase2_numThreads = (int) Integer.parseInt(this.prop.getProperty("maxThreads"));
    int phase3_numThreads = (int) Integer.parseInt(this.prop.getProperty("maxThreads")) / 4;

    long startTime = System.currentTimeMillis();
    CountDownLatch phase1_countDown = new CountDownLatch((int) Math.ceil(phase1_numThreads * 0.1));
    CsvFile csvFile = new CsvFile();
    totalReqNum += phase1_numThreads * (105);
    int startSkierID = 1;
    int endSkierID = (int) numSkiers / phase1_numThreads;
    for (int i = 0; i < phase1_numThreads; i++) {
      int range = endSkierID - startSkierID + 1;
      new Thread(new Phase("phase1", this.prop, this.basePath, startSkierID,endSkierID,1, 90,
          100, 5, this.successfulReqNum, phase1_countDown, csvFile)).start();
      startSkierID+=range;
      endSkierID+=range;
    }
    phase1_countDown.await();

    CountDownLatch phase2_countDown = new CountDownLatch((int) Math.ceil(phase2_numThreads * 0.1));
    totalReqNum += phase2_numThreads * (105);
    startSkierID = 1;
    endSkierID = (int) numSkiers / phase2_numThreads;
    for (int j = 0; j <phase2_numThreads; j++) {
      int range = endSkierID - startSkierID + 1;
      new Thread(new Phase("phase2", this.prop, this.basePath, startSkierID, endSkierID, 91, 360,
          100, 5, this.successfulReqNum, phase2_countDown, csvFile)).start();
      startSkierID+=range;
      endSkierID+=range;
    }
    phase2_countDown.await();

    CountDownLatch phase3_countDown = new CountDownLatch(phase3_numThreads);
    totalReqNum += phase3_numThreads * (110);
    startSkierID = 1;
    endSkierID = (int) numSkiers / phase3_numThreads;
    for (int k = 0; k < phase3_numThreads; k++) {
      int range = endSkierID - startSkierID + 1;
      new Thread(new Phase("phase3", this.prop, this.basePath, startSkierID, endSkierID, 361, 420,
          100, 10, this.successfulReqNum, phase3_countDown, csvFile)).start();
      startSkierID+=range;
      endSkierID+=range;
    }
    phase3_countDown.await();

    long wallTime = System.currentTimeMillis() - startTime;

    System.out.println("===================================");
    System.out.println("Client Part 1");
    System.out.println("total req: " + totalReqNum);
    System.out.println("Succesful req: " + this.successfulReqNum.getVal());
    System.out.println("wall time (milliseconds): " + wallTime);
    System.out.println("throughput: " + totalReqNum / wallTime);
    csvFile.close();

    System.out.println("===================================");
    System.out.println("Client Part 2");
    DataAnalysis da = new DataAnalysis();
    da.analyze();
    wallTime = System.currentTimeMillis() - startTime;
     System.out.println("Total Wall Time (milliseconds): " + wallTime);
     System.out.println("throughput: " + totalReqNum / wallTime);
  }
}