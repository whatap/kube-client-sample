package whatap.util;

import java.net.InetSocketAddress;
import java.net.Socket;

public class SocketUtil {
	public static class Addr {
		public String host;
		public int port;

		public Addr(String host, int port) {
			this.host = host;
			this.port = port;
		}

		public String toString() {
			return this.host + ":" + this.port;
		}

		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result + ((host == null) ? 0 : host.hashCode());
			result = prime * result + port;
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
			Addr other = (Addr) obj;
			if (host == null) {
				if (other.host != null)
					return false;
			} else if (!host.equals(other.host))
				return false;
			if (port != other.port)
				return false;
			return true;
		}

	}

	public static Addr parse(String addr) {
		if (addr == null)
			return null;
		String[] s = StringUtil.divKeyValue(addr, ":");
		if (s.length != 2)
			return null;
		return new Addr(s[0], CastUtil.cint(s[1]));
	}

	public static boolean isBindedTcpPort(int port) {
		Socket conn = new Socket();
		try {
			conn.connect(new InetSocketAddress("127.0.0.1", port), 1000);
			return true;
		} catch (Exception e) {
		} finally {
			FileUtil.close(conn);
		}
		return false;
	}
}
