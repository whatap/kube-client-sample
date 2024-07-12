package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbSettingsPack extends AbstractDbTablePack {
	public final static int name = HashUtil.hash("name");
	public final static int setting = HashUtil.hash("setting");
	public final static int unit = HashUtil.hash("unit");

    public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(name, "name", null);
			handler.process(setting, "setting", null);
			handler.process(unit, "unit", null);
		} catch (Exception e) {
		}
	}

	public short getPackType() {
		return PackEnum.DB_SETTINGS;
	}

}
