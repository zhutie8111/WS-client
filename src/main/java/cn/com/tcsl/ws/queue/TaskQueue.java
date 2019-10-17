package cn.com.tcsl.ws.queue;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TaskQueue {
	
	private static TaskQueue self;
	
	private BlockingQueue<MessageMission> blockingQueue = new LinkedBlockingQueue<MessageMission>();
	
	public synchronized static TaskQueue getInstance(){
		if (self == null){
			self = new TaskQueue();
			TaskProcessThread taskProcessThread = new TaskProcessThread();
			Thread t = new Thread(taskProcessThread);
			t.setName("TaskProcessThread");
			t.start();
		}

		return self;
	}

	public boolean add(MessageMission messageMission){
		boolean success = blockingQueue.offer(messageMission);
		return success;
	}

	public MessageMission get(){
		try {
			MessageMission messageMission = blockingQueue.take();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
