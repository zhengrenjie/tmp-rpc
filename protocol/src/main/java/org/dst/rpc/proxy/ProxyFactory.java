package org.dst.rpc.proxy;

import org.dst.rpc.Invoker;
import org.dst.rpc.core.URL;
import java.lang.reflect.Proxy;


public class ProxyFactory<T> {

  @SuppressWarnings("unchecked")
  public T getProxy(Class<T> clazz, URL url, Invoker invoker) {
    return (T) Proxy.newProxyInstance(
        clazz.getClassLoader(), new Class[]{clazz}, new Reference(clazz, url, invoker));
  }

}
