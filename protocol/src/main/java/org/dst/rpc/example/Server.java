package org.dst.rpc.example;

import org.dst.rpc.HandlerDelegate;
import org.dst.rpc.ServerImpl;
import org.dst.rpc.api.transport.Handler;
import org.dst.rpc.core.URL;
import org.dst.rpc.netty.NettyTransportFactory;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public class Server {

  public static void main(String[] args) {
    IServer impl = new IServerImpl();
    URL url = new URL("dst", "127.0.0.1:8080", IServer.class.getName());
    Handler handler = new HandlerDelegate(new ServerImpl<>(url,impl,IServer.class));
    org.dst.rpc.api.transport.Server server = NettyTransportFactory.getInstance().createServer(url, handler);
    server.init();

  }

}
