package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.IntSet;

public class KubeNodePack extends AbstractPack {

	public int host_ip;
	public int listen_port;
	public long starttime;
	public int sys_cores;
	public long sys_mem;
	public int container_id;

	public short getPackType() {
		return PackEnum.KUBE_NODE;
	}

	@Override
	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeByte(1);// version

		DataOutputX out = new DataOutputX();
		out.writeInt(this.host_ip);
		out.writeLong(this.starttime);
		out.writeDecimal(this.sys_cores);
		out.writeDecimal(this.sys_mem);
		out.writeDecimal(this.listen_port);
		out.writeDecimal(this.container_id);

		out.writeDecimal(0);

		dout.writeBlob(out.toByteArray());

	}

	@Override
	public Pack read(DataInputX din) {
		super.read(din);
		byte ver = din.readByte();
		if (ver == 0)
			return this;

		DataInputX in = new DataInputX(din.readBlob());
		this.host_ip = in.readInt();
		this.starttime = in.readLong();
		this.sys_cores = (int) in.readDecimal();
		this.sys_mem = in.readDecimal();
		this.listen_port = (int) in.readDecimal();
		this.container_id = (int) in.readDecimal();

		int sz = (int) in.readDecimal();
		for (int i = 0; i < sz; i++) {
			in.readBlob();
		}

		return this;
	}


	public IntSet getMicroOidSet() {
		IntSet out = new IntSet();
		
		return out;
	}

}
