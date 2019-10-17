package cn.com.tcsl.ws.queue;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class MethodRegister {
	
	public static Map<String, MessageMission> map = new ConcurrentHashMap<String, MessageMission>();
	
	public static void register(String methodName, MessageMission clazz){
		map.put(methodName, clazz);
	}

	public static MessageMission get(String name){
		return map.get(name);
	}
	
	

}
