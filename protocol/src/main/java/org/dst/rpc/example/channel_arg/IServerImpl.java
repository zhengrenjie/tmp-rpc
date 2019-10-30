package org.dst.rpc.example.channel_arg;

import org.dst.rpc.api.Channel;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public class IServerImpl implements IServer {

  @Override
  public void say(Channel channel) {
    channel.send("dst");
  }

  @Override
  public void say(Channel channel, String name) {
    channel.send(name + "dst");
  }

}
