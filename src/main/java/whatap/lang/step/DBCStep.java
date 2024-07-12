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

public class DBCStep extends Step {
	public int hash;
	public int elapsed;
	public int error;
	transient public int sid;
	transient public int serial;
	transient public int inst;

	public byte getStepType() {
		return sid == 0 ? StepEnum.DBC : StepEnum.DBC_X;
	}

	public int getElapsed() {
		return elapsed;
	}

	public void write(DataOutputX out) throws IOException {
		if (sid == 0) {
			super.write(out);
			out.writeDecimal(hash);
			out.writeDecimal(elapsed);
			out.writeDecimal(error);
		}else {
			toX().write(out);
		}
	}

	public Step read(DataInputX in) throws IOException {
		super.read(in);
		this.hash = (int) in.readDecimal();
		this.elapsed = (int) in.readDecimal();
		this.error = (int) in.readDecimal();
		return this;
	}
	private DBCStepX toX() {
		DBCStepX o = new DBCStepX();
		o.parent = this.parent;
		o.index = this.index;
		o.start_time = this.start_time;
		o.hash = this.hash;
		o.elapsed = this.elapsed;
		o.error = this.error;
		o.sid=this.sid;
		o.serial=this.serial;
		o.inst_id=this.inst;
		return o;
	}
	
}
