package whatap.lang.var;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.CompareUtil;

public class I3 implements Comparable<I3> {
	public int v1;
	public int v2;
	public int v3;

	public I3(){		
	}
	public I3(int v1, int v2, int v3) {
		this.v1 = v1;
		this.v2 = v2;
		this.v3 = v3;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + v1;
		result = prime * result + v2;
		result = prime * result + v3;
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
		I3 other = (I3) obj;
		return (v2 == other.v2 && v3 == other.v3 && v1 == other.v1);
	}

	public int compareTo(I3 o) {
		if (this.v1 != o.v1)
			return CompareUtil.compareTo(this.v1, o.v1);
		if (this.v2 != o.v2)
			return CompareUtil.compareTo(this.v2, o.v2);
		return CompareUtil.compareTo(this.v3, o.v3);
	}

	public byte[] toBytes() {
		byte[] b = new byte[12];
		DataOutputX.toBytes(b, 0, v1);
		DataOutputX.toBytes(b, 4, v2);
		DataOutputX.toBytes(b, 8, v3);
		return b;
	}

	public I3 toObject(byte[] b) {
		this.v1 = DataInputX.toInt(b, 0);
		this.v2 = DataInputX.toInt(b, 4);
		this.v3 = DataInputX.toInt(b, 8);
		return this;
	}
}
