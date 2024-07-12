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
 * 
 *  The initial idea for this class is from "org.apache.commons.lang.IntHashMap"; 
 *  http://commons.apache.org/commons-lang-2.6-src.zip
 *
 */
package whatap.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * @author Paul Kim (sjkim@whatap.io)
 */
public class StringFloatMap {
	private static final int DEFAULT_CAPACITY = 101;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	private StringFloatEntry table[];
	private int count;
	private int threshold;
	private float loadFactor;

	public StringFloatMap(int initCapacity, float loadFactor) {
		if (initCapacity < 0)
			throw new RuntimeException("Capacity Error: " + initCapacity);
		if (loadFactor <= 0)
			throw new RuntimeException("Load Count Error: " + loadFactor);
		if (initCapacity == 0)
			initCapacity = 1;
		this.loadFactor = loadFactor;
		table = new StringFloatEntry[initCapacity];
		threshold = (int) (initCapacity * loadFactor);
	}

	private float NONE = 0;

	public StringFloatMap setNullValue(float none) {
		this.NONE = none;
		return this;
	}

	public StringFloatMap() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	public int size() {
		return count;
	}

	public synchronized StringEnumer keys() {
		return new Enumer(TYPE.KEYS);
	}
	public String[] keyArray() {
		String[] _keys = new String[this.size()];
		StringEnumer en = this.keys();
		for (int i = 0; i < _keys.length; i++)
			_keys[i] = en.nextString();
		return _keys;
	}
	public synchronized FloatEnumer values() {
		return new Enumer(TYPE.VALUES);
	}

	public synchronized Enumeration<StringFloatEntry> entries() {
		return new Enumer(TYPE.ENTRIES);
	}

	public synchronized boolean containsValue(float value) {
		StringFloatEntry tab[] = table;
		int i = tab.length;
		while (i-- > 0) {
			for (StringFloatEntry e = tab[i]; e != null; e = e.next) {
				if (e.value == value) {
					return true;
				}
			}
		}
		return false;
	}

