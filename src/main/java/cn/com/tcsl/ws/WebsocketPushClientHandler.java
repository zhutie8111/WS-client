package cn.com.tcsl.ws;

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

    private PushMessage pushMessage;

    public WebsocketPushClientHandler(WebSocketClientHandshaker handshaker) {
        this.handshaker = handshaker;
    }

    public WebsocketPushClientHandler(WebSocketClientHandshaker handshaker, PushMessage pushMessage) {
        this.handshaker = handshaker;
        this.pushMessage = pushMessage;
    }

    public ChannelFuture handshakeFuture() {
        return handshakeFuture;
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) {
        handshakeFuture = ctx.newPromise();
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) {
        handshaker.handshake(ctx.channel());
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) {
        System.out.println("WebSocket Client disconnected!");
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, Object msg) throws Exception {
        Channel ch = ctx.channel();
        if (!handshaker.isHandshakeComplete()) {
            try {
                handshaker.finishHandshake(ch, (FullHttpResponse) msg);
                System.out.println("WebSocket Client connected!");
                handshakeFuture.setSuccess();
            } catch (WebSocketHandshakeException e) {
                System.out.println("WebSocket Client failed to connect");
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
            System.out.println("WebSocket Client received message: " + textFrame.text());

            pushMessage.onMessage(ch, textFrame.text());

        } else if (frame instanceof PongWebSocketFrame) {
            System.out.println("WebSocket Client received pong");

            pushMessage.onMessage(ch, frame);

        } else if (frame instanceof CloseWebSocketFrame) {
            System.out.println("WebSocket Client received closing");

            pushMessage.onMessage(ch, frame);//执行后将关闭

            ch.close();
        }else if(frame instanceof  BinaryWebSocketFrame){
            BinaryWebSocketFrame binaryFrame = (BinaryWebSocketFrame)msg;

            ByteBuf buf = binaryFrame.content();
            int availableBytesNumber = buf.readableBytes();
            byte[] receivedBytes = new byte[availableBytesNumber];
            buf.readBytes(receivedBytes);

            //buf.release();

            // byte [] bytes = receivedBytes;

            pushMessage.onMessage(ch, receivedBytes);//执行后将关闭

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
