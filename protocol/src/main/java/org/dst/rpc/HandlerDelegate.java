package org.dst.rpc;

import org.dst.rpc.api.remote.Request;
import org.dst.rpc.api.transport.Endpoint;
import org.dst.rpc.api.transport.Handler;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public class HandlerDelegate implements Handler {

  private Invoker serverImpl;

  public HandlerDelegate(Invoker serverImpl) {
    this.serverImpl = serverImpl;
  }

  @Override
  public String getServerName() {
    return serverImpl.getInterface().getName();
  }

  @Override
  public Object handle(Endpoint endpoint, Object message) {
    return serverImpl.invoke((Request) message);
  }
}
