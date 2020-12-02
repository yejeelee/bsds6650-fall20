import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.dbcp2.BasicDataSource;

public class MessageReceiver {
  private final static String QUEUE_NAME = "task_queue1";
  public static int count = 0;
  private static BasicDataSource dataSource = DBCPDataSource.getDataSource();

//http://3.86.181.178:8080/RabbitMQ_war/
  public static void main(String[] argv) throws Exception {
    ConnectionFactory factory = new ConnectionFactory();
    factory.setUsername("yejee94");
    factory.setPassword("bsds6650-f20");
    factory.setVirtualHost("/");
    factory.setHost("ec2-3-86-181-178.compute-1.amazonaws.com");
    factory.setPort(5672);
//    factory.setHost("localhost");
    final Connection connection = factory.newConnection();
    Runnable runnable = new Runnable() {
      @Override
      public void run() {
        try {
          Channel channel = connection.createChannel();
          channel.queueDeclare(QUEUE_NAME, true, false, false, null);
          System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
          channel.basicQos(200);
          DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("[x] " + Thread.currentThread().getId() + " Received '" + count + ": " + message + "'");
            count++;
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            LiftRide lift = new Gson().fromJson(message, LiftRide.class);
            LiftRideDao liftRideDao = new LiftRideDao();
            try {
              liftRideDao.createLiftRide(lift, dataSource.getConnection());
              //updateSkierIDwithTotalVertical();
            } catch(SQLException e) {
              System.err.println(e);
            } finally {
              System.out.println(" [x] Done");
              channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
            }
          };
          channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
//          channel.close();
        } catch (Exception e) {
          Logger.getLogger(MessageReceiver.class.getName()).log(Level.SEVERE, null, e);
        }
      }
    };

    ArrayList<Thread> threads = new ArrayList<>();
    for(int i = 0; i < 40; i++) {
      Thread recv1 = new Thread(runnable);
      threads.add(recv1);
      recv1.start();
//      recv1.join();
    }
    for(Thread th : threads) {
      th.join();
    }

//    Channel channel = connection.createChannel();
//    channel.queueDeclare(QUEUE_NAME, true, false, false, null);
//    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
//    channel.basicQos(1);
//    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//      String message = new String(delivery.getBody(), "UTF-8");
//      System.out.println(" [x] Received '" + message + "'");
//      Gson gson = new GsonBuilder().setPrettyPrinting().create();
//      LiftRide lift = new Gson().fromJson(message, LiftRide.class);
//      LiftRideDao liftRideDao = new LiftRideDao();
//      try {
//        liftRideDao.createLiftRide(lift, dataSource.getConnection());
//      } catch(SQLException e) {
//
//      } finally {
//        System.out.println(" [x] Done");
//        channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
//      }
//    };
//
//    channel.basicConsume(QUEUE_NAME, false, deliverCallback, consumerTag -> { });
  }
}
