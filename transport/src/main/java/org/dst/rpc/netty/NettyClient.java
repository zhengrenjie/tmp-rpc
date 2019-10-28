package org.dst.rpc.netty;

import org.dst.rpc.api.remote.DefaultResponseFuture;
import org.dst.rpc.api.remote.Request;
import org.dst.rpc.api.remote.Response;
import org.dst.rpc.api.remote.ResponseFuture;
import org.dst.rpc.api.transport.AbstractChannel;
import org.dst.rpc.api.transport.AbstractClient;
import org.dst.rpc.api.transport.Channel;
import org.dst.rpc.api.transport.Endpoint;
import org.dst.rpc.api.transport.Handler;
import org.dst.rpc.codec.Codec;
import org.dst.rpc.codec.FastJsonSerialization;
import org.dst.rpc.core.URL;
import org.dst.rpc.exception.DstException;
import org.dst.rpc.exception.TransportException;
import org.dst.rpc.netty.netty_codec.NettyDecoder;
import org.dst.rpc.netty.netty_codec.NettyEncoder;
import org.dst.rpc.codec.DstCodec;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 *
 */
public class NettyClient extends AbstractClient {

  private io.netty.channel.Channel clientChannel;
  private NioEventLoopGroup nioEventLoopGroup;


  private ExecutorService executor;

  public NettyClient(URL serverUrl) {
    super(serverUrl);
    executor = Executors.newSingleThreadExecutor();
    nioEventLoopGroup = new NioEventLoopGroup();
  }

  @Override
  protected Channel createChannel() {

    Channel channel = new AbstractChannel() {
      @Override
      protected void doOpen() {
        Bootstrap bootstrap = new Bootstrap();
        int CONNECT_TIMEOUT_MILLIS = getUrl().getInt("CONNECT_TIMEOUT_MILLIS", 3000);
        boolean TCP_NODELAY = getUrl().getBoolean("TCP_NODELAY", true);
        boolean SO_KEEPALIVE = getUrl().getBoolean("SO_KEEPALIVE", true);
        bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MILLIS);
        bootstrap.option(ChannelOption.TCP_NODELAY, TCP_NODELAY);
        bootstrap.option(ChannelOption.SO_KEEPALIVE, SO_KEEPALIVE);
        bootstrap.group(nioEventLoopGroup)
            .channel(NioSocketChannel.class)
            .handler(new ChannelInitializer<SocketChannel>() {
              @Override
              protected void initChannel(SocketChannel ch) throws Exception {
                ChannelPipeline pipeline = ch.pipeline();
                pipeline.addLast("decoder", new NettyDecoder());
                pipeline.addLast("encoder", new NettyEncoder());
                pipeline.addLast("handler", new ClientChannelHandler(
                    new Handler() {
                      @Override
                      public String getServerName() {
                        return getUrl().getPath();
                      }

                      @Override
                      public Object handle(Endpoint endpoint, Object message) {
                        receive(message);
                        return null;
                      }
                    }
                ));
              }
            });
        ChannelFuture future;
        try {
          future = bootstrap.connect(getUrl().getHost(), getUrl().getPort()).sync();
        } catch (InterruptedException i) {
          close();
          // todo : log or retry
          throw new TransportException("NettyClient: connect().sync() interrupted", i);
        }
        // 标志当前的Channel已经打开
        // 保存当前的netty channel。
        clientChannel = future.channel();
        // 新起一个线程去监听close事件
        executor.submit(() -> {
          try {
            clientChannel.closeFuture().sync();
          } catch (Exception e) {
            // todo : log
          } finally {
            close();
          }
        });
      }

      /**
       * 先关掉clientChannel，再关掉executor
       */
      @Override
      protected void doClose() {
        if (!isOpen()) {
          return;
        }
        if (clientChannel != null) {
          clientChannel.close();
        }
        if (nioEventLoopGroup != null) {
          nioEventLoopGroup.shutdownGracefully();
        }
        executor.shutdown();
      }

      @Override
      public void send(Object message) {
        byte[] msg = getChannel().getCodec().encode(message);
        clientChannel.writeAndFlush(msg);
      }

      @Override
      public void receive(Object message) {
        Response response = (Response) message;
        ResponseFuture future = getResponseFuture(response.getRequestId());
        future.onSuccess(response);
      }
    };

    Codec codec = new DstCodec(new FastJsonSerialization());
    channel.setCodec(codec);
    return channel;
  }

  @Override
  public Response invoke(Request request) {
    ResponseFuture response = new DefaultResponseFuture();
    addCurrentTask(request.getRequestId(), response);
    getChannel().send(request);
    try {
      return (Response) response.getValue();
    } catch (Exception e) {
      Response errorResponse = new Response();
      errorResponse.setRequestId(request.getRequestId());
      errorResponse.setException(new TransportException("NettyClient: response.getValue interrupted!"));
      return errorResponse;
    }
  }



  private class ClientChannelHandler extends ChannelDuplexHandler {

    private Handler handler;

    public ClientChannelHandler(Handler handler) {
      this.handler = handler;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
      Object object = getChannel().getCodec().decode((byte[]) msg);
      if (!(object instanceof Response)) {
        throw new DstException(
            "NettyChannelHandler: unsupported message type when encode: " + object.getClass());
      }
      processResponse(ctx, (Response) object);
    }

    private void processResponse(ChannelHandlerContext ctx, Response msg) {
      handler.handle(NettyClient.this, msg);
    }
  }
}
