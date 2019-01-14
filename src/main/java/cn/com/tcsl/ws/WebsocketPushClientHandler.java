package cn.com.tcsl.ws;

import cn.com.tcsl.ws.message.ReceiveMessage;
import cn.com.tcsl.ws.utils.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.util.CharsetUtil;

/**
 * Created by Tony on 2018/11/3.
 */
public class WebsocketPushClientHandler extends SimpleChannelInboundHandler<Object> {

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
            LogUtils.console_print("WebSocket Client received pong");

            receiveMessage.onMessage(ch, frame);

        } else if (frame instanceof CloseWebSocketFrame) {
            LogUtils.console_print("WebSocket Client received close Frame");

            receiveMessage.onMessage(ch, frame);//执行后将关闭

            //receive a closing frame to shutdown the event loop
            ch.eventLoop().shutdownGracefully();

            ch.close().sync();
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
        LogUtils.console_print("exceptionCaught "+ cause.getMessage());
        ctx.close();
    }
}
