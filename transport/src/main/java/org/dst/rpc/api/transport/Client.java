package org.dst.rpc.api.transport;

import org.dst.rpc.api.remote.Request;
import org.dst.rpc.api.remote.Response;

/**
 * 客户端，客户端具有请求的功能
 *
 *
 */
public interface Client extends Endpoint {

  Response invoke(Request request);

}
