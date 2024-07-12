/*
 *  @(#) Token
 *  Copyright 2003 the original author or authors. 
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
 *  
 *   @author Paul Kim(sjokim@gmail.com)
 */

package whatap.util.expr.function;

import whatap.util.expr.Function;

import java.util.ArrayList;
import java.util.List;

public class Token implements Function {

	public Object process(List param) throws RuntimeException {
		if (param.size() != 3)
			throw new RuntimeException("formula function invalid param size ");

		try {
			String v = (String) param.get(0);
			String s1 = (String) param.get(1);
			Number idx = (Number) param.get(2);
			ArrayList ist = new ArrayList();
			int i = v.indexOf(s1);
			int start = 0;
			while (i >= 0) {
				String sss = v.substring(start, i);
				ist.add(sss);
				start = i + s1.length();
				i = v.indexOf(s1, start);
			}
			ist.add(v.substring(start));
			return (String) ist.get(idx.intValue());
		} catch (Exception e) {
			throw new RuntimeException("formula function : " + e);
		}
	}

}
