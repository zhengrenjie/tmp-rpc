package org.dst.rpc.example.channel_arg;

import org.dst.rpc.Reference;
import org.dst.rpc.api.Channel;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public class Client {

  public static void main(String[] args) throws Exception {
    Reference<IServer> reference = new Reference<>();
    reference.setAddress("dst://127.0.0.1:8080");
    reference.setAsync(false);
    reference.setInterfaceClass(IServer.class);

    IServer server = reference.getReference();
    Channel channel = null;
    server.say(channel);
//    if(channel != null) {
//      Object result = channel.read();
//      System.out.println(result.toString());
//    }
  }

}
