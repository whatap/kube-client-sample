package whatap.lang;

public class OKey implements Comparable<Object> {
	public final long pcode;
	public final int oid;

	public OKey(long pcode, int oid) {
		this.pcode = pcode;
		this.oid = oid;
	}

	public long getPCode() {
		return pcode;
	}

	public int getOid() {
		return oid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (oid ^ (oid >>> 32));
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
		OKey other = (OKey) obj;
		if (oid != other.oid)
			return false;
		if (pcode != other.pcode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + pcode + "," + oid + "]";
	}

	public int compareTo(Object n) {
		OKey o = (OKey) n;
		long v1 = this.pcode - o.pcode;
		if (v1 != 0)
			return v1 > 0 ? 1 : -1;
		v1 = this.oid - o.oid;
		if (v1 != 0)
			return v1 > 0 ? 1 : -1;
		return 0;
	}

	
}
