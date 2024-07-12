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

public class MethodStepX extends Step {
	public int hash;
	public int elapsed;
	public int start_cpu;
	public long start_mem;
	public int[] stack;

	public byte getStepType() {
		return StepEnum.METHOD_X;
	}

	public int getElapsed() {
		return elapsed;
	}

	public void write(DataOutputX out) throws IOException {
		super.write(out);
		out.writeByte(0);
		out.writeDecimal(hash);
		out.writeDecimal(elapsed);
		out.writeDecimal(start_cpu);
		out.writeDecimal(start_mem);
		out.writeArray(stack);
	}

	public Step read(DataInputX in) throws IOException {
		super.read(in);
		in.readByte();
		
		this.hash = (int) in.readDecimal();
		this.elapsed = (int) in.readDecimal();
		this.start_cpu = (int) in.readDecimal();
		this.start_mem =  in.readDecimal();
		this.stack = in.readArray(stack);

		return this;
	}
}
