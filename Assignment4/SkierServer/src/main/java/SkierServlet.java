import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.IOException;
//import java.sql.Connection;
import java.nio.charset.StandardCharsets;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp2.BasicDataSource;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConnectionFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {

  private final static String QUEUE_NAME = "task_queue1";
  private String errorMsg = "{\n"
      + "  \"message\": \"string\"\n"
      + "}";
  private BasicDataSource dataSource = DBCPDataSource.getDataSource();
  private com.rabbitmq.client.Connection rabbitMQConnection;
  private java.sql.Connection dbConnection;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUsername("yejee94");
    factory.setPassword("bsds6650-f20");
    factory.setVirtualHost("/");
    factory.setHost("ec2-54-205-30-25.compute-1.amazonaws.com");
    factory.setPort(5672);
    try {
      rabbitMQConnection = factory.newConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      dbConnection = dataSource.getConnection();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    res.setContentType("application/json");
    String urlPath = req.getPathInfo();

    if (urlPath.equals("/liftrides")) {
      Runnable runnable = new Runnable() {
        @Override
        public void run() {
          try {
            Channel channel = rabbitMQConnection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
            res.setStatus(HttpServletResponse.SC_OK);
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String ans = reader.lines().collect(Collectors.joining());
            channel.basicPublish("", QUEUE_NAME, null, ans.getBytes(StandardCharsets.UTF_8));
            channel.close();
          } catch (Exception e) {
            Logger.getLogger(SkierServlet.class.getName()).log(Level.SEVERE, null, e);
          }
        }
      };
      Thread t1 = new Thread(runnable);
      t1.start();
      try {
        t1.join();
      } catch (InterruptedException e) {
        System.err.println(e);
      }
    }
  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType("application/json");
    String urlPath = req.getPathInfo();

    if (urlPath == null || urlPath.length() == 0) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write(errorMsg);
      return;
    }
    String queryString = req.getQueryString();
    String[] urlParts = urlPath.split("/");
    String ans = null;
    Get get = new Get(dbConnection);
    if (urlParts.length == 6 && queryString == null) {
      try {
        ans = get.getVerticalSpecificDay(urlParts);
      } catch (SQLException e) {
        System.out.println(e);
      }
    } else if (urlParts.length == 3 && queryString != null) {
      try {
        ans = get.getVerticalSpecificResort(urlParts, queryString);
      } catch (SQLException e) {
        System.out.println(e);
      }
    } else {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write(errorMsg);
      return;
    }
    if (ans == null) {
      res.setStatus(HttpServletResponse.SC_BAD_REQUEST);
      res.getWriter().write(errorMsg);
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      res.getWriter().write(ans);
    }
    return;
  }

}

//      ans = "{\n"
//          + "  \"resortID\": \"" + "SilverMt" + "\",\n"
//          + "  \"totalVert\": \"" + 0 + "\" \n"
//          + "}";
//          res.setStatus(HttpServletResponse.SC_OK);
//          res.getWriter().write(ans);
