package org.dst.rpc.example;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public class IServerImpl implements IServer {

  @Override
  public String say() {
    return "dst";
  }

  @Override
  public String say(String name) {
    return "dst " + name;
  }
}
