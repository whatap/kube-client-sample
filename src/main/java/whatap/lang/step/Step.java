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
package whatap.lang.step;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

abstract public class Step implements Comparable<Step> {
	public int parent;
	public int index;
	public int start_time;

	public int getOrder() {
		return index;
	}

	abstract public int getElapsed();
	
	public void write(DataOutputX out) throws IOException {
		out.writeDecimal(parent);
		out.writeDecimal(index);
		out.writeDecimal(start_time);
	}

	public Step read(DataInputX in) throws IOException {
		this.parent = (int) in.readDecimal();
		this.index = (int) in.readDecimal();
		this.start_time = (int) in.readDecimal();
		return this;
	}

	public int compareTo(Step o) {
		return this.getOrder() - o.getOrder();
	}

	abstract public byte getStepType();

	public static byte[] toBytes(Step[] p) {
		if (p == null)
			return null;
		DataOutputX dout = new DataOutputX(p.length * 30);
		for (int i = 0; i < p.length; i++) {
			dout.writeStep(p[i]);
		}
		return dout.toByteArray();
	}

	public static byte[] toBytes(List<Step> p) {
		if (p == null)
			return null;
		int size = p.size();
		DataOutputX dout = new DataOutputX(size * 30);
		for (int i = 0; i < size; i++) {
			dout.writeStep(p.get(i));
		}
		return dout.toByteArray();
	}

	public static Step[] toObjects(byte[] buff) throws IOException {
		if (buff == null)
			return null;
		ArrayList<Step> arr = new ArrayList<Step>();
		DataInputX din = new DataInputX(buff);
		while (din.available() > 0) {
			arr.add(din.readStep());
		}
		return (Step[]) arr.toArray(new Step[arr.size()]);

	}

//	public byte opt;
//	protected boolean isTrue(int flag) {
//		return (this.opt & flag) != 0;
//	}
//	public void setTrue(int flag) {
//		this.opt |= (byte)flag;
//	}
	
	
	public boolean drop;

}
