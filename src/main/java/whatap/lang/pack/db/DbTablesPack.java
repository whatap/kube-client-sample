package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbTablesPack extends AbstractDbTablePack {

	public final static int table = HashUtil.hash("table");

	// mysql
	public final static int rows = HashUtil.hash("rows");
	public final static int data = HashUtil.hash("data");
	public final static int index = HashUtil.hash("index");
	public final static int file_size = HashUtil.hash("file_size");

	// oracle
	public final static int db = HashUtil.hash("db");	// db name or con name
	public final static int owner = HashUtil.hash("owner"); // oracle
	public final static int mb = HashUtil.hash("MB"); // oracle

	public synchronized static void send(H3<Integer, String, String> handler) {
		try {
			handler.process(table, "table", null);

			handler.process(rows, "rows", null);
			handler.process(data, "data", null);
			handler.process(index, "index", null);
			handler.process(file_size, "file_size", null);

			handler.process(db, "db", null);
			handler.process(owner, "owner", null);
			handler.process(mb, "MB", null);
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.DB_TABLES;
	}
}
