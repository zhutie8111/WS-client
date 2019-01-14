package cn.com.tcsl.ws;

import cn.com.tcsl.ws.status.ClientKeepalive;

import java.util.concurrent.Callable;

/**
 * Created by Administrator on 2018/11/12.
 */
public class ClientWorkerThread implements Callable<WebsocketPushClient> {


    private WebsocketPushClient websocketPushClient;

    private ClientKeepalive clientKeepalive;

    public ClientWorkerThread(WebsocketPushClient client){
        this.websocketPushClient = client;

    }

    public WebsocketPushClient call() throws Exception {
        try{

            websocketPushClient.connect();

            Boolean keepLiveFlag = websocketPushClient.getWebsocketConfig().getKeepAlive();
            if ( keepLiveFlag != null && keepLiveFlag){
                clientKeepalive = new ClientKeepalive(websocketPushClient);
               // clientKeepalive.setClientInstance(this);

                clientKeepalive.watcher();
            }

        }catch (Exception e){
            e.printStackTrace();
           // throw new RuntimeException(e);
        }

        return websocketPushClient;
    }



}
