package org.dst.rpc;


import org.dst.rpc.api.async.Response;
import org.dst.rpc.api.async.Request;
import org.dst.rpc.common.URL;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public interface Invoker<T> {

  URL getURL();

  Class<T> getInterface();

  Response invoke(Request request);

}
