/*
 *  @(#) Count
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

import whatap.util.ArrayUtil;
import whatap.util.expr.Function;

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class Count implements Function {

	public Object process(List param) throws RuntimeException {
		try {
			int cnt = 0;
			for (int i = 0; i < param.size(); i++) {
				Object o = param.get(i);
				if (o == null)
					continue;
				if (o instanceof List) {
					cnt += ((List) o).size();
				} else if (o instanceof Map) {
					cnt += ((Map) o).size();
				} else if (o.getClass().isArray()) {
					cnt += Array.getLength(o);
				} else {
					cnt++;
				}
			}

			return new Integer(cnt);
		} catch (Exception e) {
			throw new RuntimeException("formula function : " +e);
		}
	}

	public static void main(String[] args) {
		Object o = new float[] { 0, 0 };
		System.out.println(o.getClass().getName());
		System.out.println(ArrayUtil.len(o));
	}
}
