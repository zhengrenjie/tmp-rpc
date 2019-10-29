package org.dst.rpc.example.async;

import org.dst.rpc.api.async.Response;

/**
 * @author zrj CreateDate: 2019/10/28
 */
public interface IServer {

  Response say();

  Response say(String name);

}
