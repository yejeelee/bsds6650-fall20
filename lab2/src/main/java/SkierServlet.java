import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "SkierServlet")
public class SkierServlet extends HttpServlet {

  protected void doPost(HttpServletRequest req, HttpServletResponse res)
      throws ServletException, IOException {

  }

  protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
    res.setContentType("text/plain");
    String urlPath = req.getPathInfo();

    // check we have a URL!
    if (urlPath == null || urlPath.length() == 0) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
      res.getWriter().write("missing paramterers");
      return;
    }

    String[] urlParts = urlPath.split("/");
    // and now validate url path and return the response status code
    // (and maybe also some value if input is valid)
    if (!isUrlValid(urlParts)) {
      res.setStatus(HttpServletResponse.SC_NOT_FOUND);
    } else {
      res.setStatus(HttpServletResponse.SC_OK);
      // do any sophisticated processing with urlParts which contains all the url params
      // TODO: process url params in `urlParts`
      res.getWriter().write("It works!");
    }
  }

  private boolean isUrlValid(String[] urlPath) {
    // TODO: validate the request url path according to the API spec
    // urlPath  = "/1/seasons/2019/day/1/skier/123"
    // urlParts = [, 1, seasons, 2019, day, 1, skier, 123]
    return validateSkierUrl(urlPath);
  }

  private boolean validateResortUrl(String[] urlPath, Integer start) {
    if (isInt(urlPath[start])) {
      if(urlPath[start + 1] == "seasons") {
        return true;
      }
    }
    return false;
  }

  private boolean isInt(String str) {
    try {
      int path = Integer.parseInt(str);
    } catch (Exception e) {
      return false;
    }
    return true;
  }

  private boolean validateSkierUrl(String[] urlPath) {
    if (isInt(urlPath[1])) {
      if (urlPath[2].equals("vertical")) {
        return (urlPath.length == 3);
      }
      if(urlPath[2].equals("seasons")) {
        if (!(urlPath[3] instanceof String) || urlPath[3].length() != 4) {
          return false;
        }
        if(urlPath[4].equals("days")) {
          if(!isInt(urlPath[5])) {
            return false;
          }
          if (Integer.parseInt(urlPath[5]) < 1 || Integer.parseInt(urlPath[5]) > 366) {
            return false;
          }
        }
        if (urlPath[6].equals("skiers")) {
          if (!isInt(urlPath[7])) {
            return false;
          }
          return true;
        }
      }
    }
    return false;
  }
}


