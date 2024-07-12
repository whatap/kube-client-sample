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
import whatap.util.CompareUtil;

import java.io.IOException;
import java.util.Arrays;


public class DoubleArray  implements Value, Comparable, ArrayValue {

	public double[] value;

	public DoubleArray() {
	}

	public DoubleArray(double[] value) {
		this.value = value;
	}

	public int length() {
		return value == null ? 0 : value.length;
	}

	public ArrayValue subArray(int start, int end) {
		int len = end - start;
		if (len == 0) {
			return new DoubleArray(new double[0]);
		}
		if (len == this.value.length)
			return this;
		double[] out = new double[len];
		System.arraycopy(value, start, out, 0, len);
		return new DoubleArray(out);
	}
	public Value get(int i) {
		return new DoubleValue(this.value[i]);
	}
	public boolean isNumber() {
		return true;
	}
	public int compareTo(Object o) {
		if (o instanceof DoubleArray) {
			return CompareUtil.compareTo(this.value, ((DoubleArray) o).value);
		}
		return 1;
	}

	public boolean equals(Object o) {
		if (o instanceof DoubleArray) {
			return Arrays.equals(this.value,((DoubleArray) o).value);
		}
		return false;
	}

	private int _hash;
	public int hashCode() {
		if(_hash==0){
			_hash= Arrays.hashCode(this.value);
		}
		return _hash;
	}

	public byte getValueType() {
		return ValueEnum.ARRAY_DOUBLE;
	}

	public void write(DataOutputX out) throws IOException {
		out.writeArray(value);
	}

	public Value read(DataInputX in) throws IOException {
		this.value = in.readArray(new double[0]);
		return this;
	}

	public String toString() {
		return Arrays.toString(value);
	}

	public Object toJavaObject() {
		return this.value;
	}
	public Value copy() {
		return new DoubleArray(this.value);
	}
	public boolean isEmpty() {
		return this.value==null || this.value.length==0;
	}
}
