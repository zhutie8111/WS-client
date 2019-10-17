package cn.com.tcsl.ws.queue;

public class TaskProcessThread implements Runnable{
	

	public void run() {

		TaskQueue taskQueue = TaskQueue.getInstance();

		while (true){
			try {

				MessageMission mm = taskQueue.get();
				if (mm != null){
					mm.execute();
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

}
