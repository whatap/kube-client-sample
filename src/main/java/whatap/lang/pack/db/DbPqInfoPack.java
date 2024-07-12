package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbPqInfoPack extends AbstractDbTablePack {
	// oracle
	public final static int sid = HashUtil.hash("sid");
	public final static int qcsid = HashUtil.hash("qcsid");
	public final static int qcinst_id = HashUtil.hash("qcinst_id");

	public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
		handler.process(sid, "sid", null);
		handler.process(qcsid, "qcsid", null);
		handler.process(qcinst_id, "qcinst_id", null);
		} catch (Exception e) {
		}
	}

	public short getPackType() {
		return PackEnum.DB_PQ_INFO;
	}
}
