package whatap.lang.pack.sm;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.MapValue;
import whatap.lang.value.Value;
import whatap.org.json.JSONObject;

public class CpuWindow implements Cpu {
	public float usr;
	public float sys;
	public float idle;
	public float processorQueueLength;

	public Cpu create() {
		return new CpuWindow();
	}
	public float pct() {
		return 100f - idle;
	}

	public void read(DataInputX din) {
		this.usr = din.readFloat();
		this.sys = din.readFloat();
		this.idle = din.readFloat();
	}

	public void write(DataOutputX dout) {
		dout.writeFloat(this.usr);
		dout.writeFloat(this.sys);
		dout.writeFloat(this.idle);
	}
	
	public JSONObject toJson(){
		return new JSONObject()
				.put("usr", this.usr)
				.put("sys", this.sys)
				.put("idle", this.idle)
				.put("queueLength", this.processorQueueLength);
	}
	
	public Value toMapValue() {
		MapValue value = new MapValue();
		value.put("usr", this.usr);
		value.put("sys", this.sys);
		value.put("idle", this.idle);
		value.put("queueLength", this.processorQueueLength);
		return value;
	}

	public void readv2(DataInputX dinx) {
		DataInputX din = new DataInputX(dinx.readBlob());
		this.usr = din.readFloat();
		this.sys = din.readFloat();
		this.idle = din.readFloat();
		this.processorQueueLength = din.readFloat();
	}
	
	public void writev2(DataOutputX doutx) {
		DataOutputX dout= new DataOutputX();
		dout.writeFloat(this.usr);
		dout.writeFloat(this.sys);
		dout.writeFloat(this.idle);
		dout.writeFloat(this.processorQueueLength);
		doutx.writeBlob(dout.toByteArray());
	}

	public float stealpct() {
		return 0;
	}
}
