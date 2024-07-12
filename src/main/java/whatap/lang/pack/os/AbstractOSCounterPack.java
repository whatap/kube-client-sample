package whatap.lang.pack.os;

import whatap.io.DataIOException;
import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.pack.AbstractPack;
import whatap.lang.pack.Pack;
import whatap.lang.value.BooleanValue;
import whatap.lang.value.DecimalValue;
import whatap.lang.value.FloatValue;
import whatap.lang.value.IntMapValue;
import whatap.lang.value.TextValue;
import whatap.lang.value.Value;

import java.io.IOException;

abstract public class AbstractOSCounterPack extends AbstractPack {
	protected IntMapValue osCounter = new IntMapValue();

	public AbstractOSCounterPack() {
	}

	public Value get(int mx) {
		return osCounter.get(mx);
	}

	public boolean getBoolean(int mx) {
		Value v = get(mx);

		if (v instanceof BooleanValue) {
			return ((BooleanValue) v).value;
		}
		return false;
	}

	public int getInt(int mx) {
		Value v = get(mx);

		if (v instanceof Number) {
			return ((Number) v).intValue();
		}
		return 0;
	}

	public long getLong(int mx) {
		Value v = get(mx);
		if (v instanceof Number) {
			return ((Number) v).longValue();
		}
		return 0;
	}

	public float getFloat(int mx) {
		Value v = get(mx);
		if (v instanceof Number) {
			return ((Number) v).floatValue();
		}
		return 0;
	}

	public String getText(int mx) {
		Value v = get(mx);
		if (v instanceof TextValue) {
			return ((TextValue) v).value;
		}
		return null;
	}

	public void put(int mx, Value value) {
		osCounter.put(mx, value);
	}

	public void put(int mx, String value) {
		put(mx, new TextValue(value));
	}

	public void put(int mx, long value) {
		put(mx, new DecimalValue(value));
	}

	public void put(int mx, float value) {
		put(mx, new FloatValue(value));
	}

	public void clear() {
		osCounter.clear();
	}

	@Override
	abstract public short getPackType();

	@Override
	public void write(DataOutputX dout) {
		super.write(dout);
		try {
		osCounter.write(dout);
		} catch(IOException e) {
			throw new DataIOException(e);
		}
	}

	@Override
	public Pack read(DataInputX din) {
		super.read(din);
		try {
			osCounter.putAll((IntMapValue)new IntMapValue().read(din));
		} catch(IOException e) {
			throw new DataIOException(e);
		}
		return this;
	}
}
