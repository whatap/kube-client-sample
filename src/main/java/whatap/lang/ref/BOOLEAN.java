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
public class BOOLEAN {
	public BOOLEAN() {
	}
	public BOOLEAN(boolean value) {
		this.value = value;
	}
	public boolean value;
	@Override
	public int hashCode() {
		return value ? 1 : 0;
	}
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof BOOLEAN) {
			BOOLEAN other = (BOOLEAN) obj;
			return this.value == other.value;
		}
		return false;
	}
}
