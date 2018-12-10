package cn.com.tcsl.ws.status;

import java.util.concurrent.TimeUnit;

import cn.com.tcsl.ws.ClientInstance;
import cn.com.tcsl.ws.WebsocketClientInstance;
import cn.com.tcsl.ws.exception.WebSocketClientException;
import cn.com.tcsl.ws.utils.LogUtils;
import io.netty.channel.Channel;

/**
 * Created by Tony zhu on 2018/11/9.
 */
public class StatusInspector implements Runnable{

    /**
     * 被监控的WS 客户端实例
     */
    private ClientInstance clientInstance;

    private boolean running = true;

    private long DEFAULT_CHECK_ALIVE_DURATION = 30;

    public StatusInspector(ClientInstance clientInstance){
        this.clientInstance = clientInstance;
    }

    public void run() {

        setCustomizedConfig();

        try{

            while (running){

                Channel channel = clientInstance.getWebsocketPushClient().getChannel();
                if (channel!=null && !channel.isActive()){

                    LogUtils.console_print("channel is not live");

                    Boolean autoRebootClient = clientInstance.getWebsocketPushClient().getWebsocketConfig().getAutoRebootClient();
                    if (autoRebootClient != null){
                        if (autoRebootClient.booleanValue()){

                           // clientInstance.closeConnection();
                            //clientInstance.connect();
                            clientInstance = new WebsocketClientInstance(clientInstance.getWebsocketPushClient());
                            clientInstance.connect();

                            running = false;

                        }

                    }

                }else if (channel == null){
                    LogUtils.console_print("channel was not created. ");
                }else{
                    LogUtils.console_print("channel is working");
                }

                TimeUnit.SECONDS.sleep(DEFAULT_CHECK_ALIVE_DURATION);
            }

        }catch (Exception e){

            throw new RuntimeException(e);

        }

    }


    protected void setCustomizedConfig(){
        if (clientInstance != null){
        	Long configCheckLiveDuration = clientInstance.getWebsocketPushClient().getWebsocketConfig().getCheckLiveDuration();
        	
          if ( configCheckLiveDuration != null){
        	  
        	  if (configCheckLiveDuration <= 0){
        		  throw new WebSocketClientException("invalid the parameter of checkLiveDuration");
        	  }
        	  
              DEFAULT_CHECK_ALIVE_DURATION = configCheckLiveDuration;
          }
        }
    }



    public void setRunning(boolean running) {
        this.running = running;
    }


}
