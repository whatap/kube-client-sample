/*
 *  @(#) HasStrAny
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


public class HasStrAny implements Function {
	final static private Boolean TRUE = new Boolean(true);
	final static private Boolean FALSE = new Boolean(false);

	public Object process(List param) throws RuntimeException {
		if (param.size() < 2)
			return new Boolean(false);
		try {
			String str = (String) param.get(0);

			if (str != null)
				for (int i = 1; i < param.size(); i++) {
					String chars = (String) param.get(i);
					if (chars == null)
						continue;
					if (str.indexOf(chars) >= 0)
						return TRUE;
				}
			return FALSE;
		} catch (Exception e) {
			return FALSE;
		}
	}

}
