package whatap.lang.pack.sm;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;

public class Disk {
	public int deviceId; /* hash */
	public int mountPoint;
	public int fileSystem;

	public long freeSpace;
	public long usedSpace;
	public long totalSpace;

	public float freePercent;
	public float usedPercent;

	public int blksize;
	public double readIops;
	public double writeIops;
	public double readBps;
	public double writeBps;
	public float ioPercent;
	public int count = 1;
	public float queueLength;

	public void write(DataOutputX out) {
		DataOutputX dout = new DataOutputX();
		dout.writeInt(this.deviceId);
		dout.writeInt(this.mountPoint);
		dout.writeInt(this.fileSystem);

		dout.writeDecimal(this.freeSpace);
		dout.writeDecimal(this.usedSpace);
		dout.writeDecimal(this.totalSpace);

		dout.writeFloat(this.freePercent);
		dout.writeFloat(this.usedPercent);

		dout.writeInt(this.blksize);
		dout.writeDouble(this.readIops);
		dout.writeDouble(this.writeIops);
		dout.writeDouble(this.readBps);
		dout.writeDouble(this.writeBps);
		dout.writeFloat(this.ioPercent);
		//
		dout.writeInt(this.count);
		dout.writeFloat(this.queueLength);
		out.writeBlob(dout.toByteArray());
	}

	public Disk read(DataInputX in) {
		DataInputX din = new DataInputX(in.readBlob());
		this.deviceId = din.readInt();/* hash */
		this.mountPoint = din.readInt();
		this.fileSystem = din.readInt();

		this.freeSpace = din.readDecimal();
		this.usedSpace = din.readDecimal();
		this.totalSpace = din.readDecimal();

		this.freePercent = din.readFloatSafe();
		this.usedPercent = din.readFloatSafe();

		this.blksize = din.readInt();
		this.readIops = din.readDoubleSafe();
		this.writeIops = din.readDoubleSafe();
		this.readBps = din.readDoubleSafe();
		this.writeBps = din.readDoubleSafe();
		this.ioPercent = din.readFloatSafe();
		this.count = din.readInt();
		if(din.available() > 0){
			this.queueLength = din.readFloatSafe();
		}
		return this;
	}

	public void merge(Disk o) {
		this.freeSpace += o.freeSpace;
		this.usedSpace += o.usedSpace;
		this.totalSpace += o.totalSpace;

		this.freePercent += o.freePercent;
		this.usedPercent += o.usedPercent;

		this.blksize += o.blksize;
		this.readIops += o.readIops;
		this.writeIops += o.writeIops;
		this.readBps += o.readBps;
		this.writeBps += o.writeBps;
		this.ioPercent += o.ioPercent;
		this.count+=o.count;
		this.queueLength += o.queueLength;
	}
}
