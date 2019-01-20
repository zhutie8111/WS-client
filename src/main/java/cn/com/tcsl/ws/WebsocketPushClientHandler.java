package cn.com.tcsl.ws;

import cn.com.tcsl.ws.message.ReceiveMessage;
import cn.com.tcsl.ws.status.Heartbeat;
import cn.com.tcsl.ws.utils.LogUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.*;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.handler.codec.http.websocketx.*;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.util.CharsetUtil;

/**
 * Created by Tony on 2018/11/3.
 */
public class WebsocketPushClientHandler extends SimpleChannelInboundHandler<Object> {

    private Heartbeat heartbeat;

    private WebsocketConfig websocketConfig;

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
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        LogUtils.console_print("WebSocket Client user event trigger");
        if (evt instanceof IdleStateEvent){
            IdleStateEvent idleStateEvent = (IdleStateEvent)evt;

            switch (idleStateEvent.state()){
                case WRITER_IDLE:
                    handlerWriterIdleEvent(ctx);
                    break;
                case READER_IDLE:
                    handlerReaderIdleEvent(ctx);
                    break;
                case ALL_IDLE:
                    handlerAllIdleEvent(ctx);
                    break;
                default:break;
            }

        }else{
            super.userEventTriggered(ctx, evt);
        }

    }


    protected void handlerWriterIdleEvent(ChannelHandlerContext ctx){
        LogUtils.console_print("No write to channel for a while");
        //need sending heart beat then set the flag as true
        if (heartbeat != null && getWebsocketConfig().getNeedHeartBeat()){
            heartbeat.pulse();
        }
        LogUtils.console_print("send a heart beat with ping websocket frame");
    }

    protected void handlerReaderIdleEvent(ChannelHandlerContext ctx){
        LogUtils.console_print("No read from channel for a while");
        //has time out for heart beat, to close the connection to trigger reconnection
        try {
            ctx.close();
            ctx.disconnect().sync();
            LogUtils.console_print("reader idle event fired, disconnect server");
        } catch (InterruptedException e) {
            e.printStackTrace();
            LogUtils.console_print("Fail to disconnect the server");
        }finally {
            if(ctx != null){
                ctx.close();
            }
        }

    }

    protected void handlerAllIdleEvent(ChannelHandlerContext ctx){
        LogUtils.console_print("No read or write for a while");
        //nothing to do now!
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        if (!handshakeFuture.isDone()) {
            handshakeFuture.setFailure(cause);
        }
        LogUtils.console_print("exceptionCaught "+ cause.getMessage());
        if (ctx != null){
            ctx.close();
        }

    }


    public Heartbeat getHeartbeat() {
        return heartbeat;
    }

    public void setHeartbeat(Heartbeat heartbeat) {
        this.heartbeat = heartbeat;
    }

    public WebsocketConfig getWebsocketConfig() {
        return websocketConfig;
    }

    public void setWebsocketConfig(WebsocketConfig websocketConfig) {
        this.websocketConfig = websocketConfig;
    }
}
