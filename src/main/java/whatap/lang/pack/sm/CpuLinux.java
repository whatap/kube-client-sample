package whatap.lang.pack.sm;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.MapValue;
import whatap.lang.value.Value;
import whatap.org.json.JSONObject;

public class CpuLinux implements Cpu {
	public float usr;
	public float sys;
	public float idle;
	public float nice;
	public float irq;
	public float softirq;
	public float steal;
	public float iowait;
	public float load1;
	public float load5;
	public float load15;

	public Cpu create() {
		return new CpuLinux();
	}

	public float pct() {
		return 100f - idle;
	}

	public void read(DataInputX din) {
		this.usr = din.readFloat();
		this.sys = din.readFloat();
		this.idle = din.readFloat();
		this.nice = din.readFloat();
		this.irq = din.readFloat();
		this.softirq = din.readFloat();
		this.steal = din.readFloat();
		this.iowait = din.readFloat();
	}

	public void write(DataOutputX dout) {
		dout.writeFloat(this.usr);
		dout.writeFloat(this.sys);
		dout.writeFloat(this.idle);
		dout.writeFloat(this.nice);
		dout.writeFloat(this.irq);
		dout.writeFloat(this.softirq);
		dout.writeFloat(this.steal);
		dout.writeFloat(this.iowait);
	}
	
	public JSONObject toJson(){
		return new JSONObject()
				.put("usr", this.usr)
				.put("sys", this.sys)
				.put("idle", this.idle)
				.put("nice", this.nice)
				.put("irq", this.irq)
				.put("softirq", this.softirq)
				.put("steal", this.steal)
				.put("iowait", this.iowait)
				.put("load1", this.load1)
				.put("load5", this.load5)
				.put("load15", this.load15);
	}

	public Value toMapValue() {
		MapValue value = new MapValue();
		value.put("usr", this.usr);
		value.put("sys", this.sys);
		value.put("idle", this.idle);
		value.put("nice", this.nice);
		value.put("irq", this.irq);
		value.put("softirq", this.softirq);
		value.put("steal", this.steal);
		value.put("iowait", this.iowait);
		value.put("load1", this.load1);
		value.put("load5", this.load5);
		value.put("load15", this.load15);
		
		return value;
	}

	public void readv2(DataInputX dinx) {
		DataInputX din = new DataInputX(dinx.readBlob());
		this.usr = din.readFloat();
		this.sys = din.readFloat();
		this.idle = din.readFloat();
		this.nice = din.readFloat();
		this.irq = din.readFloat();
		this.softirq = din.readFloat();
		this.steal = din.readFloat();
		this.iowait = din.readFloat();
		
		this.load1 = din.readFloat();
		this.load5 = din.readFloat();
		this.load15 = din.readFloat();
	}

	public void writev2(DataOutputX doutx) {
		DataOutputX dout= new DataOutputX();
		dout.writeFloat(this.usr);
		dout.writeFloat(this.sys);
		dout.writeFloat(this.idle);
		dout.writeFloat(this.nice);
		dout.writeFloat(this.irq);
		dout.writeFloat(this.softirq);
		dout.writeFloat(this.steal);
		dout.writeFloat(this.iowait);
		
		dout.writeFloat(this.load1);
		dout.writeFloat(this.load5);
		dout.writeFloat(this.load15);	
		
		doutx.writeBlob(dout.toByteArray());
	}

	
	public float stealpct() {		
		return this.steal;
	}
}
