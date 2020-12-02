import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class MessageSender {

  private final static String QUEUE_NAME = "hello";

  public void send(String message) throws IOException {
    System.out.println("set connection" + message);
    ConnectionFactory factory = new ConnectionFactory();
    System.out.println("connection factory");
//    factory.setUsername("guest");
//    factory.setPassword("guest");
    factory.setHost("localhost");
//    factory.setPort(5672);
    System.out.println("factory set host");
    try (Connection connection = factory.newConnection();
        Channel channel = connection.createChannel()) {
      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//      String message = "Hello World!";
      channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
      System.out.println(" [x] Sent '" + message + "'");
    } catch (Exception e) {
      System.err.println(e);
    }
//    ConnectionFactory factory = new ConnectionFactory();
//    factory.setHost("localhost");
//    Connection connection = factory.newConnection();
//    Channel channel = connection.createChannel();
//
//    channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//    System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
//
//    DeliverCallback deliverCallback = (consumerTag, delivery) -> {
//      String message = new String(delivery.getBody(), "UTF-8");
//      System.out.println(" [x] Received '" + message + "'");
//    };
//    channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> { });
  }
}

//public class MessageSender {
//
//  private final static String QUEUE_NAME = "hello";
//
//  public static void main(String[] argv) throws Exception {
//    ConnectionFactory factory = new ConnectionFactory();
//    System.out.println("hi");
//    factory.setHost("localhost");
//    try (Connection connection = factory.newConnection();
//        Channel channel = connection.createChannel()) {
//      channel.queueDeclare(QUEUE_NAME, false, false, false, null);
//      String message = "Hello World!";
//      channel.basicPublish("", QUEUE_NAME, null, message.getBytes(StandardCharsets.UTF_8));
//      System.out.println(" [x] Sent '" + message + "'");
//    }
//  }
//}
