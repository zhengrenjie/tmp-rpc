package org.dst.rpc;

import org.dst.rpc.api.remote.Request;
import org.dst.rpc.api.remote.Response;
import org.dst.rpc.core.URL;
import org.dst.rpc.api.transport.Client;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public class DefaultInvoker<T> implements Invoker<T> {

  private Client client;

  public DefaultInvoker(Client client) {
    this.client = client;
  }

  @Override
  public URL getURL() {
    return null;
  }

  @Override
  public Class<T> getInterface() {
    return null;
  }

  @Override
  public Response invoke(Request request) {
    return client.invoke(request);
  }
}