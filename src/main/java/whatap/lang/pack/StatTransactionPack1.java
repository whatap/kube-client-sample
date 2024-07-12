package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class StatTransactionPack1 extends AbstractPack {

	public byte[] records;
	public int record_count;
	public int spec;
	
	//하위버전 서버와 호환을 위해 version은 옵션으로 관리함 2021.5.14 
	public byte version=4;
	//
	public long dataStartTime;
	public long dataEndTime;
	private short packType;
	public StatTransactionPack1() {
		this(PackEnum.STAT_SERVICE_1);
	}
	public StatTransactionPack1(short packType) {
    	this.packType = packType;
    }
	public short getPackType() {
		return this.packType;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("StatTransaction1 ");
		sb.append(super.toString());
		sb.append(",records=" + this.record_count);
		sb.append(",bytes=" + ArrayUtil.len(records));
		return sb.toString();
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeBlob(records);
		dout.writeDecimal(record_count);
		dout.writeByte(0);//reserved
		dout.writeDecimal(this.spec);
		
		if(this.packType==PackEnum.STAT_SERVICE_1) {
			return;
		}
		DataOutputX o =new DataOutputX();
		o.writeDecimal(dataStartTime);
		o.writeDecimal(dataEndTime);
		o.writeByte(this.version);
		dout.writeBlob(o.toByteArray());
	}

	public Pack read(DataInputX din) {
		super.read(din);
		this.records = din.readBlob();
		this.record_count = (int) din.readDecimal();
		byte reserved = din.readByte();		
	    this.spec=(int) din.readDecimal();
	    if(this.packType == PackEnum.STAT_SERVICE_1)
			return this;
	    	   
	    DataInputX in = new DataInputX(din.readBlob());
		this.dataStartTime = in.readDecimal();
		if (in.available() > 0) {
			this.dataEndTime = in.readDecimal();
		}
		if (in.available() > 0) {
			this.version = in.readByte();
		}
		return this;
	}

	public StatTransactionPack1 setRecords(int size, Enumeration<TransactionRec> items) {
		DataOutputX o = new DataOutputX();
		o.writeShort(size);
		for (int i = 0; i < size; i++) {
			TransactionRec.writeRec(o, items.nextElement(), this.version);
		}
		records = o.toByteArray();
		record_count = size;
		return this;
	}

	public StatTransactionPack1 setRecords(List<TransactionRec> items) {
		DataOutputX o = new DataOutputX();
		o.writeShort(items.size());
		for (int i = 0; i < items.size(); i++) {
			TransactionRec.writeRec(o, items.get(i), this.version);
		}
		records = o.toByteArray();
		record_count = items.size();
		return this;
	}

	
	public List<TransactionRec> getRecords() {
		List<TransactionRec> items = new ArrayList<TransactionRec>();
		if (records == null)
			return null;
		try {
			DataInputX in = new DataInputX(records);
			int size = in.readShort() & 0xffff;
			for (int i = 0; i < size; i++) {
				items.add(TransactionRec.readRec(in));
			}
		}catch(Throwable t) {
			//ignore
		}
		return items;
	}

	
}
