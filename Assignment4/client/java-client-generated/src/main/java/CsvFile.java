import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

public class CsvFile {
  FileWriter csvWriter;

  CsvFile(String filePath) throws IOException {
    csvWriter = new FileWriter(filePath);
    //csvWriter = new FileWriter("/Users/yejeelee/Desktop/Assignment2/client/java-client-generated/src/main/java/io/swagger/client/result.csv");
    csvWriter.append("StartTime");
    csvWriter.append(",");
    csvWriter.append("RequestType");
    csvWriter.append(",");
    csvWriter.append("Latency");
    csvWriter.append(",");
    csvWriter.append("ResponseCode");
    csvWriter.append("\n");
  }

  synchronized public void write (List<String> row ) throws IOException {
      csvWriter.append(String.join(",", row));
      csvWriter.append("\n");
    }


  synchronized public void close() throws IOException {
    csvWriter.close();
  }

}
