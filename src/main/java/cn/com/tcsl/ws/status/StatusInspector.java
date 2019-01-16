package cn.com.tcsl.ws.status;

import cn.com.tcsl.ws.ClientInstance;
import cn.com.tcsl.ws.WebsocketClientInstance;
import cn.com.tcsl.ws.WebsocketPushClient;
import cn.com.tcsl.ws.exception.WebSocketClientConfigException;
import cn.com.tcsl.ws.utils.LogUtils;
import io.netty.channel.Channel;

import java.util.concurrent.TimeUnit;

/**
 * Created by Tony zhu on 2018/11/9.
 */
public class StatusInspector implements Runnable{

    /**
     * 被监控的WS 客户端实例
     */
    private ClientInstance clientInstance;

    /**
     * websocket client
     */
    private WebsocketPushClient websocketPushClient;

    private boolean running = true;

    private long DEFAULT_CHECK_ALIVE_DURATION = 30;

    public StatusInspector(WebsocketPushClient client){
        websocketPushClient = client;
    }

    public void run() {

        setCustomizedConfig();

        try{

            while (running){

                Channel channel = websocketPushClient.getChannel();
                if (channel!=null && !channel.isActive()){

                    LogUtils.console_print("channel is not live");

                    Boolean autoRebootClient = websocketPushClient.getWebsocketConfig().getAutoRebootClient();
                    if (autoRebootClient != null){
                        if (autoRebootClient.booleanValue()){

                           // clientInstance.closeConnection();
                            //clientInstance.connect();
                            clientInstance = new WebsocketClientInstance(websocketPushClient);
                            clientInstance.connect();
                        }

                        running = false;

                    }

                }else if (channel == null){
                    LogUtils.console_print("status: channel was not created. ");
                }else{
                    LogUtils.console_print("status: " + channel.id()+" channel is working");
                }

                TimeUnit.SECONDS.sleep(DEFAULT_CHECK_ALIVE_DURATION);
            }

        }catch (Exception e){

            throw new RuntimeException(e);

        }

    }

    /**
     * set customized configuration
     */
    protected void setCustomizedConfig(){
        if (clientInstance != null){
        	Long configCheckLiveDuration = clientInstance.getWebsocketPushClient().getWebsocketConfig().getCheckLiveDuration();
        	
          if ( configCheckLiveDuration != null){
        	  
        	  if (configCheckLiveDuration <= 0){
        		  throw new WebSocketClientConfigException("invalid configuration of the parameter checkLiveDuration");
        	  }
        	  
              DEFAULT_CHECK_ALIVE_DURATION = configCheckLiveDuration;
          }
        }
    }


    /**
     * shut down inspector after thread wakeup and next running
     */
    public void shutdownInspectorGracefully(){
        running = false;
    }


}
