package whatap.lang.var;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.CompareUtil;

public class I2 implements Comparable<I2> {
	public int v1;
	public int v2;

	public I2() {
	}

	public I2(int v1, int v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + v2;
		result = prime * result + v1;
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
		I2 other = (I2) obj;
		return (v2 == other.v2 && v1 == other.v1);
	}

	public int compareTo(I2 o) {
		if (this.v1 != o.v1)
			return CompareUtil.compareTo(this.v1, o.v1);
		return CompareUtil.compareTo(this.v2, o.v2);
	}

	public byte[] toBytes() {
		byte[] b = new byte[8];
		DataOutputX.toBytes(b, 0, v1);
		DataOutputX.toBytes(b, 4, v2);
		return b;
	}

	public I2 toObject(byte[] b) {
		this.v1 = DataInputX.toInt(b, 0);
		this.v2 = DataInputX.toInt(b, 4);
		return this;
	}
}
