package org.dst.rpc.proxy;

import org.dst.rpc.Invoker;
import org.dst.rpc.common.URL;
import java.lang.reflect.Proxy;


public class ProxyFactory<T> {

  @SuppressWarnings("unchecked")
  public T getProxy(Class<T> clazz, URL url, Invoker invoker) {
    return (T) Proxy.newProxyInstance(
        clazz.getClassLoader(), new Class[]{clazz}, new ProxyHandler(clazz, url, invoker));
  }

}
