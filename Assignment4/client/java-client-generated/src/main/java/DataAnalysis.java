import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Stream;

public class DataAnalysis {
  public List<List<String>> datas;

  public DataAnalysis(String filePath) throws FileNotFoundException, IOException {
    this.datas = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
      String line;
      while ((line = br.readLine()) != null) {
        String[] values = line.split(",");
        datas.add(Arrays.asList(values));
      }
    }
  }

  public void analyze() throws IOException {
    List<Integer> totalPostTime = new ArrayList<>();
    List<Integer> totalGet1Time = new ArrayList<>();
    List<Integer> totalGet2Time = new ArrayList<>();

    for (int i = 0; i < datas.size(); i++) {
      if (datas.get(i).get(1).equals("POST")) {
        totalPostTime.add(Integer.parseInt(datas.get(i).get(2)));
      }
      if (datas.get(i).get(1).equals("GET1")) {
        totalGet1Time.add(Integer.parseInt(datas.get(i).get(2)));
      }
      if (datas.get(i).get(1).equals("GET2")) {
        totalGet2Time.add(Integer.parseInt(datas.get(i).get(2)));
      }
    }
    List<Integer> total = new ArrayList<>();
    Stream.of(totalPostTime, totalGet1Time, totalGet2Time).forEach(total::addAll);
    System.out.println("Mean for POSTs (milliseconds): " + findMean(totalPostTime));
    System.out.println("Mean for GETs#1 (milliseconds): " + findMean(totalGet1Time));
    System.out.println("Mean for GETs#2 (milliseconds): " + findMean(totalGet2Time));
    System.out.println("Mean for All Response time (milliseconds): " + findMean(total));
    System.out.println("Median for POSTs (milliseconds): " + findMedian(totalPostTime));
    System.out.println("Median for GETs#1 (milliseconds): " + findMedian(totalGet1Time));
    System.out.println("Median for GETs#2 (milliseconds): " + findMedian(totalGet2Time));
    System.out.println("max for POSTs (milliseconds): " + findMax(totalPostTime));
    System.out.println("max for GETs#1 (milliseconds): " + findMax(totalGet1Time));
    System.out.println("max for GETs#2 (milliseconds): " + findMax(totalGet2Time));
    Arrays.sort(totalPostTime.toArray());
    Arrays.sort(totalGet1Time.toArray());
    Arrays.sort(totalGet2Time.toArray());
    System.out.println("99th percentile for POST (milliseconds): " + totalPostTime.get((int)0.01 * totalPostTime.size()));
    System.out.println("99th percentile for GET#1 (milliseconds): " + totalGet1Time.get((int)0.01 * totalGet1Time.size()));
    System.out.println("99th percentile for GET#2 (milliseconds): " + totalGet2Time.get((int)0.01 * totalGet2Time.size()));
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
