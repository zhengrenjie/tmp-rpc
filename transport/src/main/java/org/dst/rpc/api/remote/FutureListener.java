package org.dst.rpc.api.remote;

/**
 *
 */
public interface FutureListener {

    void operationComplete(Future future) throws Exception;

}
