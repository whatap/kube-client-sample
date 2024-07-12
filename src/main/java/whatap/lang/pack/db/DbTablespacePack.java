package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbTablespacePack extends AbstractDbTablePack {
	public final static int con_name = HashUtil.hash("con_name");
	public final static int tablespace = HashUtil.hash("tablespace");
	public final static int datfile = HashUtil.hash("datfile");
	public final static int size = HashUtil.hash("size");
	public final static int used = HashUtil.hash("used");
	//public final static int percent = HashUtil.hash("percent"); // 보내지 말고 front 에서 계산하자.

	public synchronized static void send(H3<Integer, String, String> handler) {
		try {
			handler.process(con_name, "con_name", null);
			handler.process(tablespace, "tablespace", null);
			handler.process(datfile, "datfile", null);
			handler.process(size, "size", null);
			handler.process(used, "used", null);
			//handler.process(percent, "percent", null);
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.DB_TABLESPACE;
	}

}
