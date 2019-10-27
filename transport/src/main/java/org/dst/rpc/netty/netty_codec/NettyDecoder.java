package org.dst.rpc.netty.netty_codec;

import org.dst.rpc.common.constants.CodecConstants;
import org.dst.rpc.exception.TransportException;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;
import java.util.List;

/**
 * 用于处理半包粘包问题。
 *
 *
 */
public class NettyDecoder extends ByteToMessageDecoder {

  @Override
  protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
    // 数据比协议头小，直接返回
    if (in.readableBytes() <= CodecConstants.HEADER_SIZE) {
      return;
    }

    // 标记初始位置
    in.markReaderIndex();
    short magic = in.readShort();
    if(magic != CodecConstants.MAGIC_HEAD) {
      in.resetReaderIndex();
      throw new TransportException("NettyDecoder: magic number error: " + magic);
    }

    in.skipBytes(2);
    int contentLength = in.readInt();
    if(in.readableBytes() < contentLength + 8/* requestId 8 byte */) {
      in.resetReaderIndex();
      return;
    }

    // 全部读取
    in.resetReaderIndex();
    byte[] data = new byte[CodecConstants.HEADER_SIZE + contentLength];
    in.readBytes(data);
    out.add(data);
  }
}
