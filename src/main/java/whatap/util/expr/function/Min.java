/*
 *  @(#) Min
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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class Min implements Function {

	public Object process(List param) throws RuntimeException {
		if (param == null || param.size() ==0)
			return new Double(0);
		double d = Double.MAX_VALUE;
		try {

			for (int i = 0; i < param.size(); i++) {
				Object o = param.get(i);
				if (o == null) {
					d = Math.min(d, 0);
				}else if (o instanceof List) {
					d = ArrayParamUtil.min((List) o, d);
				} else if (o instanceof Map) {
					d = ArrayParamUtil.min((Map) o, d);
				} else if (o.getClass().isArray()) {
					d = ArrayParamUtil.minArr(o, d);
				} else {
					d = ArrayParamUtil.getMin(o, d);
				}
			}
            
			return new Double(d);
		} catch (Exception e) {
			throw new RuntimeException("formula function : " +e);
		}
	}

	private double sum(List p) {
		double d = 0;
		for (Iterator i = p.iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof Number) {
				d += ((Number) o).doubleValue();
			}
		}
		return d;
	}

	private double sum(Map p) {
		double d = 0;
		for (Iterator i = p.values().iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof Number) {
				d += ((Number) o).doubleValue();
			}
		}
		return d;
	}

	private double sum(Object p) {
		double d = 0;
		int size = Array.getLength(p);
		for (int i = 0; i < size; i++) {
			Object o = Array.get(p, i);
			if (o instanceof Number) {
				d += ((Number) o).doubleValue();
			}
		}
		return d;
	}
}
