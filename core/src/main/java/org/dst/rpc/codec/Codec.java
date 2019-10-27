package org.dst.rpc.codec;

import org.dst.rpc.exception.CodecException;

/**
 * 编解码器
 */
public interface Codec {

  byte[] encode(Object message) throws CodecException;

  Object decode(byte[] data) throws CodecException;

}
