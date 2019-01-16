package cn.com.tcsl.ws;

import cn.com.tcsl.ws.message.PushMessage;
import io.netty.channel.Channel;

/**
 * The interface of web socket client instance
 *
 * Created by Tony on 2018/11/9.
 */
public interface ClientInstance {

    void connect();

    void closeConnection();

    Channel getChannel() throws Exception;

    boolean isReady();

    WebsocketPushClient getWebsocketPushClient();

    WebsocketConfig getWebsocketConfig();

    PushMessage PushMessage();

}
