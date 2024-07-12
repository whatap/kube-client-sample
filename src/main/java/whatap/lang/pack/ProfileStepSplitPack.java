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

package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.step.Step;
import whatap.util.ArrayUtil;
import whatap.util.DateUtil;

import java.io.IOException;



public class ProfileStepSplitPack extends AbstractPack {


	public long txid;
	public int inx;
	public byte[] steps;
	
	public short getPackType() {
		return PackEnum.PROFILE_STEP_SPLIT;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Step ");
		sb.append(super.toString());
	    sb.append(" time="+DateUtil.timestamp(this.time));
	    sb.append(" inx="+this.inx);
		sb.append(" step_bytes=" + ArrayUtil.len(steps));

		return sb.toString();
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeByte(0);
		dout.writeLong(txid);
		dout.writeDecimal(inx);
		dout.writeBlob(steps);
	}

	public Pack read(DataInputX din)  {
		super.read(din);
		
		byte ver = din.readByte();
		this.txid=din.readLong();
		this.inx=(int) din.readDecimal();
		this.steps = din.readBlob();
		return this;
	}

	public ProfileStepSplitPack setProfile(Step[] steps) throws IOException {
		this.steps = Step.toBytes(steps);
		return this;
	}
}
