/*
 *  Copyright 2016 the original author or authors. 
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
import java.util.Enumeration;
import java.util.NoSuchElementException;

/**
 * HashTable with entry expiration. - it use only active expiration : delete
 * entry when access expired data
 * 
 * @param <K>
 *            Key type
  */
public class CacheSet<K> {
	private static final int DEFAULT_CAPACITY = 101;
	private static final float DEFAULT_LOAD_FACTOR = 0.75f;
	private CacheSetity<K> table[];
	private CacheSetity<K> header;
	private int count;
	private int threshold;
	private float loadFactor;
	private long defaultKeepTime = 0;

	public CacheSet() {
		this(DEFAULT_CAPACITY, DEFAULT_LOAD_FACTOR);
	}

	public CacheSet(int initCapacity, float loadFactor) {
		if (initCapacity < 0)
			throw new RuntimeException("Capacity Error: " + initCapacity);
		if (loadFactor <= 0)
			throw new RuntimeException("Load Count Error: " + loadFactor);
		if (initCapacity == 0)
			initCapacity = 1;
		this.loadFactor = loadFactor;
		this.table = new CacheSetity[initCapacity];
		this.header = new CacheSetity(null, 0, null);
		this.header.link_next = header.link_prev = header;
		threshold = (int) (initCapacity * loadFactor);
	}

	public int size() {
		return count;
	}

	public synchronized Enumeration<K> keys() {
		return new Enumer<K>(TYPE.KEYS);
	}

	public synchronized Enumeration<CacheSetity<K>> entries() {
		return new Enumer<CacheSetity>(TYPE.ENTRIES);
	}

	public synchronized boolean containsKey(K key) {
		return getEntry(key) != null;
	}

	public synchronized K get(K key) {
		CacheSetity<K> e = getEntry(key);
		return e == null ? null : e.getKey();
	}

	public synchronized K setKeepAlive(K key, long keepAlive) {
		CacheSetity<K> e = getEntry(key);
		if (e == null)
			return null;
		e.keepAlive(keepAlive);
		return e.getKey();
	}

