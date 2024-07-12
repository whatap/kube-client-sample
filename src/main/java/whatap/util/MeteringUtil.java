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

import whatap.lang.H1;

import java.lang.reflect.Array;

public abstract class MeteringUtil<T> {

	public final int BUCKET_SIZE;
	public final int TIME_UNIT;

	public MeteringUtil() {
		this(1000, 301);
	}

	public MeteringUtil(int bucketSize) {
		this(1000, bucketSize);
	}

	public MeteringUtil(int timeUnit, int bucketSize) {
		this.TIME_UNIT = timeUnit;
		this.BUCKET_SIZE = bucketSize;
		this._time_ = getTime();
		this._pos_ = (int) (_time_ % BUCKET_SIZE);
		this.table = new Object[bucketSize];
		for (int i = 0; i < bucketSize; i++) {
			this.table[i] = create();
		}
	}

	private final Object[] table;
	private long _time_;
	private int _pos_;

	abstract protected T create();

	abstract protected void clear(T o);

	public synchronized T getCurrentBucket() {
		int pos = getPosition();
		return (T) table[pos];
	}

	public synchronized T getLastBucket() {
		int pos = getPosition();
		return (T) table[stepback(pos)];
	}

	public synchronized T[] getLastTwoBucket() {
		int pos = getPosition();
		pos = stepback(pos);
		T t1 = (T) table[pos];
		pos = stepback(pos);
		T t2 = (T) table[pos];
		if (t1 == null) {
			return null;
		}

		// return (T[]) new Object[] { t1, t2 };
		T[] result = (T[]) Array.newInstance(t1.getClass(), 2);
		result[0] = t1;
		result[1] = t2;
		return result;
	}

	public synchronized T getCurrentBucket(long time) {
		int pos = getPosition(time);
		if (pos >= 0)
			return (T) table[pos];
		else
			return null;
	}

	public synchronized int getPosition() {
		long curTime = getTime();
		if (curTime != _time_) {
			for (int i = 0; i < (curTime - _time_) && i < BUCKET_SIZE; i++) {
				_pos_ = (_pos_ + 1 > BUCKET_SIZE - 1) ? 0 : _pos_ + 1;
				clear((T) table[_pos_]);
			}
			_time_ = curTime;
			_pos_ = (int) (_time_ % BUCKET_SIZE);
		}
		return _pos_;
	}

	public synchronized int getPosition(long time) {
		long curTime = getTime();
		if (curTime != _time_) {
			for (int i = 0; i < (curTime - _time_) && i < BUCKET_SIZE; i++) {
				_pos_ = (_pos_ + 1 > BUCKET_SIZE - 1) ? 0 : _pos_ + 1;
				clear((T) table[_pos_]);
			}
			_time_ = curTime;
			_pos_ = (int) (_time_ % BUCKET_SIZE);
		}
		long theTime = time / TIME_UNIT;
		if (theTime > curTime || theTime < curTime - BUCKET_SIZE + 2)
			return -1;
		return (int) (theTime % BUCKET_SIZE);
	}

	protected int check(int period) {
		if (period >= BUCKET_SIZE)
			period = BUCKET_SIZE - 1;
		return period;
	}

	protected int stepback(int pos) {
		if (pos == 0)
			pos = BUCKET_SIZE - 1;
		else
			pos--;
		return pos;
	}

	public synchronized int search(int period, H1<T> h) {

		period = check(period);
		int pos = getPosition();
		// pos = stepback(pos); //현제 포지션은 스킵한다.
		try {
			for (int i = 0; i < period; i++, pos = stepback(pos)) {
				h.process((T) table[pos]);
			}
		} catch (Exception e) {
		}
		return period;
	}

	public synchronized int search(int period, H1<T> h, int skip) {
		period = check(period);
		int pos = getPosition();
		for (int i = 0; i < skip; i++) {
			pos = stepback(pos); // 현제 포지션은 스킵한다.
		}
		try {
			for (int i = 0; i < period; i++, pos = stepback(pos)) {
				h.process((T) table[pos]);
			}
		} catch (Exception e) {
		}
		return period;
	}

	public synchronized T[] search(int period) {
		period = check(period);
		int pos = getPosition();
		// pos = stepback(pos); //현제 포지션은 스킵한다.
		T[] out = (T[]) new Object[period];
		for (int i = 0; i < period; i++, pos = stepback(pos)) {
			out[i] = ((T) table[pos]);
		}
		return out;
	}

	protected long getTime() {
		return DateUtil.currentTime() / TIME_UNIT;
	}

	public  void reset(long time, T bucket) {
		int pos = getPosition(time);
		if(pos >=0) {
			table[pos]=bucket;
		}			
	}
}
