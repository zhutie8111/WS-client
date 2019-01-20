package cn.com.tcsl.ws;

import cn.com.tcsl.ws.message.ReceiveMessage;
import cn.com.tcsl.ws.utils.GZIPUtil;
import io.netty.channel.Channel;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2019/1/17.
 */
public class UsageTest {







    public static  void main(String args[]){
        WebsocketConfig config = new WebsocketConfig();
        config.setHost("192.168.9.215");
        config.setPort(9001);
        config.setPath("websocket");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", 318);
        config.setSuffixParams(params);
        ReceiveMessage callback = new ReceiveMessage(){
            @Override
            public void onMessage(Channel channel, byte[] bytes) {
                super.onMessage(channel, bytes);
                try{
                    String t = new String(GZIPUtil.uncompress(bytes), "Utf-8");
                }catch(Exception e) {

                }

            }
        };

        WebsocketPushClient client = new WebsocketPushClient(config, callback);
        ClientInstance clientInstance = new WebsocketClientInstance(client);

        clientInstance.connect();

        clientInstance.closeConnection();
    }

}
