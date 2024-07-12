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
package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.value.BooleanValue;
import whatap.lang.value.DecimalValue;
import whatap.lang.value.ListValue;
import whatap.lang.value.MapValue;
import whatap.lang.value.TextValue;
import whatap.lang.value.Value;
import whatap.net.ParamDef;
import whatap.util.StringEnumer;
import whatap.util.StringKeyLinkedMap;
import whatap.util.StringKeyLinkedMap.StringKeyLinkedEntry;

import java.util.Enumeration;

public class ParamPack extends AbstractPack {
	public int id;
	protected StringKeyLinkedMap<Value> table = new StringKeyLinkedMap<Value>();
	public long request;
	public long response;

	public int size() {
		return table.size();
	}

	public boolean isEmpty() {
		return table.isEmpty();
	}

	public boolean containsKey(String key) {
		return table.containsKey(key);
	}

	public StringEnumer keys() {
		return table.keys();
	}

	public String[] keyArray() {
		return table.keyArray();
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
			return (float) ((Number) v).floatValue();
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

	public Value remove(String key) {
		return (Value) table.remove(key);
	}

	public void clear() {
		table.clear();
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		buf.append("ParamPack ");
		buf.append(super.toString());
		buf.append(" id=").append(id + " (" + ParamDef.values.get(id)).append(") ");
		buf.append(" request=").append(request + " ");
		buf.append(" response=").append(response + " ");
		buf.append(table);
		return buf.toString();
	}

	public short getPackType() {
		return PackEnum.PARAMETER;
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeInt(id);
		dout.writeDecimal(request);
		dout.writeDecimal(response);
		dout.writeDecimal(table.size());
		Enumeration<StringKeyLinkedEntry<Value>> en = table.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<Value> e = en.nextElement();
			dout.writeText(e.getKey());
			dout.writeValue((Value) e.getValue());
		}
	}

	public Pack read(DataInputX din) {
		super.read(din);
		this.id = din.readInt();
		this.request = din.readDecimal();
		this.response = din.readDecimal();
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

	public int hashCode() {
		return System.identityHashCode(this);
	}

	public boolean equals(Object obj) {
		return (this == obj);
	}

	public MapValue toMapValue() {
		MapValue map = new MapValue();
		Enumeration<StringKeyLinkedEntry<Value>> en = table.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<Value> e = en.nextElement();
			map.put(e.getKey(), (Value) e.getValue());
		}
		return map;
	}

	public ParamPack setMapValue(MapValue mapValue) {
		if (mapValue == null)
			return this;
		StringEnumer keys = mapValue.keys();
		while (keys.hasMoreElements()) {
			String key = keys.nextString();
			Value value = mapValue.get(key);
			this.table.put(key, value);
		}
		return this;
	}

	public ParamPack reponse() {
		if (this.request == 0)
			return this;
		this.response = this.request;
		this.request = 0;
		return this;
	}

	public String toFormatString() {
		StringBuilder buf = new StringBuilder();
		buf.append(super.toFormatString());
		buf.append(", id=").append(id + " (" + ParamDef.values.get(id)).append(") ");
		buf.append(", request=").append(request);
		buf.append(", response=").append(response).append("\n");
		StringEnumer en = this.table.keys();
		while (en.hasMoreElements()) {
			String key = en.nextString();
			buf.append("\t").append(key).append("=").append(table.get(key)).append("\n");
		}
		return buf.toString();
	}
}
