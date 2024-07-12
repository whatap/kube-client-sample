package whatap.lang.pack.sm;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.pack.AbstractPack;
import whatap.lang.pack.Pack;
import whatap.lang.pack.PackEnum;

public class SMNetworkPack extends AbstractPack {
	public short os;
	public Network[] networks;

	public short getPackType() {
		return PackEnum.SM_NETWORK;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("OSNetworkPack ");
		sb.append(super.toString());

		return sb.toString();
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeShort(this.os);
		dout.writeDecimal(networks.length);
		for (int i = 0; i < this.networks.length; i++) {
			this.networks[i].write(dout);
		}
	}

	public Pack read(DataInputX din) {
		super.read(din);
		this.os = din.readShort();
		int cnt = (int) din.readDecimal();
		this.networks = new Network[cnt];
		for (int i = 0; i < cnt; i++) {
			this.networks[i] = new Network();
			this.networks[i].read(din);
		}
		return this;
	}

}
