package org.dst.rpc;


import org.dst.rpc.api.remote.Request;
import org.dst.rpc.api.remote.Response;
import org.dst.rpc.core.URL;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public interface Invoker<T> {

  URL getURL();

  Class<T> getInterface();

  Response invoke(Request request);

}