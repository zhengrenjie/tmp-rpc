package org.dst.rpc;

import org.dst.rpc.api.async.Request;
import org.dst.rpc.api.Handler;

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
  public Object handle(Object message) {
    return serverImpl.invoke((Request) message);
  }
}
