package org.dst.rpc.example.channel_arg;

import org.dst.rpc.api.Channel;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public interface IServer {

  void say(Channel channel);

  void say(Channel channel, String name);

}
