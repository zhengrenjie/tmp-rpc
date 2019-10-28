package org.dst.rpc.proxy;

import org.dst.rpc.Invoker;
import org.dst.rpc.api.remote.Request;
import org.dst.rpc.api.remote.Response;
import org.dst.rpc.core.URL;
import org.dst.rpc.exception.DstException;
import org.dst.rpc.utils.RequestIdGenerator;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;


/**
 * @author zrj CreateDate: 2019/9/4
 */
public class Reference<T> implements InvocationHandler {

  private URL url;

  private Invoker invoker;

  private Class<T> interfaceClazz;

  public Reference(Class<T> clazz, URL url, Invoker invoker) {
    interfaceClazz = clazz;
    this.url = url;
    this.invoker = invoker;
  }

  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

    if (isLocalMethod(method)) {
      if ("toString".equals(method.getName())) {
        return "";
      }
      if ("equals".equals(method.getName())) {
        return false;
      }
      throw new DstException("can not invoke local method:" + method.getName());
    }

    Request request = new Request();
    request.setRequestId(RequestIdGenerator.next());
    request.setInterfaceName(method.getDeclaringClass().getName());
    request.setMethodName(method.getName());
    request.setArgsType(getArgsTypeString(args));
    request.setArgsValue(args);
    Response response = invoker.invoke(request);

    if(response.getException() != null) {
      throw response.getException();
    }

    return response.getReturnValue();
  }

  private String getArgsTypeString(Object[] args) {
    if(args == null || args.length <= 0) {
      return "";
    }
    StringBuilder sb = new StringBuilder();
    for(Object object : args) {
      sb.append(object.getClass().getName()).append(",");
    }
    if(sb.length() > 0) {
      sb.setLength(sb.length() - ",".length());
    }
    return sb.toString();
  }

  /**
   * tostring,equals,hashCode,finalize等接口未声明的方法不进行远程调用
   *
   * @param method
   * @return
   */
  public boolean isLocalMethod(Method method) {
    if (method.getDeclaringClass().equals(Object.class)) {
      try {
        Method interfaceMethod = interfaceClazz.getDeclaredMethod(method.getName(), method.getParameterTypes());
        return false;
      } catch (Exception e) {
        return true;
      }
    }
    return false;
  }
}
