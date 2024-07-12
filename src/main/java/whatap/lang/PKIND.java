package whatap.lang;

public class PKIND implements Comparable<Object> {
	public final long pcode;
	public final int okind;

	public PKIND(long pcode, int oid) {
		this.pcode = pcode;
		this.okind = oid;
	}

	public long getPCode() {
		return pcode;
	}

	public int getOid() {
		return okind;
	}



	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
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
		PKIND other = (PKIND) obj;
		if (okind != other.okind)
			return false;
		if (pcode != other.pcode)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[" + pcode + "," + okind + "]";
	}

	public int compareTo(Object n) {
		PKIND o = (PKIND) n;
		long v1 = this.pcode - o.pcode;
		if (v1 != 0)
			return v1 > 0 ? 1 : -1;
		v1 = this.okind - o.okind;
		if (v1 != 0)
			return v1 > 0 ? 1 : -1;
		return 0;
	}

	
}
