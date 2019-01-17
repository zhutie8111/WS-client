package cn.com.tcsl.ws;

import cn.com.tcsl.ws.message.Sender;
import io.netty.channel.Channel;

/**
 * The common interface of web socket client instance <br>
 *
 * <h3>Usage Example</h3>
 *
 * The common steps to create WS client:
 *
 * <pre> {@code
 *
 * WebsocketConfig config = new WebsocketConfig();
 *  config.setHost(IP);
 *  config.setPort(PORT);
 *  config.setPath("websocket");
 *  Map<String, Object> params = new HashMap<String, Object>();
 *  params.put("shopId", 318);
 *  config.setSuffixParams(params);
 *  ReceiveMessage callback = new ReceiveMessage(){
 *  @Override
 *  public void onMessage(Channel channel, byte[] bytes) {
 *  super.onMessage(channel, bytes);
 *  try{
 *  String t = new String(GZIPUtil.uncompress(bytes), "Utf-8");
 *  }catch(Exception e) {
 *
 *  }
 *
 *  }
 *  };
 *
 *  WebsocketPushClient client = new WebsocketPushClient(config, callback);
 *  ClientInstance clientInstance = new WebsocketClientInstance(client);
 *
 *  clientInstance.connect();
 *
 * }</pre>
 *
 *
 *
 *
 * Created by Tony on 2018/11/9.
 */
public interface ClientInstance {

    /**
     * create a connection to WS server
     */
    void connect();

    /**
     * close the connection of this client from server
     */
    void closeConnection();

    /**
     * Fetch the channel created by client instance
     *
     * @return
     * @throws Exception
     */
    Channel getChannel() throws Exception;


    /**
     * True means the channel is ready.
     * @return
     */
    boolean isReady();

    /**
     * Get the intance of web socket client
     * @return
     */
    WebsocketPushClient getWebsocketPushClient();

    /**
     * Get the configuration about this client
     * @return
     */
    WebsocketConfig getWebsocketConfig();

    /**
     * Get the default message sender
     * @return
     */
    Sender PushMessage();

}
