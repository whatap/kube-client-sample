/*
 *  @(#) Over
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Over implements Function {

	public Object process(List param) throws RuntimeException {
		if (param == null || param.size() != 2)
			throw new RuntimeException("formula function invalid param size ");

		try {
			List<Double> out = new ArrayList<Double>();
			Object o = param.get(0);
			double base = ArrayParamUtil.getDouble(param.get(1));

			if (o instanceof List) {
				ArrayParamUtil.over((List) o, base, out);
			} else if (o instanceof Map) {
				ArrayParamUtil.over((Map) o, base, out);
			} else if (o.getClass().isArray()) {
				ArrayParamUtil.overArr(o, base, out);
			}else{
				double d =ArrayParamUtil.getDouble(o);
				if(base <= d){
					out.add(d);
				}
			}
			switch (out.size()) {
			case 0:
				return null;
			case 1:
				return out.get(0);
			default:
				double[] ov = new double[out.size()];
				for (int i = 0; i < ov.length; i++)
					ov[i] = out.get(i);
				return ov;
			}
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
