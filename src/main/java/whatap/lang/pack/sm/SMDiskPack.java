package whatap.lang.pack.sm;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.pack.AbstractPack;
import whatap.lang.pack.Pack;
import whatap.lang.pack.PackEnum;

public class SMDiskPack extends AbstractPack {
	public short type;
	public short os;
	public Disk[] disks;

	public short getPackType() {
		return PackEnum.SM_DISK;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("OSDiskPack ");
		sb.append(super.toString());

		return sb.toString();
	}

	public float getDiskDeviceIopercentSum(){
		float sum = 0;

		for (Disk disk : this.disks) {
			sum += disk.ioPercent;
		}
		return sum;
	}
	
	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeShort(this.os);
		dout.writeDecimal(disks.length);
		for (int i = 0; i < this.disks.length; i++) {
			this.disks[i].write(dout);
		}
	}
	
	public Pack read(DataInputX din) {
		super.read(din);
		this.os = din.readShort();
		int cnt = (int)din.readDecimal();
		this.disks = new Disk[cnt];
		for (int i = 0; i < cnt; i++) {
			this.disks[i] = new Disk();
			this.disks[i].read(din);
		}
		return this;
	}	
}
