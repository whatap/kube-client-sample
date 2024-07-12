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

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * @author Paul Kim (sjkim@whatap.io)
 */
public class StringFloatLinkedMap {
	private static final int DEFAULT_CAPACITY = 101;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	private StringFloatLinkedEntry table[];
	private StringFloatLinkedEntry header;
	private int count;
	private int threshold;
	private float loadFactor;

	public StringFloatLinkedMap(int initCapacity, float loadFactor) {
		if (initCapacity < 0)
			throw new RuntimeException("Capacity Error: " + initCapacity);
		if (loadFactor <= 0)
			throw new RuntimeException("Load Count Error: " + loadFactor);
		if (initCapacity == 0)
			initCapacity = 1;
		this.loadFactor = loadFactor;
		this.table = new StringFloatLinkedEntry[initCapacity];
		this.header = new StringFloatLinkedEntry(null, 0, null);
		this.header.link_next = header.link_prev = header;
		threshold = (int) (initCapacity * loadFactor);
	}

	public StringFloatLinkedMap() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	private float NONE = 0;

	public StringFloatLinkedMap setNullValue(float none) {
		this.NONE = none;
		return this;
	}

	public int size() {
		return count;
	}

	public String[] keyArray() {
		String[] _keys = new String[this.size()];
		StringEnumer en = this.keys();
		for (int i = 0; i < _keys.length; i++)
			_keys[i] = en.nextString();
		return _keys;
	}

	public synchronized StringEnumer keys() {
		return new Enumer(TYPE.KEYS);
	}

	public synchronized FloatEnumer values() {
		return new Enumer(TYPE.VALUES);
	}

	public synchronized Enumeration<StringFloatLinkedEntry> entries() {
		return new Enumer(TYPE.ENTRIES);
	}

	public synchronized boolean containsValue(long value) {
		StringFloatLinkedEntry tab[] = table;
		int i = tab.length;
		while (i-- > 0) {
			for (StringFloatLinkedEntry e = tab[i]; e != null; e = e.next) {
				if (e.value == value) {
					return true;
				}
			}
		}
		return false;
	}

