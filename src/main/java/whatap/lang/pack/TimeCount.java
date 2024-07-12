package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;

public  class TimeCount {
	public int count;
	public int error;
	public long time;

	public TimeCount() {
	}

	public TimeCount(int count, int error, long time) {
		this.count = count;
		this.error = error;
		this.time = time;
	}

	public void add(int time, boolean error) {
		this.count++;
		this.time += time;
		if (error)
			this.error++;
	}

	public void merge(TimeCount o) {
		this.count += o.count;
		this.time += o.time;
		this.error += o.error;
	}

	public TimeCount copy() {
		return new TimeCount(count, error, time);
	}

	public TimeCount read(DataInputX in) {
		this.count = (int) in.readDecimal();
		this.error = (int) in.readDecimal();
		this.time = in.readDecimal();
		return this;
	}

	public TimeCount write(DataOutputX o) {
		o.writeDecimal(this.count);
		o.writeDecimal(this.error);
		o.writeDecimal(this.time);
		return this;
	}
}