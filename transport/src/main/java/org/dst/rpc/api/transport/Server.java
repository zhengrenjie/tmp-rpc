package org.dst.rpc.api.transport;

/**
 *
 */
public interface Server extends Endpoint {

  RoutableHandler getRoutableHandler();

}
