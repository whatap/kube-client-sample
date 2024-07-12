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

public class SecureMsgStep extends Step {

	public int hash;
	public byte opt;
	public byte crc;
	public byte[] value;

	public SecureMsgStep() {
	}

	public int getElapsed() {
		return 0;
	}
	public SecureMsgStep(int start_time) {
		this.start_time = start_time;
	}

	public byte getStepType() {
		return StepEnum.SECURE_MESSAGE;
	}

	public String toString() {
		return "SecureMsgStep " + hash;
	}

	public void write(DataOutputX out) throws IOException {
		super.write(out);
		out.writeDecimal(hash);
		out.writeByte(opt);
		out.writeByte(crc);
		out.writeBlob(value);
	}

	public Step read(DataInputX in) throws IOException {
		super.read(in);
		this.hash = (int) in.readDecimal();
		this.opt = in.readByte();
		this.crc = in.readByte();
		this.value = in.readBlob();
		return this;
	}
}