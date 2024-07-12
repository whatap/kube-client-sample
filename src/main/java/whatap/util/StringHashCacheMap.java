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
import java.util.Enumeration;
import java.util.NoSuchElementException;
public class StringHashCacheMap {
	private static final int DEFAULT_CAPACITY = 101;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	private StringHashCacheEntry table[];
	private StringHashCacheEntry header;
	private int count;
	private int threshold;
	private float loadFactor;
	public StringHashCacheMap(int initCapacity, float loadFactor) {
		if (initCapacity < 0)
			throw new RuntimeException("Capacity Error: " + initCapacity);
		if (loadFactor <= 0)
			throw new RuntimeException("Load Count Error: " + loadFactor);
		if (initCapacity == 0)
			initCapacity = 1;
		this.loadFactor = loadFactor;
		this.table = new StringHashCacheEntry[initCapacity];
		this.header = new StringHashCacheEntry(null, 0, null);
		this.header.link_next = header.link_prev = header;
		threshold = (int) (initCapacity * loadFactor);
	}
	public StringHashCacheMap() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
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
	public synchronized IntEnumer values() {
		return new Enumer(TYPE.VALUES);
	}
	public synchronized Enumeration<StringHashCacheEntry> myEntries() {
		return new Enumer(TYPE.ENTRIES);
	}
	public synchronized boolean containsValue(int value) {
		StringHashCacheEntry tab[] = table;
		int i = tab.length;
		while (i-- > 0) {
			for (StringHashCacheEntry e = tab[i]; e != null; e = e.next) {
				if (e.value == value) {
					return true;
				}
			}
		}
		return false;
	}
	public synchronized boolean containsKey(String key) {
		StringHashCacheEntry tab[] = table;
		int index = hash(key) % tab.length;
		for (StringHashCacheEntry e = tab[index]; e != null; e = e.next) {
			if (e.key == key) {
				return true;
			}
		}
		return false;
	}
	public synchronized int get(String key) {
		if (key == null)
			return 0;
		StringHashCacheEntry tab[] = table;
		int index = hash(key) % tab.length;
		for (StringHashCacheEntry e = tab[index]; e != null; e = e.next) {
			if (e.key == key) {
				return e.value;
			}
		}
		return 0;
	}
	public synchronized int getFirsValue() {
		if (isEmpty())
			return 0;
		return this.header.link_next.value;
	}
	public synchronized int getLastValue() {
		if (isEmpty())
			return 0;
		return this.header.link_prev.value;
	}
	private int hash(String key) {
		return key.hashCode() & Integer.MAX_VALUE;
	}
	protected void rehash() {
		int oldCapacity = table.length;
		StringHashCacheEntry oldMap[] = table;
		int newCapacity = oldCapacity * 2 + 1;
		StringHashCacheEntry newMap[] = new StringHashCacheEntry[newCapacity];
		threshold = (int) (newCapacity * loadFactor);
		table = newMap;
		for (int i = oldCapacity; i-- > 0;) {
			StringHashCacheEntry old = oldMap[i];
			while (old != null) {
				StringHashCacheEntry e = old;
				old = old.next;
				String key = e.key;
				int index = hash(key) % newCapacity;
				e.next = newMap[index];
				newMap[index] = e;
			}
		}
	}
	private int max;
	public StringHashCacheMap setMax(int max) {
		this.max = max;
		return this;
	}

