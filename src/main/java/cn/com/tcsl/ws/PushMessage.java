package cn.com.tcsl.ws;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.BinaryWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PingWebSocketFrame;
import io.netty.handler.codec.http.websocketx.PongWebSocketFrame;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;

/**
 * Created by Tony on 2018/11/7.
 */
public class PushMessage {

    private Channel channel;

    public PushMessage(){};

    public PushMessage(Channel channel){
        this.channel = channel;
    }

    public void send(Object object){
        if (channel != null ){

            if (channel.isActive()){
                if (object instanceof String){
                    String text = (String) object;
                    TextWebSocketFrame textWebSocketFrame = new TextWebSocketFrame(text);
                    channel.writeAndFlush(textWebSocketFrame);

                }else if(object instanceof byte[]){
                    byte[] bytes = (byte[])object;
                    ByteBuf buf = Unpooled.buffer();

                    buf.writeBytes(bytes);
                    BinaryWebSocketFrame binaryWebSocketFrame = new BinaryWebSocketFrame(buf);
                    channel.writeAndFlush(binaryWebSocketFrame);
                }else{
                    channel.writeAndFlush(object);
                }

            }else{
                throw new RuntimeException("The channel was not active.");
            }

        }else{
            throw new RuntimeException("There was not valid channel to send message.");
        }

    }

    public void ping(){
        if (channel != null){
            if (channel.isActive()){
                PingWebSocketFrame pingWebSocketFrame = new PingWebSocketFrame();
                channel.writeAndFlush(pingWebSocketFrame);
            }else{
                throw new RuntimeException("The channel was not active.");
            }

        }else{
            throw new RuntimeException("There was not valid channel to send message.");
        }
    }

    public void pong(){
        if (channel != null){
            if (channel.isActive()){
                PongWebSocketFrame pongWebSocketFrame = new PongWebSocketFrame();
                channel.writeAndFlush(pongWebSocketFrame);
            }else{
                throw new RuntimeException("The channel was not active.");
            }

        }else{
            throw new RuntimeException("There was not valid channel to send message.");
        }
    }




    public void setChannel(Channel channel) {
        this.channel = channel;
    }
}
