package cn.com.tcsl.ws;

import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;

import java.util.concurrent.*;

/**
 * Created by Tony on 2018/11/3.
 */
public class WebsocketClientInstance {

    private WebsocketConfig websocketConfig;

    private WebsocketPushClient websocketPushClient;

    private Channel channel;

    private Future<WebsocketPushClient> future;

    public WebsocketClientInstance(WebsocketPushClient client){
        this.websocketPushClient = client;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        future = executorService.submit(new ClientThread(websocketPushClient));



       // connect();
    }

    public WebsocketClientInstance(WebsocketConfig config, WebsocketPushClient client){
        this.websocketConfig = config;
        this.websocketPushClient = client;

        websocketPushClient.setWebsocketConfig(websocketConfig);

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        future = executorService.submit(new ClientThread(websocketPushClient));


        // connect();
    }

    protected void connect(){

        try {
            websocketPushClient.connect();
            channel = websocketPushClient.getChannel();
        } catch (Exception e) {
            e.printStackTrace();

        }

    }

    public void closeConnection(){
        /*if (getChannel() != null){
            if (getChannel().isActive()){
                CloseWebSocketFrame closeWebSocketFrame = new CloseWebSocketFrame();
                getChannel().writeAndFlush(closeWebSocketFrame);
            }else{
                throw new RuntimeException("Channel is not active");
            }
        }else{
            throw new RuntimeException("Channel is null");
        }*/
        try{

            if (future.isDone()){

                WebsocketPushClient client = future.get();
                Channel ch = client.getChannel();
                ch.writeAndFlush(new CloseWebSocketFrame());
                ch.closeFuture().sync();
                client.getGroupCopy().shutdownGracefully();
            }


        }catch (Exception e){
            e.printStackTrace();
        }


    }

    protected Channel getChannel(){

       // if (future.isDone()){
            try {
                channel = future.get().getChannel();
                return channel;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        //}
        return null;
    }

    public void setWebsocketConfig(WebsocketConfig websocketConfig) {
        this.websocketConfig = websocketConfig;
    }

    public void setWebsocketPushClient(WebsocketPushClient websocketPushClient) {
        this.websocketPushClient = websocketPushClient;
    }

    public class ClientThread implements Callable<WebsocketPushClient>{

        private  WebsocketPushClient websocketPushClient;


        public ClientThread(WebsocketPushClient client){
            this.websocketPushClient = client;
        }


        public WebsocketPushClient call() throws Exception {
            websocketPushClient.connect();
           // return websocketPushClient.getChannel();
            return websocketPushClient;
        }
    }



}
