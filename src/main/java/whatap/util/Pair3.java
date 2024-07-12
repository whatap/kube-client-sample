package whatap.util;

public class Pair3<L, M, R> implements Comparable<Pair3<L, M, R>> {
	private final L left;
	private final M middle;
	private final R right;

	public Pair3(L left, M middle, R right) {
		this.left = left;
		this.middle = middle;
		this.right = right;
	}

	public L getLeft() {
		return left;
	}

	public M getMiddle() {
		return middle;
	}

	public R getRight() {
		return right;
	}

	@Override
	public String toString() {
		return "[" + left + "," + middle + "," + right + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((left == null) ? 0 : left.hashCode());
		result = prime * result + ((middle == null) ? 0 : middle.hashCode());
		result = prime * result + ((right == null) ? 0 : right.hashCode());
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
		Pair3 other = (Pair3) obj;
		if (left == null) {
			if (other.left != null)
				return false;
		} else if (!left.equals(other.left))
			return false;
		if (middle == null) {
			if (other.middle != null)
				return false;
		} else if (!middle.equals(other.middle))
			return false;
		if (right == null) {
			if (other.right != null)
				return false;
		} else if (!right.equals(other.right))
			return false;
		return true;
	}

	public int compareTo(Pair3<L, M, R> o) {
		int v1 = CompareUtil.compareTo((Comparable)this.left,(Comparable)o.left);
		if (v1 != 0)
			return v1;
		v1 = CompareUtil.compareTo((Comparable)this.middle,(Comparable)o.middle);
		if (v1 != 0)
			return v1;
		return CompareUtil.compareTo((Comparable)this.right,(Comparable)o.right);
	}
}
