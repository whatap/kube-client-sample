package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.IntIntMap;
import whatap.util.IntIntMap.IntIntEntry;

import java.util.Enumeration;

public class StatRemoteIpPack extends AbstractPack {
	public IntIntMap iptable = new IntIntMap(2001,1f);
	
	private short packType;
	public long dataStartTime;
	
	public StatRemoteIpPack() {
		this(PackEnum.STAT_REMOTE_IP);
	}
	public StatRemoteIpPack(short t) {
		this.packType=t;
	}	

	public short getPackType() {
		return this.packType;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("StatRemoteIp ");
		sb.append(super.toString());
		sb.append(",iptable=" + this.iptable.size());
		return sb.toString();
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeDecimal(iptable.size());
		Enumeration<IntIntEntry> en = iptable.entries();
		while (en.hasMoreElements()) {
			IntIntEntry e = en.nextElement();
			dout.writeInt(e.getKey());
			dout.writeInt(e.getValue());
		}
		
		if(this.packType ==  PackEnum.STAT_REMOTE_IP)
			return;
		
		DataOutputX o =new DataOutputX();
		o.writeDecimal(dataStartTime);
		
		dout.writeBlob(o.toByteArray());
	}

	public Pack read(DataInputX din) {
		super.read(din);
		int cnt = (int) din.readDecimal();
		this.iptable = new IntIntMap(cnt, 1.0f);
		for (int i = 0; i < cnt; i++) {
			int ip = din.readInt();
			int count = din.readInt();

			this.iptable.put(ip, count);
		}
		if(this.packType ==  PackEnum.STAT_REMOTE_IP)
			return this;
		
		DataInputX in = new DataInputX(din.readBlob());
		this.dataStartTime = in.readDecimal();
		return this;	
	}
}
