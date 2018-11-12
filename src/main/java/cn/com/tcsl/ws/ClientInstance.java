package cn.com.tcsl.ws;

import io.netty.channel.Channel;

/**
 * Created by Tony on 2018/11/9.
 */
public interface ClientInstance {

    public void connect();

    public void closeConnection();

    public Channel getChannel() throws Exception;

    public boolean isReady();

    public WebsocketPushClient getWebsocketPushClient();

}
