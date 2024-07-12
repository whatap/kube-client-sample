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

package whatap.util;

import whatap.lang.ref.LONG;
import whatap.lang.value.DecimalValue;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FormatUtil {

	public static String print(Object o, String format) {
		if (o == null)
			return "";
		if (format == null)
			return o.toString();
		try {
			if (o instanceof Date) {
				return new SimpleDateFormat(format).format((Date) o);
			} else if (o instanceof Number || o instanceof BigDecimal) {
				return new DecimalFormat(format).format(o);
			} else if (o instanceof DecimalValue) {
				return new DecimalFormat(format).format(new Long(((DecimalValue) o).value));
			}
		} catch (Throwable e) {
		}
		return o.toString();
	}

	private static char[] unit = { 'b', 'k', 'm', 'g', 't', 'p' };

	public static String prtMem(double mem) {
		int x = 0;
		for (x = 0; mem >= 1024 && x < unit.length; x++) {
			mem /= 1024;
		}
		if (x < 2)
			return print(mem, "#,##0") + unit[x];
		else
			return print(mem, "#,##0.0") + unit[x];
	}

	public static String prtNet(double mem) {
		int x = 0;
		for (x = 0; mem >= 1024 && x < unit.length; x++) {
			mem /= 1024;
		}
		switch (x) {
		case 0:
			return print(mem, "#,##0");
		case 1:
			return print(mem, "#,##0") + unit[x];
		default:
			return print(mem, "#,##0.0") + unit[x];
		}
	}

	public static String str3(long s) {
		if (s < 0)
			return Long.toString(s);
		if (s == 0)
			return "000";
		if (s < 10)
			return "00" + s;
		if (s < 100)
			return "0" + s;
		return Long.toString(s);
	}

	public static String str2(long s) {
		if (s < 0)
			return Long.toString(s);
		if (s == 0)
			return "00";
		if (s < 10)
			return "0" + s;
		return Long.toString(s);
	}

	static DecimalFormat fmt2 = new DecimalFormat("#,##0.0#");
	static DecimalFormat fmt1 = new DecimalFormat("#,##0.0");
	static DecimalFormat fmt0 = new DecimalFormat("#,##0");

	public static String prt2(double v) {
		synchronized (fmt2) {
			return fmt2.format(v);
		}
	}

	public static String prt2(double[] v) {
		StringBuffer sb = new StringBuffer();
		synchronized (fmt2) {
			for (int i = 0; i < v.length; i++) {
				if (i > 0)
					sb.append(",");
				sb.append(fmt2.format(v[i]));

			}
		}
		return sb.toString();
	}

	public static String prt2(float v) {
		synchronized (fmt2) {
			return fmt2.format(v);
		}
	}

	public static String prt2(float[] v) {
		StringBuffer sb = new StringBuffer();
		synchronized (fmt2) {
			for (int i = 0; i < v.length; i++) {
				if (i > 0)
					sb.append(",");
				sb.append(fmt2.format(v[i]));

			}
		}
		return sb.toString();
	}

	public static String prt1(double v) {
		synchronized (fmt1) {
			return fmt1.format(v);
		}
	}

	public static String prt1(double[] v) {
		StringBuffer sb = new StringBuffer();
		synchronized (fmt1) {
			for (int i = 0; i < v.length; i++) {
				if (i > 0)
					sb.append(",");
				sb.append(fmt1.format(v[i]));

			}
		}
		return sb.toString();
	}

	public static String prt1(float v) {
		synchronized (fmt1) {
			return fmt1.format(v);
		}
	}

	public static String prt1(float[] v) {
		StringBuffer sb = new StringBuffer();
		synchronized (fmt1) {
			for (int i = 0; i < v.length; i++) {
				if (i > 0)
					sb.append(",");
				sb.append(fmt1.format(v[i]));

			}
		}
		return sb.toString();
	}

	public static String prt0(double v) {
		synchronized (fmt0) {
			return fmt0.format(v);
		}
	}

	public static String prt0(float v) {
		synchronized (fmt0) {
			return fmt0.format(v);
		}
	}

	public static String prt0(float[] v) {
		StringBuffer sb = new StringBuffer();
		synchronized (fmt2) {
			for (int i = 0; i < v.length; i++) {
				if (i > 0)
					sb.append(",");
				sb.append(fmt0.format(v[i]));

			}
		}
		return sb.toString();
	}

	public static String prt0(double[] v) {
		StringBuffer sb = new StringBuffer();
		synchronized (fmt2) {
			for (int i = 0; i < v.length; i++) {
				if (i > 0)
					sb.append(",");
				sb.append(fmt0.format(v[i]));

			}
		}
		return sb.toString();
	}

	public static String prt0(int[] v) {
		StringBuffer sb = new StringBuffer();
		synchronized (fmt2) {
			for (int i = 0; i < v.length; i++) {
				if (i > 0)
					sb.append(",");
				sb.append(fmt0.format(v[i]));

			}
		}
		return sb.toString();
	}

	public static String prt0(long[] v) {
		StringBuffer sb = new StringBuffer();
		synchronized (fmt2) {
			for (int i = 0; i < v.length; i++) {
				if (i > 0)
					sb.append(",");
				sb.append(fmt0.format(v[i]));

			}
		}
		return sb.toString();
	}

	public static String prt0(long v) {
		synchronized (fmt0) {
			return fmt0.format(v);
		}
	}

	public static String p000(int i) {
		if (i < 10)
			return "00" + i;
		if (i < 100)
			return "0" + i;
		return Integer.toString(i);
	}

	public static String period(long time) {
	
		LONG ms = new LONG(time);
		int d = calc(ms,DateUtil.MILLIS_PER_DAY);
		int h =calc(ms,DateUtil.MILLIS_PER_HOUR);
		int m =calc(ms,DateUtil.MILLIS_PER_MINUTE);
		int s = calc(ms,DateUtil.MILLIS_PER_SECOND);
		StringBuilder sb = new StringBuilder();
		fmt(sb, d, "d");
		fmt(sb, h, "h");
		fmt(sb, m, "m");
		fmt(sb, s, "s");
		fmt(sb, ms.value, "ms");		
		return sb.toString();
	}
	private static void fmt(StringBuilder sb, long d, String unit) {
		if(d>0) {
			if(sb.length() >0) {
				sb.append(':');
			}
			sb.append(d).append(unit);
		}
	}

	private static int calc(LONG ms, long unit) {
		int c = (int)(ms.value/unit);
		ms.value = ms.value% unit;
		return c;
	}

}
