import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.apache.commons.dbcp2.BasicDataSource;

public class LiftRideDao {

  public void createLiftRide(LiftRide newLiftRide, Connection conn) {
    PreparedStatement preparedStatement = null;
    String insertQueryStatement = "INSERT INTO LiftRides (skierId, resortId, dayId, time, liftId, vertical) " +
        "VALUES (?,?,?,?,?,?)";
    try {
      preparedStatement = conn.prepareStatement(insertQueryStatement);
      preparedStatement.setString(1, newLiftRide.getSkierID());
      preparedStatement.setString(2, newLiftRide.getResortID());
      preparedStatement.setString(3, newLiftRide.getDayID());
      preparedStatement.setString(4, newLiftRide.getTime());
      preparedStatement.setString(5, newLiftRide.getLiftID());
      preparedStatement.setInt(6, Integer.parseInt(newLiftRide.getLiftID()) * 10);

      // execute insert SQL statement
      preparedStatement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    } finally {
      try {
        if (conn != null) {
          conn.close();
        }
        if (preparedStatement != null) {
          preparedStatement.close();
        }
      } catch (SQLException se) {
        se.printStackTrace();
      }
    }
  }
}