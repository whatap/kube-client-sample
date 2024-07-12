package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbAWRSysStatPack extends AbstractDbTablePack {
	public final static int stat_hash = HashUtil.hash("stat_hash");
	public final static int value = HashUtil.hash("value");

	public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(stat_hash, "stat_hash", null);
			handler.process(value, "value", null);
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.DB_AWR_SYSSTAT;
	}

}
