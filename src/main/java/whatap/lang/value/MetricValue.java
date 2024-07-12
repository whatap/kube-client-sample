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

import java.text.DecimalFormat;

public class MetricValue implements Value {

	public int count;
	public double sum;
	public float min;
	public float max;
	public float last;

	public byte getValueType() {
		return ValueEnum.METRIC;
	}

	public void write(DataOutputX out) {
		if (this.count == 0) {
			out.writeByte(0);
		} else {
			out.writeByte(1);
			out.writeDecimal(count);
			out.writeDouble(sum);
			out.writeFloat(min);
			out.writeFloat(max);
			out.writeFloat(last);
		}
	}

	public Value read(DataInputX in) {
		byte mode = in.readByte();

		switch (mode) {
		case 0:
			return this;
		case 1:
			this.count = (int) in.readDecimal();
			this.sum = in.readDouble();
			this.min = in.readFloat();
			this.max = in.readFloat();
			this.last = in.readFloat();
		}
		return this;
	}

	public int compareTo(Object o) {
		return CompareUtil.compareTo(this.hashCode(), o.hashCode());
	}

	public String toString() {
		DecimalFormat fmt = new DecimalFormat("#0.0#################");
		StringBuffer sb = new StringBuffer();
		sb.append("[sum=").append(fmt.format(new Double(sum)));
		sb.append(",count=").append(count);
		sb.append(",min=").append(fmt.format(new Float(min)));
		sb.append(",max=").append(fmt.format(new Float(max)));
		sb.append(",last=").append(fmt.format(new Float(last)));
		sb.append("]");
		return sb.toString();
	}

	public Object toJavaObject() {
		return this;
	}

	public void addcount() {
		this.count++;
	}

	public MetricValue add(Number value) {
		if (value == null)
			return this;
		float fval = value.floatValue();
		if (this.count == 0) {
			this.count = 1;
			this.sum = fval;
			this.max = fval;
			this.min = fval;
		} else {
			this.count++;
			this.sum += fval;
			this.max = Math.max(this.max, fval);
			this.min = Math.min(this.min, fval);
		}
		this.last = value.floatValue();
		return this;
	}

	public double sum() {
		return this.sum;
	}

	public float min() {
		return this.min;
	}

	public float max() {
		return this.max;
	}

	public float last() {
		return this.last;
	}

	public float avg() {
		return (float) (count == 0 ? 0 : sum / count);
	}

	public int getCount() {
		return this.count;
	}
	public Value copy() {
		MetricValue o = new MetricValue();
		o.count = this.count;
		o.max = this.max;
		o.min = this.min;
		o.sum = this.sum;
		o.last = this.last;
		return o;
	}
	public boolean isEmpty() {
		return this.count==0;
	}
}
