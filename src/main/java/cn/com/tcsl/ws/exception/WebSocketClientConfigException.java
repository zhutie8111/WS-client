package cn.com.tcsl.ws.exception;

public class WebSocketClientConfigException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public WebSocketClientConfigException(String msg){
		super(msg);
	}

	
	public WebSocketClientConfigException(String msg, Throwable e){
		super(msg, e);
	}
	
	

}
