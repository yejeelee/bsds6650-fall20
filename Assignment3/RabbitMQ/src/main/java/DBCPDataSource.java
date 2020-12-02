import org.apache.commons.dbcp2.BasicDataSource;

public class DBCPDataSource {
  private static BasicDataSource dataSource;

  // NEVER store sensitive information below in plain text!
//  private static final String HOST_NAME = System.getProperty("MySQL_IP_ADDRESS");
//  private static final String PORT = System.getProperty("MySQL_PORT");
//  private static final String DATABASE = "LiftRides";
//  private static final String USERNAME = System.getProperty("DB_USERNAME");
//  private static final String PASSWORD = System.getProperty("DB_PASSWORD");

//  private static final String HOST_NAME = "34.225.14.247";
  //private static final String HOST_NAME = "localhost";

  private static final String HOST_NAME = "database-1.c6bunshpt5md.us-east-1.rds.amazonaws.com";
  private static final String PORT = "3306";
  private static final String DATABASE = "LiftRides";
  private static final String USERNAME = "root";
  private static final String PASSWORD = "bsds6650-f20";

  static {
    // https://dev.mysql.com/doc/connector-j/8.0/en/connector-j-reference-jdbc-url-format.html
    dataSource = new BasicDataSource();
//    try {
//      Class.forName("com.mysql.cj.jdbc.Driver");
//    } catch (ClassNotFoundException e) {
//      e.printStackTrace();
//    }
    String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC", HOST_NAME, PORT, DATABASE);
    dataSource.setUrl(url);
    dataSource.setUsername(USERNAME);
    dataSource.setPassword(PASSWORD);
    dataSource.setMinIdle(0);
    dataSource.setMaxIdle(-1);
    dataSource.setMaxTotal(-1);
//    dataSource.setInitialSize(10);
//    dataSource.setMaxTotal(60);
    System.out.println(url);
  }

  public static BasicDataSource getDataSource() {
    try {
      Class.forName("com.mysql.cj.jdbc.Driver");
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
    return dataSource;
  }
}
