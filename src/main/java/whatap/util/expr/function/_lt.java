/*
 *  @(#) Lower
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

// lower than
public class _lt implements Function {
	public String desc() {
		return "(base, list)";
	}
	public Object process(List param) {
		if (param == null || param.size() < 2)
			throw new RuntimeException("lower() invalid param size ");
		try {
			List<Double> out = new ArrayList<Double>();
			double base = ArrayParamUtil.getDouble(param.get(0));

			if(param.size() == 2){
				Object o = param.get(1);
				if (o instanceof List) {
					ArrayParamUtil.lower((List) o, base, out);
				} else if (o instanceof Map) {
					ArrayParamUtil.lower((Map) o, base, out);
				} else if (o.getClass().isArray()) {
					ArrayParamUtil.lowerArr(o, base, out);
				}
			}else {
				int sz = param.size();
				for(int i=1; i<sz; i++){
					double d =ArrayParamUtil.getDouble(param.get(i));
					if(base > d){
						out.add(d);
					}
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
		} catch (RuntimeException e) {
			throw new RuntimeException("lower() : " + e);
		} catch (Exception e) {
			throw new RuntimeException("lower() : " + e);
		}
	}

}
