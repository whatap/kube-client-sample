package whatap.lang.pack.bsm;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.pack.AbstractPack;
import whatap.lang.pack.Pack;
import whatap.lang.pack.PackEnum;
import whatap.util.ArrayUtil;

import java.util.ArrayList;
import java.util.List;

public class BsmRecordPack extends AbstractPack {

	public byte[] records;
	public int record_count;

	public short getPackType() {
		return PackEnum.BSM_RECORD;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("BsmRecordPack ");
		sb.append(super.toString());
		sb.append(",records=" + this.record_count);
		sb.append(",bytes=" + ArrayUtil.len(records));
		return sb.toString();
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeDecimal(record_count);
		dout.writeBlob(records);
	}

	public Pack read(DataInputX din) {
		super.read(din);
		this.record_count = (int) din.readDecimal();
		this.records = din.readBlob();
		return this;
	}

	public BsmRecordPack setRecords(List<BsmRecord> items) {
		DataOutputX o = new DataOutputX();
		o.writeDecimal(items.size());
		for (int i = 0; i < items.size(); i++) {
			items.get(i).write(o);
		}
		records = o.toByteArray();
		record_count = items.size();
		return this;
	}

	public List<BsmRecord> getRecords() {
		List<BsmRecord> items = new ArrayList<BsmRecord>();
		if (records == null)
			return items;
		DataInputX in = new DataInputX(records);
		int size = (int) in.readDecimal();
		for (int i = 0; i < size; i++) {
			items.add(new BsmRecord().read(in));
		}
		return items;
	}

}
