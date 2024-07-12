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
package whatap.lang.value;


import whatap.io.DataInputX;
import whatap.io.DataOutputX;

import java.io.IOException;


public interface Value {

	public void write(DataOutputX out) throws IOException;
	public Value read(DataInputX in) throws IOException;
	public byte getValueType();
	public Object  toJavaObject();
	public int compareTo(Object o);
	public Value copy();
	public boolean isEmpty();
}