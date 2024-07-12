package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.ArrayUtil;
import whatap.util.BitUtil;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class StatErrorPack extends AbstractPack {
	public static class ErrorRec {
		public int classHash;
		public int service;
		public long errorSnapId;
		public int msg;
		public int count;

		public void merge(ErrorRec o) {
			this.count += o.count;
		}

		public long getKey() {
			return BitUtil.composite(classHash, service);
		}

		public ErrorRec setClassAndTxUrl(int classHash, int txUrl) {
			this.classHash = classHash;
			this.service = txUrl;
			return this;
		}
	}

	public byte[] records;
	public int record_count;

	private short packType;
	public long dataStartTime;
	
	public StatErrorPack() {
		this(PackEnum.STAT_ERROR);
	}
	public StatErrorPack(short t) {
		this.packType=t;
	}
	public short getPackType() {
		return this.packType;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("StatError ");
		sb.append(super.toString());
		sb.append(",records=" + this.record_count);
		sb.append(",bytes=" + ArrayUtil.len(records));
		return sb.toString();
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeBlob(records);
		dout.writeDecimal(record_count);

		if(this.packType == PackEnum.STAT_ERROR) {
			return;
		}
		DataOutputX o = new DataOutputX();
		o.writeDecimal(dataStartTime);
		
		dout.writeBlob(o.toByteArray());
	}

	public Pack read(DataInputX din) {
		super.read(din);
		this.records = din.readBlob();
		this.record_count = (int) din.readDecimal();
		
		if(this.packType == PackEnum.STAT_ERROR) {
			return this;
		}
		
		DataInputX in = new DataInputX(din.readBlob());
		this.dataStartTime=in.readDecimal();
		
		return this;
	}

	public StatErrorPack setRecords(int size, Enumeration<ErrorRec> items) {
		DataOutputX o = new DataOutputX();
		o.writeShort(size);
		for (int i = 0; i < size; i++) {
			writeRec(o, items.nextElement());
		}
		records = o.toByteArray();
		record_count = size;
		return this;
	}

	public StatErrorPack setRecords(List<ErrorRec> items) {
		DataOutputX o = new DataOutputX();
		o.writeShort(items.size());
		for (int i = 0; i < items.size(); i++) {
			writeRec(o, items.get(i));
		}
		records = o.toByteArray();
		record_count = items.size();
		return this;
	}

	public static void writeRec(DataOutputX o, ErrorRec m) {
		o.writeInt(m.classHash);
		o.writeInt(m.service);
		o.writeLong(m.errorSnapId);
		o.writeDecimal(m.msg);
		o.writeDecimal(m.count);
	}

	public static ErrorRec readRec(DataInputX in) {
		ErrorRec m = new ErrorRec();
		m.classHash = in.readInt();
		m.service = in.readInt();
		m.errorSnapId = in.readLong();
		m.msg = (int) in.readDecimal();
		m.count = (int) in.readDecimal();
		return m;
	}

	public List<ErrorRec> getRecords() {
		List<ErrorRec> items = new ArrayList<ErrorRec>();
		if (records == null)
			return null;
		DataInputX in = new DataInputX(records);
		int size = in.readShort();
		for (int i = 0; i < size; i++) {
			items.add(readRec(in));
		}
		return items;
	}
}
