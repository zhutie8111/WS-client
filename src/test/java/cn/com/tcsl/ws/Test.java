package cn.com.tcsl.ws;

import cn.com.tcsl.ws.message.DefaultReceiveMessage;
import cn.com.tcsl.ws.message.PushMessage;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Demo
 * Created by Tony on 2018/11/3.
 */

public class Test {

    private WebsocketConfig config;

    private ClientInstance instance;

    public static void main(String args[]){
        Test t = new Test();

        t.before();
        t.ready();
        t.connectToServer();
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.sendPing();
        t.close();
    }

    public void before(){
        config = new WebsocketConfig();
        config.setHost("192.168.9.214");
        config.setPort(9001);
        config.setPath("websocket");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", 318);

        config.setSuffixParams(params);

        config.setKeepAlive(true);
        config.setAutoRebootClient(true);
    }


    public void ready(){
        instance = new WebsocketClientInstance(new WebsocketPushClient(config, new DefaultReceiveMessage(){



        }));



    }

    public void sendPing(){

        PushMessage pushMessage = null;
        try {

           if( !instance.isReady()) return;
            pushMessage = new PushMessage(instance.getChannel());
            pushMessage.ping();
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    public void connectToServer(){

        instance.connect();
    }


    public void close(){
        instance.closeConnection();
    }



}
