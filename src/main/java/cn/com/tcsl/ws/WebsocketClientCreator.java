package cn.com.tcsl.ws;

import io.netty.channel.Channel;

/**
 * Created by Administrator on 2018/11/3.
 */
public class WebsocketClientCreator {

    private WebsocketConfig websocketConfig;

    private WebsocketPushClient websocketPushClient;

    private Channel channel;

    public WebsocketClientCreator(WebsocketPushClient client){
        this.websocketPushClient = client;
        connect();
    }

    public WebsocketClientCreator (WebsocketConfig config, WebsocketPushClient client){
        this.websocketConfig = config;
        this.websocketPushClient = client;

        websocketPushClient.setWebsocketConfig(websocketConfig);
        //websocketPushClient.setPushMessage();
        connect();
    }

    protected void connect(){

        try {
            websocketPushClient.connect();
            channel = websocketPushClient.getChannel();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public Channel getChannel(){
       return this.channel;
    }

    public void setWebsocketConfig(WebsocketConfig websocketConfig) {
        this.websocketConfig = websocketConfig;
    }

    public void setWebsocketPushClient(WebsocketPushClient websocketPushClient) {
        this.websocketPushClient = websocketPushClient;
    }



}
