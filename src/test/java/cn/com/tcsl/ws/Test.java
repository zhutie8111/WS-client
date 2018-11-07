package cn.com.tcsl.ws;

import cn.com.tcsl.ws.utils.GZIPUtil;
import io.netty.channel.Channel;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Demo
 * Created by Tony on 2018/11/3.
 */

public class Test {


    public static void main(String args[]){

       /* String url = "ws://192.168.9.215:9001/websocket?shopId=325";
        try{
            URI uri = new URI(url);
            System.out.println(uri.getPort());
            System.out.println(uri.getScheme());
            System.out.println(uri.getHost());
        }catch (Exception e){

        }*/

        Test test = new Test();

        WebsocketConfig config = new WebsocketConfig();
        config.setHost("192.168.9.215");
        config.setPort(9001);
        config.setPath("websocket");
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("shopId", 318);

        config.setSuffixParams(params);

        for (int i = 0 ;i <1000; i++){
            config.getSuffixParams().put("shopId", 318+i);
            Thread t = new Thread(new clientThread(test, config));
            t.start();
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }







    }


    private void wsclient(WebsocketConfig config){


        WebsocketClientCreator creator = new WebsocketClientCreator(new WebsocketPushClient(config, new PushMessage(){


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

    public static class clientThread implements Runnable{

        private Test t;

        private   WebsocketConfig config;

        public clientThread(Test test, WebsocketConfig config){
            this.t = test;
            this.config = config;
        }

        public void run() {
            t.wsclient(config);
        }
    }

}
