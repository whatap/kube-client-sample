package whatap.lang.var;

import whatap.io.DataOutputX;
import whatap.util.CompareUtil;

public class LongInt implements Comparable<LongInt> {
	public long v1;
	public int v2;

	public LongInt() {
	}

	public LongInt(long v1, int v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (v1 ^ (v1 >>> 32));
		result = prime * result + v2;
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
		LongInt other = (LongInt) obj;
		if (v1 != other.v1)
			return false;
		if (v2 != other.v2)
			return false;
		return true;
	}

	public int compareTo(LongInt o) {
		if (this.v1 != o.v1)
			return CompareUtil.compareTo(this.v1, o.v1);
		return CompareUtil.compareTo(this.v2, o.v2);
	}

	public byte[] toBytes() {
		byte[] b = new byte[12];
		DataOutputX.toBytes(b, 0, v1);
		DataOutputX.toBytes(b, 8, v2);
		return b;
	}
}
