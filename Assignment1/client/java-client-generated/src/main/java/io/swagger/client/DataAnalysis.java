package io.swagger.client;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class DataAnalysis {
  public List<List<String>> datas;

  public DataAnalysis() throws FileNotFoundException, IOException {
    this.datas = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader("/Users/yejeelee/Desktop/java-client-generated/src/main/java/io/swagger/client/result.csv"))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        datas.add(Arrays.asList(values));
      }
    }
  }

  public void analyze() throws IOException {
    List<Integer> totalPostTime = new ArrayList<>();
    List<Integer> totalGetTime = new ArrayList<>();

    for (int i = 0; i < datas.size(); i++) {
      if (datas.get(i).get(1).equals("POST")) {
        totalPostTime.add(Integer.parseInt(datas.get(i).get(2)));
      }
      if (datas.get(i).get(1).equals("GET")) {
        totalGetTime.add(Integer.parseInt(datas.get(i).get(2)));
      }
    }
    System.out.println("Mean for POSTs (milliseconds): " + findMean(totalPostTime));
    System.out.println("Mean for GETs (milliseconds): " + findMean(totalGetTime));
    System.out.println("Median for POSTs (milliseconds): " + findMedian(totalPostTime));
    System.out.println("Median for GETs (milliseconds): " + findMedian(totalGetTime));
    System.out.println("max for POSTs (milliseconds): " + findMax(totalPostTime));
    System.out.println("max for GETs (milliseconds): " + findMax(totalGetTime));
    Arrays.sort(totalPostTime.toArray());
    Arrays.sort(totalGetTime.toArray());
    System.out.println("99th percentile for POST (milliseconds): " + totalPostTime.get((int)0.01 * totalPostTime.size()));
    System.out.println("99th percentile for GET (milliseconds): " + totalGetTime.get((int)0.01 * totalGetTime.size()));
  }

  double findMean(List<Integer> lst) {
    int sum = 0;
    for (int i = 0; i < lst.size(); i++)
      sum += lst.get(i);

    return (double)sum/(double)lst.size();
  }

  int findMax(List<Integer> lst) {
    int max = 0;
    for (int i = 0; i < lst.size(); i++) {
      if (lst.get(i) > max) {
        max = lst.get(i);
      }
    }
    return max;
  }

  double findMedian(List<Integer> lst) {
    Arrays.sort(lst.toArray());
    if (lst.size() % 2 != 0) {
      return (double)lst.get(lst.size()/2);
    }
    return (double) (lst.get((int)(lst.size() - 1)/2) + lst.get((int)lst.size()/2))/2.0;

  }

}
