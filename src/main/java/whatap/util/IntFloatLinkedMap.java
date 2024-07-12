/*
 * 
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

import whatap.io.DataInputX;
import whatap.io.DataOutputX;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * @author Paul Kim (sjkim@whatap.io)
 */
public class IntFloatLinkedMap {
	private static final int DEFAULT_CAPACITY = 101;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	private IntFloatLinkedEntry table[];
	private IntFloatLinkedEntry header;
	private int count;
	private int threshold;
	private float loadFactor;
	private int NONE = 0;

	public IntFloatLinkedMap setNullValue(int none) {
		this.NONE = none;
		return this;
	}

	public IntFloatLinkedMap(int initCapacity, float loadFactor) {
		if (initCapacity < 0)
			throw new RuntimeException("Capacity Error: " + initCapacity);
		if (loadFactor <= 0)
			throw new RuntimeException("Load Count Error: " + loadFactor);
		if (initCapacity == 0)
			initCapacity = 1;
		this.loadFactor = loadFactor;
		this.table = new IntFloatLinkedEntry[initCapacity];
		this.header = new IntFloatLinkedEntry(0, 0, null);
		this.header.link_next = header.link_prev = header;
		threshold = (int) (initCapacity * loadFactor);
	}

	public IntFloatLinkedMap() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	public int size() {
		return count;
	}

	public int[] keyArray() {
		int[] _keys = new int[this.size()];
		IntEnumer en = this.keys();
		for (int i = 0; i < _keys.length; i++)
			_keys[i] = en.nextInt();
		return _keys;
	}

	public synchronized IntEnumer keys() {
		return new Enumer(TYPE.KEYS);
	}

	public synchronized FloatEnumer values() {
		return new Enumer(TYPE.VALUES);
	}

	public synchronized Enumeration<IntFloatLinkedEntry> entries() {
		return new Enumer<IntFloatLinkedEntry>(TYPE.ENTRIES);
	}

	public synchronized boolean containsValue(float value) {
		IntFloatLinkedEntry tab[] = table;
		for (int i = tab.length; i-- > 0;) {
			for (IntFloatLinkedEntry e = tab[i]; e != null; e = e.hash_next) {
				if (CompareUtil.equals(e.value, value)) {
					return true;
				}
			}
		}
		return false;
	}

	public synchronized boolean containsKey(int key) {
		IntFloatLinkedEntry tab[] = table;
		int index = hash(key) % tab.length;
		IntFloatLinkedEntry e = tab[index];
		while (e != null) {
			if (CompareUtil.equals(e.key, key)) {
				return true;
			}
			e = e.hash_next;
		}
		return false;
	}

	public synchronized float get(int key) {
		IntFloatLinkedEntry tab[] = table;
		int index = hash(key) % tab.length;
		for (IntFloatLinkedEntry e = tab[index]; e != null; e = e.hash_next) {
			if (CompareUtil.equals(e.key, key)) {
				return e.value;
			}
		}
		return NONE;
	}

	public synchronized int getFirstKey() {
		return this.header.link_next.key;
	}

	public synchronized int getLastKey() {
		return this.header.link_prev.key;
	}

	public synchronized float getFirstValue() {
		return this.header.link_next.value;
	}

	public synchronized float getLastValue() {
		return this.header.link_prev.value;
	}

	private int hash(int key) {
		return key & Integer.MAX_VALUE;
	}

	protected void rehash() {
		int oldCapacity = table.length;
		IntFloatLinkedEntry oldMap[] = table;
		int newCapacity = oldCapacity * 2 + 1;
		IntFloatLinkedEntry newMap[] = new IntFloatLinkedEntry[newCapacity];
		threshold = (int) (newCapacity * loadFactor);
		table = newMap;
		for (int i = oldCapacity; i-- > 0;) {
			IntFloatLinkedEntry old = oldMap[i];
			while (old != null) {
				IntFloatLinkedEntry e = old;
				old = old.hash_next;
				int key = e.key;
				int index = hash(key) % newCapacity;
				e.hash_next = newMap[index];
				newMap[index] = e;
			}
		}
	}

	private int max;

	public IntFloatLinkedMap setMax(int max) {
		this.max = max;
		return this;
	}

	public float put(int key, float value) {
		return _put(key, value, MODE.LAST);
	}

	public float putLast(int key, float value) {
		return _put(key, value, MODE.FORCE_LAST);
	}

	public float putFirst(int key, float value) {
		return _put(key, value, MODE.FORCE_FIRST);
	}

