package whatap.util;

import java.util.Random;

public class WorkingDate {

	public static void main(String[] args) {
		int tot_dt = 110;
		long from_tm = DateUtil.yyyymmdd("20170701");
		long to_tm = DateUtil.yyyymmdd("20180430");

		long delta = (to_tm - from_tm) / DateUtil.MILLIS_PER_DAY;

		System.out.println(delta);

		long tm = from_tm;
		Random r = new Random();
		for (int i = 0; i < 110; i++) {
			tm += ((i % 2 == 0 ? r.nextInt(5) : 0) + 1) * DateUtil.MILLIS_PER_DAY;
			while (isHDay(tm)) {
				tm += DateUtil.MILLIS_PER_DAY;
			}

			String yyyymmdd = DateUtil.format(tm, "yyyy-MM-dd");
			String wday = DateUtil.helper.getWeekDay(DateUtil.yyyymmdd(tm));

			System.out.println( yyyymmdd + " <" + i+">");
			if (i % 5 == 0)
				System.out.println("");
		}

	}

	static StringSet hday = new StringSet();
	static {
		hday.put("20170127");
		hday.put("20170130");
		hday.put("20170501");
		hday.put("20170503");
		hday.put("20170504");
		hday.put("20170505");
		hday.put("20170605");
		hday.put("20170606");
		hday.put("20170815");
		hday.put("20171002");
		hday.put("20171003");
		hday.put("20171004");
		hday.put("20171005");
		hday.put("20171225");

		hday.put("20180101");
		hday.put("20180215");
		hday.put("20180216");
		hday.put("20180301");
		hday.put("20180501");
		hday.put("20180507");
		hday.put("20180522");
	}

	public static boolean isHDay(long tm) {
		String yyyymmdd = DateUtil.yyyymmdd(tm);
		String wday = DateUtil.helper.getWeekDay(yyyymmdd);
		if (wday.equals("Sat")) {
			return true;
		} else if (wday.equals("Sun")) {
			return true;
		}
		return hday.hasKey(yyyymmdd);
	}

}
