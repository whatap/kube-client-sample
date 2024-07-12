package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.DoubleValue;
import whatap.lang.value.FloatValue;
import whatap.lang.value.ListValue;
import whatap.lang.value.MapValue;
import whatap.lang.value.MetricValue;
import whatap.lang.value.SummaryValue;
import whatap.lang.value.Value;
import whatap.lang.value.ValueEnum;
import whatap.util.HashUtil;

public class TagCountPack extends AbstractPack implements ITagPack {
	public String category;
	public long tagHash;
	
	
	public MapValue tags = new MapValue();
	public MapValue fields = new MapValue();
	
    public Integer originOid;

	public short getPackType() {
		return PackEnum.TAG_COUNT;
	}

	
	@Override
	public String toString() {
		return super.toString()+" [category=" + category +", taghash=" +this.tagHash+ ", tags=" + tags + ", fields=" + fields + "]";
	}

	public String getTagString(String name) {
		return this.tags.getText(name);
	}
	public Value getTag(String name) {
		return this.tags.get(name);
	}

	public void putTag(String name, long value) {
		this.tags.put(name, value);
	}
	public void putTag(String name, String value) {
		this.tags.put(name, value);
	}
	public void putTag(String name, Value value) {
		this.tags.put(name, value);
	}
	
	public void put(String name, String value) {
		this.fields.put(name, value);
	}

	public void put(String name, int value) {
		this.fields.put(name, value);
	}
	public void add(String name, int value) {
		this.fields.put(name, value+this.fields.getInt(name));
	}
	public void put(String name, long value) {
		this.fields.put(name, value);
	}
	public void add(String name, long value) {
		this.fields.put(name, value+this.fields.getLong(name));
	}

	public void put(String name, float value) {
		this.fields.put(name, new FloatValue(value));
	}
	public void add(String name, float value) {
		this.fields.put(name, value+this.fields.getFloat(name));
	}

	public void put(String name, double value) {
		this.fields.put(name, new DoubleValue(value));
	}
	public void add(String name, double value) {
		this.fields.put(name, value+this.fields.getDouble(name));
	}
	public void put(String name, Value value) {
		this.fields.put(name, value);
	}

	public ListValue newList(String name) {
		ListValue list = new ListValue();
		this.put(name, list);
		return list;
	}
	public ListValue getList(String name) {
		return (ListValue) this.get(name);
	}
	
	public Value get(String name) {
		return this.fields.get(name);
	}

	public double getFloat(String name) {
		Value val = this.fields.get(name);
		if (val == null)
			return 0;
		switch (val.getValueType()) {
		case ValueEnum.DOUBLE_SUMMARY:
		case ValueEnum.LONG_SUMMARY:
			return ((SummaryValue) val).doubleAvg();
		case ValueEnum.METRIC:
			return ((MetricValue) val).avg();
		default:
			if (val instanceof Number) {
				return ((Number) val).doubleValue();
			}
		}
		return 0;
	}

	public long getLong(String name) {
		Value val = this.fields.get(name);
		if (val == null)
			return 0;
		switch (val.getValueType()) {
		case ValueEnum.DOUBLE_SUMMARY:
		case ValueEnum.LONG_SUMMARY:
			return ((SummaryValue) val).longAvg();
		case ValueEnum.METRIC:
			return (long)(((MetricValue) val).avg());
		default:
			if (val instanceof Number) {
				return ((Number) val).longValue();
			}
		}
		return 0;
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeByte(1);
		dout.writeText(this.category);
		if (this.tagHash == 0 && this.tags.size() > 0) {
			DataOutputX tagIO = new DataOutputX();
			tagIO.writeValue(tags);
			byte[] tagBytes = tagIO.toByteArray();
			this.tagHash = HashUtil.hash64(tagBytes);
			dout.writeDecimal(tagHash);
			dout.write(tagBytes);
		} else {
			dout.writeDecimal(tagHash);
			dout.writeValue(tags);
		}
		dout.writeValue(fields);
		if(this.originOid!=null) {
			dout.writeBoolean(true);
			dout.writeInt(this.originOid);
		}else {
			dout.writeBoolean(false);
		}
	}

	public Pack read(DataInputX din) {
		super.read(din);
		int ver = din.readByte();
		this.category = din.readText();
		this.tagHash = din.readDecimal();
		this.tags = (MapValue) din.readValue();
		this.fields = (MapValue) din.readValue();
		if(ver==0)
			return this;
		
		if(din.readBoolean()) {
			this.originOid = din.readInt();
		}
		return this;
	}
	
	public void setOriginOid(Integer oid) {
		this.originOid=oid;
	}

	public void resetOid() {
		if (this.originOid != null) {
			this.oid = this.originOid;
		}
		this.originOid = null;
	}
	public boolean isEmpty() {
		return this.fields.isEmpty();
	}

	public int size() {
		return this.fields.size();
	}

	public void clear() {
		this.fields.clear();
	}


	

	public String getCategory() {
		return this.category;
	}


	public Value getField(String key) {
		return this.get(key);
	}


	public long createTagHash(String... tags) {
		if(tags==null||tags.length==0)
			return 0;
		
		DataOutputX tagIO = new DataOutputX();
		for (String t : tags) {
			Value v = this.tags.get(t);
			if (v != null) {
				tagIO.writeValue(v);
			}
		}
		byte[] tagBytes = tagIO.toByteArray();
		return HashUtil.hash64(tagBytes);
	}

}
