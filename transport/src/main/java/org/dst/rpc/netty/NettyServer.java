package org.dst.rpc.netty;

import org.dst.rpc.api.remote.Request;
import org.dst.rpc.api.remote.Response;
import org.dst.rpc.api.transport.AbstractChannel;
import org.dst.rpc.api.transport.AbstractServer;
import org.dst.rpc.api.transport.Channel;
import org.dst.rpc.api.transport.Handler;
import org.dst.rpc.codec.Codec;
import org.dst.rpc.core.URL;
import org.dst.rpc.exception.DstException;
import org.dst.rpc.netty.netty_codec.NettyDecoder;
import org.dst.rpc.netty.netty_codec.NettyEncoder;
import org.dst.rpc.codec.DstCodec;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyServer extends AbstractServer {

  private static Logger logger = LoggerFactory.getLogger(NettyServer.class);

  private io.netty.channel.Channel serverChannel;
  private NioEventLoopGroup bossGroup;
  private NioEventLoopGroup workerGroup;


  public NettyServer( URL url, Handler handler) {
    super(url);
    getRoutableHandler().registerHandler(handler);
    bossGroup = new NioEventLoopGroup(1);
    workerGroup = new NioEventLoopGroup();
  }

  @Override
  protected Channel createChannel() {
    Channel channel = new AbstractChannel() {
      @Override
      protected void doOpen() {
        ServerBootstrap serverBootstrap = new ServerBootstrap();
        serverBootstrap.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class)
            .childHandler(new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("decoder", new NettyDecoder());
                pipeline.addLast("encoder", new NettyEncoder());
                pipeline.addLast("handler", new ServerChannelHandler(getRoutableHandler()));
              }
            });
        serverBootstrap.childOption(ChannelOption.TCP_NODELAY, true);
        serverBootstrap.childOption(ChannelOption.SO_KEEPALIVE, true);
        try {
          ChannelFuture f = serverBootstrap.bind(getUrl().getPort()).sync();
          serverChannel = f.channel();
        } catch (Exception e) {
          // todo: log
          logger.error("NettyServer bind error", e);
        }
      }

      @Override
      protected void doClose() {
        if (serverChannel != null) {
          serverChannel.close();
        }
        if (bossGroup != null) {
          bossGroup.shutdownGracefully();
          bossGroup = null;
        }
        if (workerGroup != null) {
          workerGroup.shutdownGracefully();
          workerGroup = null;
        }
      }

      @Override
      public void send(Object message) {

      }

      @Override
      public void receive(Object message) {

      }
    };
    Codec codec = new DstCodec();
    channel.setCodec(codec);
    return channel;
  }


  private class ServerChannelHandler extends ChannelDuplexHandler {

    private Handler handler;

    public ServerChannelHandler(Handler handler) {
      this.handler = handler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
      Object object = getChannel().getCodec().decode((byte[]) msg);
      if (!(object instanceof Request)) {
        throw new DstException("ServerChannelHandler: unsupported message type when decode: " + object.getClass());
      }
      processRequest(ctx, (Request) object);
    }

    private void processRequest(ChannelHandlerContext ctx, Request msg) {
      Object result = handler.handle(NettyServer.this, msg);
      Response response = new Response();
      response.setRequestId(msg.getRequestId());

      if(result instanceof Exception) {
        response.setException((Exception) result);
      } else {
        response.setReturnValue(result);
      }
      sendResponse(ctx, response);
    }

    private ChannelFuture sendResponse(ChannelHandlerContext ctx, Response response) {
      byte[] msg = getChannel().getCodec().encode(response);
      if (ctx.channel().isActive()) {
        return ctx.channel().writeAndFlush(msg);
      }

      return null;
    }
  }
}
