package cn.com.tcsl.ws.message;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 * Abstract class for receiving message implementation.<br>
 *
 * Created by Tony on 2018/11/5.
 */
public abstract class ReceiveMessage {

    public void onMessage(Channel channel, String text){

    }

    public void onMessage(Channel channel, byte[] bytes){

    }

    public void onMessage(Channel channel, WebSocketFrame frame){

    }

}
