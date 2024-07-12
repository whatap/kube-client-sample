package whatap.lang.var;

import whatap.util.CompareUtil;

public class IntString implements Comparable<IntString> {
	public int v1;
	public String v2;

	public IntString() {
	}

	public IntString(int v1, String v2) {
		this.v1 = v1;
		this.v2 = v2;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + v1;
		result = prime * result + ((v2 == null) ? 0 : v2.hashCode());
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
		IntString other = (IntString) obj;
		if (v1 != other.v1)
			return false;
		if (v2 == null) {
			if (other.v2 != null)
				return false;
		} else if (!v2.equals(other.v2))
			return false;
		return true;
	}

	public int compareTo(IntString o) {
		if (this.v1 != o.v1)
			return CompareUtil.compareTo(this.v1, o.v1);
		return CompareUtil.compareTo(this.v2, o.v2);
	}

}
