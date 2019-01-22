package cn.com.tcsl.ws.queue;

public class TaskProcessThread implements Runnable{
	
	private TaskQueue tqueue;
	
	public TaskProcessThread(TaskQueue tq){
		tqueue = tq;
	}

	public void run() {
		
		while (true){
			try {
				MessageMission mm = tqueue.get().take();
				mm.execute();
			} catch (InterruptedException e) {
				
				e.printStackTrace();
			}
		}
		
	}

}
