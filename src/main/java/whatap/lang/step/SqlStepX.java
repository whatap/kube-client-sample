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
import whatap.util.BitUtil;

import java.io.IOException;

public class SqlStepX extends Step {
	public byte xtype;
	public int hash;
	public int elapsed;
	public long error;
	public int dbc;

	public byte[] p1;
	public byte[] p2;
	public byte pcrc;
	public int start_cpu;
	public long start_mem;
	public int[] stack;
	public int sid;
	public int serial;
	public int inst;

	public byte getStepType() {
		return StepEnum.SQL_X;
	}

	public int getElapsed() {
		return elapsed;
	}

	public void write(DataOutputX out) throws IOException {
		super.write(out);
		out.writeByte(sid==0?0:1);
		out.writeDecimal(hash);
		out.writeDecimal(elapsed);
		out.writeDecimal(error);
		out.writeByte(xtype);
		out.writeDecimal(dbc);
		out.writeBlob(p1);
		out.writeBlob(p2);
		out.writeByte(pcrc);
		out.writeDecimal(start_cpu);
		out.writeDecimal(start_mem);
		out.writeArray(stack);
		if (sid != 0) {
			out.writeDecimal(sid);
			long v = BitUtil.composite(inst, serial);
			out.writeDecimal(v);
		}

	}

	public Step read(DataInputX in) throws IOException {
		super.read(in);
		byte ver = in.readByte();
		
		this.hash = (int) in.readDecimal();
		this.elapsed = (int) in.readDecimal();
		this.error = in.readDecimal();
		this.xtype = in.readByte();
		this.dbc = (int) in.readDecimal();

		this.p1 = in.readBlob();
		this.p2 = in.readBlob();
		this.pcrc = in.readByte();
		this.start_cpu = (int) in.readDecimal();
		this.start_mem = (int) in.readDecimal();
		this.stack = in.readArray(stack);

		if(ver==0) {
			return this;
		}
		
		this.sid =  (int)in.readDecimal();
		long inst_and_serial =  in.readDecimal();
		this.inst = BitUtil.getHigh(inst_and_serial);
		this.serial = BitUtil.getLow(inst_and_serial);
		
		
		return this;
	}
}
