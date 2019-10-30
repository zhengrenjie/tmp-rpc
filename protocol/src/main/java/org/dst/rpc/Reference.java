package org.dst.rpc;

import org.dst.rpc.api.Client;
import org.dst.rpc.config.ParamConstants;
import org.dst.rpc.common.URL;
import org.dst.rpc.exception.DstException;
import org.dst.rpc.netty.NettyClient;
import org.dst.rpc.proxy.ProxyFactory;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public class Reference<T> {

  private Class<T> interfaceClass;
  private String address;
  private boolean isAsync;

  private URL serverUrl;

  public Reference() {
    this.serverUrl = new URL();
  }

  public void setInterfaceClass(Class<T> interfaceClass) {
    this.interfaceClass = interfaceClass;
    serverUrl.setPath(interfaceClass.getName());
  }

  public void setAddress(String address) {
    this.address = address;
    if(!address.contains("://")) {
      throw new DstException("Empty protocol");
    }
    String protocol = address.substring(0, address.indexOf("://"));
    serverUrl.setProtocol(protocol);
    serverUrl.setAddress(address.substring(address.indexOf("://") + "://".length()));
  }

  public void setAsync(boolean async) {
    isAsync = async;
    serverUrl.setConfig(ParamConstants.isAsync, String.valueOf(async));
  }

  public T getReference() {

    Client client = new NettyClient(serverUrl);
    client.init();
    Invoker invoker = new DefaultInvoker(client);
    return new ProxyFactory<T>().getProxy(interfaceClass, serverUrl, invoker);
  }

}
