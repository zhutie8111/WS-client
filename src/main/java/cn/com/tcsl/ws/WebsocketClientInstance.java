package cn.com.tcsl.ws;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import cn.com.tcsl.ws.exception.WebSocketClientException;
import cn.com.tcsl.ws.status.ClientKeepalive;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;

/**
 * Created by Tony on 2018/11/3.
 */
public class WebsocketClientInstance implements ClientInstance {

    private WebsocketConfig websocketConfig;

    private WebsocketPushClient websocketPushClient;

    private Channel channel;

    private Future<WebsocketPushClient> future;

    private ClientKeepalive clientKeepalive;

    public WebsocketClientInstance(WebsocketPushClient client){
        this.websocketPushClient = client;
    }

    /**
     * create a thread to start the websocket client
     * 
     */
    public void connect(){

        if (this.websocketPushClient != null){

            //websocketPushClient.setReceiveMessage(websocketPushClient.getReceiveMessage());
           // websocketPushClient.setWebsocketConfig(websocketPushClient.getWebsocketConfig());

            ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1));
            //ExecutorService executorService = Executors.newSingleThreadExecutor();

            future = executorService.submit(new ClientWorkerThread(websocketPushClient));
            executorService.shutdown();


        }
        
        Boolean keepLiveFlag = websocketPushClient.getWebsocketConfig().getKeepAlive();
        if ( keepLiveFlag != null && keepLiveFlag){
                clientKeepalive = new ClientKeepalive();
                clientKeepalive.setClientInstance(this);
                clientKeepalive.watcher();
        }
    }


    public void closeConnection(){

        try{

            if (future != null && future.isDone()){

                WebsocketPushClient client = future.get();

                if (client != null){
                    Channel ch = client.getChannel();
                     
                    ch.writeAndFlush(new CloseWebSocketFrame());
                    ch.closeFuture().sync();
                    ch.close();
                    if (client.getGroupCopy() == null){
                    	client.getGroupCopy().shutdownGracefully();
                    }
                }

            }else{
                throw new WebSocketClientException("Fail to get Websocket client.");
            }

        }catch (Exception e){
            e.printStackTrace();
        }


    }

    public Channel getChannel() throws Exception{

        if (future != null && future.isDone()){
            channel = future.get().getChannel();
            return channel;
        }

        return null;
    }


    public void setWebsocketPushClient(WebsocketPushClient websocketPushClient) {
        this.websocketPushClient = websocketPushClient;
    }

    public WebsocketPushClient getWebsocketPushClient() {
        return websocketPushClient;
    }

    public WebsocketConfig getWebsocketConfig() {
        return websocketConfig;
    }

    public void setWebsocketConfig(WebsocketConfig websocketConfig) {
        this.websocketConfig = websocketConfig;
    }

    public boolean isReady(){
        if (channel != null && channel.isActive()){
            return true;
        }else{
            return false;
        }
    }



}
