package cn.com.tcsl.ws.exception;

public class WebSocketClientException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	public WebSocketClientException(String msg){
		super(msg);
	}

	
	public WebSocketClientException(String msg, Throwable e){
		super(msg, e);
	}

}
