package whatap.util;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.DecimalValue;
import whatap.lang.value.DoubleValue;
import whatap.lang.value.FloatValue;
import whatap.lang.value.TextValue;
import whatap.lang.value.Value;

import java.util.Arrays;
import java.util.Comparator;

public class ValueList extends AnyList{
	private static final int DEFAULT_CAPACITY = 10;
	private static final Value[] EMPTY = {};
	private static final int MAX_SIZE = Integer.MAX_VALUE - 8;

	transient Value[] table;
	private int size;

	public ValueList() {
		this.table = EMPTY;
	}

	public ValueList(int initialCapa) {
		if (initialCapa <= 0)
			this.table = EMPTY;
		else
			this.table = new Value[initialCapa];
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

			Value[] copy = new Value[newSize];
			System.arraycopy(table, 0, copy, 0, table.length);
			table = copy;
		}
	}

	public Value get(int i) {
		if (i >= size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);
		return this.table[i];
	}

	public Value set(int i, Value v) {
		if (i >= size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);

		Value ov = this.table[i];
		this.table[i] = v;
		return ov;
	}

	public Value remove(int i) {
		if (i >= size)
			throw new IndexOutOfBoundsException("Index: " + i + ", Size: " + size);

		Value ov = this.table[i];

		int movNum = size - i - 1;
		if (movNum > 0)
			System.arraycopy(table, i + 1, table, i, movNum);

		table[--size] = null;
		return ov;
	}

	public void add(Value e) {
		ensure(size + 1);
		table[size++] = e;
	}

	public void addAll(ValueList other) {
		ensure(this.size + other.size);
		for (int i = 0; i < other.size; i++) {
			this.table[this.size++] = other.table[i];
		}
	}

	public void addAll(Value[] other) {
		ensure(this.size + other.length);
		for (int i = 0; i < other.length; i++) {
			this.table[this.size++] = other[i];
		}
	}

	public int size() {
		return this.size;
	}
	public AnyList setSize(int size) {
		ensure(size);
		this.size=size;
		return this;
	}
	public Value[] toArray() {
		return Arrays.copyOf(this.table, size);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ValueList [table=");
		for(int i=0; i < size; i++) {
			if(i>0) builder.append(',');
			builder.append(get(i));
		}
		builder.append("]");
		return builder.toString();
	}

	public ValueList sort(boolean asc) {
		if (size > 0) {
			if(asc) {
			Arrays.sort(this.table, 0, size - 1);
			}else {
				Arrays.sort(this.table, 0, size - 1 ,new Comparator<Value>() {
					public int compare(Value o1, Value o2) {
						return o2.compareTo(o1);
					}; 
				});				
			}
		}
		return this;
	}

	public void setInt(int i, int v) {
		this.set(i, new DecimalValue(v));
	}

	public void setFloat(int i, float v) {
		this.set(i, new FloatValue(v));
	}

	public void setLong(int i, long v) {
		this.set(i, new DecimalValue(v));
	}

	public void setDouble(int i, double v) {
		this.set(i, new DoubleValue(v));
	}

	public void setString(int i, String v) {
		this.set(i, new TextValue(v));
	}

	public void addInt(int v) {
		this.add(new DecimalValue(v));
	}

	public void addFloat(float v) {
		this.add(new FloatValue(v));
	}

	public void addLong(long v) {
		this.add(new DecimalValue(v));
	}

	public void addDouble(double v) {
		this.add(new DoubleValue(v));
	}

	public void addString(String v) {
		this.add(new TextValue(v));
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
		return CastUtil.cString(this.get(i));
	}
	public Object getObject(int i) {
		return this.get(i);
	}
	
	public void write(DataOutputX dout) {
		dout.writeInt3(this.size);
		for (int i = 0; i < size; i++) {
			dout.writeValue(this.get(i));
		}
	}

	public void read(DataInputX in) {
		int sz = in.readInt3();
		for (int i = 0; i < sz; i++) {
			this.add(in.readValue());
		}
	}

	public byte getType() {
		return AnyList.VALUE;
	}
	public AnyList filtering(int[] inx) {
		ValueList out= new ValueList(this.size());
		for(int i=0; i<inx.length ; i++) {
			out.add(this.get(inx[i]));
		}
		return out;
	}
	private static class KeyVal{
		int key;
		Value value;
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
		return this.get(i);
	}
}