	public synchronized K setKeepAlive(K key) {
		CacheSetity<K> e = getEntry(key);
		if (e == null)
			return null;
		e.keepAlive(defaultKeepTime);
		return e.getKey();
	}
	private K getValue(CacheSetity<K> e) {
		if (e == null)
			return null;
		if (e.isExpired()) {
			remove(e.key);
			return null;
		}
		return e.key;
	}
	public synchronized K getFirst() {
		if (isEmpty())
			return null;
		return getValue(this.header.link_next);
	}
	public synchronized K getLast() {
		if (isEmpty())
			return null;
		return getValue(this.header.link_prev);
	}
	private CacheSetity<K> getEntry(K key) {
		if (key == null)
			return null;
		CacheSetity<K> tab[] = table;
		int index = hash(key) % tab.length;
		for (CacheSetity<K> e = tab[index]; e != null; e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				if (e.isExpired()) {
					remove(e.getKey());
					return null;
				} else {
					return e;
				}
			}
		}
		return null;
	}

	public synchronized int getRemindTime(K key) {
		CacheSetity entry = getEntry(key);
		if (entry != null) {
			if (entry.timeOfExpiration == 0)
				return Integer.MAX_VALUE;
			return (int) (entry.timeOfExpiration - System.currentTimeMillis());
		} else {
			return 0;
		}
	}

	public void clearExpiredItems() {
		try {
			ArrayList<K> delete = new ArrayList<K>();
			Enumeration<CacheSetity<K>> en = this.entries();
			while (en.hasMoreElements()) {
				CacheSetity e = en.nextElement();
				if (e.isExpired()) {
					delete.add((K) e.getKey());
				}
			}
			for (int i = 0; i < delete.size(); i++) {
				remove(delete.get(i));
			}
		} catch (Throwable t) {
			t.printStackTrace();
		}
	}

	private int hash(Object key) {
		return (int) (key.hashCode()) & Integer.MAX_VALUE;
	}

	protected void rehash() {
		int oldCapacity = table.length;
		CacheSetity oldMap[] = table;
		int newCapacity = oldCapacity * 2 + 1;
		CacheSetity newMap[] = new CacheSetity[newCapacity];
		threshold = (int) (newCapacity * loadFactor);
		table = newMap;
		for (int i = oldCapacity; i-- > 0;) {
			for (CacheSetity<K> old = oldMap[i]; old != null;) {
				CacheSetity<K> e = old;
				old = old.next;
				K key = e.key;
				int index = hash(key) % newCapacity;
				e.next = newMap[index];
				newMap[index] = e;
			}
		}
	}

	private int max;

	public CacheSet<K> setMaxRow(int max) {
		this.max = max;
		return this;
	}

	public CacheSet<K> setDefaultKeepTime(long time) {
		this.defaultKeepTime = time;
		return this;
	}

	public K put(K key, long keepTime) {
		return _put(key, keepTime, MODE.LAST);
	}

	public K putLast(K key, long keepTime) {
		return _put(key, keepTime, MODE.FORCE_LAST);
	}

	public K putFirst(K key, long keepTime) {
		return _put(key, keepTime, MODE.FORCE_FIRST);
	}

	public K put(K key) {
		return _put(key, defaultKeepTime, MODE.LAST);
	}

	public K putLast(K key) {
		return _put(key, defaultKeepTime, MODE.FORCE_LAST);
	}

	public K putFirst(K key) {
		return _put(key, defaultKeepTime, MODE.FORCE_FIRST);
	}

	private synchronized K _put(K key, long keepTime, int m) {
		CacheSetity<K> tab[] = table;
		int index = hash(key) % tab.length;
		for (CacheSetity<K> e = tab[index]; e != null; e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				K old = e.key;
				e.key=key;
				e.keepAlive(keepTime);
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
		CacheSetity e = new CacheSetity(key, keepTime, tab[index]);
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
		return null;
	}

	public synchronized K remove(K key) {
		if (key == null)
			return null;
		CacheSetity<K> tab[] = table;
		int index = hash(key) % tab.length;
		for (CacheSetity<K> e = tab[index], prev = null; e != null; prev = e, e = e.next) {
			if (CompareUtil.equals(e.key, key)) {
				if (prev != null) {
					prev.next = e.next;
				} else {
					tab[index] = e.next;
				}
				count--;
				K oldValue = e.key;
				e.key = null;
				//
				unchain(e);
				return oldValue;
			}
		}
		return null;
	}

	public synchronized K removeFirst() {
		if (isEmpty())
			return null;
		return remove(header.link_next.key);
	}

	public synchronized K removeLast() {
		if (isEmpty())
			return null;
		return remove(header.link_prev.key);
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public synchronized void clear() {
		CacheSetity tab[] = table;
		for (int index = tab.length; --index >= 0;)
			tab[index] = null;
		this.header.link_next = header.link_prev = header;
		count = 0;
	}

	public String toString() {
		StringBuffer buf = new StringBuffer();
		Enumeration<K> it = keys();
		buf.append("{");
		for (int i = 0; it.hasMoreElements(); i++) {
			K key = it.nextElement();
			if (i > 0)
				buf.append(", ");
			buf.append(key);
		}
		buf.append("}");
		return buf.toString();
	}

	public String toFormatString() {
		StringBuffer buf = new StringBuffer();
		Enumeration it = entries();
		buf.append("{\n");
		while (it.hasMoreElements()) {
			CacheSetity e = (CacheSetity) it.nextElement();
			buf.append("\t").append(e.getKey()).append("\n");
		}
		buf.append("}");
		return buf.toString();
	}

	public static class CacheSetity<K> {
		private K key;
		private long timeOfExpiration;
		private long keepTime = 0;
		CacheSetity<K> next;
		CacheSetity<K> link_next, link_prev;

		protected CacheSetity(K key, long keepTime, CacheSetity next) {
			this.key = key;
			this.keepAlive(keepTime);
			this.next = next;
		}

		protected Object clone() {
			return new CacheSetity(key, keepTime, (next == null ? null : (CacheSetity) next.clone()));
		}

		public K getKey() {
			return key;
		}

		public boolean isExpired() {
			if (timeOfExpiration > 0) {
				if (timeOfExpiration < System.currentTimeMillis())
					return true;
				else
					return false;
			} else {
				return false;
			}
		}

		public void keepAlive(long keepTime) {
			if (keepTime > 0) {
				this.keepTime = keepTime;
				this.timeOfExpiration = System.currentTimeMillis() + keepTime;
			} else {
				this.keepTime = 0;
				this.timeOfExpiration = 0;
			}
		}

		public void keep() {
			if (this.keepTime > 0) {
				this.timeOfExpiration = System.currentTimeMillis() + this.keepTime;
			}
		}

		public boolean equals(Object o) {
			if (!(o instanceof CacheSetity))
				return false;
			CacheSetity e = (CacheSetity) o;
			return CompareUtil.equals(key, e.key);
		}

		public int hashCode() {
			return key.hashCode();
		}

		public String toString() {
			return key.toString();
		}
	}


	private class Enumer<V> implements Enumeration {
		byte type;
		CacheSetity entry = CacheSet.this.header.link_next;
		CacheSetity lastEnt;

		Enumer(byte type) {
			this.type = type;
		}

		public boolean hasMoreElements() {
			return header != entry && entry != null;
		}

		public V nextElement() {
			if (hasMoreElements()) {
				CacheSetity e = lastEnt = entry;
				entry = e.link_next;
				switch (type) {
				case TYPE.KEYS:
					return (V) e.key;
				default:
					return (V) e;
				}
			}
			throw new NoSuchElementException("no more next");
		}
	}

	private void chain(CacheSetity link_prev, CacheSetity link_next, CacheSetity e) {
		e.link_prev = link_prev;
		e.link_next = link_next;
		link_prev.link_next = e;
		link_next.link_prev = e;
	}

	private void unchain(CacheSetity e) {
		e.link_prev.link_next = e.link_next;
		e.link_next.link_prev = e.link_prev;
		e.link_prev = null;
		e.link_next = null;
	}

	public static void main(String[] args) throws Exception {
		CacheSet t = new CacheSet().setDefaultKeepTime(1000).setMaxRow(100);
		for (int i = 0; i < 100; i++) {
			// if (i % 5 == 0) {
			// t.put(i, i, 10000);
			// } else {
			t.put(i, i);
			// }
		}
		Enumeration e = t.keys();
		System.out.println(t.get(0));
		System.out.println(t.get(10));
		System.out.println(t.get(99));
		Thread.sleep(2000);
		// t.clearExpiredItems();
		System.out.println("----->" + t.size());
	}
}
