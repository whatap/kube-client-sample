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

public class CompositePack extends AbstractPack {

	public Pack[] pack;

	public short getPackType() {
		return PackEnum.COMPOSITE;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("Composite ");
		sb.append(super.toString());

		return sb.toString();
	}

	public void write(DataOutputX dout) {
		super.write(dout);

		int count = pack == null ? 0 : pack.length;
		dout.writeShort(count);
		for (int i = 0; i < count; i++) {
			dout.writePack(pack[i]);
		}
	}

	public Pack read(DataInputX din) {
		super.read(din);

		int count = din.readShort();
		this.pack = new Pack[count];
		for (int i = 0; i < count; i++) {
			pack[i] = din.readPack();
		}
		return this;
	}
}
