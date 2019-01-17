package cn.com.tcsl.ws;

import cn.com.tcsl.ws.exception.WebSocketClientException;
import cn.com.tcsl.ws.message.PushMessage;
import cn.com.tcsl.ws.message.Sender;
import cn.com.tcsl.ws.utils.LogUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.CloseWebSocketFrame;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

/**
 * Created by Tony on 2018/11/3.
 */
public class WebsocketClientInstance implements ClientInstance {

    private Logger logger = LoggerFactory.getLogger(LogUtils.LOG_PROFILE_NAME);

    private WebsocketConfig websocketConfig;

    private WebsocketPushClient websocketPushClient;

    private Channel channel;

    private Future<WebsocketPushClient> future;

    public WebsocketClientInstance(WebsocketPushClient client){
        this.websocketPushClient = client;
    }

    private Sender sender;


    /**
     * create a thread to start the websocket client
     * 
     */
    public void connect(){

        if (this.websocketPushClient != null){

            ExecutorService executorService = new ThreadPoolExecutor(1, 1, 0L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1));
            //ExecutorService executorService = Executors.newSingleThreadExecutor();

            future = executorService.submit(new ClientWorkerThread(websocketPushClient));
            executorService.shutdown();

        }else{
            LogUtils.console_print("websocketPushClient is null");
        }
        

    }

    /**
     * close a connection manually
     */
    public void closeConnection(){

        try{

            if (future != null && future.isDone()){

                WebsocketPushClient client = future.get();

                if (client != null){

                    //close auto reconnection
                    client.getWebsocketConfig().setAutoRebootClient(false);

                    Channel ch = client.getChannel();
                    if (ch != null){
                        //send close frame to server initiatively
                        ch.writeAndFlush(new CloseWebSocketFrame());
                        ch.closeFuture().sync();
                        ch.close();
                    }

                    //close event loop
                    if (client.getGroupCopy() != null){
                    	client.getGroupCopy().shutdownGracefully();
                    }else{
                        ch.eventLoop().shutdownGracefully();
                    }
                }

            }else{
                if (websocketPushClient != null){
                    websocketPushClient.getChannel().close().sync();
                }

                throw new WebSocketClientException("Fail to get Websocket client object. websocket client was in abnormal status");
            }

        }catch (Exception e){
            e.printStackTrace();
            logger.error("Fail to shutdown client", e);
        }


    }

    /**
     * fetch the channel from connection
     * @return
     * @throws Exception
     */
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
        return websocketPushClient.getWebsocketConfig();
        //return websocketConfig;
    }

/*    public void setWebsocketConfig(WebsocketConfig websocketConfig) {
        this.websocketConfig = websocketConfig;
    }*/

    /**
     * indicating the status of channel
     * @return
     */
    public boolean isReady(){
        if (channel != null && channel.isActive()){
            return true;
        }else{
            return false;
        }
    }

    /**
     * Fetch the object of sending message
     *
     * @return sending message object
     */
    public Sender PushMessage(){
        if (sender == null){
            sender = new PushMessage(websocketPushClient.getChannel());
        }
        return sender;

    }


}
