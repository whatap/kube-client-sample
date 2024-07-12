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
import whatap.lang.value.MapValue;
import whatap.lang.value.Value;
import whatap.org.json.JSONObject;

import java.io.IOException;

public class MessageStepX extends Step {

	public String title;
	public String desc;
	public int ctr;
	public MapValue attr;

	public MessageStepX() {
	}

	public MessageStepX(int start_time) {
		this.start_time = start_time;
	}

	public byte getStepType() {
		return StepEnum.MESSAGE_X;
	}

	public String toString() {
		return "MessageStepX-" + title;
	}

	public int getElapsed() {
		return 0;
	}

	public void write(DataOutputX out) throws IOException {
		super.write(out);
		out.writeByte(0);	// version
		out.writeBlob(writeVer0());
	}

	private byte[] writeVer0() {
		DataOutputX o = new DataOutputX();
		o.writeText(title);
		o.writeText(desc);
		o.writeInt(ctr);
		o.writeValue(this.attr);
		return o.toByteArray();
	}

	public Step read(DataInputX in) throws IOException {
		super.read(in);
		byte ver = in.readByte();
		byte[] bytes = in.readBlob();
		if (ver == 0) {
			readVer0(bytes);
		}
		return this;
	}

	private void readVer0(byte[] bytes) {
		DataInputX i = new DataInputX(bytes);
		this.title = i.readText();
		this.desc = i.readText();
		this.ctr = i.readInt();
		Value val = i.readValue();
		if (val instanceof MapValue) {
			this.attr = (MapValue) val;
		}
	}

	public final static int SINGLE_LINE_DISPLAY = 0x00000001;

	public void setCtr(int key) {
		this.ctr = this.ctr | key;
	}

	public void setAttr(String key, Value val) {
		if (this.attr == null) {
			this.attr = new MapValue();
		}
		this.attr.put(key, val);
	}

	public JSONObject ctrToJson() {
		JSONObject o = new JSONObject();
		if (isA(SINGLE_LINE_DISPLAY)) {
			o.put("SINGLE_LINE_DISPLAY", true);
		}
		return o;
	}

	private boolean isA(int k) {
		return (this.ctr & k) != 0;
	}
}