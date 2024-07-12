/*
 *  @(#) Function
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

import whatap.util.FormatUtil;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class ArrayParamUtil {

	public static String str(List p, String format) {
		StringBuffer sb = new StringBuffer();
		for (Iterator i = p.iterator(); i.hasNext();) {
			Object o = i.next();
			if (sb.length() > 0)
				sb.append(',');
			if (format == null)
				sb.append(o);
			else
				sb.append(FormatUtil.print(o, format));
		}
		return sb.toString();
	}

	public static String strArr(Object p, String format) {
		StringBuffer sb = new StringBuffer();
		int size = Array.getLength(p);
		for (int i = 0; i < size; i++) {
			Object o = Array.get(p, i);
			if (i > 0)
				sb.append(',');
			if (format == null)
				sb.append(o);
			else
				sb.append(FormatUtil.print(o, format));
		}
		return sb.toString();
	}

	public static double sum(List p) {
		double d = 0;
		for (Object o : p) {
			if (o instanceof Number) {
				d += ((Number) o).doubleValue();
			}
		}
		return d;
	}

	public static double sum(Map p) {
		double d = 0;
		for (Object o : p.values()) {
			if (o instanceof Number) {
				d += ((Number) o).doubleValue();
			}
		}
		return d;
	}

	public static double sumArr(Object p) {
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

	public static double getDouble(Object o) {
		if (o instanceof Number) {
			return ((Number) o).doubleValue();
		}
		return 0;
	}

	public static double max(List p, double v) {
		double d = v;
		for (Iterator i = p.iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof Number) {
				d = Math.max(((Number) o).doubleValue(), d);
			}
		}
		return d;
	}

	public static double max(Map p, double v) {
		double d = 0;
		for (Iterator i = p.values().iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof Number) {
				d = Math.max(((Number) o).doubleValue(), d);
			}
		}
		return d;
	}

	public static double maxArr(Object p, double v) {
		double d = 0;
		int size = Array.getLength(p);
		for (int i = 0; i < size; i++) {
			Object o = Array.get(p, i);
			if (o instanceof Number) {
				d = Math.max(((Number) o).doubleValue(), d);
			}
		}
		return d;
	}
public static void main(String[] args) {
	System.out.println(maxArr("aa",10));
}
	public static double getMax(Object o, double v) {
		if (o instanceof Number) {
			return Math.max(((Number) o).doubleValue(), v);
		}
		return v;
	}

	// ////////////////////
	public static double min(List p, double v) {
		double d = v;
		for (Iterator i = p.iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof Number) {
				d = Math.min(((Number) o).doubleValue(), d);
			}
		}
		return d;
	}

	public static double min(Map p, double v) {
		double d = v;
		for (Object o : p.values()) {
			if (o instanceof Number) {
				d = Math.min(((Number) o).doubleValue(), d);
			}
		}
		return d;
	}

	public static double minArr(Object p, double v) {
		double d = v;
		int size = Array.getLength(p);
		for (int i = 0; i < size; i++) {
			Object o = Array.get(p, i);
			if (o instanceof Number) {
				d = Math.min(((Number) o).doubleValue(), d);
			}
		}
		return d;
	}

	public static double getMin(Object o, double v) {
		if (o instanceof Number) {
			return Math.min(((Number) o).doubleValue(), v);
		}
		return v;
	}

	// ///////////
	public static void over(List p, double base, List out) {
		for (Iterator i = p.iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof Number) {
				double d = ((Number) o).doubleValue();
				if (d >= base)
					out.add(d);
			}
		}

	}

	public static void over(Map p, double base, List out) {
		for (Iterator i = p.values().iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof Number) {
				double d = ((Number) o).doubleValue();
				if (d >= base)
					out.add(d);
			}
		}
	}

	public static void overArr(Object p, double base, List out) {
		int size = Array.getLength(p);
		for (int i = 0; i < size; i++) {
			Object o = Array.get(p, i);
			if (o instanceof Number) {
				double d = ((Number) o).doubleValue();
				if (d >= base)
					out.add(d);
			}
		}

	}

	// ///////////
	public static void lower(List p, double base, List out) {
		for (Iterator i = p.iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof Number) {
				double d = ((Number) o).doubleValue();
				if (d <= base)
					out.add(d);
			}
		}

	}

	public static void lower(Map p, double base, List out) {
		for (Iterator i = p.values().iterator(); i.hasNext();) {
			Object o = i.next();
			if (o instanceof Number) {
				double d = ((Number) o).doubleValue();
				if (d <= base)
					out.add(d);
			}
		}
	}

	public static void lowerArr(Object p, double base, List out) {
		int size = Array.getLength(p);
		for (int i = 0; i < size; i++) {
			Object o = Array.get(p, i);
			if (o instanceof Number) {
				double d = ((Number) o).doubleValue();
				if (d <= base)
					out.add(d);
			}
		}

	}
}
