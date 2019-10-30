package org.dst.rpc.netty;

import org.dst.rpc.api.Handler;
import org.dst.rpc.api.Server;
import org.dst.rpc.api.ServerFactory;
import org.dst.rpc.core.URL;

/**
 * NettyFactory实现
 */
public class NettyTransportFactory extends ServerFactory {

  private NettyTransportFactory() {}

  private static class InstanceHolder {
    private static ServerFactory factory = new NettyTransportFactory();
  }

  public static ServerFactory getInstance() {
    return InstanceHolder.factory;
  }

  @Override
  protected Server doCreateServer(URL url, Handler handler) {
    return new NettyServer(url, handler);
  }
}
