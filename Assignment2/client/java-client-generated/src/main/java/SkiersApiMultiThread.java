import io.swagger.client.RequestCounter;
import java.util.concurrent.CountDownLatch;
import java.io.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class SkiersApiMultiThread {

  final RequestCounter successfulReqNum;
  private int maxThread;
  private Integer numSkiers;
  private Integer numLifts;
  private Integer day;
  private String resortID;
  private Property property;
  private String basePath;

//  private String basePath = "http://54.236.50.254:8080/SkierServer_war";
  //a2-892017696.us-east-1.elb.amazonaws.com/SkierServer_war
  private String filePath = "/var/lib/tomcat/webapps/result.csv";
  //private String filePath = "/Users/yejeelee/Desktop/Assignment2/client/java-client-generated/src/main/java/result.csv";

  SkiersApiMultiThread(String[] args) throws IOException {
    this.maxThread = Integer.parseInt(args[0]);
    this.numSkiers = Integer.parseInt(args[1]);
    this.numLifts = Integer.parseInt(args[2]);
    this.day = Integer.parseInt(args[3]);
    this.resortID = args[4];
    this.basePath = args[5];
    property = new Property(maxThread, numSkiers, numLifts, day, resortID);

//    this.prop = ReadPropertiesFile.readPropertiesFile("/Users/yejeelee/Desktop/Assignment2/client/java-client-generated/src/main/java/io/swagger/client/param.properties");
    this.successfulReqNum = new RequestCounter();
  }

   public void start() throws IOException, InterruptedException {
    System.out.println("----START-----");
    int totalReqNum = 0;
//    int numSkiers = Integer.parseInt(this.prop.getProperty("numSkiers"));
    int phase1_numThreads = (int) this.maxThread / 4;
    int phase2_numThreads = (int) this.maxThread;
    int phase3_numThreads = (int) this.maxThread / 4;

    ExecutorService totalThreads = Executors.newFixedThreadPool(phase1_numThreads + phase2_numThreads + phase3_numThreads);
    long startTime = System.currentTimeMillis();
    CountDownLatch phase1_countDown = new CountDownLatch((int) Math.ceil(phase1_numThreads * 0.1));
    CsvFile csvFile = new CsvFile(filePath);
    totalReqNum += phase1_numThreads * (1005);
    int startSkierID = 1;
    int endSkierID = (int) numSkiers / phase1_numThreads;
    for (int i = 0; i < phase1_numThreads; i++) {
      int range = endSkierID - startSkierID + 1;

      totalThreads.execute(new Phase("phase1", this.property, this.basePath, startSkierID,endSkierID,1, 90,
          1000, 5, this.successfulReqNum, phase1_countDown, csvFile, false));
      startSkierID+=range;
      endSkierID+=range;
    }
    phase1_countDown.await();
    System.out.println("PHASE1: 10% completed");

    CountDownLatch phase2_countDown = new CountDownLatch((int) Math.ceil(phase2_numThreads * 0.1));
    totalReqNum += phase2_numThreads * (1005);
    startSkierID = 1;
    endSkierID = (int) numSkiers / phase2_numThreads;
    for (int j = 0; j <phase2_numThreads; j++) {
      int range = endSkierID - startSkierID + 1;
      totalThreads.execute(new Phase("phase2", this.property, this.basePath, startSkierID, endSkierID, 91, 360,
          1000, 5, this.successfulReqNum, phase2_countDown, csvFile, false));
      startSkierID+=range;
      endSkierID+=range;
    }
    phase2_countDown.await();
     System.out.println("PHASE2: 10% completed");

    CountDownLatch phase3_countDown = new CountDownLatch(phase3_numThreads);
    totalReqNum += phase3_numThreads * (1020);
    startSkierID = 1;
    endSkierID = (int) numSkiers / phase3_numThreads;
    for (int k = 0; k < phase3_numThreads; k++) {
      int range = endSkierID - startSkierID + 1;
      totalThreads.execute(new Phase("phase3", this.property, this.basePath, startSkierID, endSkierID, 361, 420,
          1000, 10, this.successfulReqNum, phase3_countDown, csvFile, true));
      startSkierID+=range;
      endSkierID+=range;
    }
    phase3_countDown.await();

    totalThreads.shutdown();
    System.out.println("All Threads completed!");
    try {
      totalThreads.awaitTermination(Long.MAX_VALUE, TimeUnit.HOURS);
    } catch (InterruptedException e) {
      System.err.println(e);
    }

    long wallTime = System.currentTimeMillis() - startTime;

    System.out.println("===================================");
    System.out.println("Client Part 1");
    System.out.println("total req: " + totalReqNum);
    System.out.println("Succesful req: " + this.successfulReqNum.getVal());
    System.out.println("wall time (milliseconds): " + wallTime);
    System.out.println("throughput (seconds) : " + totalReqNum / (wallTime * 0.001));
    csvFile.close();

    System.out.println("===================================");
    System.out.println("Client Part 2");
    DataAnalysis da = new DataAnalysis(filePath);
    da.analyze();
    wallTime = System.currentTimeMillis() - startTime;
     System.out.println("Total Wall Time (milliseconds): " + wallTime);
     System.out.println("throughput: (totalRequest / wallTime (Seconds)): " + totalReqNum / (wallTime * 0.001));
  }
}