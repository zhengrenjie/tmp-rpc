package org.dst.rpc.api.transport;

import org.dst.rpc.api.remote.ResponseFuture;
import org.dst.rpc.core.URL;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 客户端，保存了当前已经发送的所有请求，并保存了返回的Future
 *
 *
 */
abstract public class AbstractClient extends AbstractEndpoint implements Client {

  private Map<Long, ResponseFuture> currentTask = new ConcurrentHashMap<>();

  public AbstractClient(URL serverUrl) {
    super(serverUrl);
  }

  protected ResponseFuture getResponseFuture(long requestId) {
    return currentTask.remove(requestId);
  }

  protected void addCurrentTask(long requestId, ResponseFuture responseFuture) {
    currentTask.putIfAbsent(requestId, responseFuture);
  }

}
