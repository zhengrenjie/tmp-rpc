package org.dst.rpc.example;

import org.dst.rpc.DefaultInvoker;
import org.dst.rpc.Invoker;
import org.dst.rpc.Reference;
import org.dst.rpc.core.URL;
import org.dst.rpc.netty.NettyClient;
import org.dst.rpc.proxy.ProxyFactory;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public class Client {

  public static void main(String[] args)throws Exception {
    Reference<IServer> reference = new Reference<>();
    reference.setAddress("dst://127.0.0.1:8080");
    reference.setAsync(false);
    reference.setInterfaceClass(IServer.class);

    IServer server = reference.getReference();
    System.out.println(server.say());
    System.out.println(server.say("dst1"));
  }

}
