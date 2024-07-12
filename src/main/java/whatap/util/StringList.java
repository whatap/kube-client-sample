package whatap.util;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.TextValue;
import whatap.lang.value.Value;

import java.util.Arrays;
import java.util.Comparator;

public class StringList extends AnyList {
	private static final int DEFAULT_CAPACITY = 10;
	private static final String[] EMPTY = {};
	private static final int MAX_SIZE = Integer.MAX_VALUE - 8;

	private transient String[] table;
	private int size;

	public StringList() {
		this.table = EMPTY;
	}

	public StringList(int initialCapa) {
		if (initialCapa <= 0)
			this.table = EMPTY;
		else
			this.table = new String[initialCapa];
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

			String[] copy = new String[newSize];
			System.arraycopy(table, 0, copy, 0, table.length);
			table = copy;
		}
	}

	public String get(int i) {
		if (i >= size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);
		return this.table[i];
	}

	public String set(int i, String v) {
		if (i >= size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);

		String ov = this.table[i];
		this.table[i] = v;
		return ov;
	}

	public String remove(int i) {
		if (i >= size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);

		String ov = this.table[i];

		int movNum = size - i - 1;
		if (movNum > 0)
			System.arraycopy(table, i + 1, table, i, movNum);

		table[--size] = null;
		return ov;
	}

	public void add(String e) {
		ensure(size + 1);
		table[size++] = e;
	}

	public void addAll(StringList other) {
		ensure(this.size + other.size);
		for (int i = 0; i < other.size; i++) {
			this.table[this.size++] = other.table[i];
		}
	}

	public void addAll(String[] other) {
		ensure(this.size + other.length);
		for (int i = 0; i < other.length; i++) {
			this.table[this.size++] = other[i];
		}
	}

	
	public int size() {
		return this.size;
	}

	public String[] toArray() {
		return Arrays.copyOf(this.table, size);
	}

	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for(int i=0; i < size; i++) {
			if(i>0) builder.append(", ");
			builder.append(get(i));
		}
		builder.append("}");
		return builder.toString();
	}

	public StringList sort(boolean asc) {
		if (size > 0) {
			if(asc) {
			Arrays.sort(this.table, 0, size - 1);
			}else {
				Arrays.sort(this.table, 0, size - 1 ,new Comparator<String>() {
					
					public int compare(String o1, String o2) {
						return o2.compareTo(o1);
					};
				});
			}
		}
		return this;
	}

	
	public void setInt(int i, int v) {
		this.set(i, Integer.toString(v));
	}

	
	public void setFloat(int i, float v) {
		this.set(i, Float.toString(v));
	}

	
	public void setLong(int i, long v) {
		this.set(i, Long.toString(v));
	}

	
	public void setDouble(int i, double v) {
		this.set(i, FormatUtil.print(v, "#0.0####"));
	}

	
	public void setString(int i, String v) {
		this.set(i, v);
	}

	
	public void addInt(int v) {
		this.add(Integer.toString(v));
	}

	
	public void addFloat(float v) {
		this.add(Float.toString(v));
	}

	
	public void addLong(long v) {
		this.add(Long.toString(v));
	}

	
	public void addDouble(double v) {
		this.add(FormatUtil.print(v, "#0.0####"));
	}

	
	public void addString(String v) {
		this.add(v);
	}

	
	public int getInt(int i) {
		return CastUtil.cint(this.get(i));
	}

	
	public float getFloat(int i) {
		return CastUtil.cfloat(this.get(i));
	}

	
	public double getDouble(int i) {
		return CastUtil.cdouble(this.get(i));
	}

	
	public long getLong(int i) {
		return CastUtil.clong(this.get(i));
	}

	
	public String getString(int i) {
		return this.get(i);
	}
	
	public Object getObject(int i) {
		return this.get(i);
	}
	
	public void write(DataOutputX dout) {
		dout.writeInt3(this.size);
		for (int i = 0; i < size; i++) {
			dout.writeText(this.get(i));
		}
	}

	
	public void read(DataInputX in) {
		int sz = in.readInt3();
		for (int i = 0; i < sz; i++) {
			this.add(in.readText());
		}
	}

	
	public byte getType() {
		return AnyList.STRING;
	}
	
	public AnyList filtering(int[] inx) {
		StringList out= new StringList(this.size());
		for(int i=0; i<inx.length ; i++) {
			out.add(this.get(inx[i]));
		}
		return out;
	}
	private static class KeyVal{
		int key;
		String value;
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
	    		if(asc) {
	    			return CompareUtil.compareTo(o1.value, o2.value) ;
	    		}else {
	    			return CompareUtil.compareTo(o2.value, o1.value) ;
	    		}
	    	}
		});

	    int[] out = new int[this.size()];
	    for (int i = 0; i < this.size(); i++) {
	    	out[i]=table[i].key;
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
	    	out[i]=table[i].key;
	    }
		return out;
	}

	
	public Value getValue(int i) {
		return new TextValue(this.get(i));
	}
}
