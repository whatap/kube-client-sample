/*
 *  @(#) In
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


import java.util.List;

import whatap.util.CastUtil;
import whatap.util.expr.Function;

public class In implements Function {

	public Object process(List param) throws RuntimeException {
		if (param == null || param.size() < 2)
			throw new RuntimeException("formula function invalid param size ");
		try {
			Object src = param.get(0);
			if (src == null) {
				return new Boolean(false);
			}
			
			
			if (src instanceof Number) {
				double dblValue = ((Number) src).doubleValue();
				for (int i = 1; i < param.size(); i++) {
					Object o = param.get(i);
					if (dblValue == CastUtil.cdouble(o)) {
						return new Boolean(true);
					}
				}

			} else {
				for (int i = 1; i < param.size(); i++) {
					Object o = param.get(i);
					if (src.equals(o) == true) {
						return new Boolean(true);
					}
				}
			}
			return new Boolean(false);
		} catch (Exception e) {
			throw new RuntimeException("formula function : " +e);
		}
	}

}
