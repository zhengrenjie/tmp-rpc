package org.dst.rpc.api;

import org.dst.rpc.api.async.Response;
import org.dst.rpc.api.async.Request;

/**
 * 客户端，客户端具有请求的功能
 *
 *
 */
public interface Client extends Endpoint {

  boolean isAsync();

  Response invoke(Request request);

}
