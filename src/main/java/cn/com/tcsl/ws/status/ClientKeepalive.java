package cn.com.tcsl.ws.status;

import cn.com.tcsl.ws.WebsocketPushClient;

/**
 *
 * Inspect the status of websocket client, starting a independent thread outside
 * web socket client
 *
 *
 * Created by Tony zhu on 2018/11/9.
 */
public class ClientKeepalive {

    private WebsocketPushClient websocketPushClient;

    ClientKeepalive(){}

    public ClientKeepalive(WebsocketPushClient client){
        websocketPushClient = client;
    }

    /**
     * Instance a watcher to look at the status of web socket client
     */
    public void watcher(){
        StatusInspector inspector = new StatusInspector(websocketPushClient);
        Thread thread = new Thread(inspector);
        thread.setName("StatusInspector-thread");
        thread.start();
    }

}
