/*
 *  Copyright 2015 the original author or authors. 
 *  @https://github.com/scouter-project/scouter
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
 */
package whatap.lang.conf;

import whatap.lang.value.BooleanValue;
import whatap.lang.value.DecimalValue;
import whatap.lang.value.DoubleValue;
import whatap.lang.value.FloatValue;
import whatap.lang.value.NullValue;
import whatap.lang.value.TextValue;
import whatap.lang.value.Value;
import whatap.org.json.JSONObject;
import whatap.util.ArrayUtil;
import whatap.util.StringKeyLinkedMap;
import whatap.util.StringUtil;

import java.io.StringReader;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
public class ConfigValueUtil {
	public static Properties replaceSysProp(Properties temp) {
		Properties p = new Properties();
		Map<Object, Object> args = new HashMap<Object, Object>();
		args.putAll(System.getenv());
		args.putAll(System.getProperties());
		p.putAll(args);
		p.remove("whatap.env");
		Iterator<Object> itr = temp.keySet().iterator();
		while (itr.hasNext()) {
			String key = (String) itr.next();
			String value = (String) temp.get(key);
			p.put(key, new whatap.util.ParamText(StringUtil.trim(value)).getText(args));
		}
		String whatapEnv = System.getenv("whatap.env");
		if (whatapEnv != null) {
			try {
				Properties ee = new Properties();
				ee.load(new StringReader(whatapEnv));
				p.putAll(ee);
			} catch (Throwable e) {
			}
		}
		return p;
	}
	
	public static StringKeyLinkedMap<Object> getConfigDefault(Object o) {
		StringKeyLinkedMap<Object> map = new StringKeyLinkedMap<Object>();
		Field[] fields = o.getClass().getFields();
		for (int i = 0; i < fields.length; i++) {
			int mod = fields[i].getModifiers();
			if (Modifier.isStatic(mod) == false && Modifier.isPublic(mod)) {
				try {
					String name = fields[i].getName();
					Object value = fields[i].get(o);
					map.put(name, value);
				} catch (Exception e) {
				}
			}
		}
		return map;
	}
	public static Value toValue(Object o) {
		if (o == null)
			return new NullValue();
		if (o instanceof Float) {
			return new FloatValue(((Float) o).floatValue());
		}
		if (o instanceof Double) {
			return new DoubleValue(((Double) o).doubleValue());
		}
		if (o instanceof Number) {
			return new DecimalValue(((Number) o).longValue());
		}
		if (o instanceof Boolean) {
			return new BooleanValue(((Boolean) o).booleanValue());
		}
		if (o.getClass().isArray()) {
			String s = ArrayUtil.toString(o);
			return new TextValue(s.substring(1, s.length() - 1));
		}
		return new TextValue(o.toString());
	}
	public static void jsonToSystemProperties(String opts) {
		try {
			if (StringUtil.isEmpty(opts))
				return;
			JSONObject jo = new JSONObject(opts);
			Iterator<String> itr = jo.keys();
			while (itr.hasNext()) {
				String key = itr.next();
				String value = jo.optString(key);
				System.out.println(key+"="+value);
				System.setProperty(key, value);
			}
		} catch (Throwable e) {
		}
	}
}
