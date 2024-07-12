package whatap.lang.pack.os;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class XosMySlowQueryPack extends AbstractOSListPack {
	public final static int id = HashUtil.hash("id");
	public final static int start_time = HashUtil.hash("start_time");
	public final static int user = HashUtil.hash("user");
	public final static int host = HashUtil.hash("host");
	public final static int schema = HashUtil.hash("schema");	// 210623
	public final static int qc_hit = HashUtil.hash("qc_hit");	// 210623
	public final static int query_time = HashUtil.hash("query_time");
	public final static int lock_time = HashUtil.hash("lock_time");
	public final static int rows_sent = HashUtil.hash("rows_sent");
	public final static int rows_examined = HashUtil.hash("rows_examined");
	public final static int rows_affected =  HashUtil.hash("rows_affected");	// 210623
	public final static int bytes_sent = HashUtil.hash("bytes_sent");	// 210623
	public final static int timestamp = HashUtil.hash("timestamp");
	public final static int sql = HashUtil.hash("sql");
	public final static int thread_id = HashUtil.hash("thread_id");

	public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(id, "id", null);
			handler.process(start_time, "start_time", null);
			handler.process(user, "user", null);
			handler.process(host, "host", null);
			handler.process(schema, "schema", null);
			handler.process(qc_hit, "qc_hit", null);
			handler.process(query_time, "query_time", null);
			handler.process(lock_time, "lock_time", null);
			handler.process(rows_sent, "rows_sent", null);
			handler.process(rows_examined, "rows_examined", null);
			handler.process(rows_affected, "rows_affected", null);
			handler.process(bytes_sent, "bytes_sent", null);
			handler.process(timestamp, "timestamp", null);
			handler.process(sql, "sql", null);
			handler.process(thread_id, "thread_id", null);
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.XOS_MY_SLOW_QUERY;
	}
}
