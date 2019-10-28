package org.dst.rpc.example;

import org.dst.rpc.DefaultInvoker;
import org.dst.rpc.Invoker;
import org.dst.rpc.core.URL;
import org.dst.rpc.netty.NettyClient;
import org.dst.rpc.proxy.ProxyFactory;
import org.dst.rpc.proxy.Reference;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public class Client {

  public static void main(String[] args)throws Exception {
//    Class clazz = Class.forName("org.dst.rpc.api.remote.Response");
//
//    System.out.println(clazz.getName());
    URL url = new URL("dst", "127.0.0.1:8080", IServer.class.getName());
    org.dst.rpc.api.transport.Client client = new NettyClient(url);
    client.init();
    Invoker invoker = new DefaultInvoker(client);
    IServer server = new ProxyFactory<IServer>().getProxy(IServer.class, url, invoker);
    System.out.println(server.say());
    System.out.println(server.say("dst1"));
  }

}
