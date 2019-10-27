package org.dst.rpc.api.transport;

import org.dst.rpc.core.URL;

/**
 * 服务端
 *
 *
 */
abstract public class AbstractServer extends AbstractEndpoint implements Server {

  /**
   * Server只要存在就不为null
   */
  private RoutableHandler routableHandler;

  public AbstractServer(URL url) {
    super(url);
    routableHandler = new DefaultRoutableHandler();
  }

  @Override
  public RoutableHandler getRoutableHandler() {
    return routableHandler;
  }

}
