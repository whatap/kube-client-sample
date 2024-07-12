package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class StatHttpcPack extends AbstractPack {
	
	public byte[] records;
	public int record_count;

	private short packType;
	public long dataStartTime;
	
	
	public StatHttpcPack() {
		this(PackEnum.STAT_HTTPC);
	}
	public StatHttpcPack(short t) {
		this.packType=t;
	}	
	public short getPackType() {
		return this.packType;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("StatHttpc ");
		sb.append(super.toString());
		sb.append(",records=" + this.record_count);
		sb.append(",bytes=" + ArrayUtil.len(records));
		return sb.toString();
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeBlob(records);
		dout.writeDecimal(record_count);
		
		if (this.packType == PackEnum.STAT_HTTPC)
			return;

		DataOutputX o =new DataOutputX();
		o.writeDecimal(dataStartTime);
		
		dout.writeBlob(o.toByteArray());
	}

	public Pack read(DataInputX din) {
		super.read(din);
		this.records = din.readBlob();
		this.record_count = (int) din.readDecimal();
		if (this.packType == PackEnum.STAT_HTTPC)
			return this;

		DataInputX in = new DataInputX(din.readBlob());
		this.dataStartTime = in.readDecimal();
		return this;
	}

	public StatHttpcPack setRecords(int size, Enumeration<HttpcRec> items) {
		DataOutputX o = new DataOutputX();
		o.writeShort(size);
		for (int i = 0; i < size; i++) {
			items.nextElement().write(o);
		}
		records = o.toByteArray();
		record_count = size;
		return this;
	}

	public StatHttpcPack setRecords(List<HttpcRec> items) {
		DataOutputX o = new DataOutputX();
		o.writeShort(items.size());
		for (int i = 0; i < items.size(); i++) {
			items.get(i).write(o);
		}
		records = o.toByteArray();
		record_count = items.size();
		return this;
	}

	

	public List<HttpcRec> getRecords() {
		List<HttpcRec> items = new ArrayList<HttpcRec>();
		if (records == null)
			return null;
		DataInputX in = new DataInputX(records);
		int size = in.readShort() & 0xffff;
		for (int i = 0; i < size; i++) {
			items.add(new HttpcRec().read(in));
		}
		return items;
	}
	
}
