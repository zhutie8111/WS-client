package cn.com.tcsl.ws.message;

import io.netty.channel.Channel;

/**
 *
 * WS client sending message
 *
 * Created by zhu tie on 2019/1/17.
 */
public interface Sender {

    /**
     * add a channel to sender
     * @param channel
     */
    void setChannel(Channel channel);

    /**
     * send message, usually the object can be type of String, bytes, websocket frame those three types.
     * @param object
     */
    void send(Object object);

    /**
     * send a ping to server
     */
    void ping();

    /**
     * send a pong to server
     */
    void pong();

}