	public synchronized boolean containsKey(String key) {
		StringFloatLinkedEntry tab[] = table;
		int index = hash(key) % tab.length;
		for (StringFloatLinkedEntry e = tab[index]; e != null; e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				return true;
			}
		}
		return false;
	}

	public synchronized float get(String key) {
		if (key == null)
			return NONE;

		StringFloatLinkedEntry tab[] = table;
		int index = hash(key) % tab.length;
		for (StringFloatLinkedEntry e = tab[index]; e != null; e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				return e.value;
			}
		}
		return NONE;
	}

	public synchronized float getFirsValue() {
		if (isEmpty())
			return NONE;
		return this.header.link_next.value;
	}

	public synchronized float getLastValue() {
		if (isEmpty())
			return NONE;
		return this.header.link_prev.value;
	}

	private int hash(String key) {
		return key.hashCode() & Integer.MAX_VALUE;
	}

	protected void rehash() {
		int oldCapacity = table.length;
		StringFloatLinkedEntry oldMap[] = table;
		int newCapacity = oldCapacity * 2 + 1;
		StringFloatLinkedEntry newMap[] = new StringFloatLinkedEntry[newCapacity];
		threshold = (int) (newCapacity * loadFactor);
		table = newMap;
		for (int i = oldCapacity; i-- > 0;) {
			StringFloatLinkedEntry old = oldMap[i];
			while (old != null) {
				StringFloatLinkedEntry e = old;
				old = old.next;
				String key = e.key;
				int index = hash(key) % newCapacity;
				e.next = newMap[index];
				newMap[index] = e;
			}
		}
	}

	private int max;

	public StringFloatLinkedMap setMax(int max) {
		this.max = max;
		return this;
	}


	public float put(String key, float value) {
		return _put(key, value, MODE.LAST);
	}

	public float putLast(String key, float value) {
		return _put(key, value, MODE.FORCE_LAST);
	}

	public float putFirst(String key, float value) {
		return _put(key, value, MODE.FORCE_FIRST);
	}

	private synchronized float _put(String key, float value, int m) {
		if (key == null)
			return NONE;

		StringFloatLinkedEntry tab[] = table;
		int index = hash(key) % tab.length;
		for (StringFloatLinkedEntry e = tab[index]; e != null; e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				float old = e.value;
				e.value = value;
				switch (m) {
				case MODE.FORCE_FIRST:
					if (header.link_next != e) {
						unchain(e);
						chain(header, header.link_next, e);
					}
					break;
				case MODE.FORCE_LAST:
					if (header.link_prev != e) {
						unchain(e);
						chain(header.link_prev, header, e);
					}
					break;
				}
				return old;
			}
		}
		if (max > 0) {
			switch (m) {
			case MODE.FORCE_FIRST:
			case MODE.FIRST:
				while (count >= max) {
					// removeLast();
					String k = header.link_prev.key;
					float v = remove(k);
					overflowed(k, v);
				}
				break;
			case MODE.FORCE_LAST:
			case MODE.LAST:
				while (count >= max) {
					// removeFirst();
					String k = header.link_next.key;
					float v = remove(k);
					overflowed(k, v);
				}
				break;
			}
		}
		if (count >= threshold) {
			rehash();
			tab = table;
			index = hash(key) % tab.length;
		}
		StringFloatLinkedEntry e = new StringFloatLinkedEntry(key, value, tab[index]);
		tab[index] = e;
		switch (m) {
		case MODE.FORCE_FIRST:
		case MODE.FIRST:
			chain(header, header.link_next, e);
			break;
		case MODE.FORCE_LAST:
		case MODE.LAST:
			chain(header.link_prev, header, e);
			break;
		}
		count++;
		return NONE;
	}

	public float add(String key, float value) {
		return _add(key, value, MODE.LAST);
	}

	public float addLast(String key, float value) {
		return _add(key, value, MODE.FORCE_LAST);
	}

	public float addFirst(String key, float value) {
		return _add(key, value, MODE.FORCE_FIRST);
	}

	private synchronized float _add(String key, float value, int m) {
		if (key == null)
			return NONE;
		StringFloatLinkedEntry tab[] = table;
		int index = hash(key) % tab.length;
		for (StringFloatLinkedEntry e = tab[index]; e != null; e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				float old = e.value;
				e.value += value;
				switch (m) {
				case MODE.FORCE_FIRST:
					if (header.link_next != e) {
						unchain(e);
						chain(header, header.link_next, e);
					}
					break;
				case MODE.FORCE_LAST:
					if (header.link_prev != e) {
						unchain(e);
						chain(header.link_prev, header, e);
					}
					break;
				}
				return old;
			}
		}
		if (max > 0) {
			switch (m) {
			case MODE.FORCE_FIRST:
			case MODE.FIRST:
				while (count >= max) {
					// removeLast();
					String k = header.link_prev.key;
					float v = remove(k);
					overflowed(k, v);
				}
				break;
			case MODE.FORCE_LAST:
			case MODE.LAST:
				while (count >= max) {
					// removeFirst();
					String k = header.link_next.key;
					float v = remove(k);
					overflowed(k, v);
				}
				break;
			}
		}
		if (count >= threshold) {
			rehash();
			tab = table;
			index = hash(key) % tab.length;
		}
		StringFloatLinkedEntry e = new StringFloatLinkedEntry(key, value, tab[index]);
		tab[index] = e;
		switch (m) {
		case MODE.FORCE_FIRST:
		case MODE.FIRST:
			chain(header, header.link_next, e);
			break;
		case MODE.FORCE_LAST:
		case MODE.LAST:
			chain(header.link_prev, header, e);
			break;
		}
		count++;
		return NONE;
	}

	protected void overflowed(String key, float value) {
	}

	public synchronized float remove(String key) {
		if (key == null)
			return NONE;
		StringFloatLinkedEntry tab[] = table;
		int index = hash(key) % tab.length;
		for (StringFloatLinkedEntry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				if (prev != null) {
					prev.next = e.next;
				} else {
					tab[index] = e.next;
				}
				count--;
				float oldValue = e.value;
				e.value = NONE;
				//
				unchain(e);
				return oldValue;
			}
		}
		return NONE;
	}

	public synchronized float removeFirst() {
		if (isEmpty())
			return NONE;
		return remove(header.link_next.key);
	}

	public synchronized float removeLast() {
		if (isEmpty())
			return NONE;
		return remove(header.link_prev.key);
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean isFull() {
		return max > 0 && max <= count;
	}

	public synchronized void clear() {
		StringFloatLinkedEntry tab[] = table;
		for (int index = tab.length; --index >= 0;)
			tab[index] = null;
		this.header.link_next = header.link_prev = header;
		count = 0;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		Enumeration it = entries();
		buf.append("{");
		for (int i = 0; it.hasMoreElements(); i++) {
			StringFloatLinkedEntry e = (StringFloatLinkedEntry) (it.nextElement());
			if (i > 0)
				buf.append(", ");
			buf.append(e.getKey() + "=" + e.getValue());

		}
		buf.append("}");
		return buf.toString();
	}

	public String toFormatString() {
		DecimalFormat fm = new DecimalFormat("#,##0");
		StringBuffer buf = new StringBuffer();
		Enumeration it = entries();
		buf.append("{\n");
		while (it.hasMoreElements()) {
			StringFloatLinkedEntry e = (StringFloatLinkedEntry) it.nextElement();
			buf.append("\t").append(e.getKey() + "=" + fm.format(e.getValue())).append("\n");
		}
		buf.append("}");
		return buf.toString();
	}

	public static class StringFloatLinkedEntry {
		String key;
		float value;
		StringFloatLinkedEntry next;
		StringFloatLinkedEntry link_next, link_prev;

		protected StringFloatLinkedEntry(String key, float value, StringFloatLinkedEntry next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}

		protected Object clone() {
			return new StringFloatLinkedEntry(key, value,
					(next == null ? null : (StringFloatLinkedEntry) next.clone()));
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
			if (!(o instanceof StringFloatLinkedEntry))
				return false;
			StringFloatLinkedEntry e = (StringFloatLinkedEntry) o;
			return CompareUtil.equals(e.key, key) && CompareUtil.equals(e.value, value);
		}

		public int hashCode() {
			return key.hashCode() ^ (int) ((int) value ^ ((int) value >>> 32));
		}

		public String toString() {
			return key + "=" + value;
		}
	}

	

	private class Enumer implements Enumeration, StringEnumer, FloatEnumer {
		byte type;
		StringFloatLinkedEntry entry = StringFloatLinkedMap.this.header.link_next;

		Enumer(byte type) {
			this.type = type;
		}

		public boolean hasMoreElements() {
			return header != entry && entry != null;
		}

		public Object nextElement() {
			if (hasMoreElements()) {
				StringFloatLinkedEntry e = entry;
				entry = e.link_next;
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
			if (hasMoreElements()) {
				StringFloatLinkedEntry e = entry;
				entry = e.link_next;
				return e.value;
			}
			throw new NoSuchElementException("no more next");
		}

		public String nextString() {
			if (hasMoreElements()) {
				StringFloatLinkedEntry e = entry;
				entry = e.link_next;
				return e.key;
			}
			throw new NoSuchElementException("no more next");
		}
	}

	private void chain(StringFloatLinkedEntry link_prev, StringFloatLinkedEntry link_next, StringFloatLinkedEntry e) {
		e.link_prev = link_prev;
		e.link_next = link_next;
		link_prev.link_next = e;
		link_next.link_prev = e;
	}

	private void unchain(StringFloatLinkedEntry e) {
		e.link_prev.link_next = e.link_next;
		e.link_next.link_prev = e.link_prev;
		e.link_prev = null;
		e.link_next = null;
	}

	public synchronized void sort(Comparator<StringFloatLinkedEntry> c) {
		ArrayList<StringFloatLinkedEntry> list = new ArrayList<StringFloatLinkedEntry>(this.size());
		Enumeration<StringFloatLinkedEntry> en = this.entries();
		while (en.hasMoreElements()) {
			list.add(en.nextElement());
		}
		Collections.sort(list, c);
		this.clear();
		for (int i = 0; i < list.size(); i++) {
			StringFloatLinkedEntry e = list.get(i);
			this.put(e.getKey(), e.getValue());
		}
	}
}
