package cn.com.tcsl.ws;

import java.util.Map;

/**
 * Created by Tony on 2018/11/3.
 */
public class WebsocketConfig {

    private String scheme = "ws";

    private String host;

    private int port;

    private String path = "websocket";

    private Map<String, Object> suffixParams;

    // Optional settings below

    private Long checkLiveDuration;

    private Boolean autoRebootClient;

    private Boolean keepAlive;


    public WebsocketConfig (){
    }

    public WebsocketConfig(String host, int port){
        this.host = host;
        this.port = port;
    }


    public WebsocketConfig(String host, int port, String path){
        this.host = host;
        this.port = port;
        this.path = path;
    }

    public WebsocketConfig(String scheme, String host, int port, String path){
        this.scheme = scheme;
        this.host = host;
        this.port = port;
        this.path = path;
    }


    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public Map<String, Object> getSuffixParams() {
        return suffixParams;
    }

    public void setSuffixParams(Map<String, Object> suffixParams) {
        this.suffixParams = suffixParams;
    }

    public Long getCheckLiveDuration() {
        return checkLiveDuration;
    }

    public void setCheckLiveDuration(Long checkLiveDuration) {
        this.checkLiveDuration = checkLiveDuration;
    }

    public Boolean getAutoRebootClient() {
        return autoRebootClient;
    }

    public void setAutoRebootClient(Boolean autoRebootClient) {
        this.autoRebootClient = autoRebootClient;
    }


    public Boolean getKeepAlive() {
        return keepAlive;
    }

    public void setKeepAlive(Boolean keepAlive) {
        this.keepAlive = keepAlive;
    }
}
