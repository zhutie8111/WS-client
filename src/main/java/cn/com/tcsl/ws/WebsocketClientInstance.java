package cn.com.tcsl.ws;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;

import java.util.concurrent.*;

/**
 * Created by Tony on 2018/11/3.
 */
public class WebsocketClientInstance implements ClientInstance {

    private WebsocketConfig websocketConfig;

    private WebsocketPushClient websocketPushClient;

    private Channel channel;

    private Future<WebsocketPushClient> future;

    public WebsocketClientInstance(WebsocketPushClient client){
        this.websocketPushClient = client;
    }

    public void connect(){
        if (this.websocketPushClient != null){

            ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1));
            //ExecutorService executorService = Executors.newSingleThreadExecutor();
            future = executorService.submit(new ClientThread(websocketPushClient));
            executorService.shutdown();
        }
    }


    public void closeConnection(){

        try{

            if (future != null && future.isDone()){

                WebsocketPushClient client = future.get();
                Channel ch = client.getChannel();
                ch.writeAndFlush(new CloseWebSocketFrame());
                ch.closeFuture().sync();
                client.getGroupCopy().shutdownGracefully();
            }else{
                throw new RuntimeException("Fail to get Websocket client.");
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

    public class ClientThread implements Callable<WebsocketPushClient>{

        private  WebsocketPushClient websocketPushClient;

        public ClientThread(WebsocketPushClient client){
            this.websocketPushClient = client;
        }

        public WebsocketPushClient call() throws Exception {
            websocketPushClient.connect();
            return websocketPushClient;
        }
    }


    public boolean isReady(){
        if (channel != null && channel.isActive()){
            return true;
        }else{
            return false;
        }
    }



}
