package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.IntMapValue;
import whatap.lang.value.MapValue;
import whatap.util.IntKeyLinkedMap;
import whatap.util.IntKeyLinkedMap.IntKeyLinkedEntry;

import java.util.Enumeration;

public class KubeMasterStatPack extends AbstractPack {

	public IntKeyLinkedMap<String> apps = new IntKeyLinkedMap<String>();
	public MapValue attr = new MapValue();
	public IntMapValue okinds = new IntMapValue();
	public IntMapValue okindMap=new IntMapValue();
	public MapValue allResources=new MapValue();

	public short getPackType() {
		return PackEnum.KUBE_MASTER_STAT;
	}

	@Override
	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeByte(1);// version
		
		DataOutputX out = new DataOutputX();
		out.writeValue(this.attr);		
		writeApps(out);
		out.writeValue(this.allResources);
		
		dout.writeBlob(out.toByteArray());
	}

	

	@Override
	public Pack read(DataInputX din) {
		super.read(din);
		byte ver = din.readByte();
		
		DataInputX in = new DataInputX(din.readBlob());	
		this.attr = (MapValue) in.readValue();
		readApps(in);
		if(ver > 0){
			this.allResources = (MapValue)in.readValue();
		}
		return this;
	}
	private void writeApps(DataOutputX out) {
		out.writeDecimal(apps.size());
		Enumeration<IntKeyLinkedEntry<String>> en = apps.entries();
		while(en.hasMoreElements()) {
			IntKeyLinkedEntry<String> ent = en.nextElement();
			out.writeInt(ent.getKey());
			out.writeText(ent.getValue());
		}
	}
	private void readApps(DataInputX in) {
		int count = (int) in.readDecimal();
		for(int i = 0 ; i < count ; i++) {
			int key = in.readInt();
			String val = in.readText();
			this.apps.put(key, val);
		}
	}
}
