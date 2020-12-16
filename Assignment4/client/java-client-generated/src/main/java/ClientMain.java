import io.swagger.client.ApiException;
import java.io.IOException;

public class ClientMain {

  public static void main(String[] args) throws IOException, ApiException,InterruptedException {
//    String[] fakeArgs = new String[] {"258", "20000", "40", "1", "SilverMt", "http://localhost:8080/SkierServer_war_exploded/"};
    SkiersApiMultiThread skiersApiThread = new SkiersApiMultiThread(args);
    skiersApiThread.start();
  }

}
