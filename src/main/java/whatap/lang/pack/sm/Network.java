package whatap.lang.pack.sm;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;

public class Network {
	public int desc;
	public byte[] ip;
	public String hwAddr;
	public double trafficIn;
	public double trafficOut;
	public double packetIn;
	public double packetOut;
	public double errorOut;
	public double errorIn;
	public double droppedOut;
	public double droppedIn;
	public int count = 1;

	public void write(DataOutputX out) {
		DataOutputX dout = new DataOutputX();
		dout.writeInt(this.desc);
		dout.writeBlob(this.ip);
		dout.writeText(this.hwAddr);

		dout.writeDouble(this.trafficIn);
		dout.writeDouble(this.trafficOut);
		dout.writeDouble(this.packetIn);
		dout.writeDouble(this.packetOut);

		dout.writeDouble(this.errorOut);
		dout.writeDouble(this.errorIn);
		dout.writeDouble(this.droppedOut);
		dout.writeDouble(this.droppedIn);

		//
		dout.writeInt(this.count);
		
		out.writeBlob(dout.toByteArray());
	}

	public Network read(DataInputX in) {
		DataInputX din = new DataInputX(in.readBlob());
		this.desc = din.readInt();
		this.ip = din.readBlob();
		this.hwAddr = din.readText();

		this.trafficIn = din.readDoubleSafe();
		this.trafficOut = din.readDoubleSafe();
		this.packetIn = din.readDoubleSafe();
		this.packetOut = din.readDoubleSafe();

		this.errorOut = din.readDoubleSafe();
		this.errorIn = din.readDoubleSafe();
		this.droppedOut = din.readDoubleSafe();
		this.droppedIn = din.readDoubleSafe();
		
		if(din.available()>0){
			this.count = din.readInt();
		}
		return this;
	}

	public void merge(Network o) {
		this.trafficIn += o.trafficIn;
		this.trafficOut += o.trafficOut;
		this.packetIn += o.packetIn;
		this.packetOut += o.packetOut;
		this.errorOut += o.errorOut;
		this.errorIn += o.errorIn;
		this.droppedOut += o.droppedOut;
		this.droppedIn += o.droppedIn;
		this.count+=o.count;
	}

}