	public synchronized boolean containsKey(String key) {
		if (key == null)
			return false;
		StringFloatEntry tab[] = table;
		int index = (key.hashCode() & Integer.MAX_VALUE) % tab.length;
		for (StringFloatEntry e = tab[index]; e != null; e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				return true;
			}
		}
		return false;
	}

	public synchronized float get(String key) {
		if (key == null)
			return NONE;
		StringFloatEntry tab[] = table;
		int index = (key.hashCode() & Integer.MAX_VALUE) % tab.length;
		for (StringFloatEntry e = tab[index]; e != null; e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				return e.value;
			}
		}
		return NONE;
	}

	protected void rehash() {
		int oldCapacity = table.length;
		StringFloatEntry oldMap[] = table;
		int newCapacity = oldCapacity * 2 + 1;
		StringFloatEntry newMap[] = new StringFloatEntry[newCapacity];
		threshold = (int) (newCapacity * loadFactor);
		table = newMap;
		for (int i = oldCapacity; i-- > 0;) {
			StringFloatEntry old = oldMap[i];
			while (old != null) {
				StringFloatEntry e = old;
				old = old.next;
				int index = (e.key.hashCode() & Integer.MAX_VALUE) % newCapacity;
				e.next = newMap[index];
				newMap[index] = e;
			}
		}
	}

	public synchronized float put(String key, float value) {
		if (key == null)
			return NONE;
		StringFloatEntry tab[] = table;
		int index = (key.hashCode() & Integer.MAX_VALUE) % tab.length;
		for (StringFloatEntry e = tab[index]; e != null; e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				float old = e.value;
				e.value = value;
				return old;
			}
		}
		if (count >= threshold) {
			rehash();
			tab = table;
			index = (key.hashCode() & Integer.MAX_VALUE) % tab.length;
		}
		StringFloatEntry e = new StringFloatEntry(key, value, tab[index]);
		tab[index] = e;
		count++;
		return NONE;
	}

	public synchronized float add(String key, float value) {
		if (key == null)
			return NONE;
		StringFloatEntry tab[] = table;
		int index = (key.hashCode() & Integer.MAX_VALUE) % tab.length;
		for (StringFloatEntry e = tab[index]; e != null; e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				float old = e.value;
				e.value += value;
				return old;
			}
		}
		if (count >= threshold) {
			rehash();
			tab = table;
			index = (key.hashCode() & Integer.MAX_VALUE) % tab.length;
		}
		StringFloatEntry e = new StringFloatEntry(key, value, tab[index]);
		tab[index] = e;
		count++;
		return NONE;
	}

	public synchronized float intern(String key, float def) {
		if (key == null)
			return NONE;
		StringFloatEntry tab[] = table;
		int index = (key.hashCode() & Integer.MAX_VALUE) % tab.length;
		for (StringFloatEntry e = tab[index]; e != null; e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				return e.value;
			}
		}
		if (count >= threshold) {
			rehash();
			tab = table;
			index = (key.hashCode() & Integer.MAX_VALUE) % tab.length;
		}
		StringFloatEntry e = new StringFloatEntry(key, def, tab[index]);
		tab[index] = e;
		count++;
		return def;
	}

	public synchronized float remove(String key) {
		if (key == null)
			return NONE;
		StringFloatEntry tab[] = table;
		int index = (key.hashCode() & Integer.MAX_VALUE) % tab.length;
		for (StringFloatEntry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				if (prev != null) {
					prev.next = e.next;
				} else {
					tab[index] = e.next;
				}
				count--;
				float oldValue = e.value;
				e.value = NONE;
				return oldValue;
			}
		}
		return NONE;
	}

	public synchronized void clear() {
		StringFloatEntry tab[] = table;
		for (int index = tab.length; --index >= 0;)
			tab[index] = null;
		count = 0;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		Enumeration it = entries();
		buf.append("{");
		for (int i = 0; it.hasMoreElements(); i++) {
			StringFloatEntry e = (StringFloatEntry) (it.nextElement());
			if (i > 0)
				buf.append(", ");
			buf.append(e.getKey() + "=" + e.getValue());
		}
		buf.append("}");
		return buf.toString();
	}

	public String toFormatString() {
		StringBuffer buf = new StringBuffer();
		Enumeration it = entries();
		buf.append("{\n");
		while (it.hasMoreElements()) {
			StringFloatEntry e = (StringFloatEntry) it.nextElement();
			buf.append("\t").append(e.getKey() + "=" + e.getValue()).append("\n");
		}
		buf.append("}");
		return buf.toString();
	}

	public static class StringFloatEntry {
		String key;
		float value;
		StringFloatEntry next;

		protected StringFloatEntry(String key, float value, StringFloatEntry next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}

		protected Object clone() {
			return new StringFloatEntry(key, value, (next == null ? null : (StringFloatEntry) next.clone()));
		}

		public String getKey() {
			return key;
		}

		public float getValue() {
			return value;
		}

		public float setValue(float value) {
			float oldValue = this.value;
			this.value = value;
			return oldValue;
		}

		public boolean equals(Object o) {
			if (!(o instanceof StringFloatEntry))
				return false;
			StringFloatEntry e = (StringFloatEntry) o;
			return CompareUtil.equals(e.key, key) && CompareUtil.equals(e.value, value);
		}

		public int hashCode() {
			return key.hashCode() ^ (int) value;
		}

		public String toString() {
			return key + "=" + value;
		}
	}

	private class Enumer implements Enumeration, StringEnumer, FloatEnumer {
		StringFloatEntry[] table = StringFloatMap.this.table;
		int index = table.length;
		StringFloatEntry entry = null;
		StringFloatEntry lastReturned = null;
		byte type;

		Enumer(byte type) {
			this.type = type;
		}

		public boolean hasMoreElements() {
			while (entry == null && index > 0)
				entry = table[--index];
			return entry != null;
		}

		public Object nextElement() {
			while (entry == null && index > 0)
				entry = table[--index];
			if (entry != null) {
				StringFloatEntry e = lastReturned = entry;
				entry = e.next;
				switch (type) {
				case TYPE.KEYS:
					return e.key;
				case TYPE.VALUES:
					return e.value;
				default:
					return e;
				}
			}
			throw new NoSuchElementException("no more next");
		}

		public float nextFloat() {
			while (entry == null && index > 0)
				entry = table[--index];
			if (entry != null) {
				StringFloatEntry e = lastReturned = entry;
				entry = e.next;
				switch (type) {
				case TYPE.VALUES:
					return e.value;
				default:
					return NONE;
				}
			}
			throw new NoSuchElementException("no more next");
		}

		public String nextString() {
			while (entry == null && index > 0)
				entry = table[--index];
			if (entry != null) {
				StringFloatEntry e = lastReturned = entry;
				entry = e.next;
				switch (type) {
				case TYPE.KEYS:
					return e.key;
				default:
					return null;
				}
			}
			throw new NoSuchElementException("no more next");
		}
	}

	public synchronized void sort(Comparator<StringFloatEntry> c) {
		ArrayList<StringFloatEntry> list = new ArrayList<StringFloatEntry>(this.size());
		Enumeration<StringFloatEntry> en = this.entries();
		while (en.hasMoreElements()) {
			list.add(en.nextElement());
		}
		Collections.sort(list, c);
		this.clear();
		for (int i = 0; i < list.size(); i++) {
			StringFloatEntry e = list.get(i);
			this.put(e.getKey(), e.getValue());
		}
	}
}
