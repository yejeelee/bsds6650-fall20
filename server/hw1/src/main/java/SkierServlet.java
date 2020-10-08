import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.stream.Collectors;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {
  private String errorMsg = "{\n"
      + "  \"message\": \"string\"\n"
      + "}";

  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

    res.setContentType("application/json");
    String urlPath = req.getPathInfo();

    if(urlPath.equals("/liftrides")) {

      res.setStatus(HttpServletResponse.SC_OK);
//      JSONObject json = new JSONObject();
//      json.put("resortID", "Mission Ridge");
//      json.put("dayID", "23");
//      json.put("skierID", "7899");
//      json.put("time", "217");
//      json.put("liftID", "21");
      BufferedReader reader = req.getReader();
      String ans = reader.lines().collect(Collectors.joining());
      System.out.println(ans);

//      String resortID = req.getParameter("resortID");
//      String dayID = req.getParameter("dayID");
//      String skierID = req.getParameter("skierID");
//      String time = req.getParameter("time");
//      String liftID = req.getParameter("liftID");
//      System.out.println("resortID " + resortID);

//      String ans = "{\n"
//          + "  \"resortID\": \""+ resortID + "\",\n"
//          + "  \"dayID\": " + dayID + ",\n"
//          + "  \"skierID\": " + skierID + ",\n"
//          + "  \"time\": " + time + ",\n"
//          + "  \"liftID\": " + liftID + "\n"
//          + "}";
//      String ans = "{\n"
//          + "  \"resortID\": \"Mission Ridge\",\n"
//          + "  \"dayID\": 23,\n"
//          + "  \"skierID\": 7889,\n"
//          + "  \"time\": 217,\n"
//          + "  \"liftID\": 21\n"
//          + "}";
      res.getWriter().write(ans);

    } else {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write(errorMsg);
    }
  }
  //http://54.236.50.254:8080/hw1_war/skiers/liftrides
  //http://localhost:8080/hw1_war_exploded/skiers/342/days/10/skiers/2342
  //http://localhost:8080/hw1_war_exploded/skiers/342/vertical?resort=vali
  //http://54.236.50.254:8080/hw1_war/skiers/342/days/10/skiers/2342

  protected void doGet(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {
//    res.setContentType("application/json;charset=UTF-8");
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
    if (urlParts.length == 6 && queryString == null) {
      ans = getVerticalSpecificDay(urlParts);
    } else if (urlParts.length == 3 && queryString != null) {
      ans = getVerticalSpecificResort(urlParts, queryString);
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

  private String getVerticalSpecificDay(String[] urlPath) {
    if (urlPath[2].equals("days")) {
      if (!isInt(urlPath[3])) {
        return null;
      }
      if (Integer.parseInt(urlPath[3]) < 1 || Integer.parseInt(urlPath[3]) > 366) {
        return null;
      }
      if (urlPath[4].equals("skiers")) {
        return "{\n"
            + "  \"resortID\": \"" + urlPath[1] + "\",\n"
            + "  \"totalVert\": 56734\n"
            + "}";
      }
    }
    return null;
  }

  private String getVerticalSpecificResort(String[] urlPath, String queryParam) {
    if (urlPath[2].equals("vertical")) {
      if (queryParam.contains("resort=")) {
        String[] resortPath = queryParam.split("=");
//        return "{ \"resortID\": " + "\"" + resortPath[1] +"\"" + ", \"totalVert\": " +"\"" + "\"56734\" }" ;
        return "{\n"
            + "  \"resortID\": \"" + resortPath[1] + "\",\n"
            + "  \"totalVert\": 56734\n"
            + "}";
      }
    }
    return null;
  }

  private boolean isInt(String str) {
    try {
      int path = Integer.parseInt(str);
    } catch (Exception e) {
      return false;
    }
    return true;
  }
}