	public int put(String key, int value) {
		return _put(key, value, MODE.LAST);
	}
	public int putLast(String key, int value) {
		return _put(key, value, MODE.FORCE_LAST);
	}
	public int putFirst(String key, int value) {
		return _put(key, value, MODE.FORCE_FIRST);
	}
	private synchronized int _put(String key, int value, int m) {
		if (key == null)
			return 0;
		StringHashCacheEntry tab[] = table;
		int index = hash(key) % tab.length;
		for (StringHashCacheEntry e = tab[index]; e != null; e = e.next) {
			if (e.key == key) {
				int old = e.value;
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
					int v = remove(k);
					overflowed(k,v);
				}
				break;
			case MODE.FORCE_LAST:
			case MODE.LAST:
				while (count >= max) {
					//removeFirst();
					String k = header.link_next.key;
					int v = remove(k);
					overflowed(k,v);
				}
				break;
			}
		}
		if (count >= threshold) {
			rehash();
			tab = table;
			index = hash(key) % tab.length;
		}
		StringHashCacheEntry e = new StringHashCacheEntry(key, value, tab[index]);
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
		return 0;
	}
	protected void overflowed(String key, int value) {
	}
	public synchronized int remove(String key) {
		if (key == null)
			return 0;
		StringHashCacheEntry tab[] = table;
		int index = hash(key) % tab.length;
		for (StringHashCacheEntry e = tab[index], prev = null; e != null; prev = e, e = e.next) {
			if (e.key == key) {
				if (prev != null) {
					prev.next = e.next;
				} else {
					tab[index] = e.next;
				}
				count--;
				int oldValue = e.value;
				e.value = 0;
				//
				unchain(e);
				return oldValue;
			}
		}
		return 0;
	}
	public synchronized int removeFirst() {
		if (isEmpty())
			return 0;
		return remove(header.link_next.key);
	}
	public synchronized int removeLast() {
		if (isEmpty())
			return 0;
		return remove(header.link_prev.key);
	}
	public boolean isEmpty() {
		return size() == 0;
	}
	public synchronized void clear() {
		StringHashCacheEntry tab[] = table;
		for (int index = tab.length; --index >= 0;)
			tab[index] = null;
		this.header.link_next = header.link_prev = header;
		count = 0;
	}
	public String toString() {
		StringBuffer buf = new StringBuffer();
		Enumeration it = myEntries();
		buf.append("{");
		for (int i = 0; it.hasMoreElements(); i++) {
			StringHashCacheEntry e = (StringHashCacheEntry) (it.nextElement());
			if (i > 0)
				buf.append(", ");
			buf.append(e.getKey() + "=" + e.getValue());
		}
		buf.append("}");
		return buf.toString();
	}
	public String toFormatString() {
		StringBuffer buf = new StringBuffer();
		Enumeration it = myEntries();
		buf.append("{\n");
		while (it.hasMoreElements()) {
			StringHashCacheEntry e = (StringHashCacheEntry) it.nextElement();
			buf.append("\t").append(e.getKey() + "=" + e.getValue()).append("\n");
		}
		buf.append("}");
		return buf.toString();
	}
	public static class StringHashCacheEntry {
		String key;
		int value;
		StringHashCacheEntry next;
		StringHashCacheEntry link_next, link_prev;
		protected StringHashCacheEntry(String key, int value, StringHashCacheEntry next) {
			this.key = key;
			this.value = value;
			this.next = next;
		}
		protected Object clone() {
			return new StringHashCacheEntry(key, value, (next == null ? null : (StringHashCacheEntry) next.clone()));
		}
		public String getKey() {
			return key;
		}
		public int getValue() {
			return value;
		}
		public int setValue(int value) {
			int oldValue = this.value;
			this.value = value;
			return oldValue;
		}
		public boolean equals(Object o) {
			if (!(o instanceof StringHashCacheEntry))
				return false;
			StringHashCacheEntry e = (StringHashCacheEntry) o;
			return e.key == key && e.value == value;
		}
		public int hashCode() {
			return key.hashCode() ^ value;
		}
		public String toString() {
			return key + "=" + value;
		}
	}
	private class Enumer implements Enumeration, StringEnumer, IntEnumer {
		byte type;
		StringHashCacheEntry myEntry = StringHashCacheMap.this.header.link_next;
		Enumer(byte type) {
			this.type = type;
		}
		public boolean hasMoreElements() {
			return header != myEntry && myEntry != null;
		}
		public Object nextElement() {
			if (hasMoreElements()) {
				StringHashCacheEntry e = myEntry;
				myEntry = e.link_next;
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
		public int nextInt() {
			if (hasMoreElements()) {
				StringHashCacheEntry e = myEntry;
				myEntry = e.link_next;
				return e.value;
			}
			throw new NoSuchElementException("no more next");
		}
		public String nextString() {
			if (hasMoreElements()) {
				StringHashCacheEntry e = myEntry;
				myEntry = e.link_next;
				return e.key;
			}
			throw new NoSuchElementException("no more next");
		}
	}
	private void chain(StringHashCacheEntry link_prev, StringHashCacheEntry link_next, StringHashCacheEntry e) {
		e.link_prev = link_prev;
		e.link_next = link_next;
		link_prev.link_next = e;
		link_next.link_prev = e;
	}
	private void unchain(StringHashCacheEntry e) {
		e.link_prev.link_next = e.link_next;
		e.link_next.link_prev = e.link_prev;
		e.link_prev = null;
		e.link_next = null;
	}
	public static void main(String[] args) {
	}
	private static void print(Object e) {
		System.out.println(e);
	}
}
