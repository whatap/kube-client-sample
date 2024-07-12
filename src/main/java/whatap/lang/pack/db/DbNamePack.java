package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbNamePack extends AbstractDbTablePack {
	public final static int name = HashUtil.hash("name");

    public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(name, "name", null);
		} catch (Exception e) {
		}
	}

	public short getPackType() {
		return PackEnum.DB_NAME;
	}

}
