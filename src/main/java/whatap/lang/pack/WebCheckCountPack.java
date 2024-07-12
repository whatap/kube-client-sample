package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;

import java.util.ArrayList;
import java.util.List;

public class WebCheckCountPack extends AbstractPack {

	public String region_name;
	public String region_addr;
	
	public int host_ip;
	public long starttime;
	public int sys_cores;
	public long sys_mem;
	
	public WebCheckCount count = new WebCheckCount();
	public List<WebCheckRecord> result = new ArrayList<WebCheckRecord>();
	
	public short getPackType() {
		return PackEnum.WEB_CHECK_COUNT;
	}

	@Override
	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeByte(0);// version
		dout.writeText(this.region_addr);
		dout.writeText(this.region_name);
		
		dout.writeInt(this.host_ip);
		dout.writeLong(this.starttime);
		dout.writeDecimal(this.sys_cores);
		dout.writeDecimal(this.sys_mem);

		
		dout.writeBlob(count.toBytes());
		int sz=result.size();
		dout.writeDecimal(sz);
		for (int i = 0; i < sz; i++) {
			dout.writeBlob(result.get(i).toBytes());
		}

	}

	@Override
	public Pack read(DataInputX din) {
		super.read(din);
		byte ver = din.readByte();
		this.region_addr = din.readText();
		this.region_name=din.readText();
		
		this.host_ip = din.readInt();
		this.starttime = din.readLong();
		this.sys_cores = (int) din.readDecimal();
		this.sys_mem = din.readDecimal();
		
		this.count.toObject(din.readBlob());
		int sz = (int) din.readDecimal();
		for (int i = 0; i < sz; i++) {
			this.result.add(new WebCheckRecord().toObject(din.readBlob()));
		}
	
		return this;
	}

}