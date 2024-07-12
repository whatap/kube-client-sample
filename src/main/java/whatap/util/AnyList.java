package whatap.util;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.Value;

abstract public class AnyList {
	public final static byte INT=1;
	public final static byte LONG=2;
	public final static byte FLOAT=3;
	public final static byte DOUBLE=4;
	public final static byte STRING=5;
	public final static byte VALUE=6;

	public static AnyList create(byte type) {
		switch (type) {
		case INT:
			return new IntList();
		case LONG:
			return new LongList();
		case FLOAT:
			return new FloatList();
		case DOUBLE:
			return new DoubleList();
		case STRING:
			return new StringList();
		case VALUE:
			return new ValueList();
		}
		throw new RuntimeException("unknown list type=" + type);
	}
	
	public abstract byte getType();
	public abstract void setInt(int i, int v);
	public abstract void setFloat(int i, float v);
	public abstract void setLong(int i, long v);
	public abstract void setDouble(int i, double v);
	public abstract void setString(int i, String v);

	public abstract void addInt(int v);
	public abstract void addFloat(float v);
	public abstract void addLong(long v);
	public abstract void addDouble(double v);
	public abstract void addString(String v);

	
	public abstract int getInt(int i);
	public abstract float getFloat(int i);
	public abstract double getDouble(int i);
	public abstract long getLong(int i);
	public abstract String getString(int i);
	public abstract Object getObject(int i);
	public abstract Value getValue(int i);
	
	public abstract void write(DataOutputX out);
	public abstract void read(DataInputX in);
	
	public abstract int size();
	
	public abstract int[] sorting(boolean asc);
	public abstract int[] sorting(boolean asc, AnyList second, boolean childAsc);
	public abstract AnyList filtering(int[] index);
	
	protected int compareChild(AnyList child, boolean ord, int i1, int i2) {
		if (child.getType() == AnyList.STRING) {
			if (ord) {
				return CompareUtil.compareTo(child.getString(i1), child.getString(i2));
			} else {
				return CompareUtil.compareTo(child.getString(i2), child.getString(i1));
			}
		} else {
			if (ord) {
				return CompareUtil.compareTo(child.getDouble(i1), child.getDouble(i2));
			} else {
				return CompareUtil.compareTo(child.getDouble(i2), child.getDouble(i1));
			}
		}
	}

}
