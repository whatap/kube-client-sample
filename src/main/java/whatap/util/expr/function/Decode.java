/*
 *  @(#) Decode
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

public class Decode implements Function {

	public Object process(List param) throws RuntimeException {
		if (param == null || param.size() < 4 || param.size() % 2 != 0)
			throw new RuntimeException("formula function invalid param size ");
		
		try {
			Object src = param.get(0);
			for (int i = 1; i < param.size(); i += 2) {
				Object o = param.get(i);
				if (src == null) {
					if (o == null)
						return param.get(i + 1);
				} else if (src.equals(o) == true) {
					return param.get(i + 1);
				}
			}
			return param.get(param.size() - 1);
		} catch (Exception e) {
			throw new RuntimeException("formula function : " +e);
		}
	}

}
