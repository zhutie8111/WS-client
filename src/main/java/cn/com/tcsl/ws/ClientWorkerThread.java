package cn.com.tcsl.ws;

import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2018/11/12.
 */
public class ClientWorkerThread implements Callable<WebsocketPushClient> {


    private WebsocketPushClient websocketPushClient;

    public ClientWorkerThread(WebsocketPushClient client){
        this.websocketPushClient = client;
    }

    public WebsocketPushClient call() throws Exception {
        websocketPushClient.connect();
        return websocketPushClient;
    }

}
