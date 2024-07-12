package whatap.lang.pack.bsm;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;

public class BsmRecord {

	public int url;
	public int referer;
	public int userAgent;
	public int login;
	public int clientIp;
	public long userToken;
	public int elapsed;
	
	public void write(DataOutputX o) {
		o.writeByte(0);
		o.writeDecimal(this.url);
		o.writeDecimal(this.referer);
		o.writeDecimal(this.userAgent);
		o.writeDecimal(this.login);
		o.writeDecimal(this.clientIp);
		o.writeDecimal(this.userToken);
		o.writeDecimal(this.elapsed);
	}

	public BsmRecord read(DataInputX in) {
		byte ver = in.readByte();
		this.url = (int) in.readDecimal();
		this.referer = (int) in.readDecimal();
		this.userAgent = (int) in.readDecimal();
		this.login = (int) in.readDecimal();
		this.clientIp = (int) in.readDecimal();
		this.userToken = in.readDecimal();
		this.elapsed = (int) in.readDecimal();

		return this;
	}
}
