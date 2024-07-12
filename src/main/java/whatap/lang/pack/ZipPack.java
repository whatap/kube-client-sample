package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.ArrayUtil;
import whatap.util.CompressUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;

public class ZipPack extends AbstractPack {
	public byte[] records;
	public int recordCount;
	public byte status;

	public short getPackType() {
		return PackEnum.ZIP;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("ZipPack ");
		sb.append(super.toString());
		sb.append("records=" + ArrayUtil.len(records)+"bytes");
		return sb.toString();
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeByte(status);
		dout.writeDecimal(this.recordCount);
		dout.writeBlob(records);
	}

	public Pack read(DataInputX din) {
		super.read(din);
		this.status = din.readByte();
		this.recordCount=(int) din.readDecimal();
		this.records = din.readBlob();
		return this;
	}



	public ZipPack setRecords(int size, Enumeration<AbstractPack> items) {
		this.recordCount=size;
		DataOutputX o = new DataOutputX();	
		for (int i = 0; i < size; i++) {
			o.writePack(items.nextElement());
		}
		records = o.toByteArray();
		return this;
	}

	public ZipPack setRecords(List<AbstractPack> items) {
		this.recordCount=items.size();
		DataOutputX o = new DataOutputX();
		for (int i = 0; i < items.size(); i++) {
			o.writePack(items.get(i));
		}
		records = o.toByteArray();
		return this;
	}

	public List<AbstractPack> getRecords() {
		List<AbstractPack> items = new ArrayList<AbstractPack>();
		if (records == null)
			return null;
		DataInputX in = new DataInputX(records);	
		for (int i = 0; i < this.recordCount; i++) {
			AbstractPack p = (AbstractPack) in.readPack();
			p.pcode = this.pcode;
			p.oid = this.oid;
			// time은 자기 시간을 사용한다.
			p.okind = this.okind;
			p.onode = this.onode;
			items.add(p);
		}
		return items;
	}
	public ZipPack doZip(int limit) {
		if (this.status != 0)
			return this;
		if (this.records==null || this.records.length < limit) {
			return this;
		}
		this.status = 1;
		this.records = CompressUtil.doZip(this.records);
		return this;
	}

	public static List<AbstractPack> toList(AbstractPack... p) {
		return Arrays.asList(p);
	}
}
