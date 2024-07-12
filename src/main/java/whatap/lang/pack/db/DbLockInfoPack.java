package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbLockInfoPack extends AbstractDbTablePack {

	// oracle lock mode (holder_mode, waiter_mode)
	/* for info
	String [] lock_mode = {
			"NONE",	// 0 - lock requested but not yet obtained
			"NULL",	// 1 - no permissions
			"SS",	// 2 - ROW_S: Row share lock. concurrent read.
			"SX",	// 3 - ROW_X: Row exclusive table lock. concurrent write.
			"S",	// 4 - SHARE: Share table lock. protected read.
			"SSX",	// 5 - S/ROW_X: Share row exclusive table lock. protected write.
			"X"		// 6 - Exclusive: Exclusive table lock. exclusive access.
	};
	*/

	public static class Oracle {
		public final static int inst_no = HashUtil.hash("inst_no");	// 190306
		public final static int lock_type = HashUtil.hash("lock type");
		public final static int holder_pid = HashUtil.hash("holder pid");	// holder_sid
		public final static int holder_mode = HashUtil.hash("holder mode");
		public final static int waiter_pid = HashUtil.hash("waiter pid");	// waiter_sid
		public final static int waiter_mode = HashUtil.hash("waiter mode");
		public final static int id1 = HashUtil.hash("id1");
		public final static int id2 = HashUtil.hash("id2");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(inst_no, "inst_no", null);
				handler.process(lock_type, "lock type", null);
				handler.process(holder_pid, "holder pid", null);
				handler.process(holder_mode, "holder mode", null);
				handler.process(waiter_pid, "waiter pid", null);
				handler.process(waiter_mode, "waiter mode", null);
				handler.process(id1, "id1", null);
				handler.process(id2, "id2", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Pg {
		public final static int lock_type = HashUtil.hash("lock type");
		public final static int holder_pid = HashUtil.hash("holder pid");
		public final static int holder_mode = HashUtil.hash("holder mode");
		public final static int waiter_pid = HashUtil.hash("waiter pid");
		public final static int waiter_mode = HashUtil.hash("waiter mode");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(lock_type, "lock type", null);
				handler.process(holder_pid, "holder pid", null);
				handler.process(holder_mode, "holder mode", null);
				handler.process(waiter_pid, "waiter pid", null);
				handler.process(waiter_mode, "waiter mode", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Mysql {
		public final static int holder_pid = HashUtil.hash("holder pid");
		public final static int holder_mode = HashUtil.hash("holder mode");
		public final static int waiter_pid = HashUtil.hash("waiter pid");
		public final static int waiter_mode = HashUtil.hash("waiter mode");
		public final static int holder_type = HashUtil.hash("holder type");
		public final static int holder_state = HashUtil.hash("holder state");
		public final static int holder_table = HashUtil.hash("holder table");
		public final static int holder_index = HashUtil.hash("holder index");
		public final static int waiter_type = HashUtil.hash("waiter type");
		public final static int waiter_state = HashUtil.hash("waiter state");
		public final static int waiter_table = HashUtil.hash("waiter table");
		public final static int waiter_index = HashUtil.hash("waiter index");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(holder_pid, "holder pid", null);
				handler.process(holder_mode, "holder mode", null);
				handler.process(waiter_pid, "waiter pid", null);
				handler.process(waiter_mode, "waiter mode", null);
				handler.process(holder_type, "holder type", null);
				handler.process(holder_state, "holder state", null);
				handler.process(holder_table, "holder table", null);
				handler.process(holder_index, "holder index", null);
				handler.process(waiter_type, "waiter type", null);
				handler.process(waiter_state, "waiter state", null);
				handler.process(waiter_table, "waiter table", null);
				handler.process(waiter_index, "waiter index", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Mssql {
		public final static int holder_pid = HashUtil.hash("holder pid");
		public final static int waiter_pid = HashUtil.hash("waiter pid");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(holder_pid, "holder pid", null);
				handler.process(waiter_pid, "waiter pid", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Cubrid {
		// holder/waiter pid 로 되어 있지만, pid 가 아니고 tranindex 이다.
		public final static int holder_pid = HashUtil.hash("holder pid");
		public final static int waiter_pid = HashUtil.hash("waiter pid");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(holder_pid, "holder pid", null);
				handler.process(waiter_pid, "waiter pid", null);
			} catch (Exception e) {
			}
		}
	}

	/*
	Tibero Lock Mode that the Lock owner owns.
	0: None
	1: Row-Shared(RS)
	2: Row-Exclusive(RX)
	3: Shared(S)
	4: Shared-Row-Exclusive(SRX)
	5: Exclusive(X)
	6: Pin (Special mode for TX-wait)
	*/
	public static class Tibero {
		public final static int inst_no = HashUtil.hash("inst_no");
		public final static int lock_type = HashUtil.hash("lock type");
		public final static int holder_pid = HashUtil.hash("holder pid");
		public final static int holder_mode = HashUtil.hash("holder mode");
		public final static int waiter_pid = HashUtil.hash("waiter pid");
		public final static int waiter_mode = HashUtil.hash("waiter mode");
		public final static int id1 = HashUtil.hash("id1");
		public final static int id2 = HashUtil.hash("id2");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(inst_no, "inst_no", null);
				handler.process(lock_type, "lock type", null);
				handler.process(holder_pid, "holder pid", null);
				handler.process(holder_mode, "holder mode", null);
				handler.process(waiter_pid, "waiter pid", null);
				handler.process(waiter_mode, "waiter mode", null);
				handler.process(id1, "id1", null);
				handler.process(id2, "id2", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Altibase {
		public final static int holder_id = HashUtil.hash("holder id");
		public final static int holder_desc = HashUtil.hash("holder desc");
		public final static int holder_type = HashUtil.hash("holder type");
		public final static int waiter_id = HashUtil.hash("waiter id");
		public final static int waiter_desc = HashUtil.hash("waiter desc");
		public final static int waiter_type = HashUtil.hash("waiter type");
		public final static int tbs_id = HashUtil.hash("tbs_id");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(holder_id, "holder id", null);
				handler.process(holder_desc, "holder desc", null);
				handler.process(holder_type, "holder type", null);
				handler.process(waiter_id, "waiter id", null);
				handler.process(waiter_desc, "waiter desc", null);
				handler.process(waiter_type, "waiter type", null);
				handler.process(tbs_id, "tbs_id", null);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.DB_LOCK_INFO;
	}
}
