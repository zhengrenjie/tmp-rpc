package org.dst.rpc.api.transport;

import org.dst.rpc.core.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 */
public class DefaultRoutableHandler implements RoutableHandler {

  private Map<String, Handler> handlerMap;
  private Lock lock;

  @Override
  public List<Handler> getAllHandler() {
    return new ArrayList<>(handlerMap.values());
  }

  public DefaultRoutableHandler() {
    handlerMap = new ConcurrentHashMap<>();
    lock = new ReentrantLock();
  }

  @Override
  public Handler getHandlerByServerName(URL url) {

    return null;
  }

  @Override
  public void registerHandler(Handler handler) {
    String serverIdentify = handler.getServerName();
    handlerMap.put(serverIdentify, handler);
  }

  @Override
  public void merge(RoutableHandler handler) {
    List<Handler> handlers = handler.getAllHandler();
    if(handlers != null && handlers.size() > 0) {
      for(Handler h : handlers) {
        if(h instanceof RoutableHandler) {
          merge((RoutableHandler) h);
        } else {
          registerHandler(h);
        }
      }
    }
  }

  @Override
  public Object handle(Endpoint endpoint, Object message) {
    return null;
  }
}
