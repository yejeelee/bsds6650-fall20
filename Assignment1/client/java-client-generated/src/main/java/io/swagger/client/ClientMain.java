package io.swagger.client;

import java.io.IOException;

public class ClientMain {

  public static void main(String[] args) throws IOException, ApiException,InterruptedException {
    SkiersApiMultiThread skiersApiThread = new SkiersApiMultiThread();
    skiersApiThread.start();
  }

}
