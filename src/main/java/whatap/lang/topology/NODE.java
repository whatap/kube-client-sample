package whatap.lang.topology;
/**
 <pre>
attr 
  {type=java,was=JAVA}
listen
	192.168.219.162:3306
	192.168.219.162:7283
	192.168.219.162:8888
	192.168.219.162:21300
outter
	59.30.186.24:27985
	91.108.56.199:80
	91.108.56.199:443
	54.243.172.216:443
	104.85.170.42:443
	107.23.165.206:443
	113.29.141.66:5223
	17.188.166.16:5223
	64.233.188.188:443
	108.177.97.125:5222
</pre>
 */

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.MapValue;
import whatap.org.json.JSONArray;
import whatap.org.json.JSONObject;
import whatap.util.CastUtil;
import whatap.util.LinkedSet;
import whatap.util.StringEnumer;
import whatap.util.StringSet;

import java.util.Enumeration;

public class NODE {
	public MapValue attr = new MapValue();
	public LinkedSet<LINK> listen = new LinkedSet<LINK>();
	public LinkedSet<LINK> outter = new LinkedSet<LINK>();

	@Override
	public String toString() {
		return attr + "\nlisten\n" + toString(listen, "\t") + "\noutter\n" + toString(outter, "\t");
	}

	private String toString(LinkedSet<LINK> t, String space) {
		StringBuffer buf = new StringBuffer();
		Enumeration<LINK> it = t.elements();
		while (it.hasMoreElements()) {
			buf.append(space).append(it.nextElement()).append("\n");

		}
		return buf.toString();
	}

	public boolean isAttachable(LINK k) {
		Enumeration<LINK> en = this.listen.elements();
		while (en.hasMoreElements()) {
			LINK n = en.nextElement();
			if (n != null && n.include(k)) {
				return true;
			}
		}
		return false;

	}

	public byte[] toBytes() {
		DataOutputX out = new DataOutputX();
		out.writeByte(0); // ver0
		out.writeValue(this.attr);
		toLinkBytes(this.listen, out);
		toLinkBytes(this.outter, out);

		return out.toByteArray();
	}

	public NODE toObject(byte[] b) {
		DataInputX in = new DataInputX(b);
		byte ver = in.readByte(); // ver는 일단 더미
		this.attr = (MapValue) in.readValue();
		this.listen = toLinkObject(in);
		this.outter = toLinkObject(in);

		return this;
	}

	private LinkedSet<LINK> toLinkObject(DataInputX in) {
		LinkedSet<LINK> data = new LinkedSet<LINK>();
		int sz = (int) in.readDecimal();
		for (int i = 0; i < sz; i++) {
			data.put(new LINK().toObject(in));
		}
		return data;
	}

	private void toLinkBytes(LinkedSet<LINK> data, DataOutputX out) {
		out.writeDecimal(data.size());
		if (data.size() == 0)
			return;
		Enumeration<LINK> en = data.elements();
		while (en.hasMoreElements()) {
			LINK k = en.nextElement();
			k.toBytes(out);
		}
	}

	static class IPO {
		String ip;
		String port;

		public boolean isIPv6() {
			return ip != null && ip.indexOf(':') >= 0;
		}

		public boolean isLocal127() {
			return "127.0.0.1".equals(ip);
		}
	}

	public void addListen(StringSet localIpSet, String listenAddr) {

		IPO ipo = getIPPORT(listenAddr);
		if (ipo == null || ipo.isLocal127())
			return;

		if (ipo.ip.equals("*") || ipo.ip.equals("0.0.0.0") || ipo.ip.equals("::")) {
			StringEnumer en = localIpSet.keys();
			while (en.hasMoreElements()) {
				String local_ip = en.nextString();
				LINK k = LINK.create(local_ip, CastUtil.cint(ipo.port));
				if (k != null) {
					this.listen.put(k);
				}
			}
		} else {
			LINK k = LINK.create(ipo.ip, CastUtil.cint(ipo.port));
			if (k != null) {
				this.listen.put(k);
			}
		}

	}

	private IPO getIPPORT(String listenAddr) {
		try {
			IPO ipo = new IPO();
			int x = listenAddr.lastIndexOf(':');
			if (x < 0) {
				x = listenAddr.lastIndexOf(".");
			}
			ipo.ip = listenAddr.substring(0, x);
			ipo.port = listenAddr.substring(x + 1);
			return ipo;
		} catch (Exception e) {
			return null;
		}
	}

	public void addOutter(String local, String remote) {

		IPO localIPO = getIPPORT(local);
		if (localIPO == null || localIPO.isIPv6())
			return;

		if (this.hasListen(localIPO.ip, localIPO.port)) {
			return;
		}
		IPO remortIPO = getIPPORT(remote);
		if (remortIPO == null || remortIPO.isIPv6() || remortIPO.isLocal127())
			return;
		LINK k = LINK.create(remortIPO.ip, CastUtil.cint(remortIPO.port));
		if (k != null) {
			this.outter.put(k);
		}
	}

	private boolean hasListen(String ip, String port) {
		LINK k = LINK.create(ip, CastUtil.cint(port));
		if (k == null)
			return false;
		else
			return this.listen.contains(k);
	}

	public JSONObject toJSON() {
		JSONObject o = new JSONObject();
		o.put("attr", new JSONObject(this.attr.toJSONString()));
		o.put("listen", toJSON(this.listen));
		o.put("outter", toJSON(this.outter));
		return o;
	}

	private JSONArray toJSON(LinkedSet<LINK> data) {
		JSONArray out = new JSONArray();
		Enumeration<LINK> en = data.elements();
		while (en.hasMoreElements()) {
			LINK k = en.nextElement();
			out.put(k.toString());
		}
		return out;
	}
}
