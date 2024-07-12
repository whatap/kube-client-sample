package whatap.util;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.LongValue;
import whatap.lang.value.Value;

import java.util.Arrays;
import java.util.Comparator;

public class LongList extends AnyList{
	private static final int DEFAULT_CAPACITY = 10;
	private static final long[] EMPTY = {};
	private static final int MAX_SIZE = Integer.MAX_VALUE - 8;

	private transient long[] table;
	private int size;

	public LongList() {
		this.table = EMPTY;
	}

	public LongList(int initial) {
		if (initial <= 0)
			this.table = EMPTY;
		else
			this.table = new long[initial];
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

			long[] copy = new long[newSize];
			System.arraycopy(table, 0, copy, 0, table.length);
			table = copy;
		}
	}

	public long get(int i) {
		if (i >= size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);
		return this.table[i];
	}

	public long set(int i, long v) {
		if (i >= size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);

		long ov = this.table[i];
		this.table[i] = v;
		return ov;
	}

	public long remove(int i) {
		if (i >= size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);

		long ov = this.table[i];

		int movNum = size - i - 1;
		if (movNum > 0)
			System.arraycopy(table, i + 1, table, i, movNum);
		
		table[--size] = 0; 
		return ov;
	}

	public void add(long e) {
		ensure(size + 1);
		table[size++] = e;
	}

	public void addAll(LongList other) {
		ensure(this.size + other.size);
		for (int i = 0; i < other.size; i++) {
			this.table[this.size++] = other.table[i];
		}
	}

	public void addAll(long[] other) {
		ensure(this.size + other.length);
		for (int i = 0; i < other.length; i++) {
			this.table[this.size++] = other[i];
		}
	}

	public int size() {
		return this.size;
	}

	public long[] toArray() {
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
	public static void main(String[] args) {
		LongList f = new LongList();
		for(int i = 0 ; i <100  ; i++) {
			f.add(i);
		}
		System.out.println(f.toString());
	}
	public void setInt(int i, int v) {
		this.set(i, v);
	}
	public void setFloat(int i, float v) {
		this.set(i, (long) v);
	}
	public void setLong(int i, long v) {
		this.set(i, v);		
	}
	public void setDouble(int i, double v) {
		this.set(i, (long) v);		
	}
	public void setString(int i, String v) {
		this.set(i, CastUtil.cint(v));
	}
	public void addInt(int v) {
		this.add(v);
	}
	public void addFloat(float v) {
		this.add((long) v);
	}
	public void addLong(long v) {
		this.add(v);
	}
	public void addDouble(double v) {
		this.add((long) v);
	}
	public void addString(String v) {
		this.add(CastUtil.clong(v));
	}
	public int getInt(int i) {
		return (int) this.get(i);
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
		return Long.toString(this.get(i));
	}
	public Object getObject(int i) {
		return new Long(this.get(i));
	}
	public void write(DataOutputX dout) {
		dout.writeInt3(this.size());
		for (int i = 0; i < size; i++) {
			dout.writeDecimal(this.get(i));
		}
	}
	public void read(DataInputX in) {
		int count = in.readInt3();
		for(int i = 0 ; i <count ; i++) {
			this.add(in.readDecimal());
		}
	}
	
	public byte getType() {
		return AnyList.LONG;
	}	
	public AnyList filtering(int[] inx) {
		LongList out= new LongList(this.size());
		for(int i=0; i<inx.length ; i++) {
			out.add(this.get(inx[i]));
		}
		return out;
	}
	private static class KeyVal{
		int key;
		long value;
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
		return new LongValue(this.get(i));
	}

}
