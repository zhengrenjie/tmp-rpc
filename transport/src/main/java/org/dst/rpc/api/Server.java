package org.dst.rpc.api;

import java.util.concurrent.Executor;

/**
 *
 */
public interface Server extends Endpoint {

  RoutableHandler getRoutableHandler();

  Executor getExecutor();
}
