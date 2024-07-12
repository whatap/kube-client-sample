package whatap.lang.pack.sm;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.MapValue;
import whatap.lang.value.Value;
import whatap.org.json.JSONObject;

public class MemoryWindow implements Memory {
	public long total;
	public long free;
	public long cached;
	public long used;
	public float pused;
	public long available;
	public float pavailable;
	public float swappused;
	public long swapused;
	public long swaptotal;
	public float pageFaults;
	
	public float pct() {
		return pused;
	}

	public void write(DataOutputX dout) {
		dout.writeDecimal(this.total);
		dout.writeDecimal(this.free);
		dout.writeDecimal(this.cached);
		dout.writeDecimal(this.used);
		dout.writeFloat(this.pused);
		dout.writeDecimal(this.available);
		dout.writeFloat(this.pavailable);
	}

	public void read(DataInputX din) {
		this.total = din.readDecimal();
		this.free = din.readDecimal();
		this.cached = din.readDecimal();
		this.used = din.readDecimal();
		this.pused = din.readFloat();
		this.available = din.readDecimal();
		this.pavailable = din.readFloat();

	}
	
	public JSONObject toJson(){
		return new JSONObject()
				.put("total", this.total)
				.put("free", this.free)
				.put("cached", this.cached)
				.put("used", this.used)
				.put("pused", this.pused)
				.put("available", this.available)
				.put("pavailable", this.pavailable)
				.put("pagefaults", this.pageFaults);
		
	}

	public Value toMapValue() {
		MapValue value = new MapValue();
		value.put("total", this.total);
		value.put("free", this.free);
		value.put("cached", this.cached);
		value.put("used", this.used);
		value.put("pused", this.pused);
		value.put("available", this.available);
		value.put("pavailable", this.pavailable);
		value.put("pagefaults", this.pageFaults);
		return value;
	}

	public float swappct() {
		return swappused;
	}

	public void readv2(DataInputX dinx) {
		DataInputX din = new DataInputX(dinx.readBlob());
		this.total = din.readDecimal();
		this.free = din.readDecimal();
		this.cached = din.readDecimal();
		this.used = din.readDecimal();
		this.pused = din.readFloat();
		this.available = din.readDecimal();
		this.pavailable = din.readFloat();
		this.pageFaults = din.readFloat();
		if(din.available() > 0){
			this.swapused = din.readDecimal();
			this.swappused = din.readFloat();
			this.swaptotal = din.readDecimal();
		}		
	}

	public void writev2(DataOutputX doutx) {
		DataOutputX dout= new DataOutputX();
		dout.writeDecimal(this.total);
		dout.writeDecimal(this.free);
		dout.writeDecimal(this.cached);
		dout.writeDecimal(this.used);
		dout.writeFloat(this.pused);
		dout.writeDecimal(this.available);
		dout.writeFloat(this.pavailable);
		dout.writeFloat(this.pageFaults);
		dout.writeDecimal(this.swapused);
		dout.writeFloat(this.swappused);
		dout.writeDecimal(this.swaptotal);
		
		doutx.writeBlob(dout.toByteArray());
	}
}
