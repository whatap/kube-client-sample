package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.ArrayUtil;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

public class StatTransactionPack extends AbstractPack {

	public byte[] records;
	public int record_count;
	public byte version=2;

	public short getPackType() {
		return PackEnum.STAT_SERVICE;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("StatService ");
		sb.append(super.toString());
		sb.append(",records=" + this.record_count);
		sb.append(",bytes=" + ArrayUtil.len(records));
		return sb.toString();
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeBlob(records);
		dout.writeDecimal(record_count);
	}

	public Pack read(DataInputX din) {
		super.read(din);
		this.records = din.readBlob();
		this.record_count = (int) din.readDecimal();
		return this;
	}

	public StatTransactionPack setRecords(int size, Enumeration<TransactionRec> items) {
		DataOutputX o = new DataOutputX();
		o.writeShort(size);
		for (int i = 0; i < size; i++) {
			TransactionRec.writeRec(o, items.nextElement(),this.version);
		}
		records = o.toByteArray();
		record_count = size;
		return this;
	}

	public StatTransactionPack setRecords(List<TransactionRec> items) {
		DataOutputX o = new DataOutputX();
		o.writeShort(items.size());
		for (int i = 0; i < items.size(); i++) {
			TransactionRec.writeRec(o, items.get(i),this.version);
		}
		records = o.toByteArray();
		record_count = items.size();
		return this;
	}

	public List<TransactionRec> getRecords() {
		List<TransactionRec> items = new ArrayList<TransactionRec>();
		if (records == null)
			return null;
		DataInputX in = new DataInputX(records);
		int size = in.readShort() & 0xffff;
		for (int i = 0; i < size; i++) {
			items.add(TransactionRec.readRec(in));
		}
		return items;
	}

}
