package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;

public class WebCheckRecord {

	public long owner_pcode;
	public int worker_oid;
	public int request_id;
	public long endtime;
	public int elapsed;
	public int body_size;
	public int status;
	public String reason;

	public byte[] toBytes() {
		DataOutputX out = new DataOutputX();
		out.writeByte(0);// version;
		out.writeDecimal(owner_pcode);
		out.writeDecimal(worker_oid);
		out.writeDecimal(this.request_id);
		out.writeDecimal(endtime);
		out.writeDecimal(elapsed);
		out.writeDecimal(body_size);
		out.writeDecimal(status);
		out.writeText(reason);
		return out.toByteArray();
	}

	public WebCheckRecord toObject(byte[] b) {
		DataInputX in = new DataInputX(b);
		byte ver = in.readByte();
		this.owner_pcode = in.readDecimal();
		this.worker_oid = (int) in.readDecimal();
		this.request_id = (int) in.readDecimal();
		this.endtime = in.readDecimal();
		this.elapsed = (int) in.readDecimal();
		this.body_size = (int) in.readDecimal();
		this.status = (int) in.readDecimal();
		this.reason = in.readText();
		return this;
	}
}