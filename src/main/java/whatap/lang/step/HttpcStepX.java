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

public class HttpcStepX extends Step {
	public final static byte DEFAULT_VERSION=2;
	public static byte VERSION=DEFAULT_VERSION;
	public int url;
	public int host;
	public int port;

	public int elapsed;
	public long error;

	public int status;

	public int start_cpu;
	public long start_mem;
	public int[] stack;
	
	//
	public long stepId;
	public String driver;
	public String originUrl;
	public String param;

	public byte getStepType() {
		return StepEnum.HTTPCALL_X;
	}

	public int getElapsed() {
		return elapsed;
	}

	public void write(DataOutputX out) throws IOException {
		super.write(out);
		out.writeByte(VERSION);
		out.writeDecimal(url);
		out.writeDecimal(elapsed);
		out.writeDecimal(error);
		out.writeDecimal(host);
		out.writeDecimal(port);

		out.writeDecimal(status);
		out.writeDecimal(start_cpu);
		out.writeDecimal(start_mem);
		out.writeArray(stack);
		switch (VERSION) {
		case 1:
			out.writeDecimal(0);
			break;
		case 2:
			out.writeDecimal(this.stepId);
			out.writeText(this.driver);
			out.writeText(this.originUrl);
			out.writeText(this.param);
			break;
		}
	}

	public Step read(DataInputX in) throws IOException {
		super.read(in);
		byte ver = in.readByte();
		
		this.url = (int) in.readDecimal();
		this.elapsed = (int) in.readDecimal();
		this.error = in.readDecimal();
		this.host = (int) in.readDecimal();
		this.port = (int) in.readDecimal();
		this.status = (int) in.readDecimal();

		this.start_cpu = (int) in.readDecimal();
		this.start_mem = in.readDecimal();
		this.stack = in.readArray(stack);

		switch (ver) {
		case 1:
			in.readDecimal();
			break;
		case 2:
			this.stepId=in.readDecimal();
			this.driver=in.readText();
			this.originUrl=in.readText();
			this.param=in.readText();
			break;
		}
		return this;
	}

}
