package org.dst.rpc.common;


public class Void {

  private Void(){}

  public static Void getInstance() {
    return InstanceHolder.aVoid;
  }

  private static class InstanceHolder {
    private static Void aVoid = new Void();
  }

}
