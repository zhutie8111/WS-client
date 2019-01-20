package cn.com.tcsl.ws.exception;

public class WebSocketClientException extends RuntimeException{

	
	/**
	 * 
	 */
	private static final long serialVersionUID = 2319322756293451675L;

	public WebSocketClientException(String msg){
		super(msg);
	}

	public WebSocketClientException(Throwable t){
		super(t);
	}
	
	public WebSocketClientException(String msg, Throwable e){
		super(msg, e);
	}
	
	

}
