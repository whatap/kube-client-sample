package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbWaitClassPack extends AbstractDbTablePack {
	public final static int name = HashUtil.hash("name");
	public final static int value = HashUtil.hash("value");

	public synchronized static void send(H3<Integer, String, String> handler) {
		try {
			handler.process(name, "name", null);
			handler.process(value, "value", null);
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.DB_WAIT_CLASS;
	}
}
