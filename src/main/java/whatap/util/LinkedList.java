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
package whatap.util;

import java.lang.reflect.Array;

/**
 * @author Paul Kim (sjkim@whatap.io)
 */
public class LinkedList<E> {
	transient int size = 0;
	transient LinkedListEntity<E> first;
	transient LinkedListEntity<E> last;
	transient int max = 0;

	public synchronized void addFirst(E e) {
		final LinkedListEntity<E> f = first;
		final LinkedListEntity<E> newNode = new LinkedListEntity(null, e, f);
		first = newNode;
		if (f == null)
			last = newNode;
		else
			f.prev = newNode;
		size++;

		if (max > 0) {
			while (max < size) {
				this.removeLast();
			}
		}
	}

	public synchronized void addLast(E e) {
		final LinkedListEntity<E> l = last;
		final LinkedListEntity<E> newNode = new LinkedListEntity(l, e, null);
		last = newNode;
		if (l == null)
			first = newNode;
		else
			l.next = newNode;
		size++;

		if (max > 0) {
			while (max < size) {
				this.removeFirst();
			}
		}

	}

	public synchronized LinkedListEntity<E> putBefore(E e, LinkedListEntity<E> succ) {

		final LinkedListEntity<E> pred = succ.prev;
		final LinkedListEntity<E> newNode = new LinkedListEntity(pred, e, succ);
		succ.prev = newNode;
		if (pred == null)
			first = newNode;
		else
			pred.next = newNode;
		size++;

		if (max > 0) {
			while (max < size) {
				this.removeLast();
			}
		}

		return newNode;
	}

	public synchronized E remove(LinkedListEntity<E> x) {
		final E element = x.value;
		final LinkedListEntity<E> next = x.next;
		final LinkedListEntity<E> prev = x.prev;
		if (prev == null) {
			first = next;
		} else {
			prev.next = next;
		}
		if (next == null) {
			last = prev;
		} else {
			next.prev = prev;
		}
		size--;
		return element;
	}

	public LinkedListEntity<E> getFirst() {
		return first;
	}

	public LinkedListEntity<E> getLast() {
		return last;
	}

	public LinkedListEntity<E> getNext(LinkedListEntity<E> o) {
		return o.next;
	}

	public LinkedListEntity<E> getPrev(LinkedListEntity<E> o) {
		return o.prev;
	}

	public synchronized E removeFirst() {
		final LinkedListEntity<E> f = first;
		if (f != null)
			return remove(first);
		return null;
	}

	public synchronized E removeLast() {
		final LinkedListEntity<E> l = last;
		if (l != null)
			return remove(l);
		return null;
	}

	public int size() {
		return size;
	}

	public boolean add(E e) {
		addLast(e);
		return true;
	}

	public synchronized void clear() {
		if(size==0)
			return;
		
		for (LinkedListEntity<E> x = first; x != null;) {
			LinkedListEntity<E> next = x.next;
			x.value = null;
			x.next = null;
			x.prev = null;
			x = next;
		}
		first = last = null;
		size = 0;
	}

	public synchronized E[] toArray(E[] result) {
		if (result.length != size) {
			result = (E[]) Array.newInstance(result.getClass().getComponentType(), size);
		}
		int i = 0;
		for (LinkedListEntity<E> x = (LinkedListEntity<E>) this.first; x != null && i < size; x = x.next)
			result[i++] = x.value;
		return result;
	}

	public static class LinkedListEntity<E> {
		public E value;
		public LinkedListEntity<E> next;
		public LinkedListEntity<E> prev;

		LinkedListEntity(LinkedListEntity<E> prev, E element, LinkedListEntity<E> next) {
			this.value = element;
			this.next = next;
			this.prev = prev;
		}
	}

	@Override
	public String toString() {

		StringBuffer sb = new StringBuffer();
		int i = 0;
		for (LinkedListEntity<E> x = first; x != null; x = x.next) {
			if (sb.length() > 0) {
				sb.append(',');
			}
			sb.append(x.value);
		}
		return sb.toString();
	}

	public LinkedList<E> setMax(int x) {
		this.max = x;
		return this;
	}

	public static void main(String[] args) {
		LinkedList<String> t = new LinkedList<String>().setMax(3);
		t.add("aaa");
		t.add("bbb");
		t.add("ccc");

		LinkedListEntity<String> ent = t.getLast();
		ent = t.putBefore("ddd", ent);
		System.out.println(ent.next);
		
		t.add("eee");
		System.out.println(t);
		t.addFirst("fff");
		System.out.println(t);
	}
}
