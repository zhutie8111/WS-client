package cn.com.tcsl.ws.status;

import cn.com.tcsl.ws.WebsocketClientInstance;
import io.netty.channel.Channel;

/**
 * Created by Administrator on 2018/11/9.
 */
public class StatusInspector implements Runnable{

    private WebsocketClientInstance clientInstance;

    private boolean running = true;

    private boolean autoReboot = false;

    public StatusInspector( WebsocketClientInstance clientInstance){
        this.clientInstance = clientInstance;
    }

    public void run() {
        while (running){
            if (clientInstance.isReady()){

                try {
                    Channel channel = clientInstance.getChannel();
                    if (channel == null){
                        if (isAutoReboot()){
                            //TODO: restart automatically
                            clientInstance.connect();
                        }

                    }else if (channel != null && !channel.isActive()){

                        //TODO: restart automatically

                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else{

            }

        }

    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public boolean isAutoReboot() {
        return autoReboot;
    }

    public void setAutoReboot(boolean autoReboot) {
        this.autoReboot = autoReboot;
    }
}
