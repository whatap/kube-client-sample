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

import java.text.SimpleDateFormat;
import java.util.TimeZone;

public class DateTimeHelper {
	private static class Day {
		public Day(String date, int yyyy, int mm, int dd, String day, long time) {
			this.date = date;
			this.wday = day;
			this.time = time;
			this.yyyy = yyyy;
			this.mm = mm;
			this.dd = dd;
		}

		public final int yyyy;
		public final int mm;
		public final int dd;
		public final String date;
		public final String wday;
		public final long time;
	}

	public static final int MILLIS_PER_SECOND = 1000;
	public static final int MILLIS_PER_MINUTE = 60 * MILLIS_PER_SECOND;
	public static final int MILLIS_PER_FIVE_MINUTE = 5 * 60 * MILLIS_PER_SECOND;
	public static final int MILLIS_PER_TEN_MINUTE = 10 * MILLIS_PER_MINUTE;
	public static final int MILLIS_PER_HOUR = 60 * MILLIS_PER_MINUTE;
	public static final int MILLIS_PER_DAY = 24 * MILLIS_PER_HOUR;

	private DateTimeHelper() {
		try {
			this.BASE_TIME = 946684800000L;
			open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private DateTimeHelper(TimeZone zone) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
			sdf.setTimeZone(zone);
			this.BASE_TIME = sdf.parse("20000101").getTime();
			open();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	static String wday[] = { "Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun" };
	static int mdayLen[] = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

	private long BASE_TIME;
	private Day[][][] table = new Day[100][][];
	private Day[] dateTable = new Day[40000];
	private int LAST_DATE;

	public long getBaseTime() {
		return BASE_TIME;
	}

	private void open() throws Exception {
		long mtime = BASE_TIME;
		int seq = 0;
		int wdayIdx = 5;// 20000101:Saturday
		for (int year = 0; year < 100; year++) {
			boolean isYun = isYun(year);
			table[year] = new Day[12][];
			for (int mm = 0; mm < 12; mm++) {
				int monLen = mdayLen[mm];
				if (mm == 1 && isYun) {
					monLen++;
				}
				table[year][mm] = new Day[monLen];
				for (int dd = 0; dd < monLen; dd++) {
					String yyyyMMdd = String.format("%d%02d%02d", (year + 2000), mm + 1, dd + 1);
					dateTable[seq] = new Day(yyyyMMdd, (year + 2000), mm + 1, dd + 1, wday[wdayIdx], mtime);
					table[year][mm][dd] = dateTable[seq];
					wdayIdx = wdayIdx == 6 ? 0 : wdayIdx + 1;
					seq++;
					mtime += MILLIS_PER_DAY;
				}
			}
		}
		LAST_DATE = seq - 1;
	}

	static boolean isYun(int year) {
		if ((year % 4 == 0 && year % 100 != 0) || year % 400 == 0)
			return true;
		else
			return false;
	}

	public long yyyymmdd(String yyyyMMdd) {
		int year = Integer.parseInt(yyyyMMdd.substring(0, 4));
		int mm = Integer.parseInt(yyyyMMdd.substring(4, 6));
		int dd = Integer.parseInt(yyyyMMdd.substring(6, 8));
		if (year >= 2100)
			return table[99][11][30].time + MILLIS_PER_DAY;
		if (year < 2000)
			return BASE_TIME;
		return table[year - 2000][mm - 1][dd - 1].time;
	}

	public long hhmm(String date) {
		if (date == null)
			return 0;
		int h = Integer.parseInt(date.substring(0, 2));
		int m = Integer.parseInt(date.substring(2, 4));
		return h * MILLIS_PER_HOUR + m * MILLIS_PER_MINUTE;
	}

	public String getWeekDay(String yyyyMMdd) {
		int year = Integer.parseInt(yyyyMMdd.substring(0, 4));
		int mm = Integer.parseInt(yyyyMMdd.substring(4, 6));
		int dd = Integer.parseInt(yyyyMMdd.substring(6, 8));
		if (year >= 2100)
			return table[99][11][30].wday;
		if (year < 2000)
			return table[0][0][0].wday;
		return table[year - 2000][mm - 1][dd - 1].wday;
	}

	public String yyyymmdd(long time) {
		int idx = (int) ((time - BASE_TIME) / MILLIS_PER_DAY);
		if (idx < 0)
			idx = 0;
		return dateTable[idx].date;
	}

	public String weekday(long time) {
		int idx = (int) ((time - BASE_TIME) / MILLIS_PER_DAY);
		if (idx < 0)
			idx = 0;
		return dateTable[idx].wday;
	}

	public String datetime(long time) {
		if (time < BASE_TIME)
			return "20000101 00:00:00";
		int idx = (int) ((time - BASE_TIME) / MILLIS_PER_DAY);
		long dtime = (time - BASE_TIME) % MILLIS_PER_DAY;
		int hh = (int) (dtime / MILLIS_PER_HOUR);
		dtime = (int) (dtime % MILLIS_PER_HOUR);
		int mm = (int) (dtime / MILLIS_PER_MINUTE);
		dtime = (int) (dtime % MILLIS_PER_MINUTE);
		int ss = (int) (dtime / MILLIS_PER_SECOND);
		StringBuffer sb = new StringBuffer();
		sb.append(dateTable[idx].date).append(" ");
		sb.append(mk2(hh)).append(":");
		sb.append(mk2(mm)).append(":");
		sb.append(mk2(ss));
		return sb.toString();
	}

	public String timestamp(long time) {
		if (time < BASE_TIME)
			return "20000101 00:00:00.000";
		int idx = (int) ((time - BASE_TIME) / MILLIS_PER_DAY);
		long dtime = (time - BASE_TIME) % MILLIS_PER_DAY;
		int hh = (int) (dtime / MILLIS_PER_HOUR);
		dtime = (int) (dtime % MILLIS_PER_HOUR);
		int mm = (int) (dtime / MILLIS_PER_MINUTE);
		dtime = (int) (dtime % MILLIS_PER_MINUTE);
		int ss = (int) (dtime / MILLIS_PER_SECOND);
		int sss = (int) (dtime % 1000);
		StringBuffer sb = new StringBuffer();
		sb.append(dateTable[idx].date).append(" ");
		sb.append(mk2(hh)).append(":");
		sb.append(mk2(mm)).append(":");
		sb.append(mk2(ss));
		sb.append(".").append(mk3(sss));
		return sb.toString();
	}

	private String mk2(int n) {
		switch (n) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			return "0" + n;
		default:
			return Integer.toString(n);
		}
	}

	private String mk3(int n) {
		switch (n) {
		case 0:
		case 1:
		case 2:
		case 3:
		case 4:
		case 5:
		case 6:
		case 7:
		case 8:
		case 9:
			return "00" + n;
		default:
			return n < 100 ? "0" + n : Integer.toString(n);
		}
	}

	public String timestampFileName(long time) {
		if (time < BASE_TIME)
			return "20000101_000000_000";
		int idx = (int) ((time - BASE_TIME) / MILLIS_PER_DAY);
		long dtime = (time - BASE_TIME) % MILLIS_PER_DAY;
		int hh = (int) (dtime / MILLIS_PER_HOUR);
		dtime = (int) (dtime % MILLIS_PER_HOUR);
		int mm = (int) (dtime / MILLIS_PER_MINUTE);
		dtime = (int) (dtime % MILLIS_PER_MINUTE);
		int ss = (int) (dtime / MILLIS_PER_SECOND);
		int sss = (int) (dtime % 1000);
		StringBuffer sb = new StringBuffer();
		sb.append(dateTable[idx].date).append("_");
		sb.append(mk2(hh));
		sb.append(mk2(mm));
		sb.append(mk2(ss));
		sb.append("_").append(mk3(sss));
		return sb.toString();
	}

	public String ymdhms(long time) {
		if (time < BASE_TIME)
			return "20000101000000";
		int idx = (int) ((time - BASE_TIME) / MILLIS_PER_DAY);
		long dtime = (time - BASE_TIME) % MILLIS_PER_DAY;
		int hh = (int) (dtime / MILLIS_PER_HOUR);
		dtime = (int) (dtime % MILLIS_PER_HOUR);
		int mm = (int) (dtime / MILLIS_PER_MINUTE);
		dtime = (int) (dtime % MILLIS_PER_MINUTE);
		int ss = (int) (dtime / MILLIS_PER_SECOND);
		StringBuffer sb = new StringBuffer();
		sb.append(dateTable[idx].date);
		sb.append(mk2(hh));
		sb.append(mk2(mm));
		sb.append(mk2(ss));
		return sb.toString();
	}

	public long getYear(long time, int delta) {
		if (time < BASE_TIME)
			return BASE_TIME;
		int idx = (int) ((time - BASE_TIME) / MILLIS_PER_DAY);
		int year = dateTable[idx].yyyy + delta;
		int mm = dateTable[idx].mm;
		int dd = dateTable[idx].dd;
		if (year < 2000)
			return BASE_TIME;
		if (year > 2100)
			return dateTable[LAST_DATE].time;

		long dtime = (time - BASE_TIME) % MILLIS_PER_DAY;

		// return table[year - 2000][mm - 1][dd - 1].time + dtime;
		// 2월29일의 1년전은 2월28일이 나와야 함
		Day[] monthTable = table[year - 2000][mm - 1];
		if (monthTable.length > dd - 1) {
			return monthTable[dd - 1].time + dtime;
		} else {
			return monthTable[monthTable.length - 1].time + dtime;
		}
	}

	public long getMonth(long time, int delta) {
		if (time < BASE_TIME)
			return BASE_TIME;
		int idx = (int) ((time - BASE_TIME) / MILLIS_PER_DAY);
		int year = dateTable[idx].yyyy;
		int mm = dateTable[idx].mm;
		int dd = dateTable[idx].dd;
		delta = delta + mm - 1;
		int deltaYear = delta / 12;
		int deltaMM = delta % 12;
		if (deltaMM < 0) {
			deltaYear--;
			deltaMM = 12 + deltaMM;
		}
		year += deltaYear;
		mm = deltaMM + 1;
		if (year < 2000)
			return BASE_TIME;
		if (year > 2100)
			return dateTable[LAST_DATE].time;

		long dtime = (time - BASE_TIME) % MILLIS_PER_DAY;
		//return table[year - 2000][mm - 1][dd - 1].time + dtime;
		// 31일의 한달전은 30일 혹은 2월28일 
		Day[] monthTable = table[year - 2000][mm - 1];
		if (monthTable.length > dd - 1) {
			return monthTable[dd - 1].time + dtime;
		} else {
			return monthTable[monthTable.length - 1].time + dtime;
		}
	}

	public long getDate(long time, int delta) {
		if (time < BASE_TIME)
			return BASE_TIME;
		int idx = (int) ((time - BASE_TIME) / MILLIS_PER_DAY);
		long dtime = (time - BASE_TIME) % MILLIS_PER_DAY;
		idx += delta;
		if (idx < 0)
			return BASE_TIME;
		if (idx >= LAST_DATE)
			return dateTable[LAST_DATE].time;
		return dateTable[idx].time + dtime;
	}

	public String logtime(long time) {
		if (time < BASE_TIME)
			return "00:00:00.000";
		long dtime = (time - BASE_TIME) % MILLIS_PER_DAY;
		int hh = (int) (dtime / MILLIS_PER_HOUR);
		dtime = (int) (dtime % MILLIS_PER_HOUR);
		int mm = (int) (dtime / MILLIS_PER_MINUTE);
		dtime = (int) (dtime % MILLIS_PER_MINUTE);
		int ss = (int) (dtime / MILLIS_PER_SECOND);
		int sss = (int) (dtime % 1000);
		StringBuffer sb = new StringBuffer();
		sb.append(mk2(hh)).append(":");
		sb.append(mk2(mm)).append(":");
		sb.append(mk2(ss));
		sb.append(".").append(mk3(sss));
		return sb.toString();
	}

	public String hhmmss(long time) {
		if (time < BASE_TIME)
			return "000000";
		long dtime = (time - BASE_TIME) % MILLIS_PER_DAY;
		int hh = (int) (dtime / MILLIS_PER_HOUR);
		dtime = (int) (dtime % MILLIS_PER_HOUR);
		int mm = (int) (dtime / MILLIS_PER_MINUTE);
		dtime = (int) (dtime % MILLIS_PER_MINUTE);
		int ss = (int) (dtime / MILLIS_PER_SECOND);
		return String.format("%02d%02d%02d", hh, mm, ss);
	}

	public String hhmm(long time) {
		if (time < BASE_TIME)
			return "0000";
		long dtime = (time - BASE_TIME) % MILLIS_PER_DAY;
		int hh = (int) (dtime / MILLIS_PER_HOUR);
		dtime = (int) (dtime % MILLIS_PER_HOUR);
		int mm = (int) (dtime / MILLIS_PER_MINUTE);
		return String.format("%02d%02d", hh, mm);
	}

	public int getDateMillis(long time) {
		if (time < BASE_TIME)
			return 0;
		long dtime = (time - BASE_TIME) % MILLIS_PER_DAY;
		return (int) dtime;
	}

	public long getDateStartTime(long time) {
		if (time < BASE_TIME)
			return 0;
		long dtime = (time - BASE_TIME) % MILLIS_PER_DAY;
		return time - dtime;
	}

	public int getHour(long time) {
		return getDateMillis(time) / MILLIS_PER_HOUR;
	}

	public int getMM(long time) {
		int dtime = getDateMillis(time) % MILLIS_PER_HOUR;
		return (dtime / MILLIS_PER_MINUTE);
	}

	public long getTimeUnit(long time) {
		return (time - BASE_TIME);
	}

	public long getDateUnit(long time) {
		return (time - BASE_TIME) / MILLIS_PER_DAY;
	}

	public long getTenMinUnit(long time) {
		return (time - BASE_TIME) / MILLIS_PER_TEN_MINUTE;
	}

	public long getFiveMinUnit(long time) {
		return (time - BASE_TIME) / MILLIS_PER_FIVE_MINUTE;
	}

	public long getMinUnit(long time) {
		return (time - BASE_TIME) / MILLIS_PER_MINUTE;
	}

	public long getHourUnit(long time) {
		return (time - BASE_TIME) / MILLIS_PER_HOUR;
	}

	public long reverseHourUnit(long unit) {
		return (unit * MILLIS_PER_HOUR) + BASE_TIME;
	}

	public long reverseUnit(long unit, long millis) {
		return (unit * millis) + BASE_TIME;
	}

	public long getDateUnit() {
		return getDateUnit(DateUtil.currentTime());
	}

	private static StringKeyLinkedMap<DateTimeHelper> _table = new StringKeyLinkedMap<DateTimeHelper>().setMax(5);

	public static synchronized DateTimeHelper getDateTimeHelper() {
		DateTimeHelper helper = _table.get("GMT");
		if (helper == null) {
			helper = new DateTimeHelper();
			_table.put("GMT", helper);
		}
		return helper;
	}

	public static synchronized DateTimeHelper getDateTimeHelper(TimeZone timezone) {
		DateTimeHelper helper = _table.get(timezone.getID());
		if (helper == null) {
			helper = new DateTimeHelper(timezone);
			_table.put(timezone.getID(), helper);
		}
		return helper;
	}

	public String loghms(long time) {
		if (time < BASE_TIME)
			return "00:00:00";
		long dtime = (time - BASE_TIME) % MILLIS_PER_DAY;
		int hh = (int) (dtime / MILLIS_PER_HOUR);
		dtime = (int) (dtime % MILLIS_PER_HOUR);
		int mm = (int) (dtime / MILLIS_PER_MINUTE);
		dtime = (int) (dtime % MILLIS_PER_MINUTE);
		int ss = (int) (dtime / MILLIS_PER_SECOND);
		StringBuffer sb = new StringBuffer();
		sb.append(mk2(hh)).append(":");
		sb.append(mk2(mm)).append(":");
		sb.append(mk2(ss));
			return sb.toString();
	}

}
