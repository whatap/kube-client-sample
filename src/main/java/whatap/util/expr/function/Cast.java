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

public class Cast implements Function {

	public Object process(List param) throws RuntimeException {
		if (param.size() != 2)
			throw new RuntimeException("formula function invalid param size ");

		try {
			String fcName = (String) param.get(0);
			Object value = param.get(1);
			if (value == null)
				return null;
			if ("string".equalsIgnoreCase(fcName))
				return CastUtil.cString(value);
			if ("int".equalsIgnoreCase(fcName))
				return CastUtil.cInteger(value);
			if ("long".equalsIgnoreCase(fcName))
				return CastUtil.cLong(value);
			if ("double".equalsIgnoreCase(fcName))
				return CastUtil.cDouble(value);
			if ("float".equalsIgnoreCase(fcName))
				return CastUtil.cFloat(value);
			if ("boolean".equalsIgnoreCase(fcName))
				return CastUtil.cBoolean(value);
		} catch (Exception e) {
			throw new RuntimeException("formula function : " +e);
		}
		throw new RuntimeException("formula function unknown casting type : "+param.get(1));
	}

}
