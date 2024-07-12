/*
 *  @(#) Substring
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

import java.util.List;





public class Substring implements Function {

	public Object process(List param) throws RuntimeException {
		if (param.size() != 2 && param.size() != 3)
			throw new RuntimeException("formula function invalid param size ");

		try {
			String p1 = (String) param.get(0);
			Number p2 = (Number) param.get(1);
			if (param.size() == 2) {
				return p1.substring(p2.intValue());
			}
			Number p3 = (Number) param.get(2);
			return p1.substring(p2.intValue(), p3.intValue());
		} catch (Exception e) {
			throw new RuntimeException("formula function : " +e);
		}
	}

}
