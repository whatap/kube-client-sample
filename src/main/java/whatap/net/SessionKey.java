package whatap.net;

import whatap.util.SocketUtil;

public class SessionKey {
	public long pcode;
	public String host;
	public int port;

	public SessionKey(long pcode, String host, int port) {
		this.pcode = pcode;
		this.host = host;
		this.port = port;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((host == null) ? 0 : host.hashCode());
		result = prime * result + port;
		result = prime * result + (int) (pcode ^ (pcode >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SessionKey other = (SessionKey) obj;
		if (host == null) {
			if (other.host != null)
				return false;
		} else if (!host.equals(other.host))
			return false;
		if (port != other.port)
			return false;
		if (pcode != other.pcode)
			return false;
		return true;
	}

	public SocketUtil.Addr getSocketAddr() {
		return new SocketUtil.Addr(host, port);
	}

}
