package whatap.util;

public class Pair4<A, B, C, D> implements Comparable<Pair4<A, B, C, D>> {
	private final A a;
	private final B b;
	private final C c;
	private final D d;

	public Pair4(A a, B b, C c, D d) {
		this.a = a;
		this.b = b;
		this.c = c;
		this.d = d;
	}

	public A getA() {
		return a;
	}

	public B getB() {
		return b;
	}

	public C getC() {
		return c;
	}

	public D getD() {
		return d;
	}

	@Override
	public String toString() {
		return "[" + a + "," + b + "," + c + "," + d + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((b == null) ? 0 : b.hashCode());
		result = prime * result + ((c == null) ? 0 : c.hashCode());
		result = prime * result + ((d == null) ? 0 : d.hashCode());
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
		Pair4 other = (Pair4) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (b == null) {
			if (other.b != null)
				return false;
		} else if (!b.equals(other.b))
			return false;
		if (c == null) {
			if (other.c != null)
				return false;
		} else if (!c.equals(other.c))
			return false;
		if (d == null) {
			if (other.d != null)
				return false;
		} else if (!d.equals(other.d))
			return false;
		return true;
	}

	public int compareTo(Pair4<A, B, C, D> o) {
		int v1 = CompareUtil.compareTo((Comparable)this.a,(Comparable)o.a);
		if (v1 != 0)
			return v1;
		v1 = CompareUtil.compareTo((Comparable)this.b,(Comparable)o.b);
		if (v1 != 0)
			return v1;
		v1 = CompareUtil.compareTo((Comparable)this.c,(Comparable)o.c);
		if (v1 != 0)
			return v1;
		return CompareUtil.compareTo((Comparable)this.d,(Comparable)o.d);
	}
}