	private synchronized float _put(int key, float value, int m) {
		IntFloatLinkedEntry tab[] = table;
		int index = hash(key) % tab.length;
		for (IntFloatLinkedEntry e = tab[index]; e != null; e = e.hash_next) {
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
					int k = header.link_prev.key;
					float v = remove(k);
					overflowed(k, v);
				}
				break;
			case MODE.FORCE_LAST:
			case MODE.LAST:
				while (count >= max) {
					// removeFirst();
					int k = header.link_next.key;
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
		IntFloatLinkedEntry e = new IntFloatLinkedEntry(key, value, tab[index]);
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

	public float add(int key, float value) {
		return _add(key, value, MODE.LAST);
	}

	public float addLast(int key, float value) {
		return _add(key, value, MODE.FORCE_LAST);
	}

	public float addFirst(int key, float value) {
		return _add(key, value, MODE.FORCE_FIRST);
	}

	private synchronized float _add(int key, float value, int m) {
		IntFloatLinkedEntry tab[] = table;
		int index = hash(key) % tab.length;
		for (IntFloatLinkedEntry e = tab[index]; e != null; e = e.hash_next) {
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
					removeLast();
				}
				break;
			case MODE.FORCE_LAST:
			case MODE.LAST:
				while (count >= max) {
					removeFirst();
				}
				break;
			}
		}
		if (count >= threshold) {
			rehash();
			tab = table;
			index = hash(key) % tab.length;
		}
		IntFloatLinkedEntry e = new IntFloatLinkedEntry(key, value, tab[index]);
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

	protected void overflowed(int key, float value) {
	}

	public synchronized float remove(int key) {
		IntFloatLinkedEntry tab[] = table;
		int index = hash(key) % tab.length;
		IntFloatLinkedEntry e = tab[index];
		IntFloatLinkedEntry prev = null;
		while (e != null) {
			if (CompareUtil.equals(e.key, key)) {
				if (prev != null) {
					prev.hash_next = e.hash_next;
				} else {
					tab[index] = e.hash_next;
				}
				count--;
				float oldValue = e.value;
				e.value = NONE;
				//
				unchain(e);
				return oldValue;
			}
			prev = e;
			e = e.hash_next;
		}
		return NONE;
	}

	public synchronized float removeFirst() {
		if (isEmpty())
			return 0;
		return remove(header.link_next.key);
	}

	public synchronized float removeLast() {
		if (isEmpty())
			return 0;
		return remove(header.link_prev.key);
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public boolean isFull() {
		return max > 0 && max <= count;
	}

	public synchronized void clear() {
		IntFloatLinkedEntry tab[] = table;
		for (int index = tab.length; --index >= 0;)
			tab[index] = null;
		this.header.link_next = header;
		this.header.link_prev = header;
		count = 0;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		Enumeration it = entries();
		buf.append("{");
		for (int i = 0; it.hasMoreElements(); i++) {
			IntFloatLinkedEntry e = (IntFloatLinkedEntry) (it.nextElement());
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
			IntFloatLinkedEntry e = (IntFloatLinkedEntry) it.nextElement();
			buf.append("\t").append(e.getKey() + "=" + e.getValue()).append("\n");
		}
		buf.append("}");
		return buf.toString();
	}

	private class Enumer<V> implements Enumeration, FloatEnumer, IntEnumer {
		byte type;
		IntFloatLinkedEntry entry = IntFloatLinkedMap.this.header.link_next;

		Enumer(byte type) {
			this.type = type;
		}

		public boolean hasMoreElements() {
			return entry != null && header != entry;
		}

		public Object nextElement() {
			if (hasMoreElements()) {
				IntFloatLinkedEntry e = entry;
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
				IntFloatLinkedEntry e = entry;
				entry = e.link_next;
				switch (type) {
				case TYPE.VALUES:
					return e.value;
				}
			}
			throw new NoSuchElementException("no more next");
		}

		public int nextInt() {
			if (hasMoreElements()) {
				IntFloatLinkedEntry e = entry;
				entry = e.link_next;
				switch (type) {
				case TYPE.KEYS:
					return e.key;
				}
			}
			throw new NoSuchElementException("no more next");
		}
	}

	private void chain(IntFloatLinkedEntry link_prev, IntFloatLinkedEntry link_next, IntFloatLinkedEntry e) {
		e.link_prev = link_prev;
		e.link_next = link_next;
		link_prev.link_next = e;
		link_next.link_prev = e;
	}

	private void unchain(IntFloatLinkedEntry e) {
		e.link_prev.link_next = e.link_next;
		e.link_next.link_prev = e.link_prev;
		e.link_prev = null;
		e.link_next = null;
	}

	private static void print(Object e) {
		System.out.println(e);
	}

	public static class IntFloatLinkedEntry {
		int key;
		float value;
		IntFloatLinkedEntry hash_next;
		IntFloatLinkedEntry link_next, link_prev;

		protected IntFloatLinkedEntry(int key, float value, IntFloatLinkedEntry next) {
			this.key = key;
			this.value = value;
			this.hash_next = next;
		}

		protected Object clone() {
			return new IntFloatLinkedEntry(key, value, (hash_next == null ? null : (IntFloatLinkedEntry) hash_next.clone()));
		}

		public int getKey() {
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
			if (!(o instanceof IntFloatLinkedEntry))
				return false;
			IntFloatLinkedEntry e = (IntFloatLinkedEntry) o;
			return CompareUtil.equals(e.key, key) && CompareUtil.equals(e.value, value);
		}

		public int hashCode() {
			int x = Float.floatToIntBits(value);
			return key ^ x;
		}

		public String toString() {
			return key + "=" + value;
		}
	}

	public void toBytes(DataOutputX dout) {
		dout.writeDecimal(this.size());
		Enumeration<IntFloatLinkedEntry> en = this.entries();
		while (en.hasMoreElements()) {
			IntFloatLinkedEntry e = en.nextElement();
			dout.writeDecimal(e.getKey());
			dout.writeFloat(e.getValue());
		}
	}

	public IntFloatLinkedMap toObject(DataInputX in) {
		int cnt = (int) in.readDecimal();
		for (int i = 0; i < cnt; i++) {
			int key = (int) in.readDecimal();
			float value = in.readFloat();
			this.put(key, value);
		}
		return this;
	}

	public synchronized void sort(Comparator<IntFloatLinkedEntry> c) {
		ArrayList<IntFloatLinkedEntry> list = new ArrayList<IntFloatLinkedEntry>(this.size());
		Enumeration<IntFloatLinkedEntry> en = this.entries();
		while (en.hasMoreElements()) {
			list.add(en.nextElement());
		}
		Collections.sort(list, c);
		this.clear();
		for (int i = 0; i < list.size(); i++) {
			IntFloatLinkedEntry e = list.get(i);
			this.put(e.getKey(), e.getValue());
		}
	}
}
