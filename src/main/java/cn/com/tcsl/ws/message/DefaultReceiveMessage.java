package cn.com.tcsl.ws.message;

import cn.com.tcsl.ws.utils.GZIPUtil;
import cn.com.tcsl.ws.utils.LogUtils;
import io.netty.channel.Channel;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;

/**
 *
 * Basic implementation of received message.<br>
 *
 * Created by zhu tie on 2019/1/14.
 */
public class DefaultReceiveMessage extends ReceiveMessage {

    @Override
    public void onMessage(Channel channel, String text) {
        super.onMessage(channel, text);
        LogUtils.console_print("message: " + text);
    }

    @Override
    public void onMessage(Channel channel, byte[] bytes) {
        super.onMessage(channel, bytes);
        try{
            String t = new String(GZIPUtil.uncompress(bytes), "Utf-8");
            LogUtils.console_print("message: " + t);
        }catch (Exception e){
            e.printStackTrace();
            LogUtils.console_print("Fail to process byte message");
        }

    }

    @Override
    public void onMessage(Channel channel, WebSocketFrame frame) {
        super.onMessage(channel, frame);
        LogUtils.console_print("message: " + frame.toString());
    }
}
