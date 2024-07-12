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
 * The initial code  from  "Guarded Suspension", Java 언어로 배우는 디자인 패턴 입문 - 멀티 쓰레드 편 저자 Yuki Hiroshi
 */
package whatap.util;
public class RequestHashQueue<V> {
	private LongKeyLinkedMap<V> queue;
	private int capacity;
	public RequestHashQueue(int capacity) {
		this.capacity = capacity;
		this.queue = new LongKeyLinkedMap<V>();
	}
	public synchronized V getByKey(long key) {
		V out = queue.remove(key);
		while (out == null) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
			out = queue.remove(key);
		}
		return out;
	}
	public synchronized V getFirst() {
		while (queue.size() <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		return queue.removeFirst();
	}
	public synchronized V getFirst(long timeout) {
		if (queue.size() > 0) {
			return queue.removeFirst();
		}
		long timeto =  System.currentTimeMillis() + timeout;
		long time = timeout;
		while (queue.isEmpty()) {
			try {
				if (time > 0)
					wait(time);
			} catch (InterruptedException e) {
			}
			time = timeto -  System.currentTimeMillis();
			if (time <= 0) {
				break;
			}
		}
		if (queue.size() > 0) {
			return queue.removeFirst();
		}
		return null;
	}
	public synchronized V getByKey(long key, long timeout) {
		V out = queue.remove(key);
		if (out != null) {
			return out;
		}
		long timeto =  System.currentTimeMillis() + timeout;
		long time = timeout;
		while (out == null) {
			try {
				if (time > 0)
					wait(time);
			} catch (InterruptedException e) {
			}
			out = queue.remove(key);
			time = timeto -  System.currentTimeMillis();
			if (time <= 0) {
				break;
			}
		}
		return out;
	}
	public synchronized boolean put(long key, V o) {
		if (capacity <= 0 || queue.size() < capacity) {
			queue.put(key, o);
			notifyAll();
			return true;
		} else {
			failed(key,o);
			notify();
			return false;
		}
	}
	public synchronized boolean putForce(long key, V value) {
		if (capacity <= 0 || queue.size() < capacity) {
			queue.put(key, value);
			notifyAll();
			return true;
		} else {
			while (queue.size() >= capacity) {
				V v = queue.removeFirst();
				overflowed(v);
			}
			queue.put(key, value);
			notifyAll();
			return true;
		}
	}
	protected void failed(long key, V o) {		
	}
	protected void overflowed(V o) {		
	}
	public synchronized void clear() {
		queue.clear();
	}
	public int size() {
		return queue.size();
	}
	public int getCapacity() {
		return this.capacity;
	}
	public void setCapacity(int size) {
		this.capacity = size;
	}
}
