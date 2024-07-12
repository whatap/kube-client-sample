package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.IntIntLinkedMap;

public class KubeMasterCountPack extends AbstractPack {

	public int host_ip;
	public int listen_port;
	public long starttime;
	public int sys_cores;
	public long sys_mem;

	public int total_container_num;
	public int total_host_num;
	public int total_image_num;
	public int total_service_num;
	public IntIntLinkedMap image_container_num = new IntIntLinkedMap();
	public long agentPcode;
	public int agentOid;
	
	public short getPackType() {
		return PackEnum.KUBE_MASTER_COUNT;
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

		out.writeDecimal(this.total_container_num);
		out.writeDecimal(this.total_host_num);
		out.writeDecimal(this.total_image_num);
		out.writeDecimal(this.total_service_num);
		this.image_container_num.toBytes(out);
		out.writeLong(this.agentPcode);
		out.writeInt(this.agentOid);
		
		dout.writeBlob(out.toByteArray());
	}

	@Override
	public Pack read(DataInputX din) {
		super.read(din);
		byte ver = din.readByte();

		DataInputX in = new DataInputX(din.readBlob());
		this.host_ip = in.readInt();
		this.starttime = in.readLong();
		this.sys_cores = (int) in.readDecimal();
		this.sys_mem = in.readDecimal();

		this.listen_port = (int) in.readDecimal();
		
		this.total_container_num = (int) in.readDecimal();
		this.total_host_num = (int) in.readDecimal();
		this.total_image_num = (int) in.readDecimal();
		this.total_service_num = (int) in.readDecimal();
		this.image_container_num.toObject(in);
		this.agentPcode = in.readLong();
		this.agentOid = in.readInt();
		
		return this;
	}

}