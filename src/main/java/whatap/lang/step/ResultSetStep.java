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

public class ResultSetStep extends Step {
	public int dbc;
	public int sqlhash;
	public int elapsed;
	public int fetch;

	public ResultSetStep() {
	}

	public ResultSetStep(int start_time) {
		this.start_time = start_time;
	}

	public byte getStepType() {
		return StepEnum.RESULTSET;
	}

	public String toString() {
		return "ResultSet";
	}

	public int getElapsed() {
		return elapsed;
	}
	public void write(DataOutputX out) throws IOException {
		super.write(out);
		out.writeDecimal(elapsed);
		out.writeDecimal(fetch);
		out.writeDecimal(dbc);
		out.writeDecimal(sqlhash);
	}

	public Step read(DataInputX in) throws IOException {
		super.read(in);
		this.elapsed = (int) in.readDecimal();
		this.fetch = (int) in.readDecimal();
		this.dbc = (int) in.readDecimal();
		this.sqlhash = (int) in.readDecimal();
		return this;
	}
}
