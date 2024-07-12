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

public class RemoteStep extends Step {
	public int host;
	public String query;

	public int elapsed;
	public long error;


	public int start_cpu;
	public long start_mem;

	public byte getStepType() {
		return StepEnum.REMOTE;
	}

	public int getElapsed() {
		return elapsed;
	}

	public void write(DataOutputX out) throws IOException {
		super.write(out);
		out.writeByte(0);//version
		out.writeDecimal(host);
		out.writeText(query);
		out.writeDecimal(elapsed);
		out.writeDecimal(error);

		out.writeDecimal(start_cpu);
		out.writeDecimal(start_mem);
	}

	public Step read(DataInputX in) throws IOException {
		super.read(in);
		byte ver = in.readByte();
		this.host = (int) in.readDecimal();
		this.query = in.readText();
		this.elapsed = (int) in.readDecimal();
		this.error = in.readDecimal();

		this.start_cpu = (int) in.readDecimal();
		this.start_mem = in.readDecimal();
		
		return this;
	}

}
