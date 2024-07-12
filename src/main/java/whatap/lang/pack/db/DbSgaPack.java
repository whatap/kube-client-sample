package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbSgaPack extends AbstractDbTablePack {
	public final static int name = HashUtil.hash("name");
	public final static int value = HashUtil.hash("value");
	public final static int total = HashUtil.hash("total");	// tibero

	public synchronized static void send(H3<Integer, String, String> handler) {
		try {
			handler.process(name, "name", null);
			handler.process(value, "value", null);
			handler.process(total, "total", null);
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.DB_SGA;
	}
}
