package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbIndex2Pack extends AbstractDbTablePack {

	public final static int category = HashUtil.hash("category");	// os/sessions/stat/event 등
	public final static int class1 = HashUtil.hash("class");
	public final static int id = HashUtil.hash("id");
	public final static int hash = HashUtil.hash("hash");
	public final static int name = HashUtil.hash("name");
	public final static int weight = HashUtil.hash("weight");
	public final static int unit = HashUtil.hash("unit");

    public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(category, "category", null);
			handler.process(class1, "class", null);
			handler.process(id, "id", null);
			handler.process(hash, "hash", null);
			handler.process(name, "name", null);
			handler.process(weight, "weight", null);
			handler.process(unit, "unit", null);
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.DB_INDEX2;
	}
}
