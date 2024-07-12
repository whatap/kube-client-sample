package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.H3;
import whatap.util.AnyList;
import whatap.util.ArrayUtil;
import whatap.util.DoubleList;
import whatap.util.FloatList;
import whatap.util.IntList;
import whatap.util.LongList;
import whatap.util.StringEnumer;
import whatap.util.StringKeyLinkedMap;
import whatap.util.StringKeyLinkedMap.StringKeyLinkedEntry;
import whatap.util.StringList;

import java.util.Enumeration;

public class StatGeneralPack extends AbstractPack {
	public String id;
	private StringKeyLinkedMap<AnyList> data = new StringKeyLinkedMap<AnyList>();
	private byte[] dataBytes;
	private int dataBytesSize;

	private short packType;
	public long dataStartTime;
	
	
	public StatGeneralPack() {
		this(PackEnum.STAT_GENERAL);
	}
	public StatGeneralPack(short t) {
		this.packType=t;
	}	
	public short getPackType() {
		return this.packType;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("StatGeneral ");
		sb.append(super.toString());
		sb.append(",packtype=" + this.getPackType());
		sb.append(",data=" + this.data.size());
		sb.append(",length=" + this.dataBytesSize);
		sb.append(",bytes=" + ArrayUtil.len(dataBytes));
		return sb.toString();
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeText(this.id);
		if (this.data.size() > 0 && ArrayUtil.isEmpty(this.dataBytes)) {
			this.dataBytes = writeTable(this.data);
			this.dataBytesSize = this.dataBytes.length;
		}
		//
		dout.writeInt3(this.dataBytesSize);
		dout.write(this.dataBytes);
		
		if(this.packType == PackEnum.STAT_GENERAL)
			return;
		
		DataOutputX o =new DataOutputX();
		o.writeDecimal(dataStartTime);
		
		dout.writeBlob(o.toByteArray());
	}

	public static byte[] writeTable(StringKeyLinkedMap<AnyList> data) {
		DataOutputX dd = new DataOutputX();
		dd.writeShort(data.size());
		Enumeration<StringKeyLinkedEntry<AnyList>> en = data.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<AnyList> ent = en.nextElement();
			dd.writeText(ent.getKey());
			dd.writeByte(ent.getValue().getType()); // type
			ent.getValue().write(dd); // data
		}
		return dd.toByteArray();
	}

	public static int readTable(byte[] bytes, StringKeyLinkedMap<AnyList> data) {
		DataInputX in = new DataInputX(bytes);
		int cnt = in.readShort();
		for (int i = 0; i < cnt; i++) {
			String key = in.readText();
			AnyList a = create(in.readByte());
			a.read(in);
			data.put(key, a);
		}
		return cnt;
	}

	public static StringKeyLinkedMap<AnyList> sort(StringKeyLinkedMap<AnyList> data, String sortKey, boolean asc) {
		AnyList any = data.get(sortKey);
		if (any == null)
			return data;
		int[] ord = any.sorting(asc);
		StringKeyLinkedMap<AnyList> data2 = new StringKeyLinkedMap<AnyList>();
		StringEnumer en = data.keys();
		while (en.hasMoreElements()) {
			String key = en.nextString();
			AnyList oldList = data.get(key);
			AnyList newList = oldList.filtering(ord);
			data2.put(key, newList);
		}
		return data2;
	}

	public static StringKeyLinkedMap<AnyList> sort(StringKeyLinkedMap<AnyList> data, String sortKey, boolean asc, String sortKey2, boolean asc2) {
		AnyList f1 = data.get(sortKey);
		if (f1 == null)
			return data;
		AnyList f2 = data.get(sortKey2);
		if (f2 == null)
			return data;

		int[] ord = f1.sorting(asc, f2, asc2);

		StringKeyLinkedMap<AnyList> data2 = new StringKeyLinkedMap<AnyList>();
		StringEnumer en = data.keys();
		while (en.hasMoreElements()) {
			String key = en.nextString();
			AnyList oldList = data.get(key);
			AnyList newList = oldList.filtering(ord);
			data2.put(key, newList);
		}
		return data2;
	}

	public void sort(String sortKey, boolean asc) {
		this.data = sort(this.data, sortKey, asc);
	}

	public void sort(String sortKey, boolean asc, String sortKey2, boolean asc2) {
		this.data = sort(this.data, sortKey, asc, sortKey2, asc2);
	}

	public Pack read(DataInputX din) {
		super.read(din);
		this.id = din.readText();
		this.dataBytesSize = din.readInt3();
		this.dataBytes = din.read(dataBytesSize);
		if(this.packType ==  PackEnum.STAT_GENERAL)
			return this;
		
		DataInputX in = new DataInputX(din.readBlob());
		this.dataStartTime = in.readDecimal();
		return this;
	}

	public synchronized StringKeyLinkedMap<AnyList> getDataTable() {
		unpack();
		return this.data;
	}

	private void unpack() {
		if (this.dataBytes != null) {
			readTable(this.dataBytes, this.data);
			this.dataBytes = null;
			this.dataBytesSize = 0;
		}
	}

	public void put(String key, AnyList data) {
		this.data.put(key, data);
	}

	public AnyList get(String key) {
		this.unpack();
		return this.data.get(key);
	}

	public boolean isEmpty() {
		return this.dataBytesSize == 0 && this.data.isEmpty();
	}

	private static AnyList create(byte type) {
		switch (type) {
		case AnyList.INT:
			return new IntList();
		case AnyList.LONG:
			return new LongList();
		case AnyList.FLOAT:
			return new FloatList();
		case AnyList.DOUBLE:
			return new DoubleList();
		default:
			return new StringList();
		}
	}

	public void iterate(H3<String[], AnyList[], Integer> h) throws Exception {
		unpack();
		if (this.data.size() == 0)
			return;
		String[] title = new String[this.data.size()];
		AnyList[] values = new AnyList[this.data.size()];
		Enumeration<StringKeyLinkedEntry<AnyList>> en = this.data.entries();
        for(int i = 0 ; en.hasMoreElements() ; i++) {
        	StringKeyLinkedEntry<AnyList> ent = en.nextElement();
        	title[i]=ent.getKey();
        	values[i]=ent.getValue();
        }
		int n = values[0].size();
		for (int i = 0; i < n; i++) {
			h.process(title, values, i);
		}
	}
}
