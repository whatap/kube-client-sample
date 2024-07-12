/*
 *  @(#) Avg
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

import java.lang.reflect.Array;
import java.util.List;
import java.util.Map;

public class Avg implements Function {

	public Object process(List param) throws RuntimeException {
		if (param == null)
			return new Double(0);
		double d = 0;
		int cnt = 0;
		try {

			for (int i = 0; i < param.size(); i++) {
				Object o = param.get(i);
				if (o == null)
					continue;
				if (o instanceof List) {
					d += ArrayParamUtil.sum((List) o);
					cnt += ((List) o).size();
				} else if (o instanceof Map) {
					d += ArrayParamUtil.sum((Map) o);
					cnt += ((Map) o).size();
				} else if (o.getClass().isArray()) {
					d += ArrayParamUtil.sumArr(o);
					cnt += Array.getLength(o);
				} else {
					d += ArrayParamUtil.getDouble(o);
					cnt++;
				}
			}

			return cnt == 0 ? 0 : new Double(d / cnt);
		} catch (Exception e) {
			throw new RuntimeException("formula function : " +e);
		}
	}
}
