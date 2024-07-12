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
package whatap.lang.value;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.CompareUtil;
import whatap.util.IntEnumer;
import whatap.util.IntKeyLinkedMap;
import whatap.util.IntKeyLinkedMap.IntKeyLinkedEntry;
import whatap.util.StringKeyLinkedMap.StringKeyLinkedEntry;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;

public class IntMapValue implements Value {

	protected IntKeyLinkedMap<Value> table = new IntKeyLinkedMap<Value>();

	public IntMapValue() {
	}
	public IntMapValue(IntMapValue m) {
		this.table.putAll(m.table);
	}
	public int size() {
		return table.size();
	}

	public boolean isEmpty() {
		return table.isEmpty();
	}

	public boolean containsKey(int key) {
		return table.containsKey(key);
	}

	public int[] keyArray() {
		int[] _keys = new int[this.size()];
		IntEnumer en = this.keys();
		for (int i = 0; i < _keys.length; i++)
			_keys[i] = en.nextInt();
		return _keys;
	}

	public IntEnumer keys() {
		return table.keys();
	}

	public Value get(int key) {
		return (Value) table.get(key);
	}

	public boolean getBoolean(int key) {
		Value v = get(key);
		if (v instanceof BooleanValue) {
			return ((BooleanValue) v).value;
		}
		return false;
	}

	public int getInt(int key) {
		Value v = get(key);
		if (v instanceof Number) {
			return (int) ((Number) v).intValue();
		}
		return 0;
	}

	public long getLong(int key) {
		Value v = get(key);
		if (v instanceof Number) {
			return ((Number) v).longValue();
		}
		return 0;
	}

	public float getFloat(int key) {
		Value v = get(key);
		if (v instanceof Number) {
			return ((Number) v).floatValue();
		}
		return 0;
	}

	public String getText(int key) {
		Value v = get(key);
		if (v instanceof TextValue) {
			return ((TextValue) v).value;
		}
		return null;
	}

	public Value put(int key, Value value) {
		return (Value) table.put(key, value);
	}

	public Value put(int key, String value) {
		return put(key, new TextValue(value));
	}

	public Value put(int key, long value) {
		return put(key, new DecimalValue(value));
	}

	public Value put(int key, int value) {
		return put(key, new DecimalValue(value));
	}

	public Value put(int key, float value) {
		return put(key, new FloatValue(value));
	}

	public Value put(int key, boolean value) {
		return put(key, new BooleanValue(value));
	}

	public Value remove(int key) {
		return (Value) table.remove(key);
	}

	public void clear() {
		table.clear();
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		Enumeration en = table.entries();
		buf.append("{");
		while (en.hasMoreElements()) {
			if (buf.length() > 1) {
				buf.append(",");
			}
			IntKeyLinkedEntry<Value> e = (IntKeyLinkedEntry<Value>) en.nextElement();
			buf.append(e.getKey() + "=" + e.getValue());
		}
		buf.append("}");
		return buf.toString();
	}

	public String toJSONString() {
		StringBuffer buf = new StringBuffer();
		Enumeration en = table.entries();
		buf.append("{");
		while (en.hasMoreElements()) {
			if (buf.length() > 1) {
				buf.append(",");
			}
			StringKeyLinkedEntry<Value> e = (StringKeyLinkedEntry<Value>) en.nextElement();
			buf.append('"').append(e.getKey()).append("\" : ");
			Value value = e.getValue();
			if (value.getValueType() == ValueEnum.TEXT) {
				buf.append('"').append(value).append('"');
			} else if (value.getValueType() == ValueEnum.MAP) {
				buf.append(((IntMapValue) value).toJSONString());
			} else if (value.getValueType() == ValueEnum.LIST) {
				buf.append(((ListValue) value).toJSONString());
			} else {
				buf.append(value.toString());
			}
		}
		buf.append("}");
		return buf.toString();
	}

	public byte getValueType() {
		return ValueEnum.INT_MAP;
	}

	public void write(DataOutputX dout) throws IOException {
		int sz = table.size();
		dout.writeDecimal(sz);
		Enumeration<IntKeyLinkedEntry<Value>> en = table.entries();
		for (int i = 0; i < sz; i++) {
			IntKeyLinkedEntry<Value> e = en.nextElement();
			dout.writeInt(e.getKey());
			dout.writeValue(e.getValue());
		}
	}

	public Value read(DataInputX din) throws IOException {
		int count = (int) din.readDecimal();
		for (int t = 0; t < count; t++) {
			int key = din.readInt();
			Value value = din.readValue();
			this.put(key, value);
		}
		return this;
	}

	public ListValue newList(int name) {
		ListValue list = new ListValue();
		this.put(name, list);
		return list;
	}

	public ListValue getList(int key) {
		return (ListValue) table.get(key);
	}

	public ListValue getListNotNull(int key) {
		ListValue lv = (ListValue) table.get(key);
		return lv == null ? new ListValue() : lv;
	}

	public synchronized ListValue internList(int key) {
		ListValue lv = (ListValue) table.get(key);
		if (lv == null) {
			lv = new ListValue();
			table.put(key, lv);
		}
		return lv;
	}

	public Object toJavaObject() {
		return this.table;
	}

	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		IntMapValue other = (IntMapValue) obj;
		if (this.size() != other.size())
			return false;
		Enumeration<IntKeyLinkedEntry<Value>> en = this.table.entries();
		while (en.hasMoreElements()) {
			IntKeyLinkedEntry<Value> ent = en.nextElement();
			Value otherValue = other.get(ent.getKey());
			if (ent.getValue().equals(otherValue) == false) {
				return false;
			}
		}

		return true;
	}

	public void putAll(Map<Integer, Value> m) {
		Iterator<Map.Entry<Integer, Value>> itr = m.entrySet().iterator();
		while (itr.hasNext()) {
			Map.Entry<Integer, Value> e = itr.next();
			this.table.put(e.getKey(), e.getValue());
		}
	}

	public void putAll(IntMapValue m) {
		IntEnumer strEnumer = m.keys();
		while (strEnumer.hasMoreElements()) {
			int key = strEnumer.nextInt();
			this.table.put(key, m.get(key));
		}
	}

	public int getInt(int key, int i) {
		Value v = get(key);
		if (v instanceof Number) {
			return ((Number) v).intValue();
		}
		return i;
	}

	public long getLong(int key, long def) {
		Value v = get(key);
		if (v instanceof Number) {
			return ((Number) v).longValue();
		}
		return def;
	}

	public float getFloat(int key, float def) {
		Value v = get(key);
		if (v instanceof Number) {
			return ((Number) v).floatValue();
		}
		return def;
	}

	public String getText(int key, String def) {
		Value v = get(key);
		if (v instanceof TextValue) {
			return ((TextValue) v).value;
		}
		return def;
	}

	public boolean getBoolean(int key, boolean def) {
		Value v = get(key);
		if (v instanceof BooleanValue) {
			return ((BooleanValue) v).value;
		}
		return def;
	}


	public static boolean isEmpty(IntMapValue r) {
		return r == null || r.size() == 0;
	}
	public Value copy() {
		return new IntMapValue(this);
	}

	public int compareTo(Object o) {
		return CompareUtil.compareTo(this.hashCode(), o.hashCode());
	}

}
