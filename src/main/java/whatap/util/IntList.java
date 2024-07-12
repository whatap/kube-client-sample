package whatap.util;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.IntValue;
import whatap.lang.value.Value;

import java.util.Arrays;
import java.util.Comparator;

public class IntList extends AnyList {
	private static final int DEFAULT_CAPACITY = 10;
	private static final int[] EMPTY = {};
	private static final int MAX_SIZE = Integer.MAX_VALUE - 8;

	private transient int[] table;
	private int size;

	public IntList() {
		this.table = EMPTY;
	}

	public IntList(int initialCapa) {
		if (initialCapa <= 0)
			this.table = EMPTY;
		else
			this.table = new int[initialCapa];
	}

	private void ensure(int minCapacity) {
		if (minCapacity > table.length) {
			if (table == EMPTY) {
				minCapacity = Math.max(DEFAULT_CAPACITY, minCapacity);
			}
			int oldSize = table.length;
			int newSize = oldSize + (oldSize >> 1);
			if (newSize < minCapacity)
				newSize = minCapacity;
			if (newSize > MAX_SIZE) {
				throw new IllegalArgumentException("too big size");
			}

			int[] copy = new int[newSize];
			System.arraycopy(table, 0, copy, 0, table.length);
			table = copy;
		}
	}

	public int get(int i) {
		if (i >= size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);
		return this.table[i];
	}

	public int set(int i, int v) {
		if (i >= size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);

		int ov = this.table[i];
		this.table[i] = v;
		return ov;
	}

	public int remove(int i) {
		if (i >= size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);

		int ov = this.table[i];

		int movNum = size - i - 1;
		if (movNum > 0)
			System.arraycopy(table, i + 1, table, i, movNum);

		table[--size] = 0;
		return ov;
	}

	public void add(int e) {
		ensure(size + 1);
		table[size++] = e;
	}

	public void addAll(IntList other) {
		ensure(this.size + other.size);
		for (int i = 0; i < other.size; i++) {
			this.table[this.size++] = other.table[i];
		}
	}

	public void addAll(int[] other) {
		ensure(this.size + other.length);
		for (int i = 0; i < other.length; i++) {
			this.table[this.size++] = other[i];
		}
	}

	public int size() {
		return this.size;
	}

	public int[] toArray() {
		return Arrays.copyOf(this.table, size);
	}

	public String toString() {
		return Arrays.toString(this.table);
	}

	public void sort() {
		if (size > 0) {
			Arrays.sort(this.table, 0, size - 1);
		}
	}

	public void setInt(int i, int v) {
		this.set(i, v);
	}

	public void setFloat(int i, float v) {
		this.set(i, (int) v);
	}

	public void setLong(int i, long v) {
		this.set(i, (int) v);
	}

	public void setDouble(int i, double v) {
		this.set(i, (int) v);
	}

	public void setString(int i, String v) {
		this.set(i, CastUtil.cint(v));
	}

	public void addInt(int v) {
		this.add(v);
	}

	public void addFloat(float v) {
		this.add((int) v);
	}

	public void addLong(long v) {
		this.add((int) v);
	}

	public void addDouble(double v) {
		this.add((int) v);
	}

	public void addString(String v) {
		this.add(CastUtil.cint(v));
	}

	public int getInt(int i) {
		return this.get(i);
	}

	public float getFloat(int i) {
		return this.get(i);
	}

	public double getDouble(int i) {
		return this.get(i);
	}

	public long getLong(int i) {
		return this.get(i);
	}

	public String getString(int i) {
		return Integer.toString(this.get(i));
	}

	public Object getObject(int i) {
		return new Integer(this.get(i));
	}

	public void write(DataOutputX dout) {
		dout.writeInt3(this.size());
		for (int i = 0; i < size; i++) {
			dout.writeDecimal(this.get(i));
		}
	}

	public void read(DataInputX in) {
		int count = in.readInt3();
		for (int i = 0; i < count; i++) {
			this.add((int) in.readDecimal());
		}
	}

	public byte getType() {
		return AnyList.INT;
	}

	public AnyList filtering(int[] inx) {
		IntList out = new IntList(this.size());
		for (int i = 0; i < inx.length; i++) {
			out.add(this.get(inx[i]));
		}
		return out;
	}

	private static class KeyVal {
		int key;
		int value;
	}

	public int[] sorting(final boolean asc) {
		KeyVal[] table = new KeyVal[this.size()];
		for (int i = 0; i < this.size(); i++) {
			table[i] = new KeyVal();
			table[i].key = i;
			table[i].value = get(i);
		}

		Arrays.sort(table, new Comparator<KeyVal>() {
			public int compare(KeyVal o1, KeyVal o2) {
				if (asc) {
					return CompareUtil.compareTo(o1.value, o2.value);
				} else {
					return CompareUtil.compareTo(o2.value, o1.value);
				}
			}
		});

		int[] out = new int[this.size()];
		for (int i = 0; i < this.size(); i++) {
			out[i] = table[i].key;
		}
		return out;
	}

	public int[] sorting(final boolean asc, final AnyList child, final boolean childAsc) {
		KeyVal[] table = new KeyVal[this.size()];
		for (int i = 0; i < this.size(); i++) {
			table[i] = new KeyVal();
			table[i].key = i;
			table[i].value = get(i);
		}

		Arrays.sort(table, new Comparator<KeyVal>() {
			public int compare(KeyVal o1, KeyVal o2) {
				int c;
	    		if (asc) {
					 c = CompareUtil.compareTo(o1.value, o2.value);
				} else {
					 c = CompareUtil.compareTo(o2.value, o1.value);
				}
				return c != 0 ? c : compareChild(child, childAsc, o1.key, o2.key);
			}
		});

		int[] out = new int[this.size()];
		for (int i = 0; i < this.size(); i++) {
			out[i] = table[i].key;
		}
		return out;
	}
	public Value getValue(int i) {
		return new IntValue(this.get(i));
	}

}
