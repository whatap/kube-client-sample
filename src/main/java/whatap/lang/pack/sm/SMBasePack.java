package whatap.lang.pack.sm;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.pack.AbstractPack;
import whatap.lang.pack.Pack;
import whatap.lang.pack.PackEnum;
import whatap.lang.value.MapValue;
import whatap.lang.value.Value;

public class SMBasePack extends AbstractPack {
	public short type;
	public short os;
	public int ip;
	public Cpu cpu;
	public Cpu[] cores;
	public Memory memory;
	public long uptime;
	public long epochtime;
	public MapValue extra;

	public SMBasePack(short p) {
		type=p;
	}
	
	public SMBasePack() {
		type=PackEnum.SM_BASE_1;
	}

	public short getPackType() {
		return this.type;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("OSBasePack ");
		sb.append(super.toString());

		return sb.toString();
	}
	
	public void write(DataOutputX dout) {
		switch(this.type){
		case PackEnum.SM_BASE:
			writev1(dout); 
			break;
		case PackEnum.SM_BASE_1:
			writev2(dout);
			break;
		default:
			System.out.println("SMBasePack Pack type not supported "+this.type);
		}
	}
	
	public void writev1(DataOutputX dout) {
		super.write(dout);
		dout.writeInt(this.ip);
		dout.writeShort(this.os);
		this.cpu.write(dout);
		dout.writeByte(cores.length);
		for (int i = 0; i < this.cores.length; i++) {
			this.cores[i].write(dout);
		}
		this.memory.write(dout);
		dout.writeDecimal(this.uptime);
		dout.writeLong(this.epochtime);
	}
	
	public void writev2(DataOutputX doutx) {
		DataOutputX dout = new DataOutputX();
		super.write(dout);
		dout.writeInt(this.ip);
		dout.writeShort(this.os);
		this.cpu.writev2(dout);
		dout.writeByte(cores.length);
		for (int i = 0; i < this.cores.length; i++) {
			this.cores[i].writev2(dout);
		}
		this.memory.writev2(dout);
		dout.writeDecimal(this.uptime);
		dout.writeLong(this.epochtime);
		
		if(this.extra!=null && this.extra.size()>0) {
			dout.writeByte(1);
			dout.writeValue(this.extra);
		}else {
			dout.writeByte(0);
		}
		doutx.writeBlob(dout.toByteArray());
	}

	public Pack read(DataInputX din) {
		switch(this.type){
		case PackEnum.SM_BASE:
			readv1(din); 
			break;
		case PackEnum.SM_BASE_1:
			readv2(din);
			break;
		default:
			System.out.println("SMBasePack Pack type not supported "+this.type);
		}
		
		return this;
	}
	
	public Pack readv1(DataInputX din) {
		super.read(din);
		this.ip = din.readInt();
		this.os = din.readShort();
		switch (this.os) {
		case SMEnum.SM_LINUX:
		case SMEnum.SM_OSX:
		case SMEnum.SM_AIX:
		case SMEnum.SM_HPUX:
		case SMEnum.SM_SUNOS:
		case SMEnum.SM_OPENBSD:
		case SMEnum.SM_FREEBSD:
		case SMEnum.KUBE_MASTER:
		case SMEnum.KUBE_NODE:
			this.cpu = new CpuLinux();
			this.memory = new MemoryLinux();
			break;
		case SMEnum.SM_WINDOW:
			this.cpu = new CpuWindow();
			this.memory = new MemoryWindow();
			break;
		default:
			return this;
		}
		this.cpu.read(din);
		int cnt = din.readByte();
		this.cores = new Cpu[cnt];
		for (int i = 0; i < cnt; i++) {
			this.cores[i] = this.cpu.create();
			this.cores[i].read(din);
		}
		this.memory.read(din);
		this.uptime = din.readDecimal();
		this.epochtime = din.readLong();
		return this;
	}
	
	public Pack readv2(DataInputX dinx) {
		DataInputX din = new DataInputX(dinx.readBlob());
		super.read(din);
		this.ip = din.readInt();
		this.os = din.readShort();
		switch (this.os) {
		case SMEnum.SM_LINUX:
		case SMEnum.SM_OSX:
		case SMEnum.SM_AIX:
		case SMEnum.SM_HPUX:
		case SMEnum.SM_SUNOS:
		case SMEnum.SM_OPENBSD:
		case SMEnum.SM_FREEBSD:	
		case SMEnum.KUBE_MASTER:
		case SMEnum.KUBE_NODE:
			this.cpu = new CpuLinux();
			this.memory = new MemoryLinux();
			break;
		case SMEnum.SM_WINDOW:
			this.cpu = new CpuWindow();
			this.memory = new MemoryWindow();
			break;
		default:
			return this;
		}
		this.cpu.readv2(din);
		int cnt = din.readByte();
		this.cores = new Cpu[cnt];
		for (int i = 0; i < cnt; i++) {
			this.cores[i] = this.cpu.create();
			this.cores[i].readv2(din);
		}
		this.memory.readv2(din);
		this.uptime = din.readDecimal();
		this.epochtime = din.readLong();
		
		if (din.available() == 0)
			return this;
		if (din.readByte() > 0) {
			Value v = din.readValue();
			if(v instanceof MapValue) {
				this.extra = (MapValue)v; 
			}
		}
		return this;
	}
}
