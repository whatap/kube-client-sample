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
import whatap.org.json.JSONObject;
import whatap.util.CompareUtil;
import whatap.util.StringEnumer;
import whatap.util.StringKeyLinkedMap;
import whatap.util.StringKeyLinkedMap.StringKeyLinkedEntry;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class MapValue implements Value {

	protected StringKeyLinkedMap<Value> table = new StringKeyLinkedMap<Value>();

	public MapValue() {
	}
	public MapValue(MapValue m) {
		this.table.putAll(m.table);
	}
	public int size() {
		return table.size();
	}

	public boolean isEmpty() {
		return table.isEmpty();
	}

	public boolean containsKey(String key) {
		return table.containsKey(key);
	}

	public String[] keyArray() {
		String[] _keys = new String[this.size()];
		StringEnumer en = this.keys();
		for (int i = 0; i < _keys.length; i++)
			_keys[i] = en.nextString();
		return _keys;
	}

	public StringEnumer keys() {
		return table.keys();
	}

	public Value get(String key) {
		return (Value) table.get(key);
	}

	public boolean getBoolean(String key) {
		Value v = get(key);
		if (v instanceof BooleanValue) {
			return ((BooleanValue) v).value;
		}
		return false;
	}

	public int getInt(String key) {
		Value v = get(key);
		if (v instanceof Number) {
			return (int) ((Number) v).intValue();
		}
		return 0;
	}

	public long getLong(String key) {
		Value v = get(key);
		if (v instanceof Number) {
			return ((Number) v).longValue();
		}
		return 0;
	}

	public float getFloat(String key) {
		Value v = get(key);
		if (v instanceof Number) {
			return ((Number) v).floatValue();
		}
		return 0;
	}

	public String getText(String key) {
		Value v = get(key);
		if (v instanceof TextValue) {
			return ((TextValue) v).value;
		}
		return null;
	}

	public Value put(String key, Value value) {
		return (Value) table.put(key, value);
	}

	public Value put(String key, String value) {
		return put(key, new TextValue(value));
	}

	public Value put(String key, long value) {
		return put(key, new DecimalValue(value));
	}

	public Value put(String key, int value) {
		return put(key, new DecimalValue(value));
	}

	public Value put(String key, float value) {
		return put(key, new FloatValue(value));
	}

	public Value put(String key, double value) {
		return put(key, new DoubleValue(value));
	}

	public Value put(String key, boolean value) {
		return put(key, new BooleanValue(value));
	}

	public Value remove(String key) {
		return (Value) table.remove(key);
	}

	public void clear() {
		table.clear();
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		Enumeration<StringKeyLinkedEntry<Value>> en = table.entries();
		buf.append("{");
		while (en.hasMoreElements()) {
			if (buf.length() > 1) {
				buf.append(",");
			}
			StringKeyLinkedEntry<Value> e = (StringKeyLinkedEntry<Value>) en.nextElement();
			buf.append(e.getKey() + "=" + e.getValue());
		}
		buf.append("}");
		return buf.toString();
	}

	public String toJSONString() {
		StringBuffer buf = new StringBuffer();
		Enumeration<StringKeyLinkedEntry<Value>> en = table.entries();
		buf.append("{");
		while (en.hasMoreElements()) {
			if (buf.length() > 1) {
				buf.append(",");
			}
			StringKeyLinkedEntry<Value> e = (StringKeyLinkedEntry<Value>) en.nextElement();
			buf.append('"').append(e.getKey()).append("\" : ");
			Value value = e.getValue();
			if (value.getValueType() == ValueEnum.TEXT) {
				buf.append(JSONObject.quote(value.toString()));
			} else if (value.getValueType() == ValueEnum.MAP) {
				buf.append(((MapValue) value).toJSONString());
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
		return ValueEnum.MAP;
	}

	public void write(DataOutputX dout) throws IOException {
		int sz = table.size();
		dout.writeDecimal(sz);
		Enumeration<StringKeyLinkedEntry<Value>> en = table.entries();
		for (int i = 0; i < sz; i++) {
			StringKeyLinkedEntry<Value> e = en.nextElement();
			dout.writeText(e.getKey());
			dout.writeValue(e.getValue());
		}
	}

	public Value read(DataInputX din) throws IOException {
		int count = (int) din.readDecimal();
		for (int t = 0; t < count; t++) {
			String key = din.readText();
			Value value = din.readValue();
			this.put(key, value);
		}
		return this;
	}

	public ListValue newList(String name) {
		ListValue list = new ListValue();
		this.put(name, list);
		return list;
	}

	public ListValue getList(String key) {
		return (ListValue) table.get(key);
	}

	public ListValue getListNotNull(String key) {
		ListValue lv = (ListValue) table.get(key);
		return lv == null ? new ListValue() : lv;
	}

	public synchronized ListValue internList(String key) {
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
		MapValue other = (MapValue) obj;
		if (this.size() != other.size())
			return false;
		Enumeration<StringKeyLinkedEntry<Value>> en = this.table.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<Value> ent = en.nextElement();
			Value otherValue = other.get(ent.getKey());
			if (ent.getValue().equals(otherValue) == false) {
				return false;
			}
		}

		return true;
	}

	public void putAll(Map<String, Value> m) {
		Iterator<Entry<String, Value>> itr = m.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<String, Value> e = itr.next();
			this.table.put(e.getKey(), e.getValue());
		}
	}

	public void putAll(MapValue m) {
		StringEnumer strEnumer = m.keys();
		while (strEnumer.hasMoreElements()) {
			String key = strEnumer.nextString();
			this.table.put(key, m.get(key));
		}
	}

	public int getInt(String key, int i) {
		Value v = get(key);
		if (v instanceof Number) {
			return ((Number) v).intValue();
		}
		return i;
	}

	public long getLong(String key, long def) {
		Value v = get(key);
		if (v instanceof Number) {
			return ((Number) v).longValue();
		}
		return def;
	}

	public float getFloat(String key, float def) {
		Value v = get(key);
		if (v instanceof Number) {
			return ((Number) v).floatValue();
		}
		return def;
	}
	public double getDouble(String key) {
		Value v = get(key);
		if (v instanceof Number) {
			return ((Number) v).doubleValue();
		}
		return 0;
	}
	public String getText(String key, String def) {
		Value v = get(key);
		if (v instanceof TextValue) {
			return ((TextValue) v).value;
		}
		return def;
	}

	public boolean getBoolean(String key, boolean def) {
		Value v = get(key);
		if (v instanceof BooleanValue) {
			return ((BooleanValue) v).value;
		}
		return def;
	}

	public static boolean isEmpty(MapValue r) {
		return r == null || r.size() == 0;
	}

	public void putStringMapAll(Map<String, String> out) {
		if(out==null)
			return;
		Iterator<Entry<String, String>> it =out.entrySet().iterator();
		while(it.hasNext()) {
			Entry<String, String> e = it.next();
			this.put(e.getKey(), e.getValue());
		}		
	}

	public Enumeration<StringKeyLinkedEntry<Value>> entries() {
		return this.table.entries();
	}

	public MapValue getMap(String key) {
		return (MapValue) table.get(key);
	}
	public int compareTo(Object o) {
		return CompareUtil.compareTo(this.hashCode(), o.hashCode());
	}
	
	public Value copy() {		
		return new MapValue(this);
	}
}
