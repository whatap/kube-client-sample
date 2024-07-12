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
package whatap.lang;

import whatap.util.ClassUtil;
import whatap.util.IntKeyMap;
import whatap.util.StringIntMap;

public class ChildTypes {
	public final static byte THREAD_RUNNABLE = 1;
	public final static byte THREAD_CALLABLE = 2;

	public static IntKeyMap<String> values = ClassUtil.getConstantValueMap(ChildTypes.class, Byte.TYPE);
	public static StringIntMap names = ClassUtil.getConstantKeyMap(ChildTypes.class, Byte.TYPE);

    
}
