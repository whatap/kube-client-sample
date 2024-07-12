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
package whatap.lang.ref;
import whatap.io.DataOutputX;
public class LONG16 {
	public LONG16() {
	}
	public LONG16(long high, long low) {
		this.high = high;
		this.low = low;
	}
	public long high;
	public long low;
	public byte[] toBytes() {
		byte[] dest = new byte[16];
		DataOutputX.set(dest, 0, DataOutputX.toBytes(high));
		DataOutputX.set(dest, 8, DataOutputX.toBytes(low));
		return dest;
	}
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (high ^ (high >>> 32));
		result = prime * result + (int) (low ^ (low >>> 32));
		return result;
	}
	public boolean equals(Object obj) {
		if (obj instanceof LONG16) {
			LONG16 other = (LONG16) obj;
			return this.high == other.high && this.low == other.low;
		}
		return false;
	}
}
