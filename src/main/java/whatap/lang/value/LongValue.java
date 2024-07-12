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
package whatap.lang.value;


import whatap.io.DataInputX;
import whatap.io.DataOutputX;

import java.io.IOException;


public class LongValue extends NumberValue implements Value, Comparable {

	public long value;

	public LongValue() {
	}

	public LongValue(long value) {
		this.value = value;
	}

	public int compareTo(Object o) {
		if (o instanceof LongValue) {
			long thisVal = this.value;
			long anotherVal = ((LongValue) o).value;
			return (thisVal < anotherVal ? -1 : (thisVal == anotherVal ? 0 : 1));
		}
		return 1;
	}

	public boolean equals(Object o) {
		if (o instanceof LongValue) {
			return this.value == ((LongValue) o).value;
		}
		return false;
	}

	public int hashCode() {
		return (int) (value ^ (value >>> 32));
	}

	public byte getValueType() {
		return ValueEnum.DECIMAL_LONG;
	}

	public void write(DataOutputX out) throws IOException {
		out.writeLong(value);
	}

	public Value read(DataInputX in) throws IOException {
		this.value = in.readLong();
		return this;
	}

	public String toString() {
		return Long.toString(value);
	}

	// ////////////////////////////////
	public double doubleValue() {
		return value;
	}

	public float floatValue() {
		return (float) value;
	}

	public int intValue() {
		return (int) value;
	}

	public long longValue() {
		return (long) value;
	}

	public Object toJavaObject() {
		return this.value;
	}
	public Value copy() {		
		return new LongValue(this.value);
	}
	public boolean isEmpty() {
		return this.value==0;
	}
}
