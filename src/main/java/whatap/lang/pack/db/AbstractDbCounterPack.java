/*
 *  Copyright 2015 the original author or authors.
 *  @https://github.com/scouter-project/scouter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package whatap.lang.pack.db;

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
import whatap.util.StringKeyLinkedMap;
import whatap.util.StringKeyLinkedMap.StringKeyLinkedEntry;

import java.io.IOException;
import java.util.Enumeration;

abstract public class AbstractDbCounterPack extends AbstractPack {
	protected StringKeyLinkedMap<IntMapValue> dbms = new StringKeyLinkedMap<IntMapValue>() {
		@Override
		protected IntMapValue create(String key) {
			return new IntMapValue();
		};
	};

	public AbstractDbCounterPack() {
	}

	public String[] getDBNames() {
		return dbms.keyArray();
	}

	public Value get(String db, int mx) {
		IntMapValue dbm = this.dbms.get(db);
		return dbm != null ? dbm.get(mx) : null;
	}

	public boolean getBoolean(String db, int mx) {
		Value v = get(db, mx);
		if (v instanceof BooleanValue) {
			return ((BooleanValue) v).value;
		}
		return false;
	}

	public int getInt(String db, int mx) {
		Value v = get(db, mx);
		if (v instanceof Number) {
			return ((Number) v).intValue();
		}
		return 0;
	}

	public long getLong(String db, int mx) {
		Value v = get(db, mx);
		if (v instanceof Number) {
			return ((Number) v).longValue();
		}
		return 0;
	}

	public float getFloat(String db, int mx) {
		Value v = get(db, mx);
		if (v instanceof Number) {
			return ((Number) v).floatValue();
		}
		return 0;
	}

	public String getText(String db, int mx) {
		Value v = get(db, mx);
		if (v instanceof TextValue) {
			return ((TextValue) v).value;
		}
		return null;
	}

	public void put(String db, int mx, Value value) {
		this.dbms.intern(db).put(mx, value);
	}

	public void put(String db, int mx, String value) {
		put(db, mx, new TextValue(value));
	}

	public void put(String db, int mx, long value) {
		put(db, mx, new DecimalValue(value));
	}

	public void put(String db, int mx, float value) {
		put(db, mx, new FloatValue(value));
	}

	public void clear() {
		this.dbms.clear();
	}

	@Override
	abstract public short getPackType();

	@Override
	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeShort(this.dbms.size());
		Enumeration<StringKeyLinkedEntry<IntMapValue>> en = this.dbms.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<IntMapValue> ent = en.nextElement();
			dout.writeText(ent.getKey());
			try {
				ent.getValue().write(dout);
			}catch(IOException e){
				throw new DataIOException(e);
			}
		}
	}

	@Override
	public Pack read(DataInputX din) {
		super.read(din);
		int sz = din.readShort();
		for (int i = 0; i < sz; i++) {
			String db = din.readText();
			try {
				this.dbms.put(db, (IntMapValue) new IntMapValue().read(din));
			}catch(IOException e){
				throw new DataIOException(e);
			}
		}
		return this;
	}
}
