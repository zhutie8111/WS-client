package cn.com.tcsl.ws;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.http.DefaultHttpHeaders;
import io.netty.handler.codec.http.HttpClientCodec;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.Map;

/**
 * Created by Tony on 2018/11/3.
 */
public class WebsocketPushClient {

    private String url;


    //static final String URL = System.getProperty("url", "ws://192.168.9.215:9001/websocket?shopId=325");

    private WebsocketPushClient(){}

    public WebsocketPushClient(WebsocketConfig config){

        url = config.getScheme() + "://" + config.getHost()+":" + config.getPort() + "/"
                + config.getPath();

        if (config.getSuffixParams() != null && !config.getSuffixParams().isEmpty()){
            Map<String, Object> params = config.getSuffixParams();
            StringBuilder paramsBuilder = new StringBuilder();
            for (String key : params.keySet()){
                paramsBuilder.append(key);
                paramsBuilder.append("=");
                paramsBuilder.append(params.get(key));
                paramsBuilder.append("&");
            }
            if (paramsBuilder.length() > 0){
                paramsBuilder.deleteCharAt(paramsBuilder.length() - 1);
            }
            url = url + "?" + paramsBuilder.toString();
        }

    }

    public void init() throws Exception{
        {
            URI uri = new URI(url);
            String scheme = uri.getScheme() == null? "ws" : uri.getScheme();
            final String host = uri.getHost() == null? "127.0.0.1" : uri.getHost();
            final int port;
            //如果没有设置端口号
            if (uri.getPort() == -1) {
                if ("ws".equalsIgnoreCase(scheme)) {
                    port = 80;
                } else if ("wss".equalsIgnoreCase(scheme)) {
                    port = 443;
                } else {
                    port = -1;
                }
            } else {
                port = uri.getPort();
            }

            if (!"ws".equalsIgnoreCase(scheme) && !"wss".equalsIgnoreCase(scheme)) {
                System.err.println("Only WS(S) is supported.");
                throw new RuntimeException("Scheme was invalid, Only WS(S) is supported.");
            }

            final boolean ssl = "wss".equalsIgnoreCase(scheme);
            final SslContext sslCtx;
            if (ssl) {
                // sslCtx = SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build();
                sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
            } else {
                sslCtx = null;
            }

            EventLoopGroup group = new NioEventLoopGroup();
            try {
                // Connect with V13 (RFC 6455 aka HyBi-17). You can change it to V08 or V00.
                // If you change it to V00, ping is not supported and remember to change
                // HttpResponseDecoder to WebSocketHttpResponseDecoder in the pipeline.
                final WebsocketPushClientHandler handler =
                        new WebsocketPushClientHandler(
                                WebSocketClientHandshakerFactory.newHandshaker(
                                        uri, WebSocketVersion.V13, null, true, new DefaultHttpHeaders()));

                Bootstrap b = new Bootstrap();
                b.group(group)
                        .channel(NioSocketChannel.class)
                        .handler(new ChannelInitializer<SocketChannel>() {
                            @Override
                            protected void initChannel(SocketChannel ch) {
                                ChannelPipeline p = ch.pipeline();
                                //wss 连接
                                if (sslCtx != null) {
                                    p.addLast(sslCtx.newHandler(ch.alloc(), host, port));
                                }
                                p.addLast(
                                        new HttpClientCodec(),
                                        new HttpObjectAggregator(8192),
                                        //WebSocketClientCompressionHandler.INSTANCE,
                                        handler);
                            }
                        });

                Channel ch = b.connect(uri.getHost(), port).sync().channel();
                handler.handshakeFuture().sync();

                BufferedReader console = new BufferedReader(new InputStreamReader(System.in));
                while (true) {
                    String msg = console.readLine();
                    if (msg == null) {
                        break;
                    } else if ("bye".equals(msg.toLowerCase())) {
                        ch.writeAndFlush(new CloseWebSocketFrame());
                        ch.closeFuture().sync();
                        break;
                    } else if ("ping".equals(msg.toLowerCase())) {
                        WebSocketFrame frame = new PingWebSocketFrame(Unpooled.wrappedBuffer(new byte[] { 8, 1, 8, 1 }));
                        ch.writeAndFlush(frame);
                    } else {
                        WebSocketFrame frame = new TextWebSocketFrame(msg);
                        ch.writeAndFlush(frame);
                    }
                }
            } finally {
                group.shutdownGracefully();
            }
        }

    }


}
