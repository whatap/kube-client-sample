package whatap.lang;

public class PKOID implements Comparable<Object> {
	public final long pcode;
	public final int okind;
	public final int oid;

	public PKOID(long pcode, int okind, int oid) {
		this.pcode = pcode;
		this.okind = okind;
		this.oid = oid;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + oid;
		result = prime * result + okind;
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
		PKOID other = (PKOID) obj;
		if (oid != other.oid)
			return false;
		if (okind != other.okind)
			return false;
		if (pcode != other.pcode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + pcode + "," + okind + "," + oid + "]";
	}

	public int compareTo(Object n) {
		PKOID o = (PKOID) n;
		long v1 = this.pcode - o.pcode;
		if (v1 != 0)
			return v1 > 0 ? 1 : -1;
		v1 = this.okind - o.okind;
		if (v1 != 0)
			return v1 > 0 ? 1 : -1;
		v1 = this.oid - o.oid;
		if (v1 != 0)
			return v1 > 0 ? 1 : -1;
		return 0;
	}

}
