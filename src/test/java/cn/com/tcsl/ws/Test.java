package cn.com.tcsl.ws;

import cn.com.tcsl.ws.utils.GZIPUtil;
import io.netty.channel.Channel;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Demo
 * Created by Tony on 2018/11/3.
 */

public class Test {

    private WebsocketConfig config;

    private WebsocketClientInstance creator;

    public static void main(String args[]){
        Test t = new Test();

        t.before();
        t.test1();
        try {
            TimeUnit.SECONDS.sleep(1);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        t.close();
    }

    public void before(){
        config = new WebsocketConfig();
        config.setHost("192.168.9.215");
        config.setPort(9001);
        config.setPath("websocket");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", 318);

        config.setSuffixParams(params);
    }


    public void test1(){
        creator = new WebsocketClientInstance(new WebsocketPushClient(config, new ReceiveMessage(){


            public void onMessage(Channel channel, byte[] bytes){
                String t = null;
                try {

                    t = new String(GZIPUtil.uncompress(bytes), "Utf-8");


                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                System.out.println(channel.id() + " "+ new Date() +" "+ t);
            }

        }));


    }


    public void close(){
        creator.closeConnection();
    }



}
