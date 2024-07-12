/*
 *  @(#) Cast
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

import whatap.util.CastUtil;
import whatap.util.expr.Function;

import java.util.List;

public class _double implements Function {

	public String desc() {
		return "(number)";
	}
	public Object process(List param)  {
		if (param.size() != 1)
			throw new RuntimeException("double() invalid param size ");

		try {
			Object value = param.get(0);
			if (value == null)
				return 0.0D;
			if (value.getClass() == Double.class) {
				return value;
			}
			if (value instanceof Number)
				return ((Number) value).doubleValue();
			
			return CastUtil.cDouble(value);
		} catch (RuntimeException e) {
			throw new RuntimeException("double() : " + e);
		} catch (Exception e) {
			throw new RuntimeException("double() : " + e);
		}
	}

}
