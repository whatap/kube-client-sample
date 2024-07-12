package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbDeadLockInfoPack extends AbstractDbTablePack {

	public final static int dead_lock_time = HashUtil.hash("dead lock time");
	public final static int dead_lock_id = HashUtil.hash("dead lock id");
	public final static int thread_id1 = HashUtil.hash("thread id1");
	public final static int query_id1 = HashUtil.hash("query id1");
	public final static int host1 = HashUtil.hash("host1");
	public final static int user1 = HashUtil.hash("user1");
	public final static int state1 = HashUtil.hash("state1");
	public final static int query1 = HashUtil.hash("query1");
	public final static int thread_id2 = HashUtil.hash("thread id2");
	public final static int query_id2 = HashUtil.hash("query id2");
	public final static int host2 = HashUtil.hash("host2");
	public final static int user2 = HashUtil.hash("user2");
	public final static int state2 = HashUtil.hash("state2");
	public final static int query2 = HashUtil.hash("query2");

	public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(dead_lock_time, "dead lock time", null);
			handler.process(dead_lock_id, "dead lock id", null);
			handler.process(thread_id1, "thread id1", null);
			handler.process(query_id1, "query id1", null);
			handler.process(host1, "host1", null);
			handler.process(user1, "user1", null);
			handler.process(state1, "state1", null);
			handler.process(query1, "query1", null);
			handler.process(thread_id2, "thread id2", null);
			handler.process(query_id2, "query id2", null);
			handler.process(host2, "host2", null);
			handler.process(user2, "user2", null);
			handler.process(state2, "state2", null);
			handler.process(query2, "query2", null);
		} catch (Exception e) {
		}
	}

	public short getPackType() {
		return PackEnum.DB_DEADLOCK_INFO;
	}
}
