import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class Get {
  private String dayId;
  private String resortId;
  private String skierId;

  public String getVerticalSpecificDay(String[] urlPath) throws SQLException {
    if (urlPath[2].equals("days")) {
      if (!isInt(urlPath[3])) {
        return null;
      }
      if (Integer.parseInt(urlPath[3]) < 1 || Integer.parseInt(urlPath[3]) > 366) {
        return null;
      }
      this.dayId = urlPath[3];
      this.resortId = urlPath[1];
      if (urlPath[4].equals("skiers")) {
        this.skierId = urlPath[5];
        BasicDataSource dataSource = DBCPDataSource.getDataSource();
        PreparedStatement preparedStatement = null;
        ResultSet result= null;
        int vertical = 0;
        Connection con =null;
        try {
          con = dataSource.getConnection();
          preparedStatement = con.prepareStatement("SELECT totalVertical FROM LiftRides.SkierTotalVert"
              + " WHERE skierID=? ");
          preparedStatement.setString(1, this.skierId);
//          preparedStatement = con.prepareStatement("SELECT sum(vertical) FROM LiftRides.LiftRides"
//              + " WHERE skierID=? AND dayID=? AND resortID=?");
//          preparedStatement.setString(1, this.skierId);
//          preparedStatement.setString(2, this.dayId);
//          preparedStatement.setString(3, this.resortId);
          result = preparedStatement.executeQuery();
          while(result.next()) {
            vertical = result.getInt("totalVertical");
//            vertical = result.getInt("sum(vertical)");
          }
          return "{\n"
              + "  \"resortID\": \"" + this.resortId + "\",\n"
              + "  \"totalVert\": \"" + vertical + "\" \n"
              + "}";

        } catch (Exception e) {

        } finally {
          if (con != null) {
            con.close();
          }
          if (preparedStatement != null) {
            preparedStatement.close();
          }
        }
      }
    }
    return null;
  }

  public String getVerticalSpecificResort(String[] urlPath, String queryParam) throws SQLException {
    if (urlPath[2].equals("vertical")) {
      if (queryParam.contains("resort=")) {
        String[] resortPath = queryParam.split("=");
        this.skierId = urlPath[1];
        this.resortId = resortPath[1];
        BasicDataSource dataSource = DBCPDataSource.getDataSource();
        PreparedStatement preparedStatement = null;
        ResultSet result= null;
        int vertical = 0;
        Connection con =null;
        try {
          con = dataSource.getConnection();
          preparedStatement = con.prepareStatement("SELECT totalVertical FROM LiftRides.SkierTotalVert"
              + " WHERE skierID=? ");
          preparedStatement.setString(1, this.skierId);
//          preparedStatement = con.prepareStatement("SELECT sum(vertical) FROM LiftRides.LiftRides"
//              + " WHERE skierID=? AND resortID=?");
//          preparedStatement.setInt(1, Integer.parseInt(this.skierId));
//          preparedStatement.setString(2, this.resortId);
          result = preparedStatement.executeQuery();
          while(result.next()) {
            vertical = result.getInt("totalVertical");
//            vertical = result.getInt("sum(vertical)");
          }
          return "{\n"
              + "  \"resortID\": \"" + this.resortId + "\",\n"
              + "  \"totalVert\": \"" + vertical + "\"\n"
              + "}";
        } catch (Exception e) {

        } finally {
          if (con != null) {
            con.close();
          }
          if (preparedStatement != null) {
            preparedStatement.close();
          }
        }

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
