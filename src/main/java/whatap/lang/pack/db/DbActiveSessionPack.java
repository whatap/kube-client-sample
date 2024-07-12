/*
 *  Copyright 2015 the original author or authors.
 *  @https://github.com/scouter-project/scouter
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;
import whatap.util.StringIntMap;

public class DbActiveSessionPack extends AbstractDbTablePack {

	public static interface YesNo {
		public final static byte none = 0;
		public final static byte yes = 1;	// "YES"
		public final static byte no = 2;	// "NO"
	}

	public static interface TrueFalse {
		public final static byte none = 0;
		public final static byte true1 = 1;	// "TRUE"
		public final static byte false1 = 2;	// "FALSE"
	}

	public static interface PgState {
		public final static byte none = 0;
		public final static byte active = 1;
		public final static byte idle = 2;
		public final static byte idle_in_transaction = 3;
		public final static byte idle_in_transaction_aborted = 4;
		public final static byte fastpath_function_call = 5;
		public final static byte disabled = 6;
	}

	public static interface OraStatus {
		public final static byte none = 0;
		public final static byte active = 1;
		public final static byte inactive = 2;
		public final static byte killed = 3;
		public final static byte cached = 4;
		public final static byte sniped = 5;
	}

	public static interface OraType {
		public final static byte none = 0;
		public final static byte user = 1;
		public final static byte background = 2;
		public final static byte recursive = 3;
	}

	public static interface OraPdmlStatus {
		public final static byte none = 0;
		public final static byte enabled = 1;
		public final static byte disabled = 2;
		public final static byte forced = 3;
	}

	public static interface OraPddlStatus {
		public final static byte none = 0;
		public final static byte enabled = 1;
		public final static byte disabled = 2;
		public final static byte forced = 3;
	}

	public static interface OraPqStatus {
		public final static byte none = 0;
		public final static byte enabled = 1;
		public final static byte disabled = 2;
		public final static byte forced = 3;
	}

	public static interface OraState {
		public final static byte none = 0;
		public final static byte waiting = 1;
		public final static byte waited_unknown_time = 2;
		public final static byte waited_short_time = 3;
		public final static byte waited_knonw_time = 4;
	}

	// Active session list
	public static interface OraWaitClass {
		public final static byte Other = 0;
		public final static byte Application = 1;
		public final static byte Configuration = 2;
		public final static byte Administrative = 3;
		public final static byte Concurrency = 4;
		public final static byte Commit = 5;
		public final static byte Idle = 6;
		public final static byte Network = 7;
		public final static byte UserIO = 8;	// User I/O
		public final static byte SystemIO = 9;	// System I/O
		public final static byte Scheduler = 10;
		public final static byte Cluster = 11;	// 190319 RAC 에만 있음.
	}

	// Lock tree
	public static interface OraLockMode {
		public final static byte NA = 0;	// N/A /* not available */
		public final static byte Null = 1;	// Null /* no permissions */
		public final static byte Rows = 2;	// Row-S (SS) /* concurrent read */
		public final static byte Rowx = 3;	// Row-X (SX) /* concurrent write */
		public final static byte Share = 4; // Share /* protected read */
		public final static byte Srowx = 5;	// S/Row-X (SSX) /* protected write */
		public final static byte Exclusive = 6; // Exclusive /* exclusive access */
	}

	public static interface MyType {
		public final static byte none = 0;
		public final static byte foreground = 1;	// "FOREGROUND"
		public final static byte background = 2;	// "BACKGROUND"
	}

	// Instrumented, History
	public static interface MyYesNo {
		public final static byte none = 0;
		public final static byte yes = 1;	// "YES"
		public final static byte no = 2;	// "NO"
	}

	public static interface MyConnectionType {
		public final static byte none = 0;
		public final static byte tcpip = 1;		//	"TCP/IP"
		public final static byte ssltls = 2;	// "SSL/TLS"
		public final static byte socket = 3;	// "Socket"
		public final static byte namedPipe = 4;	// "Named Pipe"
		public final static byte sharedMemory = 5;	// "Shared Memory"
	}

	public static interface MsStatus {
		public final static byte background = 1;	// "Background"
		public final static byte running = 2;		// "Running"
		public final static byte runnable = 3;		// "Runnable"
		public final static byte sleeping = 4;		// "Sleeping"
		public final static byte suspended = 5;		// "Suspended"
		public final static byte dormant = 6;		// "Dormant"
		public final static byte preconnect = 7;	// "Preconnect"
	}

	public static interface MsIsolation {
		public final static byte unspecified = 0;		// "Unspecified"
		public final static byte readuncommitted = 1;	// "ReadUncommitted"
		public final static byte readcommitted = 2;		// "ReadCommitted"
		public final static byte repeatable = 3;		// "Repeatable"
		public final static byte serializable = 4;		// "Serializble"
		public final static byte snapshot = 5;			// "Snapshot"
	}

	// tibero 문서에 정의가 확실하지 않음. dbms_lock.sleep() 에 command 는 6 번으로 오는데, 문서에 정의가 없음. v$sqltext 에는 다른값으로 되어 있음.
	// v$sqltext 에 있는 내용으로 재 정의. 5 에는 정의가 없지만, 6 과 동일한듯. 190315
	public static interface TibCommand {
		public final static byte none = 0;
		public final static byte select = 1;
		public final static byte insert = 2;
		public final static byte update = 3;
		public final static byte delete = 4;
		public final static byte merge = 5;
		public final static byte call = 6;
		public final static byte max = 7;
	}

	public static interface TibStatus {
		public final static byte none = 0;
		public final static byte ready = 1;
		public final static byte running = 2;
		public final static byte tx_recovering = 3;
		public final static byte sess_cleanup = 4;
		public final static byte assigned = 5;
		public final static byte closing = 6;
		public final static byte rolling_back = 7;
	}

	public static interface TibType {
		public final static byte none = 0;
		public final static byte wthr = 1;
		public final static byte cthr = 2;
		public final static byte lgwr = 3;
		public final static byte ckpt = 4;
		public final static byte larc = 5;
		public final static byte agent = 6;
		public final static byte mthr = 7;
		public final static byte dbwr = 8;
		public final static byte lnw = 9;
	}

	public static interface TibState {
		public final static byte none = 0;
		public final static byte invalid = 1;
		public final static byte new1 = 2;	// "NEW"
		public final static byte idle = 3;
		public final static byte running = 4;
		public final static byte waiting = 5;
		public final static byte recv_waiting = 6;
		public final static byte stop_by_mthr = 7;
		public final static byte dead = 8;
	}

	// sql_trace, pdml_status, pddl_status, pq_status
	public static interface TibEnable {
		public final static byte none = 0;
		public final static byte enabled = 1;
		public final static byte disabled = 2;
		public final static byte force = 3;
	}

	// pdml_enabled
	public static interface TibYesNo {
		public final static byte none = 0;
		public final static byte yes = 1;	// "YES"
		public final static byte no = 2;	// "NO"
	}

	public static interface AltiSessionState {
		public final static byte none = 0;
		public final static byte init = 1;
		public final static byte auth = 2;
		public final static byte service_ready = 3;
		public final static byte service = 4;
		public final static byte end = 5;
		public final static byte rollback = 6;
		public final static byte unknown = 7;
	}

	public static interface AltiTaskState {
		public final static byte none = 0;
		public final static byte waiting = 1;
		public final static byte ready = 2;
		public final static byte executing = 3;
		public final static byte queue_wait = 4;
		public final static byte queue_ready = 5;
		public final static byte unknown = 6;
	}

	public static class MyCommand {
		String [] strCommand = {
				"none", "Binlog Dump", "Change user", "Close stmt", "Connect",	// 0 ~ 4
				"Connect Out", "Create DB", "Daemon", "Debug","Delayed insert",	// 5 ~ 9
				"Drop DB", "Error", "Execute", "Fetch", "Field List",	// 10 ~ 14
				"Init DB", "Kill", "Long Data", "Ping", "Prepare",	// 15 ~ 19
				"Processlist", "Query", "Quit", "Refresh", "Register Slave",	// 20 ~ 24
				"Reset stmt", "Set option", "Shutdown", "Sleep", "Statistics",	// 25 ~ 29
				"Table Dump", "Time" }; // 30, 31

		StringIntMap mapCommand = new StringIntMap();

		public MyCommand() {
			for (int i = 0; i < strCommand.length; i ++) {
				mapCommand.put(strCommand[i], i);
			}
		}

		public byte getCommandByte(String strCommand) {
			if (strCommand == null) return 0;
			return (byte)mapCommand.get(strCommand);
		}
	}

	//public final static int lock_count = HashUtil.hash("lock wait count");	// lock wait sessions 로 통일하기로 함. 171208
	public static class Pg {
		public final static int pid = HashUtil.hash("pid");
		public final static int datname = HashUtil.hash("datname");
		public final static int runtime = HashUtil.hash("runtime");
		//public final static int cpuusage = HashUtil.hash("cpuusage");	// xos 200804 삭제.
		public final static int cpu_xos = HashUtil.hash("cpu(xos)");	// 위의 cpuusage 삭제 예정.
		public final static int rss_xos = HashUtil.hash("rss(xos)");
		public final static int pss_xos = HashUtil.hash("pss(xos)");
		public final static int ioread_xos = HashUtil.hash("ioread(xos)");
		public final static int iowrite_xos = HashUtil.hash("iowrite(xos)");
		public final static int application_name = HashUtil.hash("application_name");
		public final static int client_addr = HashUtil.hash("client_addr");
		public final static int client_hostname = HashUtil.hash("client_hostname");
		public final static int client_port = HashUtil.hash("client_port");
		public final static int backend_start = HashUtil.hash("backend_start");
		public final static int xact_start = HashUtil.hash("xact_start");
		public final static int query_start = HashUtil.hash("query_start");
		public final static int state_change = HashUtil.hash("state_change");
		public final static int usename = HashUtil.hash("usename");
		public final static int wait_event_type = HashUtil.hash("wait_event_type");
		public final static int wait_event = HashUtil.hash("wait_event");
		public final static int state = HashUtil.hash("state");
		public final static int query_hash = HashUtil.hash("query hash");
		public final static int query_param = HashUtil.hash("query param");
		public final static int backend_xid = HashUtil.hash("backend_xid");
		public final static int backend_xmin = HashUtil.hash("backend_xmin");
		public final static int backend_type = HashUtil.hash("backend_type");
		public final static int query_id = HashUtil.hash("query_id");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(pid, "pid", null);
				handler.process(datname, "datname", null);
				handler.process(runtime, "runtime", null);
				//handler.process(cpuusage, "cpuusage", null);	// xos
				handler.process(cpu_xos, "cpu(xos)", null);	// 위의 cpuusage 삭제 예정.
				handler.process(rss_xos, "rss(xos)", null);
				handler.process(pss_xos, "pss(xos)", null);
				handler.process(ioread_xos, "ioread(xos)", null);
				handler.process(iowrite_xos, "iowrite(xos)", null);
				handler.process(application_name, "application_name", null);
				handler.process(client_addr, "client_addr", null);
				handler.process(client_hostname, "client_hostname", null);
				handler.process(client_port, "client_port", null);
				handler.process(backend_start, "backend_start", null);
				handler.process(xact_start, "xact_start", null);
				handler.process(query_start, "query_start", null);
				handler.process(state_change, "state_change", null);
				handler.process(usename, "usename", null);
				handler.process(wait_event_type, "wait_event_type", null);
				handler.process(wait_event, "wait_event", null);
				handler.process(state, "state", null);
				handler.process(query_hash, "query hash", null);
				handler.process(query_param, "query param", null);
				handler.process(backend_xid, "backend_xid", null);
				handler.process(backend_xmin, "backend_xmin", null);
				handler.process(backend_type, "backend_type", null);
				handler.process(query_id, "query_id", null);
			} catch (Exception e) {
			}
		}
	}
 
	//public final static int active_session_count = HashUtil.hash("active session count");	// active sessions 로 통일. 171208

	public static class Oracle {
		public final static int con_id = HashUtil.hash("con_id");
		public final static int db = HashUtil.hash("db");
		public final static int saddr = HashUtil.hash("saddr");
		public final static int sid = HashUtil.hash("sid");
		public final static int serial = HashUtil.hash("serial#");
		//public final static int paddr = HashUtil.hash("paddr");	// 컬럼 삭제.
		public final static int username = HashUtil.hash("username");
		public final static int command = HashUtil.hash("command");
		public final static int taddr = HashUtil.hash("taddr");
		public final static int lockwait = HashUtil.hash("lockwait");
		public final static int status = HashUtil.hash("status");
		public final static int schemaname = HashUtil.hash("schemaname");
		public final static int osuser = HashUtil.hash("osuser");
		public final static int spid = HashUtil.hash("spid");
		//public final static int cpuusage = HashUtil.hash("cpuusage");	// xos 로부터 받은 cpu usage. 200804 삭제.
		public final static int cpu_xos = HashUtil.hash("cpu(xos)");	// 위의 cpuusage 삭제 예정.
		public final static int rss_xos = HashUtil.hash("rss(xos)");
		public final static int pss_xos = HashUtil.hash("pss(xos)");
		public final static int ioread_xos = HashUtil.hash("ioread(xos)");
		public final static int iowrite_xos = HashUtil.hash("iowrite(xos)");
		public final static int cpu_used = HashUtil.hash("cpu_used");	// oracle 18 이상 v$process 의  cpu_used
		public final static int cpu_usage = HashUtil.hash("cpu_usage");	// oracle 18 이상 cpu_used 로 계산한 cpu_usage
		public final static int process = HashUtil.hash("process");
		public final static int machine = HashUtil.hash("machine");
		public final static int port = HashUtil.hash("port");
		public final static int terminal = HashUtil.hash("terminal");
		public final static int program = HashUtil.hash("program");
		public final static int type = HashUtil.hash("type");
		public final static int sql_address = HashUtil.hash("sql_address");
		public final static int sql_hash_value = HashUtil.hash("sql_hash_value");
		public final static int sql_id = HashUtil.hash("sql_id");
		public final static int sql_child_number = HashUtil.hash("sql_child_number");
		public final static int sql_exec_start = HashUtil.hash("sql_exec_start");
		public final static int sql_exec_id = HashUtil.hash("sql_exec_id");
		public final static int prev_sql_addr = HashUtil.hash("prev_sql_addr");
		public final static int prev_hash_value = HashUtil.hash("prev_hash_value");
		public final static int prev_sql_id = HashUtil.hash("prev_sql_id");
	 	public final static int prev_child_number = HashUtil.hash("prev_child_number");
		public final static int prev_exec_start = HashUtil.hash("prev_exec_start");
		public final static int prev_exec_id = HashUtil.hash("prev_exec_id");
		public final static int plsql_entry_object_id = HashUtil.hash("plsql_entry_object_id");
		public final static int plsql_entry_subprogram_id = HashUtil.hash("plsql_entry_subprogram_id");
		public final static int plsql_object_id = HashUtil.hash("plsql_object_id");
		public final static int plsql_subprogram_id = HashUtil.hash("plsql_subprogram_id");
		public final static int module = HashUtil.hash("module");
		public final static int action = HashUtil.hash("action");
		public final static int client_info = HashUtil.hash("client_info");
		public final static int row_wait_obj = HashUtil.hash("row_wait_obj#");
		public final static int row_wait_file = HashUtil.hash("row_wait_file#");
		public final static int row_wait_block = HashUtil.hash("row_wait_block#");
		public final static int row_wait_row = HashUtil.hash("row_wait_row#");
		//public final static int top_level_call = HashUtil.hash("top_level_call");	// column 삭제.
		public final static int logon_time = HashUtil.hash("logon_time");
		public final static int last_call_et = HashUtil.hash("last_call_et");
		public final static int pdml_status = HashUtil.hash("pdml_status");
		public final static int pddl_status = HashUtil.hash("pddl_status");
		public final static int pq_status = HashUtil.hash("pq_status");
		//public final static int current_queue_duration = HashUtil.hash("current_queue_duration");	// column 삭제.
		public final static int client_identifier = HashUtil.hash("client_identifier");
		public final static int blocking_session_status = HashUtil.hash("blocking_session_status");
		public final static int blocking_instance = HashUtil.hash("blocking_instance");
		public final static int blocking_session = HashUtil.hash("blocking_session");
		public final static int final_blocking_session_status = HashUtil.hash("final_blocking_session_status");
		public final static int final_blocking_instance = HashUtil.hash("final_blocking_instance");
		public final static int final_blocking_session = HashUtil.hash("final_blocking_session");
		public final static int seq = HashUtil.hash("seq#");
		public final static int event_no = HashUtil.hash("event#");
		public final static int event = HashUtil.hash("event");
		public final static int p1 = HashUtil.hash("p1");
		public final static int p2 = HashUtil.hash("p2");
		public final static int p3 = HashUtil.hash("p3");
		public final static int wait_class = HashUtil.hash("wait_class#");
		public final static int wait_time = HashUtil.hash("wait_time");
		public final static int seconds_in_wait = HashUtil.hash("seconds_in_wait");
		public final static int state = HashUtil.hash("state");
		public final static int service_name = HashUtil.hash("service_name");
		public final static int stat1 = HashUtil.hash("stat1");	// session logical reads
		public final static int stat2 = HashUtil.hash("stat2");	// physical reads
		public final static int stat3 = HashUtil.hash("stat3");	// execute count
		public final static int stat4 = HashUtil.hash("stat4");	// parse count (hard)
		public final static int stat5 = HashUtil.hash("stat5");	// parse count (total)
		public final static int stat6 = HashUtil.hash("stat6");	// opened cursors current
		public final static int stat7 = HashUtil.hash("stat7");	// db block changes
		public final static int stat8 = HashUtil.hash("stat8");	// session pga memory
		public final static int stat1_sigma = HashUtil.hash("stat1_sigma");
		public final static int stat2_sigma = HashUtil.hash("stat2_sigma");
		public final static int stat3_sigma = HashUtil.hash("stat3_sigma");
		public final static int stat4_sigma = HashUtil.hash("stat4_sigma");
		public final static int stat5_sigma = HashUtil.hash("stat5_sigma");
		public final static int stat6_sigma = HashUtil.hash("stat6_sigma");
		public final static int stat7_sigma = HashUtil.hash("stat7_sigma");
		public final static int stat8_sigma = HashUtil.hash("stat8_sigma");
		public final static int undo_segid = HashUtil.hash("undo_segid");
		public final static int undo_blk = HashUtil.hash("undo_blk");
		public final static int undo_rec = HashUtil.hash("undo_rec");
		public final static int sql_hash = HashUtil.hash("sql_hash");
		public final static int sql_param = HashUtil.hash("sql_param");
		public final static int prev_sql_hash = HashUtil.hash("prev_sql_hash");
		public final static int prev_sql_param = HashUtil.hash("prev_sql_param");
		public final static int sql_text = HashUtil.hash("sql_text");
		public final static int prev_sql = HashUtil.hash("prev_sql");
		public final static int prev_plan_hash_value = HashUtil.hash("prev_plan_hash_value");
		public final static int plan_hash_value = HashUtil.hash("plan_hash_value");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(con_id, "con_id", null);
				handler.process(db, "db", null);
				handler.process(saddr, "saddr", null);
				handler.process(sid, "sid", null);
				handler.process(serial, "serial#", null);
				//handler.process(paddr, "paddr", null);
				handler.process(username, "username", null);
				handler.process(command, "command", null);
				handler.process(taddr, "taddr", null);
				handler.process(lockwait, "lockwait", null);
				handler.process(status, "status", null);
				handler.process(schemaname, "schemaname", null);
				handler.process(osuser, "osuser", null);
				handler.process(spid, "spid", null);
				//handler.process(cpuusage, "cpuusage", null);	// xos
				handler.process(cpu_xos, "cpu(xos)", null);	// 위의 cpuusage 삭제 예정.
				handler.process(rss_xos, "rss(xos)", null);
				handler.process(pss_xos, "pss(xos)", null);
				handler.process(ioread_xos, "ioread(xos)", null);
				handler.process(iowrite_xos, "iowrite(xos)", null);
				handler.process(cpu_used, "cpu_used", null);
				handler.process(cpu_usage, "cpu_usage", null);
				handler.process(process, "process", null);
				handler.process(machine, "machine", null);
				handler.process(port, "port", null);
				handler.process(terminal, "terminal", null);
				handler.process(program, "program", null);
				handler.process(type, "type", null);
				handler.process(sql_address, "sql_address", null);
				handler.process(sql_hash_value, "sql_hash_value", null);
				handler.process(sql_id, "sql_id", null);
				handler.process(sql_child_number, "sql_child_number", null);
				handler.process(sql_exec_start, "sql_exec_start", null);
				handler.process(sql_exec_id, "sql_exec_id", null);
				handler.process(prev_sql_addr, "prev_sql_addr", null);
				handler.process(prev_hash_value, "prev_hash_value", null);
				handler.process(prev_sql_id, "prev_sql_id", null);
				handler.process(prev_child_number, "prev_child_number", null);
				handler.process(prev_exec_start, "prev_exec_start", null);
				handler.process(prev_exec_id, "prev_exec_id", null);
				handler.process(plsql_entry_object_id, "plsql_entry_object_id", null);
				handler.process(plsql_entry_subprogram_id, "plsql_entry_subprogram_id", null);
				handler.process(plsql_object_id, "plsql_object_id", null);
				handler.process(plsql_subprogram_id, "plsql_subprogram_id", null);
				handler.process(module, "module", null);
				handler.process(action, "action", null);
				handler.process(client_info, "client_info", null);
				handler.process(row_wait_obj, "row_wait_obj#", null);
				handler.process(row_wait_file, "row_wait_file#", null);
				handler.process(row_wait_block, "row_wait_block#", null);
				handler.process(row_wait_row, "row_wait_row#", null);
				handler.process(logon_time, "logon_time", null);
				handler.process(last_call_et, "last_call_et", null);
				handler.process(pdml_status, "pdml_status", null);
				handler.process(pddl_status, "pddl_status", null);
				handler.process(pq_status, "pq_status", null);
				handler.process(client_identifier, "client_identifier", null);
				handler.process(blocking_session_status, "blocking_session_status", null);
				handler.process(blocking_instance, "blocking_instance", null);
				handler.process(blocking_session, "blocking_session", null);
				handler.process(final_blocking_session_status, "final_blocking_session_status", null);
				handler.process(final_blocking_instance, "final_blocking_instance", null);
				handler.process(final_blocking_session, "final_blocking_session", null);
				handler.process(seq, "seq#", null);
				handler.process(event_no, "event#", null);
				handler.process(event, "event", null);
				handler.process(p1, "p1", null);
				handler.process(p2, "p2", null);
				handler.process(p3, "p3", null);
				handler.process(wait_class, "wait_class#", null);
				handler.process(wait_time, "wait_time", null);
				handler.process(seconds_in_wait, "seconds_in_wait", null);
				handler.process(state, "state", null);
				handler.process(service_name, "service_name", null);
				handler.process(stat1, "stat1", null);
				handler.process(stat2, "stat2", null);
				handler.process(stat3, "stat3", null);
				handler.process(stat4, "stat4", null);
				handler.process(stat5, "stat5", null);
				handler.process(stat6, "stat6", null);
				handler.process(stat7, "stat7", null);
				handler.process(stat8, "stat8", null);
				handler.process(stat1_sigma, "stat1_sigma", null);
				handler.process(stat2_sigma, "stat2_sigma", null);
				handler.process(stat3_sigma, "stat3_sigma", null);
				handler.process(stat4_sigma, "stat4_sigma", null);
				handler.process(stat5_sigma, "stat5_sigma", null);
				handler.process(stat6_sigma, "stat6_sigma", null);
				handler.process(stat7_sigma, "stat7_sigma", null);
				handler.process(stat8_sigma, "stat8_sigma", null);
				handler.process(undo_segid, "undo_segid", null);
				handler.process(undo_blk, "undo_blk", null);
				handler.process(undo_rec, "undo_rec", null);
				handler.process(sql_hash, "sql_hash", null);
				handler.process(sql_param, "sql_param", null);
				handler.process(prev_sql_hash, "prev_sql_hash", null);
				handler.process(prev_sql_param, "prev_sql_param", null);
				handler.process(sql_text, "sql_text", null);
				handler.process(prev_sql, "prev_sql", null);
				handler.process(prev_plan_hash_value, "prev_plan_hash_value", null);
				handler.process(plan_hash_value, "plan_hash_value", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Mysql {
		public final static int id = HashUtil.hash("id");
		public final static int user = HashUtil.hash("user");
		public final static int host = HashUtil.hash("host");
		public final static int db = HashUtil.hash("db");
		public final static int command = HashUtil.hash("command");
		public final static int state = HashUtil.hash("state");
		public final static int os_id = HashUtil.hash("os_id");
		//public final static int cpuusage = HashUtil.hash("cpuusage");	// xos 로부터 받은 cpu usage. 200804 삭제
		public final static int cpu_xos = HashUtil.hash("cpu(xos)");	// 위의 cpuusage 삭제 예정.
		public final static int thread_id = HashUtil.hash("thread_id");
		public final static int time = HashUtil.hash("time");
		public final static int type = HashUtil.hash("type");
		public final static int name = HashUtil.hash("name");
		public final static int connection_type = HashUtil.hash("connection_type");
		public final static int info = HashUtil.hash("info");
		public final static int query_hash = HashUtil.hash("query hash");
		public final static int query_param = HashUtil.hash("query param");

		public final static int time_ms = HashUtil.hash("time_ms");
		public final static int stage = HashUtil.hash("stage");
		public final static int max_stage = HashUtil.hash("max_stage");
		public final static int progress = HashUtil.hash("progress");
		public final static int memory_used = HashUtil.hash("memory_used");
		public final static int examined_rows = HashUtil.hash("examined_rows");
		public final static int query_id = HashUtil.hash("query_id");
		public final static int tid = HashUtil.hash("tid");
		public final static int max_memory_used = HashUtil.hash("max_memory_used");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(id, "id", null);
				handler.process(user, "user", null);
				handler.process(host, "host", null);
				handler.process(db, "db", null);
				handler.process(command, "command", null);
				handler.process(state, "state", null);
				handler.process(os_id, "os_id", null);
				//handler.process(cpuusage, "cpuusage", null);	// xos 로부터 받은 cpu usage
				handler.process(cpu_xos, "cpu(xos)", null);	// 위의 cpuusage 삭제 예정.
				handler.process(thread_id, "thread_id", null);
				handler.process(time, "time", null);
				handler.process(name, "name", null);
				handler.process(connection_type, "connection_type", null);
				handler.process(info, "info", null);
				handler.process(query_hash, "query hash", null);
				handler.process(query_param, "query param", null);

				handler.process(time_ms, "time_ms", null);
				handler.process(stage, "stage", null);
				handler.process(max_stage, "max_stage", null);
				handler.process(progress, "progress", null);
				handler.process(memory_used, "memory_used", null);
				handler.process(examined_rows, "examined_rows", null);
				handler.process(query_id, "query_id", null);
				handler.process(tid, "tid", null);
				handler.process(max_memory_used, "max_memory_used", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Mssql {
		public final static int id = HashUtil.hash("id");
		public final static int objectid = HashUtil.hash("objectid");
		public final static int object = HashUtil.hash("object");
		public final static int db = HashUtil.hash("db");
		public final static int status = HashUtil.hash("status");
		public final static int elapsed_time = HashUtil.hash("elapsed_time");
		public final static int wait_type = HashUtil.hash("wait_type");
		public final static int wait_time = HashUtil.hash("wait_time");
		public final static int wait_resource = HashUtil.hash("wait_resource");
		public final static int last_wait_type = HashUtil.hash("last_wait_type");
		public final static int blocking_session_id = HashUtil.hash("blocking_session_id");	// 190426
		public final static int command = HashUtil.hash("command");
		public final static int cpu_usage = HashUtil.hash("cpu_usage");	// cpu(%)
		public final static int percent_complete = HashUtil.hash("percent_complete");
		public final static int completion_time = HashUtil.hash("completion_time");
		public final static int memory_usage = HashUtil.hash("memory_usage");
		public final static int cpu_time = HashUtil.hash("cpu_time");
		public final static int reads = HashUtil.hash("reads");
		public final static int writes = HashUtil.hash("writes");
		public final static int logical_reads = HashUtil.hash("logical_reads");
		public final static int row_count = HashUtil.hash("row_count");
		public final static int cpu_time_sigma = HashUtil.hash("cpu_time_sigma");	// cpu_time(sigma)
		public final static int reads_sigma = HashUtil.hash("reads_sigma");	// reads(sigma)
		public final static int writes_sigma = HashUtil.hash("writes_sigma");	// writes(sigma)
		public final static int logical_reads_sigma = HashUtil.hash("logical_reads_sigma");	// logical_reads(sigma)
		public final static int granted_query_memory = HashUtil.hash("granted_query_memory");
		public final static int isolation = HashUtil.hash("isolation");
		public final static int login_time = HashUtil.hash("login_time");
		public final static int last_request_start_time = HashUtil.hash("last_request_start_time");
		public final static int host = HashUtil.hash("host");
		public final static int application = HashUtil.hash("application");
		public final static int user = HashUtil.hash("user");
		public final static int client = HashUtil.hash("client");
		public final static int sql_hash = HashUtil.hash("sql_hash");
		public final static int sql_param = HashUtil.hash("sql_param");
		public final static int plan_handle = HashUtil.hash("plan_handle");
		public final static int dop = HashUtil.hash("dop");
		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(id, "id", null);
				handler.process(objectid, "objectid", null);
				handler.process(object, "object", null);
				handler.process(db, "db", null);
				handler.process(status, "status", null);
				handler.process(elapsed_time, "elapsed_time", null);
				handler.process(wait_type, "wait_type", null);
				handler.process(wait_time, "wait_time", null);
				handler.process(wait_resource, "wait_resource", null);
				handler.process(last_wait_type, "last_wait_type", null);
				handler.process(blocking_session_id, "blocking_session_id", null);
				handler.process(command, "command", null);
				handler.process(cpu_usage, "cpu_usage", null);
				handler.process(percent_complete, "percent_complete", null);
				handler.process(completion_time, "completion_time", null);
				handler.process(memory_usage, "memory_usage", null);
				handler.process(cpu_time, "cpu_time", null);
				handler.process(reads, "reads", null);
				handler.process(writes, "writes", null);
				handler.process(logical_reads, "logical_reads", null);
				handler.process(row_count, "row_count", null);
				handler.process(cpu_time_sigma, "cpu_time_sigma", null);
				handler.process(reads_sigma, "reads_sigma", null);
				handler.process(writes_sigma, "writes_sigma", null);
				handler.process(logical_reads_sigma, "logical_reads_sigma", null);
				handler.process(granted_query_memory, "granted_query_memory", null);
				handler.process(isolation, "isolation", null);
				handler.process(login_time, "login_time", null);
				handler.process(last_request_start_time, "last_request_start_time", null);
				handler.process(host, "host", null);
				handler.process(application, "application", null);
				handler.process(user, "user", null);
				handler.process(client, "client", null);
				handler.process(sql_hash, "sql_hash", null);
				handler.process(sql_param, "sql_param", null);
				handler.process(plan_handle, "plan_handle", null);
				handler.process(dop, "dop", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Tibero {
		public final static int sid = HashUtil.hash("sid");
		public final static int serial = HashUtil.hash("serial#");
		public final static int username = HashUtil.hash("username");
		public final static int ipaddr = HashUtil.hash("ipaddr");
		public final static int command = HashUtil.hash("command");	// TibCommand
		public final static int status = HashUtil.hash("status");	// TibStatus
		public final static int schemaname = HashUtil.hash("schemaname");
		public final static int type = HashUtil.hash("type");	// TibType
		public final static int sql_id = HashUtil.hash("sql_id");
		public final static int prev_sql_id = HashUtil.hash("prev_sql_id");
		public final static int sql_et = HashUtil.hash("sql_et");
		public final static int logon_time = HashUtil.hash("logon_time");
		public final static int state = HashUtil.hash("state");	// TibState
		public final static int wlock_wait = HashUtil.hash("wlock_wait");
		public final static int wait_event = HashUtil.hash("wait_event");
		public final static int event_name = HashUtil.hash("event_name");	// 190218 제거. 190307 다시 추가.
		public final static int wait_time = HashUtil.hash("wait_time");
		public final static int id1 = HashUtil.hash("id1");	// 190218 추가.
		public final static int id2 = HashUtil.hash("id2");	// 190218 추가.
		public final static int seq = HashUtil.hash("seq#");	// 190218 추가.
		public final static int pga_used_mem = HashUtil.hash("pga_used_mem");
		public final static int sql_trace = HashUtil.hash("sql_trace");	// 181220
		public final static int prog_name = HashUtil.hash("prog_name");
		public final static int client_pid = HashUtil.hash("client_pid");
		public final static int pid = HashUtil.hash("pid");
		public final static int cpuusage = HashUtil.hash("cpuusage");	// cpu(xos) 가 아님.
		public final static int cpu_xos = HashUtil.hash("cpu(xos)");
		public final static int rss_xos = HashUtil.hash("rss(xos)");
		public final static int pss_xos = HashUtil.hash("pss(xos)");
		public final static int ioread_xos = HashUtil.hash("ioread(xos)");
		public final static int iowrite_xos = HashUtil.hash("iowrite(xos)");
		public final static int wthr_id = HashUtil.hash("wthr_id");
		public final static int os_thr_id = HashUtil.hash("os_thr_id");
		public final static int osuser = HashUtil.hash("osuser");
		public final static int machine = HashUtil.hash("machine");
		public final static int terminal = HashUtil.hash("terminal");
		public final static int module = HashUtil.hash("module");
		public final static int action = HashUtil.hash("action");
		public final static int client_info = HashUtil.hash("client_info");
		public final static int client_identifier = HashUtil.hash("client_identifier");
		public final static int pdml_enabled = HashUtil.hash("pdml_enabled");	// TibYesNo
		public final static int pdml_status = HashUtil.hash("pdml_status");	// TibEnable
		public final static int pddl_status = HashUtil.hash("pddl_status");	// TibEnable
		public final static int pq_status = HashUtil.hash("pq_status");	// TibEnable
		public final static int row_wait_obj_id = HashUtil.hash("row_wait_obj_id");
		public final static int row_wait_file_no = HashUtil.hash("row_wait_file_no");
		public final static int row_wait_block_no = HashUtil.hash("row_wait_block_no");
		public final static int row_wait_row_no = HashUtil.hash("row_wait_row_no");
		public final static int consumer_group = HashUtil.hash("consumer_group");
		public final static int consumed_cpu_time = HashUtil.hash("consumed_cpu_time");
		public final static int usn = HashUtil.hash("usn");
		public final static int used_blk = HashUtil.hash("used_blk");
		public final static int used_rec = HashUtil.hash("used_rec");
		public final static int sql_hash = HashUtil.hash("sql_hash");
		public final static int sql_param = HashUtil.hash("sql_param");
		public final static int prev_sql_hash = HashUtil.hash("prev_sql_hash");
		public final static int prev_sql_param = HashUtil.hash("prev_sql_param");
		public final static int sql_text = HashUtil.hash("sql_text");
		public final static int prev_sql = HashUtil.hash("prev_sql");
		public final static int stat1 = HashUtil.hash("stat1");	// logical reads
		public final static int stat2 = HashUtil.hash("stat2");	// physical reads
		public final static int stat3 = HashUtil.hash("stat3");	// execute count
		public final static int stat4 = HashUtil.hash("stat4");	// parse count (hard)
		public final static int stat5 = HashUtil.hash("stat5");	// parse count (total)
		public final static int stat6 = HashUtil.hash("stat6");	// block updates
		public final static int stat1_sigma = HashUtil.hash("stat1_sigma");
		public final static int stat2_sigma = HashUtil.hash("stat2_sigma");
		public final static int stat3_sigma = HashUtil.hash("stat3_sigma");
		public final static int stat4_sigma = HashUtil.hash("stat4_sigma");
		public final static int stat5_sigma = HashUtil.hash("stat5_sigma");
		public final static int stat6_sigma = HashUtil.hash("stat6_sigma");
		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(sid, "sid", null);
				handler.process(serial, "serial#", null);
				handler.process(username, "username", null);
				handler.process(ipaddr, "ipaddr", null);
				handler.process(command, "command", null);
				handler.process(status, "status", null);
				handler.process(schemaname, "schemaname", null);
				handler.process(type, "type", null);
				handler.process(sql_id, "sql_id", null);
				handler.process(prev_sql_id, "prev_sql_id", null);
				handler.process(sql_et, "sql_et", null);
				handler.process(logon_time, "logon_time", null);
				handler.process(state, "state", null);
				handler.process(wlock_wait, "wlock_wait", null);
				handler.process(wait_event, "wait_event", null);
				handler.process(event_name, "event_name" , null);	// 190218 제거. 190307 다시 추가.
				handler.process(wait_time, "wait_time", null);
				handler.process(id1, "id1", null);	// 190218 추가.
				handler.process(id2, "id2", null);	// 190218 추가.
				handler.process(seq, "seq#", null);	// 190218 추가.
				handler.process(pga_used_mem, "pga_used_mem", null);
				handler.process(sql_trace,"sql_trace", null);
				handler.process(prog_name, "prog_name", null);
				handler.process(client_pid, "client_pid", null);
				handler.process(cpuusage, "cpuusage", null);
				handler.process(cpu_xos, "cpu(xos)", null);
				handler.process(rss_xos, "rss(xos)", null);
				handler.process(pss_xos, "pss(xos)", null);
				handler.process(ioread_xos, "ioread(xos)", null);
				handler.process(iowrite_xos, "iowrite(xos)", null);
				handler.process(pid, "pid", null);
				handler.process(wthr_id, "wthr_id", null);
				handler.process(os_thr_id, "os_thr_id", null);
				handler.process(osuser, "osuser", null);
				handler.process(machine, "machine", null);
				handler.process(terminal, "terminal", null);
				handler.process(module, "module", null);
				handler.process(action, "action", null);
				handler.process(client_info, "client_info", null);
				handler.process(client_identifier, "client_identifier", null);
				handler.process(pdml_enabled, "pdml_enabled", null);
				handler.process(pdml_status, "pdml_status", null);
				handler.process(pddl_status, "pddl_status", null);
				handler.process(pq_status, "pq_status", null);
				handler.process(row_wait_obj_id, "row_wait_obj_id", null);
				handler.process(row_wait_file_no, "row_wait_file_no", null);
				handler.process(row_wait_block_no, "row_wait_block_no", null);
				handler.process(row_wait_row_no, "row_wait_row_no", null);
				handler.process(consumer_group, "consumer_group", null);
				handler.process(consumed_cpu_time, "consumed_cpu_time", null);
				handler.process(usn, "usn", null);
				handler.process(used_blk, "used_blk", null);
				handler.process(used_rec, "used_rec", null);
				handler.process(sql_hash, "sql_hash", null);
				handler.process(sql_param, "sql_param", null);
				handler.process(prev_sql_hash, "prev_sql_hash", null);
				handler.process(prev_sql_param, "prev_sql_param", null);
				handler.process(sql_text, "sql_text", null);
				handler.process(prev_sql, "prev_sql", null);
				handler.process(stat1, "stat1", null);
				handler.process(stat2, "stat2", null);
				handler.process(stat3, "stat3", null);
				handler.process(stat4, "stat4", null);
				handler.process(stat5, "stat5", null);
				handler.process(stat6, "stat6", null);
				handler.process(stat1_sigma, "stat1_sigma", null);
				handler.process(stat2_sigma, "stat2_sigma", null);
				handler.process(stat3_sigma, "stat3_sigma", null);
				handler.process(stat4_sigma, "stat4_sigma", null);
				handler.process(stat5_sigma, "stat5_sigma", null);
				handler.process(stat6_sigma, "stat6_sigma", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Cubrid {
		public final static int user = HashUtil.hash("user");
		public final static int sql_id = HashUtil.hash("sql_id");
		public final static int host = HashUtil.hash("host");
		public final static int pid = HashUtil.hash("pid");
		public final static int program = HashUtil.hash("program");
		public final static int query_time = HashUtil.hash("query_time");
		public final static int tran_time = HashUtil.hash("tran_time");
		public final static int tranindex = HashUtil.hash("tranindex");
		public final static int transtatus = HashUtil.hash("transtatus");
		public final static int lock_holder = HashUtil.hash("lock_holder");
		public final static int sql_hash = HashUtil.hash("sql_hash");
		public final static int sql_param = HashUtil.hash("sql_param");
		public final static int sql_text = HashUtil.hash("sql_text");	// 사용하지 않음.
		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(user, "user", null);
				handler.process(sql_id, "sql_id", null);
				handler.process(host, "host", null);
				handler.process(pid, "pid", null);
				handler.process(program, "program", null);
				handler.process(query_time, "query_time", null);
				handler.process(tran_time, "tran_time", null);
				handler.process(tranindex, "tranindex", null);
				handler.process(transtatus, "transtatus", null);
				handler.process(lock_holder, "lock_holder", null);
				handler.process(sql_hash, "sql_hash", null);
				handler.process(sql_param, "sql_param", null);
				handler.process(sql_text, "sql_text", null);	// 사용하지 않음.
			} catch (Exception e) {
			}
		}
	}

	public static class Altibase {
		public final static int id = HashUtil.hash("id");
		public final static int trans_id = HashUtil.hash("trans_id");
		public final static int task_state = HashUtil.hash("task_state");
		public final static int comm_name = HashUtil.hash("comm_name");
		public final static int xa_session_flag = HashUtil.hash("xa_session_flag");
		public final static int xa_associate_flag = HashUtil.hash("xa_associate_flag");
		public final static int query_time_limit = HashUtil.hash("query_time_limit");
		public final static int ddl_time_limit = HashUtil.hash("ddl_time_limit");
		public final static int fetch_time_limit = HashUtil.hash("fetch_time_limit");
		public final static int utrans_time_limit = HashUtil.hash("utrans_time_limit");
		public final static int idle_time_limit = HashUtil.hash("idle_time_limit");
		public final static int idle_start_time = HashUtil.hash("idle_start_time");
		public final static int active_flag = HashUtil.hash("active_flag");
		public final static int opened_stmt_count = HashUtil.hash("opened_stmt_count");
		public final static int client_package_version = HashUtil.hash("client_package_version");
		public final static int client_protocol_version = HashUtil.hash("client_protocol_version");
		public final static int client_pid = HashUtil.hash("client_pid");
		public final static int client_type = HashUtil.hash("client_type");
		public final static int client_app_info = HashUtil.hash("client_app_info");
		public final static int client_nls = HashUtil.hash("client_nls");
		public final static int db_username = HashUtil.hash("db_username");
		public final static int db_userid = HashUtil.hash("db_userid");
		public final static int default_tbsid = HashUtil.hash("default_tbsid");
		public final static int default_temp_tbsid = HashUtil.hash("default_temp_tbsid");
		public final static int sysdba_flag = HashUtil.hash("sysdba_flag");
		public final static int autocommit_flag = HashUtil.hash("autocommit_flag");
		public final static int session_state = HashUtil.hash("session_state");
		public final static int isolation_level = HashUtil.hash("isolation_level");
		public final static int replication_mode = HashUtil.hash("replication_mode");
		public final static int transaction_mode = HashUtil.hash("transaction_mode");
		public final static int commit_write_wait_mode = HashUtil.hash("commit_write_wait_mode");
		public final static int optimizer_mode = HashUtil.hash("optimizer_mode");
		public final static int header_display_mode = HashUtil.hash("header_display_mode");
		public final static int current_stmt_id = HashUtil.hash("current_stmt_id");
		public final static int stack_size = HashUtil.hash("stack_size");
		public final static int default_date_format = HashUtil.hash("default_date_format");
		public final static int trx_update_max_logsize = HashUtil.hash("trx_update_max_logsize");
		public final static int paralle_dml_mode = HashUtil.hash("paralle_dml_mode");
		public final static int login_time = HashUtil.hash("login_time");
		public final static int failover_source = HashUtil.hash("failover_source");
		public final static int nls_territory = HashUtil.hash("nls_territory");
		public final static int nls_iso_currency = HashUtil.hash("nls_iso_currency");
		public final static int nls_currency = HashUtil.hash("nls_currency");
		public final static int nls_numeric_characters = HashUtil.hash("nls_numeric_characters");
		public final static int time_zone = HashUtil.hash("time_zone");
		public final static int lob_cache_threshold = HashUtil.hash("lob_cache_threshold");
		public final static int query_rewrite_enable = HashUtil.hash("query_rewrite_enable");
		public final static int dblink_global_transaction_level = HashUtil.hash("dblink_global_transaction_level");
		public final static int dblink_remote_statement_autocommit = HashUtil.hash("dblink_remote_statement_autocommit");
		public final static int max_statements_per_session = HashUtil.hash("max_statements_per_session");
		public final static int ssl_cipher = HashUtil.hash("ssl_cipher");
		public final static int ssl_certificate_subject = HashUtil.hash("ssl_certificate_subject");
		public final static int ssl_certificate_issuer = HashUtil.hash("ssl_certificate_issuer");
		public final static int client_info = HashUtil.hash("client_info");
		public final static int module = HashUtil.hash("module");
		public final static int action = HashUtil.hash("action");
		public final static int elapse_time = HashUtil.hash("elapse_time");
		public final static int seqnum = HashUtil.hash("seqnum");
		public final static int event = HashUtil.hash("event");
		public final static int p1 = HashUtil.hash("p1");
		public final static int p2 = HashUtil.hash("p2");
		public final static int p3 = HashUtil.hash("p3");
		public final static int wait_time = HashUtil.hash("wait_time");
		public final static int second_in_time = HashUtil.hash("second_in_time");
		public final static int sql_hash = HashUtil.hash("sql_hash");
		public final static int sql_param = HashUtil.hash("sql_param");
		public final static int stat1 = HashUtil.hash("stat1");	// data page gets
		public final static int stat2 = HashUtil.hash("stat2");	// data page read
		public final static int stat3 = HashUtil.hash("stat3");	// memory table access count
		public final static int stat4 = HashUtil.hash("stat4");	// rebuild count
		public final static int stat5 = HashUtil.hash("stat5");	// execute success count
		public final static int stat1_sigma = HashUtil.hash("stat1_sigma");
		public final static int stat2_sigma = HashUtil.hash("stat2_sigma");
		public final static int stat3_sigma = HashUtil.hash("stat3_sigma");
		public final static int stat4_sigma = HashUtil.hash("stat4_sigma");
		public final static int stat5_sigma = HashUtil.hash("stat5_sigma");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(id, "id", null);
				handler.process(trans_id, "trans_id", null);
				handler.process(task_state, "task_state", null);
				handler.process(comm_name, "comm_name", null);
				handler.process(xa_session_flag, "xa_session_flag", null);
				handler.process(xa_associate_flag, "xa_associate_flag", null);
				handler.process(query_time_limit, "query_time_limit", null);
				handler.process(ddl_time_limit, "ddl_time_limit", null);
				handler.process(fetch_time_limit, "fetch_time_limit", null);
				handler.process(utrans_time_limit, "utrans_time_limit", null);
				handler.process(idle_time_limit, "idle_time_limit", null);
				handler.process(idle_start_time, "idle_start_time", null);
				handler.process(active_flag, "active_flag", null);
				handler.process(opened_stmt_count, "opened_stmt_count", null);
				handler.process(client_package_version, "client_package_version", null);
				handler.process(client_protocol_version, "client_protocol_version", null);
				handler.process(client_pid, "client_pid", null);
				handler.process(client_type, "client_type", null);
				handler.process(client_app_info, "client_app_info", null);
				handler.process(client_nls, "client_nls", null);
				handler.process(db_username, "db_username", null);
				handler.process(db_userid, "db_userid", null);
				handler.process(default_tbsid, "default_tbsid", null);
				handler.process(default_temp_tbsid, "default_temp_tbsid", null);
				handler.process(sysdba_flag, "sysdba_flag", null);
				handler.process(autocommit_flag, "autocommit_flag", null);
				handler.process(session_state, "session_state", null);
				handler.process(isolation_level, "isolation_level", null);
				handler.process(replication_mode, "replication_mode", null);
				handler.process(transaction_mode, "transaction_mode", null);
				handler.process(commit_write_wait_mode, "commit_write_wait_mode", null);
				handler.process(optimizer_mode, "optimizer_mode", null);
				handler.process(header_display_mode, "header_display_mode", null);
				handler.process(current_stmt_id, "current_stmt_id", null);
				handler.process(stack_size, "stack_size", null);
				handler.process(default_date_format, "default_date_format", null);
				handler.process(trx_update_max_logsize, "trx_update_max_logsize", null);
				handler.process(paralle_dml_mode, "paralle_dml_mode", null);
				handler.process(login_time, "login_time", null);
				handler.process(failover_source, "failover_source", null);
				handler.process(nls_territory, "nls_territory", null);
				handler.process(nls_iso_currency, "nls_iso_currency", null);
				handler.process(nls_currency, "nls_currency", null);
				handler.process(nls_numeric_characters, "nls_numeric_characters", null);
				handler.process(time_zone, "time_zone", null);
				handler.process(lob_cache_threshold, "lob_cache_threshold", null);
				handler.process(query_rewrite_enable, "query_rewrite_enable", null);
				handler.process(dblink_global_transaction_level, "dblink_global_transaction_level", null);
				handler.process(dblink_remote_statement_autocommit, "dblink_remote_statement_autocommit", null);
				handler.process(max_statements_per_session, "max_statements_per_session", null);
				handler.process(ssl_cipher, "ssl_cipher", null);
				handler.process(ssl_certificate_subject, "ssl_certificate_subject", null);
				handler.process(ssl_certificate_issuer, "ssl_certificate_issuer", null);
				handler.process(client_info, "client_info", null);
				handler.process(module, "module", null);
				handler.process(action, "action", null);
				handler.process(elapse_time, "elapse_time", null);
				handler.process(seqnum, "seqnum", null);
				handler.process(event, "event", null);
				handler.process(p1, "p1", null);
				handler.process(p2, "p2", null);
				handler.process(p3, "p3", null);
				handler.process(wait_time, "wait_time", null);
				handler.process(second_in_time, "second_in_time", null);
				handler.process(sql_hash, "sql_hash", null);
				handler.process(sql_param, "sql_param", null);
				handler.process(stat1, "stat1", null);
				handler.process(stat2, "stat2", null);
				handler.process(stat3, "stat3", null);
				handler.process(stat4, "stat4", null);
				handler.process(stat5, "stat5", null);
				handler.process(stat1_sigma, "stat1_sigma", null);
				handler.process(stat2_sigma, "stat2_sigma", null);
				handler.process(stat3_sigma, "stat3_sigma", null);
				handler.process(stat4_sigma, "stat4_sigma", null);
				handler.process(stat5_sigma, "stat5_sigma", null);
			} catch (Exception e) {
			}
		}
	}

	// 이걸 사용하지 않고, 실제 가져온 값을 사용한다.
	public static class Redis {
		public final static int id = HashUtil.hash("id");
		public final static int addr = HashUtil.hash("addr");
		public final static int fd = HashUtil.hash("fd");
		public final static int name = HashUtil.hash("name");
		public final static int age = HashUtil.hash("age");
		public final static int idle = HashUtil.hash("idle");
		public final static int flags = HashUtil.hash("flags");
		public final static int db = HashUtil.hash("db");
		public final static int sub = HashUtil.hash("sub");
		public final static int psub = HashUtil.hash("psub");
		public final static int multi = HashUtil.hash("multi");
		public final static int qbuf = HashUtil.hash("qbuf");
		public final static int qbuf_free = HashUtil.hash("qbuf-free");
		public final static int obl = HashUtil.hash("obl");
		public final static int oll = HashUtil.hash("oll");
		public final static int omem = HashUtil.hash("omem");
		public final static int events = HashUtil.hash("events");
		public final static int cmd = HashUtil.hash("cmd");
		public final static int user = HashUtil.hash("user");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(id, "id", null);
				handler.process(addr, "addr", null);
				handler.process(fd, "fd", null);
				handler.process(name, "name", null);
				handler.process(age, "age", null);
				handler.process(idle, "idle", null);
				handler.process(flags, "flags", null);
				handler.process(db, "db", null);
				handler.process(sub, "sub", null);
				handler.process(psub, "psub", null);
				handler.process(multi, "multi", null);
				handler.process(qbuf, "qbuf", null);
				handler.process(qbuf_free, "qbuf-free", null);
				handler.process(obl, "obl", null);
				handler.process(oll, "oll", null);
				handler.process(omem, "omem", null);
				handler.process(events, "events", null);
				handler.process(cmd, "cmd", null);
				handler.process(user, "user", null);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.DB_ACTIVE_SESSION_LIST;
	}
}
