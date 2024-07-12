package whatap.lang.slog;

import whatap.util.AnsiPrint;

import java.text.ParseException;
import java.util.List;

public class Main {
	public static void main(String[] args) throws ParseException {
		SLog fm = new SLog().append("id", "k12").append("age", 24).append("addr",
				"Seoul SungDongGu SungSu-1Ga 123").append("rate", new Double(0.1201));

		String log =  "[2023-05-16 08:32:34,160 GMT][INFO ][i.w.yard.core.ProfileCore.alert(671)]" + fm;
	
		String l3="[2023/08/01 00:51:18][INFO ][[32mRequest[0m.log(131)] [+] [33mid[0m=tagcount [33mcmd[0m=exist [33mpcode[0m=38 [33mcategory[0m=server_disk [33mstime[0m=\"2023-08-01 00:50:00.000\" [33metime[0m=\"2023-08-01 00:51:00.000\" [33mdur[0m=60,000 [32mrows[0m=1 [32melapsed[0m=0 [32mstatus[0m=200";
		String l2="[2023-08-01 02:19:24,933 GMT][INFO ][[32mRequest[0m.log(131)] [+] [33mid[0m=logsink.mxql [33mcmd[0m=mxqlSearchCount [33mpcode[0m=185 [33mcategory[0m=AppLog [33mstime[0m=\"2023-08-01 02:17:00.000\" [33metime[0m=\"2023-08-01 02:18:00.000\" [33mdur[0m=60,000 [33mfetch[0m=200 [33msearch[0m=[{key=oid,isExcluded=false,ignoreCase=true,val=[{type=equal,value=1792953114,origin=1792953114,ignoreCase=true}]}] [32mrows[0m=0 [32melapsed[0m=0 [32mstatus[0m=200 ";
	String l5="[2023-08-01 02:42:02,540 GMT][INFO ][i.w.y.cache.dbx.DbxRealCounterCache.<init>(59)] DbxRealCounterCache panel_field ==> {MONGODB=cpu, REDIS=used_cpu} ";
		System.out.println(l2);

		l2 = AnsiPrint.erase(l2);
		System.out.println(l2);
		
		SLogParser p = new SLogParser("time", "level", "location");
		List<Field> m = p.parse(l2);
		System.out.println(m);
	
//		SimpleDateFormat d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss,SSS");
//		d.setTimeZone(TimeZone.getTimeZone("GMT"));
//		Date time= d.parse(m.get("time"));
//		System.out.println(new Timestamp(time.getTime()));
	}

}
