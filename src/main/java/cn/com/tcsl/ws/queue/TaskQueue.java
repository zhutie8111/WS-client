package cn.com.tcsl.ws.queue;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class TaskQueue {
	
	private static TaskQueue tQueue;
	
	private BlockingQueue<MessageMission> queue = new ArrayBlockingQueue<MessageMission>(200);
	
	public synchronized static TaskQueue getInstance(){
		if (tQueue == null){
			tQueue = new TaskQueue();
			TaskProcessThread taskProcessThread = new TaskProcessThread(tQueue);
			Thread t = new Thread(taskProcessThread);
			t.start();
		}

		return tQueue;
	}
	

	public BlockingQueue<MessageMission> get(){
		return queue;
	}

	
}
