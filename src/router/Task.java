package router;

import java.util.TimerTask;

public class Task extends TimerTask {

	private Router router;
	
	public Task(Router router) {
		this.router = router;
	}

	@Override
	public void run() {
		//System.out.println("Timer expire");
		router.expire();
	}

}
