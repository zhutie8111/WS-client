package cn.com.tcsl.ws;

import cn.com.tcsl.ws.utils.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelPromise;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketClientHandshaker;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketHandshakeException;
import io.netty.util.CharsetUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by Tony on 2018/11/3.
 */
public class WebsocketPushClientHandler extends SimpleChannelInboundHandler<Object> {

    private static Logger logger = LoggerFactory.getLogger(LogUtils.LOG_PROFILE_NAME);

    private final WebSocketClientHandshaker handshaker;

    private ChannelPromise handshakeFuture;

    private ReceiveMessage receiveMessage;

    public WebsocketPushClientHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public WebsocketPushClientHandler(WebSocketClientHandshaker handshaker, ReceiveMessage receiveMessage) {
        this.handshaker = handshaker;
        this.receiveMessage = receiveMessage;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception{
        super.channelActive(ctx);
        handshaker.handshake(ctx.channel());
        LogUtils.console_print("channelActive");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception{
        super.channelInactive(ctx);
        LogUtils.console_print("WebSocket Client disconnected!");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                LogUtils.console_print("WebSocket Client connected!");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
            	LogUtils.console_print("WebSocket Client failed to connect, " + e.getMessage());
                handshakeFuture.setFailure(e);
            }
            return;
        }

        if (msg instanceof FullHttpResponse) {
            FullHttpResponse response = (FullHttpResponse) msg;
            throw new IllegalStateException(
                    "Unexpected FullHttpResponse (getStatus=" + response.getStatus() +
                            ", content=" + response.content().toString(CharsetUtil.UTF_8) + ')');
        }

        WebSocketFrame frame = (WebSocketFrame) msg;
        if (frame instanceof TextWebSocketFrame) {
            TextWebSocketFrame textFrame = (TextWebSocketFrame) frame;
            LogUtils.console_print("WebSocket Client received message: " + textFrame.text());

            receiveMessage.onMessage(ch, textFrame.text());

        } else if (frame instanceof PongWebSocketFrame) {
            System.out.println("WebSocket Client received pong");

            receiveMessage.onMessage(ch, frame);

        } else if (frame instanceof CloseWebSocketFrame) {
            System.out.println("WebSocket Client received close Frame");

            receiveMessage.onMessage(ch, frame);//执行后将关闭

            ch.close();
        }else if(frame instanceof  BinaryWebSocketFrame){
            BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame)msg;
            ByteBuf buf = binaryFrame.content();
            if (buf.isReadable()){
                int availableBytesNumber = buf.readableBytes();
                byte[] receivedBytes = new byte[availableBytesNumber];
                buf.readBytes(receivedBytes);
                
                receiveMessage.onMessage(ch, receivedBytes);
            }
            //buf.release();
            // byte [] bytes = receivedBytes;
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        ctx.close();
    }
}
