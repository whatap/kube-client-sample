package whatap.lang.topology;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.CompareUtil;
import whatap.util.HashUtil;

import java.net.InetAddress;

public class LINK {
	public byte[] ip;
	public int port;

	@Override
	public String toString() {
		if (ip == null)
			return "0.0.0.0:" + port;
		try {
			return InetAddress.getByAddress(ip).getHostAddress() + ":" + port;
		} catch (Exception e) {
			return "0.0.0.0:" + port;
		}
	}

	public static LINK create(String ipStr, int port) {
		LINK k = new LINK();
		try {
			k.ip = InetAddress.getByName(ipStr).getAddress();
		} catch (Exception e) {
			k.ip = new byte[4];
		}
		k.port = port;
		return k;
	}

	public boolean include(LINK k) {
		if (CompareUtil.equals(this.ip, k.ip) == false)
			return false;

		if (port == 0)
			return true;
		return this.port == k.port;
	}

	public boolean equals(Object o) {
		if (o.getClass() != this.getClass())
			return false;

		LINK k = (LINK) o;
		if (CompareUtil.equals(this.ip, k.ip) == false)
			return false;

		return this.port == k.port;
	}

	public int hashCode() {
		return HashUtil.hash(ip) | port;
	}

	public LINK toBytes(DataOutputX out) {
		out.writeBlob(this.ip);
		out.writeInt(port);
		return this;
	}

	public LINK toObject(DataInputX in) {
		this.ip = in.readBlob();
		this.port = in.readInt();
		return this;
	}

public static void main(String[] args) {
	LINK k = new LINK();
	k.ip = new byte[16];
	k.port=123;
	System.out.println(LINK.create("0:0:0:0:0:0:0:0", 123));
}
}
