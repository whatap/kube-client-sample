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

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

/**
 * @author Paul S.J. Kim(sjokim@gmail.com)
 */
public abstract class BinSearchFile<R, K> implements IClose {
	private RandomAccessFile raf;
	private int LENGTH;
	private long COUNT;
	private long START;
	private long END;

	public BinSearchFile(File file, int len) {
		this(file);
		setRecordLength(len);
	}

	public BinSearchFile(File file) {
		try {
			this.raf = new RandomAccessFile(file, "r");
			setFileSearchRange(0, this.raf.length());
		} catch (IOException e) {
			this.COUNT = 0;
		}
	}

	public BinSearchFile(RandomAccessFile raf, long start, long end) {
		this.raf = raf;
		this.START = start;
		this.END = end;
	}

	public BinSearchFile<R, K> setFileSearchRange(long start, long end) {
		this.START = start;
		this.END = end;
		return this;
	}

	public BinSearchFile<R, K> setRecordLength(int len) {
		try {
			this.LENGTH = len;
			this.COUNT = (END - START) / this.LENGTH;
		} catch (Exception e) {
			this.COUNT = 0;
		}
		return this;
	}

	protected long cur = 0;
	protected long low = 0;
	protected long high = 0;

	public R find(K key) {
		if (isEmpty())
			return null;
		try {
			if (_find(key))
				return record(START + cur * LENGTH);
		} catch (Exception e) {
			return null;
		}
		return null;
	}

	public R findLessEqual(K key) {
		if (isEmpty())
			return null;
		try {
			if (_find(key))
				return record(START + cur * LENGTH);
			cur = high;
			if (high < 0)
				return null;
			return record(START + cur * LENGTH);
		} catch (Exception e) {
			return null;
		}
	}

	public R findGreaterEqual(K key) {
		if (isEmpty())
			return null;
		try {
			if (_find(key))
				return record(START + cur * LENGTH);
			cur = low;
			if (low >= COUNT)
				return null;
			return record(START + cur * LENGTH);
		} catch (Exception e) {
			return null;
		}
	}

	private boolean _find(K key) throws Exception {
		cur = -1;
		low = 0;
		high = COUNT - 1;
		while (high >= low) {
			long middle = (low + high) / 2;
			long cmp = compare(key, key(START + middle * LENGTH));
			if (cmp == 0) {
				cur = middle;
				return true;
			} else if (cmp > 0) {
				low = middle + 1;
			} else if (cmp < 0) {
				high = middle - 1;
			}
		}
		return false;
	}

	private boolean isEmpty() {
		if (COUNT <= 0) {
			return true;
		}
		return false;
	}

	public R readPrev() {
		if (cur < 0 || cur >= COUNT)
			return null;
		if (--cur < 0)
			return null;
		try {
			return record(START + cur * LENGTH);
		} catch (Exception e) {
			cur = -1;
			return null;
		}
	}

	public R readNext() {
		if (cur < 0 || cur >= COUNT)
			return null;
		if (++cur >= COUNT)
			return null;
		try {
			return record(START + cur * LENGTH);
		} catch (Exception e) {
			cur = -1;
			return null;
		}
	}

	public byte[] read(long pos, int len) throws IOException {
		byte[] buf = new byte[len];
		this.raf.seek(pos);
		this.raf.read(buf);
		return buf;
	}

	protected abstract R record(long pos) throws Exception;

	protected abstract K key(long pos) throws Exception;

	protected abstract int compare(K key1, K key2);

	public void close() {
		FileUtil.close(this.raf);
	}
}
