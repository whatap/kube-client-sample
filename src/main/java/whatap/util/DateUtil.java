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
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
/**
 * Agent에서는 일단 기본은 GMT로 동작해야한다.
 * @author Paul Kim
 */
public class DateUtil {
	public static final long MILLIS_PER_SECOND = 1000;
	public static final long MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
	public static final long MILLIS_PER_FIVE_MINUTE = 5 * 60 * MILLIS_PER_SECOND;
	public static final long MILLIS_PER_TEN_MINUTE = 10 * MILLIS_PER_MINUTE;
	public static final long MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
	public static final long MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;
	public static final int SECONDS_PER_DAY = (int) (MILLIS_PER_DAY / 1000);
	public final static DateTimeHelper helper = DateTimeHelper.getDateTimeHelper();
	public static final float MICROS_PER_SECOND = 1000000;
	private static StringKeyLinkedMap<SimpleDateFormat> parsers = new StringKeyLinkedMap<SimpleDateFormat>() {
		protected SimpleDateFormat create(String pattern) {
			SimpleDateFormat sdf= new SimpleDateFormat(pattern);
			sdf.setTimeZone(TimeZone.getTimeZone("GMT"));
            return sdf;
		};
	}.setMax(100);

	public static String datetime(long time) {
		return helper.datetime(time);
	}

	public static String timestamp(long time) {
		return helper.timestamp(time);
	}

	public static String yyyymmdd(long time) {
		return helper.yyyymmdd(time);
	}

	public static String weekday(long time) {
		return helper.weekday(time);
	}

	public static long getDateUnit() {
		return helper.getDateUnit();
	}

	public static long getDateUnit(long time) {
		return helper.getDateUnit(time);
	}

	public static String ymdhms(long time) {
		return helper.yyyymmdd(time) + helper.hhmmss(time);
	}

	public static String hhmmss(long time) {
		return helper.hhmmss(time);
	}

	public static String hhmm(long now) {
		return helper.hhmm(now);
	}

	public static String yyyymmdd() {
		return helper.yyyymmdd(DateUtil.currentTime());
	}

	public static String getLogTime() {
		return helper.logtime(DateUtil.currentTime());
	}

	public static String getLogTime(long time) {
		return helper.logtime(time);
	}

	public static long yyyymmdd(String date) {
		return helper.yyyymmdd(date);
	}

	public static long hhmm(String date) {
		return helper.hhmm(date);
	}

	public static long getTime(String date, String format) {
		if (format.equals("yyyyMMdd"))
			return helper.yyyymmdd(date);
		try {
			SimpleDateFormat sdf = parsers.intern(format);
			synchronized (sdf) {
				return sdf.parse(date).getTime();
			}
		} catch (ParseException e) {
			return 0;
		}
	}

	
	public static String format(long stime, String format) {
		if (format.equals("yyyyMMdd"))
			return helper.yyyymmdd(stime);
		SimpleDateFormat sdf = parsers.intern(format);
		synchronized (sdf) {
			return sdf.format(new Date(stime));
		}
	}

	public static String format(long stime, String format, Locale locale) {
		if (format.equals("yyyyMMdd"))
			return helper.yyyymmdd(stime);
		SimpleDateFormat sdf = parsers.intern(format + locale.getCountry());
		synchronized (sdf) {
			return sdf.format(new Date(stime));
		}
	}

	public static long parse(String date, String format) {
		if (format.equals("yyyyMMdd"))
			return helper.yyyymmdd(date);
		SimpleDateFormat sdf = parsers.intern(format);
		synchronized (sdf) {
			try {
				return sdf.parse(date).getTime();
			} catch (ParseException e) {
				return helper.getBaseTime();
			}
		}
	}

	public static boolean isSameDay(Date date, Date date2) {
		return helper.getDateUnit(date.getTime()) == helper.getDateUnit(date2.getTime());
	}

	public static boolean isToday(long time) {
		return helper.getDateUnit(time) == helper.getDateUnit(DateUtil.currentTime());
	}

	public static int getHour(Date date) {
		return helper.getHour(date.getTime());
	}

	public static int getMin(Date date) {
		return helper.getMM(date.getTime());
	}

	public static int getHour(long time) {
		return helper.getHour(time);
	}

	public static int getMin(long time) {
		return helper.getMM(time);
	}

	public static String timestamp() {
		return helper.timestamp(DateUtil.currentTime());
	}

	public static String timestampFileName() {
		return helper.timestampFileName(DateUtil.currentTime());
	}

	public static int getDateMillis(long time) {
		return helper.getDateMillis(time);
	}

	public static long getDateStartTime(long time) {
		return helper.getDateStartTime(time);
	}

	public static long getTimeUnit(long time) {
		return helper.getTimeUnit(time);
	}

	public static long getHourUnit(long time) {
		return helper.getHourUnit(time);
	}

	public static long getTenMinUnit(long time) {
		return helper.getTenMinUnit(time);
	}

	public static long getFiveMinUnit(long time) {
		return helper.getFiveMinUnit(time);
	}

	public static long getMinUnit(long time) {
		return helper.getMinUnit(time);
	}

	public static long reverseHourUnit(long unit) {
		return helper.reverseHourUnit(unit);
	}

	public static long reverseUnit(long unit, long millis) {
		return helper.reverseUnit(unit, millis);
	}

	public static long now() {
		return DateUtil.currentTime();
	}

	public static long systime() {
		return System.currentTimeMillis();
	}

	public static long getYear(long time, int delta) {
		return helper.getYear(time, delta);
	}

	public static long getMonth(long time, int delta) {
		return helper.getMonth(time, delta);
	}

	public static long getDate(long time, int delta) {
		return helper.getDate(time, delta);
	}

	public static void main(String[] args) throws ParseException {
		System.out.println(new Date());
		long time = DateUtil.currentTime();
		String s =timestamp(time);
		System.out.println(s);
		long t2 =DateUtil.getTime(s, "yyyyMMdd HH:mm:ss.SSS");
		System.out.println("t1="+time);
		System.out.println("t2="+t2);
	
	}

	private static long delta;

	public static long currentTime() {
		return System.currentTimeMillis() + delta;
	}

	public static long setServerTime(long time, double syncfactor) {
		long now = System.currentTimeMillis();
		delta = time - now;
		if (delta != 0) {
			delta = (long) (delta * syncfactor);
		}
		return delta;
	}

	public static long getServerDelta() {
		return delta;
	}

	public static long sinceMillis(long lastLogDebugPrint) {
		return now() - lastLogDebugPrint;
	}

	public static long nanoToMillis() {
		return System.nanoTime()/1000000;
	}
}
