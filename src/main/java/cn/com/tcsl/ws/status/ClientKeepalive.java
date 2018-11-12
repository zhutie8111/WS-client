package cn.com.tcsl.ws.status;

import cn.com.tcsl.ws.ClientInstance;
import cn.com.tcsl.ws.WebsocketConfig;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by Administrator on 2018/11/9.
 */
public class ClientKeepalive {

    private ClientInstance clientInstance;

    private WebsocketConfig config;

    public void watcher(){
        ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1));

        //getClientInstance().getWebsocketPushClient().setWebsocketConfig(config);
        StatusInspector inspector = new StatusInspector(getClientInstance());
        inspector.setAutoReboot(true);

        executorService.execute(inspector);
        executorService.shutdown();

    }

    public ClientInstance getClientInstance() {
        return clientInstance;
    }

    public void setClientInstance(ClientInstance clientInstance) {
        this.clientInstance = clientInstance;
    }

    public WebsocketConfig getConfig() {
        return config;
    }

    public void setConfig(WebsocketConfig config) {
        this.config = config;
    }
}
