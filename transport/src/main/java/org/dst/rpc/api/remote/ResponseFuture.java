package org.dst.rpc.api.remote;

/**
 *
 */
public interface ResponseFuture extends Future {

  void onSuccess(Response response);

  void onFailure(Response response) ;

}
