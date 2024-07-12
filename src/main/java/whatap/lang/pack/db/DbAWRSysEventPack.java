package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbAWRSysEventPack extends AbstractDbTablePack {
	public final static int event_hash = HashUtil.hash("event_hash");
	public final static int total_waits = HashUtil.hash("total_waits");
	public final static int time_waited = HashUtil.hash("time_waited");
	public final static int total_timeouts = HashUtil.hash("total_timeouts");

	public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(event_hash, "event_hash", null);
			handler.process(total_waits, "total_waits", null);
			handler.process(time_waited, "time_waited", null);
			handler.process(total_timeouts, "total_timeouts", null);
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.DB_AWR_SYSEVENT;
	}

}
