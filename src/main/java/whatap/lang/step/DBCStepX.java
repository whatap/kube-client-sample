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

public class DBCStepX extends Step {
	public byte flag;
	public int hash;
	public int elapsed;
	public int error;
	public int sid;
	public int serial;
	public int inst_id;

	public byte getStepType() {
		return StepEnum.DBC_X;
	}

	public int getElapsed() {
		return elapsed;
	}

	public void write(DataOutputX out) throws IOException {
		super.write(out);
		DataOutputX dout = new DataOutputX();
		dout.writeByte(flag);
		dout.writeDecimal(hash);
		dout.writeDecimal(elapsed);
		dout.writeDecimal(error);
		dout.writeDecimal(sid);
		dout.writeDecimal(serial);
		dout.writeDecimal(inst_id);
		

		out.writeShortBytes(dout.toByteArray());
	}

	public Step read(DataInputX in) throws IOException {
		super.read(in);
		DataInputX din = new DataInputX(in.readShortBytes());
		this.flag=din.readByte();
		this.hash = (int) din.readDecimal();
		this.elapsed = (int) din.readDecimal();
		this.error = (int) din.readDecimal();
		this.sid =  (int)din.readDecimal();
		
		if (din.available() > 0) {
			this.serial = (int)din.readDecimal();
		}
		if (din.available() > 0) {
			this.inst_id = (int)din.readDecimal();
		}
		
		return this;
	} 
}
