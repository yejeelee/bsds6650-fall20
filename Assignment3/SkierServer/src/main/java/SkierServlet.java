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
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import java.util.logging.Level;
import java.util.logging.Logger;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {
  private final static String QUEUE_NAME = "task_queue1";
  private String errorMsg = "{\n"
      + "  \"message\": \"string\"\n"
      + "}";
  private String resortId;
  private String skierId;
  private String dayId;
  private String time;
  private String liftId;
  private BasicDataSource dataSource = DBCPDataSource.getDataSource();
//  private Connection conn;
//  private Channel channel;
  private Connection connection;
//  private Channel channel;

  public void init(ServletConfig config) throws ServletException {
    super.init(config);
    System.out.println("hihihi");
    ConnectionFactory factory = new ConnectionFactory();
//    System.out.println("factory");
    factory.setUsername("yejee94");
    factory.setPassword("bsds6650-f20");
    factory.setVirtualHost("/");
    factory.setHost("ec2-3-86-181-178.compute-1.amazonaws.com");
    factory.setPort(5672);
//    factory.setHost("localhost");
//    Connection connection = null;
    try {
      connection = factory.newConnection();
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
//        final Channel channel = connection.createChannel();
        @Override
        public void run() {
          try {
            // comment all println out
            Channel channel = connection.createChannel();
//            channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME, true, false, false, null);
//            System.out.println("hi");
            res.setStatus(HttpServletResponse.SC_OK);
            BufferedReader reader = req.getReader();
            StringBuilder sb = new StringBuilder();
            String ans = reader.lines().collect(Collectors.joining());
            MessageSender sender = new MessageSender();
//            System.out.println("ans: " + ans);
            channel.basicPublish("", QUEUE_NAME, null, ans.getBytes(StandardCharsets.UTF_8));
//            System.out.println(" [x] Sent '" + ans + "'");
//            System.out.println("sent");
            channel.close();
          } catch (Exception e) {
            Logger.getLogger(SkierServlet.class.getName()).log(Level.SEVERE, null, e);
//            e.printStackTrace();
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

//  protected void doPost(HttpServletRequest req, HttpServletResponse res)
//      throws ServletException, IOException {
//
//    res.setContentType("application/json");
//    String urlPath = req.getPathInfo();
//
//    if(urlPath.equals("/liftrides")) {
//      try {
//        channel = connection.createChannel();
//        channel.queueDeclare(QUEUE_NAME, true, false, false, null);
//        System.out.println("hi");
//        res.setStatus(HttpServletResponse.SC_OK);
//        BufferedReader reader = req.getReader();
//        StringBuilder sb = new StringBuilder();
//        String ans = reader.lines().collect(Collectors.joining());
//        MessageSender sender = new MessageSender();
//        System.out.println("ans: " + ans);
//        channel.basicPublish("", QUEUE_NAME, null, ans.getBytes(StandardCharsets.UTF_8));
//        System.out.println(" [x] Sent '" + ans + "'");
//        System.out.println("sent");
//        channel.close();
//      } catch (Exception e) {
//        e.printStackTrace();
//      }
//  }
  //http://54.236.50.254:8080/hw1_war/skiers/liftrides
  //http://localhost:8080/hw1_war_exploded/skiers/342/days/10/skiers/2342
  //http://localhost:8080/hw1_war_exploded/skiers/342/vertical?resort=vali
  //http://54.236.50.254:8080/hw1_war/skiers/342/days/10/skiers/2342
  //http://54.236.50.254:8080/server_war/skiers/10220/vertical?resort=SilverMt

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
    Get get = new Get();
    if (urlParts.length == 6 && queryString == null) {
      try {
        ans = get.getVerticalSpecificDay(urlParts);
      } catch (SQLException e) {
        System.out.println(e);
      }
    } else if (urlParts.length == 3 && queryString != null) {
      try {
        ans = get.getVerticalSpecificResort(urlParts, queryString);
      } catch(SQLException e) {
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
//      System.out.println("GET: " + ans);
    }
    return;
  }

//  private String getVerticalSpecificDay(String[] urlPath) throws SQLException {
//    if (urlPath[2].equals("days")) {
//      if (!isInt(urlPath[3])) {
//        return null;
//      }
//      if (Integer.parseInt(urlPath[3]) < 1 || Integer.parseInt(urlPath[3]) > 366) {
//        return null;
//      }
//      this.dayId = urlPath[3];
//      this.resortId = urlPath[1];
//      if (urlPath[4].equals("skiers")) {
//        this.skierId = urlPath[5];
//        BasicDataSource dataSource = DBCPDataSource.getDataSource();
//        PreparedStatement preparedStatement = null;
//        ResultSet result= null;
//        int vertical = 0;
//        Connection con =null;
//        try {
//          con = dataSource.getConnection();
//          preparedStatement = con.prepareStatement("SELECT sum(vertical) FROM LiftRides.LiftRides"
//              + " WHERE skierID=? AND dayID=? AND resortID=?");
//          preparedStatement.setString(1, this.skierId);
//          preparedStatement.setString(2, this.dayId);
//          preparedStatement.setString(3, this.resortId);
//          result = preparedStatement.executeQuery();
//          while(result.next()) {
//            vertical = result.getInt("sum(vertical)");
//          }
//          return "{\n"
//              + "  \"resortID\": \"" + this.resortId + "\",\n"
//              + "  \"totalVert\": \"" + vertical + "\" \n"
//              + "}";
//
//        } catch (Exception e) {
//
//        } finally {
//          if (con != null) {
//            con.close();
//          }
//          if (preparedStatement != null) {
//            preparedStatement.close();
//          }
//        }
//      }
//    }
//    return null;
//  }
//
//  private String getVerticalSpecificResort(String[] urlPath, String queryParam) throws SQLException {
//    if (urlPath[2].equals("vertical")) {
//      if (queryParam.contains("resort=")) {
//        String[] resortPath = queryParam.split("=");
//        this.skierId = urlPath[1];
//        this.resortId = resortPath[1];
//        BasicDataSource dataSource = DBCPDataSource.getDataSource();
//        PreparedStatement preparedStatement = null;
//        ResultSet result= null;
//        int vertical = 0;
//        Connection con =null;
//        try {
//          con = dataSource.getConnection();
//          preparedStatement = con.prepareStatement("SELECT sum(vertical) FROM LiftRides.LiftRides"
//              + " WHERE skierID=? AND resortID=?");
//          preparedStatement.setInt(1, Integer.parseInt(this.skierId));
//          preparedStatement.setString(2, this.resortId);
//          result = preparedStatement.executeQuery();
//          while(result.next()) {
//            vertical = result.getInt("sum(vertical)");
//          }
//          return "{\n"
//              + "  \"resortID\": \"" + this.resortId + "\",\n"
//              + "  \"totalVert\": \"" + vertical + "\"\n"
//              + "}";
//        } catch (Exception e) {
//
//        } finally {
//          if (con != null) {
//            con.close();
//          }
//          if (preparedStatement != null) {
//            preparedStatement.close();
//          }
//        }
//
//      }
//    }
//    return null;
//  }
//
//  private boolean isInt(String str) {
//    try {
//      int path = Integer.parseInt(str);
//    } catch (Exception e) {
//      return false;
//    }
//    return true;
//  }
}

