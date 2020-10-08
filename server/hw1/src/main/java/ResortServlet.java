import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "ResortServlet")
public class ResortServlet extends HttpServlet {
  private String errorMsg = "{\n"
      + "  \"message\": \"string\"\n"
      + "}";

  protected void doPost(HttpServletRequest request,
      HttpServletResponse response)
      throws ServletException, IOException {

  }

  //http://localhost:8080/hw1_war_exploded/resort/day/top10vert?resort=bali&dayID=34
  protected void doGet(HttpServletRequest req,
      HttpServletResponse res)
      throws ServletException, IOException {
    res.setContentType("application/json");
    String urlPath = req.getPathInfo();
    String queryString = req.getQueryString();
    System.out.println(urlPath);
    System.out.println(queryString);

    if (urlPath == null || urlPath.length() == 0 ||   queryString.length() == 0) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write(errorMsg);
      return;
    }
    if (urlPath.equals("/day/top10vert")) {
      String ans = "{\n"
          + "  \"topTenSkiers\": [\n"
          + "    {\n"
          + "      \"skierID\": 888899,\n"
          + "      \"VertcialTotal\": 30400\n"
          + "    }\n"
          + "  ]\n"
          + "}";
      res.getWriter().write(ans);
      res.setStatus(HttpServletResponse.SC_OK);
      return;
    } else {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write(errorMsg);
      return;
    }
  }
}
