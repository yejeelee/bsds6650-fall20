import java.util.ArrayList;
import java.util.List;

public class RowComponent {
  private String startTime;
  private String requestType;
  private String latency;
  private String responseCode;

  synchronized public void setStartTime(String time) {
    this.startTime = time;
  }
  synchronized public void setRequestType(String type) {
    this.requestType = type;
  }
  synchronized public void setLatency(String latency) {
    this.latency = latency;
  }
  synchronized public void setResponseCode(String statusCode) {
    this.responseCode = statusCode;
  }

  synchronized public ArrayList<String> getRow() {
    ArrayList<String> row = new ArrayList<>();
    row.add(startTime);
    row.add(requestType);
    row.add(latency);
    row.add(responseCode);
    return row;
  }

}
