package cn.com.tcsl.ws;

/**
 * Created by Administrator on 2018/11/3.
 */
public class WebsocketClientCreator {

    private WebsocketConfig websocketConfig;

    private WebsocketPushClient websocketPushClient;

    public WebsocketClientCreator (WebsocketConfig config, WebsocketPushClient client){
        this.websocketConfig = config;
        this.websocketPushClient = client;

        websocketPushClient.setWebsocketConfig(websocketConfig);

        try {
            websocketPushClient.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
