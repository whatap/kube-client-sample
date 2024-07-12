package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.ArrayUtil;

public class RealtimeUserPack1 extends AbstractPack {
	public int cadinality;
	public byte[] logbits;
	private short readType;
	
	public RealtimeUserPack1(short readType){
		this.readType=readType;
	}
	public RealtimeUserPack1() {
		this(PackEnum.REALTIME_USER_1);
	}
	public short getPackType() {
		return PackEnum.REALTIME_USER_1;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("RealtimeUserPack ");
		sb.append(super.toString());
		sb.append(",logbits=" + ArrayUtil.len(logbits));
		return sb.toString();
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeBlob(logbits);
		dout.writeDecimal(cadinality);
	}

	public Pack read(DataInputX din) {
		super.read(din);
		this.logbits = din.readBlob();
		if(this.readType == PackEnum.REALTIME_USER)
			return this;
		
		this.cadinality=(int)din.readDecimal();
		return this;
	}
}
