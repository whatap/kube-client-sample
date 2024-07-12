package whatap.lang.var;

import whatap.io.DataOutputX;
import whatap.util.CompareUtil;

public class L3 implements Comparable<L3> {
	public long v1;
	public long v2;
	public long v3;

	public L3(long v1, long v2, long v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (v1 ^ (v1 >>> 32));
		result = prime * result + (int) (v2 ^ (v2 >>> 32));
		result = prime * result + (int) (v3 ^ (v3 >>> 32));
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
		L3 other = (L3) obj;
		if (v1 != other.v1)
			return false;
		if (v2 != other.v2)
			return false;
		if (v3 != other.v3)
			return false;
		return true;
	}

	public int compareTo(L3 o) {
		if (this.v1 != o.v1)
			return CompareUtil.compareTo(this.v1, o.v1);
		if (this.v2 != o.v2)
			return CompareUtil.compareTo(this.v2, o.v2);
		return CompareUtil.compareTo(this.v3, o.v3);
	}

	public byte[] toBytes() {
		byte[] b = new byte[24];
		DataOutputX.toBytes(b, 0, v1);
		DataOutputX.toBytes(b, 8, v2);
		DataOutputX.toBytes(b, 16, v3);
		return b;
	}
}
