package whatap.util;

public class DeferedRun<V> {
	public static interface Exec<V> {
		public V call();
	}

	private long interval;
	private long last_exec;
	private V value;
	private V def;
	private Exec<V> exec;

	public DeferedRun(Exec<V> exec, long interval, V def) {
		this.interval = interval;
		this.def = def;
		this.exec = exec;
	}

	public V call() {
		long now = System.currentTimeMillis();
		if (last_exec == 0 || now > last_exec + interval) {
			last_exec = now;
			value = (V) exec.call();
		}
		return value == null ? def : value;
	}

	public void setInterval(long t) {
		this.interval = t;
	}
}
