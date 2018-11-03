package cn.com.tcsl.ws;

import java.net.URI;

/**
 * Created by Administrator on 2018/11/3.
 */

public class Test {


    public static void main(String args[]){
        String url = "ws://192.168.9.215:9001/websocket?shopId=325";
        try{
            URI uri = new URI(url);
            System.out.println(uri.getPort());
            System.out.println(uri.getScheme());
            System.out.println(uri.getHost());
        }catch (Exception e){

        }

    }

}
