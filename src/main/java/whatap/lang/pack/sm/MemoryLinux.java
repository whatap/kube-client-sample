package whatap.lang.pack.sm;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.MapValue;
import whatap.lang.value.Value;
import whatap.org.json.JSONObject;

public class MemoryLinux implements Memory {
	public long total;
	public long free;
	public long cached;
	public long used;
	public float pused;
	public long available;
	public float pavailable;
	
	public long buffers;
	public long shared;
	
	public long swapused;
	public float swappused;
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

		dout.writeDecimal(this.buffers);
		dout.writeDecimal(this.shared);
		dout.writeDecimal(this.swapused);
		dout.writeFloat(this.swappused);
		dout.writeDecimal(this.swaptotal);
	}

	public void read(DataInputX din) {
		this.total = din.readDecimal();
		this.free = din.readDecimal();
		this.cached = din.readDecimal();
		this.used = din.readDecimal();
		this.pused = din.readFloat();
		this.available = din.readDecimal();
		this.pavailable = din.readFloat();

		this.buffers = din.readDecimal();
		this.shared = din.readDecimal();
		
		this.swapused = din.readDecimal();
		this.swappused = din.readFloat();
		this.swaptotal = din.readDecimal();
	}

	public JSONObject toJson(){
		JSONObject jsonObject= new JSONObject()
				.put("total", this.total)
				.put("free", this.free)
				.put("cached", this.cached)
				.put("used", this.used)
				.put("pused", this.pused)
				.put("available", this.available)
				.put("pavailable", this.pavailable)
				.put("buffers", this.buffers)
				.put("shared", this.shared);
				
		try{
			jsonObject.put("swapused", this.swapused);
			jsonObject.put("swaptotal", this.swaptotal);
			jsonObject.put("swappused", this.swappused);
		}catch(Exception e){
			jsonObject.put("swapused", 0);
			jsonObject.put("swaptotal", 0);
			jsonObject.put("swappused", 0);
		}
		jsonObject.put("pagefaults", this.pageFaults);
				
		return jsonObject;
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
		value.put("buffers", this.buffers);
		value.put("shared", this.shared);
		value.put("swapused", this.swapused);
		value.put("swappused", this.swappused);
		value.put("swaptotal", this.swaptotal);
		value.put("pagefaults", this.pageFaults);
		return value;
	}

	public float swappct() {
		return swappused;
	}

	
	public void readv2(DataInputX dinx) {
		byte[] bytes =dinx.readBlob();
//for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
//    System.out.println(ste);
//}
//		System.out.println("MemoryLinux.readv2 length:"+bytes.length);
		DataInputX din = new DataInputX(bytes);
		this.total = din.readDecimal();
		this.free = din.readDecimal();
		this.cached = din.readDecimal();
		this.used = din.readDecimal();
		this.pused = din.readFloat();
		this.available = din.readDecimal();
		this.pavailable = din.readFloat();

		this.buffers = din.readDecimal();
		this.shared = din.readDecimal();
		
		this.swapused = din.readDecimal();
		this.swappused = din.readFloat();
		this.swaptotal = din.readDecimal();
		this.pageFaults = din.readFloat();
	}

	public void writev2(DataOutputX doutx) {
//		for (StackTraceElement ste : Thread.currentThread().getStackTrace()) {
//		    System.out.println(ste);
//		}
		DataOutputX dout= new DataOutputX();
		dout.writeDecimal(this.total);
		dout.writeDecimal(this.free);
		dout.writeDecimal(this.cached);
		dout.writeDecimal(this.used);
		dout.writeFloat(this.pused);
		dout.writeDecimal(this.available);
		dout.writeFloat(this.pavailable);

		dout.writeDecimal(this.buffers);
		dout.writeDecimal(this.shared);
		dout.writeDecimal(this.swapused);
		dout.writeFloat(this.swappused);
		dout.writeDecimal(this.swaptotal);
		
		dout.writeFloat(this.pageFaults);
		
		doutx.writeBlob(dout.toByteArray());
	}
}
