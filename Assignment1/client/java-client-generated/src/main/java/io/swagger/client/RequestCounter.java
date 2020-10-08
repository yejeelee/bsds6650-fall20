package io.swagger.client;

public class RequestCounter {
  private int count = 0;

  synchronized public void inc() {
    count++;
  }

  synchronized public void incBySpecificAmount(int n) {
    count+=n;
  }

  synchronized public int getVal() {
    return this.count;
  }

}
