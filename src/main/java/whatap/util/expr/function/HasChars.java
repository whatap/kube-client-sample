/*
 *  @(#) HasChars
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





public class HasChars implements Function {

	public Object process(List param) throws RuntimeException {
		if (param.size() != 2)
			throw new RuntimeException("formula function invalid param size ");

		try {
			String str = (String) param.get(0);
			String chars = (String) param.get(1);
			if (str == null || chars == null || "".equals(chars))
				return new Boolean(false);
			char a_ch[] = chars.toCharArray();
			char a_str[] = str.toCharArray();
			int a_str_len = a_str.length;
			int a_ch_len = a_ch.length;
			for (int i = 0; i < a_str_len; i++)
				for (int j = 0; j < a_ch_len; j++)
					if (a_str[i] == a_ch[j])
						return new Boolean(true);
			return new Boolean(false);
		} catch (Exception e) {
			return new Boolean(false);
		}
	}

}
