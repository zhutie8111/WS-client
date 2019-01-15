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

    private Integer readerIdleTimeSeconds = 60;

    private Integer writerIdleTimeSeconds = 60;

    private Integer allIdleTimeSeconds = 120;

    private Boolean needHeartBeat = Boolean.FALSE;


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

    public Integer getReaderIdleTimeSeconds() {
        return readerIdleTimeSeconds;
    }

    public void setReaderIdleTimeSeconds(Integer readerIdleTimeSeconds) {
        this.readerIdleTimeSeconds = readerIdleTimeSeconds;
    }

    public Integer getWriterIdleTimeSeconds() {
        return writerIdleTimeSeconds;
    }

    public void setWriterIdleTimeSeconds(Integer writerIdleTimeSeconds) {
        this.writerIdleTimeSeconds = writerIdleTimeSeconds;
    }

    public Integer getAllIdleTimeSeconds() {
        return allIdleTimeSeconds;
    }

    public void setAllIdleTimeSeconds(Integer allIdleTimeSeconds) {
        this.allIdleTimeSeconds = allIdleTimeSeconds;
    }

    public Boolean getNeedHeartBeat() {
        return needHeartBeat;
    }

    public void setNeedHeartBeat(Boolean needHeartBeat) {
        this.needHeartBeat = needHeartBeat;
    }
}
