package org.dst.rpc.api.remote;

/**
 *
 */
public interface Future {

  boolean cancel();

  boolean isCancelled();

  boolean isDone();

  boolean isSuccess();

  Object getValue() throws InterruptedException;

  Exception getException();

  void addListener(FutureListener listener);

}
