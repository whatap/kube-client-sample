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

public class RequestDoubleQueue<V> {
	private java.util.LinkedList<V> queue1;
	private java.util.LinkedList<V> queue2;
	private int capacity1;
	private int capacity2;

	public RequestDoubleQueue(int capacity1, int capacity2) {
		this.capacity1 = capacity1;
		this.capacity2 = capacity2;
		this.queue1 = new java.util.LinkedList<V>();
		this.queue2 = new java.util.LinkedList<V>();
	}

	public synchronized V get() {
		while (queue1.size() <= 0 && queue2.size() <= 0) {
			try {
				wait();
			} catch (InterruptedException e) {
			}
		}
		if (queue1.size() > 0) {
			return queue1.removeFirst();
		}
		if (queue2.size() > 0) {
			return queue2.removeFirst();
		}
		return null;
	}

	public synchronized V getNoWait() {
		if (queue1.size() > 0) {
			return queue1.removeFirst();
		}
		if (queue2.size() > 0) {
			return queue2.removeFirst();
		}
		return null;
	}

	public synchronized V get(long timeout) {

		if (queue1.size() > 0) {
			return queue1.removeFirst();
		}
		if (queue2.size() > 0) {
			return queue2.removeFirst();
		}
		long timeto =  System.currentTimeMillis() + timeout;
		long time = timeout;
		while (queue1.isEmpty() && queue2.isEmpty()) {
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
		if (queue1.size() > 0) {
			return queue1.removeFirst();
		}
		if (queue2.size() > 0) {
			return queue2.removeFirst();
		}
		return null;
	}

	public synchronized boolean putForce1(V o) {
		return putForce(queue1, capacity1, o);
	}

	public synchronized boolean putForce2(V o) {
		return putForce(queue2, capacity2, o);
	}

	private boolean putForce(java.util.LinkedList<V> q, int sz, V o) {
		if (sz <= 0 || q.size() < capacity1) {
			q.add(o);
			notifyAll();
			return true;
		} else {
			while (q.size() >= sz) {
				V v = q.removeFirst();
				overflowed(v);
			}
			q.add(o);
			notifyAll();
			return false;
		}
	}

	protected void overflowed(V v) {
	}

	protected void failed(V v) {
	}

	public synchronized boolean put1(V o) {
		return put(queue1, capacity1, o);
	}

	public synchronized boolean put2(V o) {
		return put(queue1, capacity1, o);
	}

	private boolean put(java.util.LinkedList<V> queue, int capacity, V o) {
		if (capacity <= 0 || queue.size() < capacity) {
			queue.add(o);
			notifyAll();
			return true;
		} else {
			failed(o);
			notifyAll();
			return false;
		}
	}

	public synchronized void clear() {
		queue1.clear();
		queue2.clear();
	}

	public int size() {
		return queue1.size() + queue2.size();
	}

	public int size1() {
		return queue1.size();
	}

	public int size2() {
		return queue2.size();
	}

	public int getCapacity1() {
		return this.capacity1;
	}

	public int getCapacity2() {
		return this.capacity2;
	}

	public void setCapacity(int size1, int size2) {
		this.capacity1 = size1;
		this.capacity2 = size2;
	}
}
