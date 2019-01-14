package cn.com.tcsl.ws.status;

import cn.com.tcsl.ws.ClientInstance;
import cn.com.tcsl.ws.WebsocketPushClient;

/**
 * Created by Tony zhu on 2018/11/9.
 */
public class ClientKeepalive {

    private ClientInstance clientInstance;

    private WebsocketPushClient websocketPushClient;

    public ClientKeepalive(WebsocketPushClient client){
        websocketPushClient = client;
    }

    public void watcher(){
        //ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
        //        new LinkedBlockingQueue<Runnable>(1));

        //getClientInstance().getWebsocketPushClient().setWebsocketConfig(config);
        //StatusInspector inspector = new StatusInspector(getClientInstance());
        StatusInspector inspector = new StatusInspector(websocketPushClient);

        Thread thread = new Thread(inspector);
        thread.setName("StatusInspector-thread");
        thread.start();

        //executorService.execute(inspector);
        //executorService.shutdown();

    }

    public ClientInstance getClientInstance() {
        return clientInstance;
    }

    public void setClientInstance(ClientInstance clientInstance) {
        this.clientInstance = clientInstance;
    }

}
