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
 */
package whatap.util;

import whatap.util.LinkedMap.LinkedEntry;

import java.util.Enumeration;
import java.util.Map.Entry;
import java.util.TreeMap;

public class TimedSeries<K, V> {

	private LinkedMap<K, TreeMap<Long, V>> table = new LinkedMap<K, TreeMap<Long, V>>() {
		protected TreeMap<Long, V> create(K key) {
			return new TreeMap<Long, V>();
		};
	};
	private long minTime = Long.MAX_VALUE;
	private long maxTime = Long.MIN_VALUE;

	public void add(K key, long time, V value) {
		TreeMap<Long, V> tree = table.intern(key);
		tree.put(time, value);

		if (time > maxTime)
			maxTime = time;
		if (time < minTime)
			minTime = time;
	}

	public LinkedMap<K, V> getInTimeList(long time) {
		return getInTimeList(time, Long.MAX_VALUE - time);
	}

	public LinkedMap<K, V> getInTimeList(long time, long valid) {
		LinkedMap<K, V> out = new LinkedMap<K, V>();
		Enumeration<LinkedEntry<K, TreeMap<Long, V>>> en = table.entries();
		while (en.hasMoreElements()) {
			LinkedEntry<K, TreeMap<Long, V>> bt = en.nextElement();
			Entry<Long, V> ent = bt.getValue().ceilingEntry(time);
			if (ent != null && (time + valid >= ent.getKey())) {
				out.put(bt.getKey(), ent.getValue());
			}
		}
		return out;
	}

	public V get(K key, long time) {
		return getInTime(key, time, Long.MAX_VALUE - time);
	}

	public V getInTime(K key, long time, long valid) {
		TreeMap<Long, V> series = table.get(key);
		if (series != null) {
			Entry<Long, V> entry = series.ceilingEntry(time);
			if (entry != null && (time + valid >= entry.getKey().longValue())) {
				return (V) entry.getValue();
			}
		}
		return null;
	}

	public int getSeriesCount() {
		return table.size();
	}

	public long getMinTime() {
		return minTime;
	}

	public long getMaxTime() {
		return maxTime;
	}

	public Enumeration<K> getNameEnumer() {
		return table.keys();
	}
}