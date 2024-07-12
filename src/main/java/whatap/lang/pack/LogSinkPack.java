package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.BooleanValue;
import whatap.lang.value.MapValue;
import whatap.lang.value.Value;
import whatap.util.HashUtil;

public class LogSinkPack extends AbstractPack {
	public String category;
	public long tagHash;
	public MapValue tags = new MapValue();
	public long line;
	public String content;
	public MapValue fields = new MapValue();

	transient public boolean dropped;
	

	public short getPackType() {
		return PackEnum.LOGSINK;
	}

	@Override
	public String toString() {	
		return super.toString() +" [category=" + category + ", tagHash=" + tagHash + ", tags=" + tags + ", content=" + content
				+", fields=" + fields+ "]";
	}

	public byte[] getTagAsBytes() {
		DataOutputX out = new DataOutputX();
		out.writeValue(tags);
		return out.toByteArray();
	}

	public byte[] getContentBytes() {
		DataOutputX o = new DataOutputX();
		o.writeByte(1);
		o.writeText(this.content);
		o.writeDecimal(line);
		return o.toByteArray();
	}
	public void putCtr(String key, Value value) {
		if (key.charAt(0) == '!') {
			this.fields.put(key, value);
		}
	}

	public Value getCtr(String key) {
		return this.fields.get(key);
	}

	public boolean getCtrBoolean(String key) {
		Value v = this.fields.get(key);
		if (v instanceof BooleanValue) {
			return ((BooleanValue) v).value;
		}
		return false;
	}
	
	public String content() {
		return this.content==null?"":this.content;
	}
	public void content(String str) {
		this.content=str;
	}
	public void setContentBytes(byte[] d) {
		try {
		    if(d ==null || d.length<1)
		    	return;
			DataInputX in =new DataInputX(d);
			byte ver = in.readByte();
			if(ver==1) {
				this.content=in.readText();
				this.line = in.readDecimal();				
			}
		} catch (Throwable e) {
		}
	}
	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeByte(0);
		dout.writeText(this.category);
		dout.writeDecimal(tagHash);
		dout.writeValue(tags);
		dout.writeDecimal(line);
		dout.writeText(this.content);
		if (this.fields.size() > 0) {
			dout.writeBoolean(true);
			dout.writeValue(this.fields);
		} else {
			dout.writeBoolean(false);
		}
	}

	public Pack read(DataInputX din) {
		super.read(din);
		int ver = din.readByte();
		this.category = din.readText();
		this.tagHash = din.readDecimal();
		this.tags = (MapValue) din.readValue();
		this.line=din.readDecimal();
		this.content = din.readText();
		if (din.readBoolean()) {
			this.fields = (MapValue) din.readValue();
		}
		return this;
	}

	public byte[] resetTagHash() {
		DataOutputX out = new DataOutputX();
		out.writeValue(tags);
		byte[] tagBytes = out.toByteArray();
		this.tagHash = HashUtil.hash64(tagBytes);
		return tagBytes;
	}

	public void transferOidToTag() {
		if (this.oid != 0 && this.tags.containsKey("oid") == false) {
			this.tags.put("oid", this.oid);
			this.tagHash = 0;
		}
		if (this.okind != 0 && this.tags.containsKey("okind") == false) {
			this.tags.put("okind", this.okind);
			this.tagHash = 0;
		}
		if (this.onode != 0 && this.tags.containsKey("onode") == false) {
			this.tags.put("onode", this.onode);
			this.tagHash = 0;
		}
	}

}
