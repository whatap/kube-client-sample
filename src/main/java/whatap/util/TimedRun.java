package whatap.util;

public class TimedRun {
	long lasttime;
	private Runnable runnable;

	public TimedRun() {
	}

	public TimedRun(Runnable runnable) {
		this.runnable = runnable;
	}

	public void call(int interval) {
		long now =  System.currentTimeMillis();
		if (now - lasttime > interval) {
			lasttime = now;
			try {
				runnable.run();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	public boolean isOk(int interval) {
		long now =  System.currentTimeMillis();
		if (now - lasttime > interval) {
			lasttime = now;
			return true;
		}
		return false;
	}
}
