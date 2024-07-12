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


public class DbRealCounterPack extends AbstractDbCounterPack {
	public final static int cpu = HashUtil.hash("cpu");	// in oracle, this is for user + sys + iowait
	public final static int sys_cpu = HashUtil.hash("sys cpu");
	public final static int user_cpu = HashUtil.hash("user cpu");
	public final static int busy_cpu = HashUtil.hash("busy cpu");	// 200807 for oracle cpu without iowait
	public final static int iowait_cpu = HashUtil.hash("iowait cpu");
	public final static int memory_usage = HashUtil.hash("memory");
	public final static int cpu_cores = HashUtil.hash("cpu_cores");

	public final static int fatal_count = HashUtil.hash("fatal count");
	public final static int warning_count = HashUtil.hash("warning count");

	public final static int connect_error = HashUtil.hash("connect_error");
	public final static int max_connections = HashUtil.hash("max_connections");

	//public final static int active_sessions = HashUtil.hash("active sessions");

	public synchronized static void sendCommon(H3 <Integer, String, String> handler) {
		try {
			handler.process(cpu, "cpu", "%");
			handler.process(sys_cpu, "sys cpu", "%");
			handler.process(user_cpu, "user cpu", "%");
			handler.process(busy_cpu, "busy cpu", "%");
			handler.process(iowait_cpu, "iowait cpu", "%");
			handler.process(memory_usage, "memory", "%");
			handler.process(cpu_cores, "cpu_cores", null);

			handler.process(fatal_count, "fatal count", null);
			handler.process(warning_count, "warning count", null);

			handler.process(connect_error, "connect_error", null);
			handler.process(max_connections, "max_connections", null);
		} catch (Exception e) {
		}
	}

	public static class Pg {
		public final static int numbackends = HashUtil.hash("numbackends");
		public final static int commit_count = HashUtil.hash("commit count");
		public final static int rollback_count = HashUtil.hash("rollback count");
		public final static int blks_read = HashUtil.hash("blks_read");
		public final static int blks_hit = HashUtil.hash("blks_hit");
		//public final static int blks_read1 = HashUtil.hash("blks read");	// 삭제 예정. 200803 에 삭제.
		//public final static int blks_hit1 = HashUtil.hash("blks hit");	// 삭제 예정. 200803 에 삭제.
		public final static int tup_returned = HashUtil.hash("tup_returned");
		public final static int tup_fetched = HashUtil.hash("tup_fetched");
		public final static int tup_inserted = HashUtil.hash("tup_inserted");
		public final static int tup_updated = HashUtil.hash("tup_updated");
		public final static int tup_deleted = HashUtil.hash("tup_deleted");
		public final static int dml_count = HashUtil.hash("dml count");
		public final static int conflicts = HashUtil.hash("conflicts");
		public final static int temp_files = HashUtil.hash("temp_files");
		public final static int temp_bytes = HashUtil.hash("temp_bytes");
		public final static int deadlock_count = HashUtil.hash("deadlock count");
		public final static int blk_read_time = HashUtil.hash("blk_read_time");
		public final static int blk_write_time = HashUtil.hash("blk_write_time");
		public final static int blks_hit_ratio = HashUtil.hash("blks hit ratio");

	    public final static int check_point = HashUtil.hash("check point");
	    public final static int checkpoints_timed = HashUtil.hash("checkpoints timed");
	    public final static int checkpoint_write_time = HashUtil.hash("checkpoint write time");
	    public final static int buffers_checkpoint = HashUtil.hash("buffers checkpoint");

		public final static int active_sessions = HashUtil.hash("active sessions");
	    public final static int total_sessions = HashUtil.hash("total sessions");
	    public final static int total_runtime = HashUtil.hash("total runtime");
	    public final static int max_runtime = HashUtil.hash("max runtime");
	    public final static int long_running_sessions = HashUtil.hash("long running sessions");
	    public final static int wait_sessions = HashUtil.hash("wait sessions");
		public final static int lock_wait_sessions = HashUtil.hash("lock wait sessions");
		public final static int idle_trx = HashUtil.hash("idle_trx");
		public final static int idle_trx_time = HashUtil.hash("idle_trx_time");

		public final static int table_hit_ratio = HashUtil.hash("table hit ratio");
		public final static int index_hit_ratio = HashUtil.hash("index hit ratio");

		public final static int vacuum_running_count = HashUtil.hash("vacuum running count");

		public final static int node_state = HashUtil.hash("node_state");
		public final static int replication_delay = HashUtil.hash("replication_delay");
		public final static int replication_lag = HashUtil.hash("replication_lag");
		public final static int replication_broken = HashUtil.hash("replication_broken");
		public final static int replication_count = HashUtil.hash("replication_count");
//		public final static int wait_count = HashUtil.hash("wait_count");
//		public final static int total_wait = HashUtil.hash("total_wait");
		public final static int uptime = HashUtil.hash("uptime");
		public final static int seq_scan = HashUtil.hash("seq_scan");
		public final static int idx_scan = HashUtil.hash("idx_scan");
		public final static int wal_count = HashUtil.hash("wal_count");
		public final static int oldest_cur_xid = HashUtil.hash("oldest_cur_xid");
		public final static int per_to_wraparound = HashUtil.hash("per_to_wraparound");
		public final static int per_to_emergency_autovac = HashUtil.hash("per_to_emergency_autovac");
		public final static int cache = HashUtil.hash("cache");
		public final static int tps = HashUtil.hash("tps");
		public final static int confl_tablespace = HashUtil.hash("confl_tablespace");
		public final static int confl_lock = HashUtil.hash("confl_lock");
		public final static int confl_snapshot = HashUtil.hash("confl_snapshot");
		public final static int confl_bufferpin = HashUtil.hash("confl_bufferpin");
		public final static int confl_deadlock = HashUtil.hash("confl_deadlock");
		
		
		public synchronized static void send(H3 <Integer, String, String> handler) {
			sendCommon(handler);
			try {
				handler.process(numbackends, "numbackends", null);
				handler.process(commit_count, "commit count", null);
				handler.process(rollback_count, "rollback count", null);
				handler.process(blks_read, "blks_read", null);
				handler.process(blks_hit," blks_hit", null);
				//handler.process(blks_read1, "blks read", null);
				//handler.process(blks_hit1, "blks hit", null);
				handler.process(tup_returned, "tup_returned", null);
				handler.process(tup_fetched, "tup_fetched", null);
				handler.process(tup_inserted, "tup_inserted", null);
				handler.process(tup_updated, "tup_updated", null);
				handler.process(tup_deleted, "tup_deleted", null);
				handler.process(dml_count, "dml count", null);
				handler.process(conflicts, "conflicts", null);
				handler.process(temp_files, "temp_files", null);
				handler.process(temp_bytes, "temp_bytes", null);
				handler.process(deadlock_count, "deadlock count", null);
				handler.process(blk_read_time, "blk_read_time", null);
				handler.process(blk_write_time, "blk_write_time", null);
				handler.process(blks_hit_ratio, "blks hit ratio", null);

				handler.process(check_point, "check point", null);
				handler.process(checkpoints_timed, "checkpoints timed", null);
				handler.process(checkpoint_write_time, "checkpoint write time point", null);
				handler.process(buffers_checkpoint, "buffers checkpoint", null);

				handler.process(active_sessions, "active sessions", null);
				handler.process(total_sessions, "total sessions", null);
				handler.process(total_runtime, "total runtime", null);
				handler.process(long_running_sessions, "long running sessions", null);
				handler.process(wait_sessions, "wait sessions", null);
				handler.process(lock_wait_sessions, "lock wait sessions", null);
				handler.process(idle_trx, "idle_trx", null);
				handler.process(idle_trx_time, "idle_trx_time", null);

				handler.process(table_hit_ratio, "table hit ratio", null);
				handler.process(index_hit_ratio, "index hit ratio", null);

				handler.process(vacuum_running_count, "vacuum running count", null);

				handler.process(wait_sessions, "wait sessions", null);

				handler.process(node_state, "node_state", null);
				handler.process(replication_delay, "replication_delay", null);
				handler.process(replication_lag, "replication_lag", null);
				handler.process(replication_broken, "replication_broken", null);
				handler.process(replication_count, "replication_count", null);
//				handler.process(wait_count, "wait_count", null);
//				handler.process(total_wait, "total_wait", null);
				handler.process(uptime, "uptime", null);
				handler.process(seq_scan, "seq_scan", null);
				handler.process(idx_scan, "idx_scan", null);
				handler.process(wal_count, "wal_count", null);
				handler.process(oldest_cur_xid, "oldest_cur_xid", null);
				handler.process(per_to_wraparound, "per_to_wraparound", null);
				handler.process(per_to_emergency_autovac, "per_to_emergency_autovac", null);
				handler.process(cache, "cache", null);
				handler.process(tps, "tps", null);
				handler.process(confl_tablespace, "confl_tablespace", null);
				handler.process(confl_lock, "confl_lock", null);
				handler.process(confl_snapshot, "confl_snapshot", null);
				handler.process(confl_bufferpin, "confl_bufferpin", null);
				handler.process(confl_deadlock, "confl_deadlock", null);
			
			} catch (Exception e) {
			}
		}
	}

	public static class Oracle {
		public final static int active_sessions = HashUtil.hash("active sessions");
		public final static int total_sessions = HashUtil.hash("total sessions");
		public final static int lock_wait_sessions = HashUtil.hash("lock wait sessions");
		public final static int total_elapse_time = HashUtil.hash("total elapse time");
		public final static int long_running_sessions = HashUtil.hash("long running sessions");
		public final static int wait_sessions = HashUtil.hash("wait sessions");
		public final static int txn_sessions = HashUtil.hash("txn sessions");
		public final static int pq_sessions = HashUtil.hash("pq sessions");
		//public final static int session_logical_reads = HashUtil.hash("session logical reads");
		//public final static int execute_count = HashUtil.hash("execute count");
		public final static int buffer_cache_hit_ratio = HashUtil.hash("buffer cache hit ratio");
		public final static int soft_parse_ratio = HashUtil.hash("soft parse ratio");
		public final static int hard_parse_ratio = HashUtil.hash("hard parse ratio");
		public final static int library_cache_hit_ratio = HashUtil.hash("library cache hit ratio");
		public final static int library_cache_pin_hit_ratio = HashUtil.hash("library cache pin hit ratio");
		public final static int library_cache_get_hit_ratio = HashUtil.hash("library cache get hit ratio");
		public final static int latch_hit_ratio = HashUtil.hash("latch hit ratio");
		public final static int shared_pool_free_mem_ratio = HashUtil.hash("shared pool free mem ratio");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			sendCommon(handler);
			try {
				handler.process(active_sessions, "active sessions", null);
				handler.process(total_sessions, "total sessions", null);
				handler.process(lock_wait_sessions, "lock wait sessions", null);
				handler.process(total_elapse_time, "total elapse time", null);
				handler.process(long_running_sessions, "long running sessions", null);
				handler.process(wait_sessions, "wait sessions", null);
				handler.process(txn_sessions, "txn sessions", null);
				handler.process(pq_sessions, "pq sessions", null);
				handler.process(buffer_cache_hit_ratio, "buffer cache hit ratio", null);
				handler.process(soft_parse_ratio, "soft parse ratio", null);
				handler.process(hard_parse_ratio, "hard parse ratio", null);
				handler.process(library_cache_hit_ratio, "library cache hit ratio", null);
				handler.process(library_cache_pin_hit_ratio, "library cache pin hit ratio", null);
				handler.process(library_cache_get_hit_ratio, "library cache get hit ratio", null);
				handler.process(latch_hit_ratio, "latch hit ratio", null);
				handler.process(shared_pool_free_mem_ratio, "shared pool free mem ratio", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Mysql {
		public final static int writes = HashUtil.hash("Writes");
		public final static int innodb_rows_writes = HashUtil.hash("Innodb_rows_writes");
		public final static int long_running_sessions = HashUtil.hash("long running sessions");
	    public final static int total_runtime = HashUtil.hash("total runtime");
	    public final static int max_runtime = HashUtil.hash("max runtime");
		public final static int active_sessions = HashUtil.hash("active sessions");
		public final static int lock_wait_sessions = HashUtil.hash("lock wait sessions");

		public final static int threads_running = HashUtil.hash("Threads_running");
		public final static int questions = HashUtil.hash("Questions");
		public final static int innodb_buffer_pool_read_requests = HashUtil.hash("Innodb_buffer_pool_read_requests");
		public final static int innodb_row_lock_current_waits = HashUtil.hash("Innodb_row_lock_current_waits");

		public final static int node_state = HashUtil.hash("node_state");
		public final static int seconds_behind_master = HashUtil.hash("seconds_behind_master");
		public final static int slave_io_state = HashUtil.hash("slave_io_state");
		public final static int slave_sql_running_state = HashUtil.hash("slave_sql_running_state");

		// kwlee defined : replication_delay 의 값을 통일하여 표시할 지표.
		public final static int replication_delay = HashUtil.hash("replication_delay");
		public final static int replication_broken = HashUtil.hash("replication_broken");
		public final static int deadlock_sessions = HashUtil.hash("deadlock sessions");
		public final static int waiting_for_table_flush_sessions = HashUtil.hash("waiting for table flush sessions");
		public final static int trx_rseg_history_len = HashUtil.hash("trx_rseg_history_len");
		public final static int active_transactions = HashUtil.hash("active_transactions");
		

	    public synchronized static void send(H3 <Integer, String, String> handler) {
	    	sendCommon(handler);
			try {
				handler.process(total_runtime, "total runtime", null);
				handler.process(long_running_sessions, "long running sessions", null);
				handler.process(writes, "Writes", null);
				handler.process(innodb_rows_writes, "Innodb_rows_writes", null);
				handler.process(active_sessions, "active sessions", null);
				handler.process(lock_wait_sessions, "lock wait sessions", null);

				handler.process(node_state, "node_state", null);
				handler.process(seconds_behind_master, "seconds_behind_master", null);
				handler.process(slave_io_state, "slave_io_state", null);
				handler.process(slave_sql_running_state, "slave_sql_running_state", null);

				handler.process(replication_delay, "replication_delay", null);
				handler.process(replication_broken, "replication_broken", null);
				handler.process(deadlock_sessions, "deadlock sessions", null);
				handler.process(waiting_for_table_flush_sessions, "waiting for table flush sessions", null);
				handler.process(trx_rseg_history_len, "trx_rseg_history_len", null);
				handler.process(active_transactions, "active_transactions", null);
			} catch (Exception e) {
			}
	    }
	}

	public static class Mssql {
		public final static int active_sessions = HashUtil.hash("active sessions");
		public final static int total_sessions = HashUtil.hash("total sessions");
		public final static int total_elapsed_time = HashUtil.hash("total_elapsed_time");
		public final static int long_running_sessions = HashUtil.hash("long running sessions");
		public final static int lock_wait_sessions = HashUtil.hash("lock wait sessions");

	    public synchronized static void send(H3 <Integer, String, String> handler) {
	    	sendCommon(handler);
			try {
				handler.process(active_sessions, "active sessions", null);
				handler.process(total_sessions, "total sessions", null);
				handler.process(total_elapsed_time, "total_elapsed_time", null);
				handler.process(long_running_sessions, "long running sessions", null);
				handler.process(lock_wait_sessions, "lock wait sessions", null);
			} catch (Exception e) {
			}
	    }
	}

	public static class Tibero {
		public final static int active_sessions = HashUtil.hash("active sessions");
		public final static int total_sessions = HashUtil.hash("total sessions");
		public final static int lock_wait_sessions = HashUtil.hash("lock wait sessions");
		public final static int total_elapse_time = HashUtil.hash("total elapse time");
		public final static int long_running_sessions = HashUtil.hash("long running sessions");
		public final static int wait_sessions = HashUtil.hash("wait sessions");
		public final static int pq_sessions = HashUtil.hash("pq sessions");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			sendCommon(handler);
			try {
				handler.process(active_sessions, "active sessions", null);
				handler.process(total_sessions, "total sessions", null);
				handler.process(lock_wait_sessions, "lock wait sessions", null);
				handler.process(total_elapse_time, "total elapse time", null);
				handler.process(long_running_sessions, "long running sessions", null);
				handler.process(wait_sessions, "wait sessions", null);
				handler.process(pq_sessions, "pq sessions", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Cubrid {
		public final static int active_sessions = HashUtil.hash("active sessions");
		public final static int total_sessions = HashUtil.hash("total sessions");
		public final static int lock_wait_sessions = HashUtil.hash("lock wait sessions");
		public final static int total_query_time = HashUtil.hash("total query time");
		public final static int long_running_sessions = HashUtil.hash("long running sessions");
		public final static int executions = HashUtil.hash("executions");

		public final static int node_state = HashUtil.hash("node_state");	// 0 - none, 1 - master, 2 - slave
		
		public final static int psize = HashUtil.hash("psize");
		public final static int tps = HashUtil.hash("tps");
		public final static int qps = HashUtil.hash("qps");
		public final static int broker_count = HashUtil.hash("broker count");

		// kwlee defined : replication_delay 의 값을 통일하여 표시할 지표.
		public final static int replication_delay = HashUtil.hash("replication_delay");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			sendCommon(handler);
			try {
				handler.process(active_sessions, "active sessions", null);
				handler.process(total_sessions, "total sessions", null);
				handler.process(lock_wait_sessions, "lock wait sessions", null);
				handler.process(total_query_time, "total query time", null);
				handler.process(long_running_sessions, "long running sessions", null);
				handler.process(executions, "executions", null);

				handler.process(node_state, "node_state", null);
				handler.process(replication_delay, "replication_delay", null);
				handler.process(psize, "psize", null);
				handler.process(tps, "tps", null);
				handler.process(qps, "qps", null);
				handler.process(broker_count, "broker count", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Altibase {
		public final static int active_sessions = HashUtil.hash("active sessions");
		public final static int total_sessions = HashUtil.hash("total sessions");
		public final static int lock_wait_sessions = HashUtil.hash("lock wait sessions");
		public final static int total_elapse_time = HashUtil.hash("total elapse time");
		public final static int long_running_sessions = HashUtil.hash("long running sessions");
		public final static int wait_sessions = HashUtil.hash("wait sessions");
		public final static int pq_sessions = HashUtil.hash("pq sessions");
		public final static int rep_gap_max = HashUtil.hash("rep_gap_max");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			sendCommon(handler);
			try {
				handler.process(active_sessions, "active sessions", null);
				handler.process(total_sessions, "total sessions", null);
				handler.process(lock_wait_sessions, "lock wait sessions", null);
				handler.process(total_elapse_time, "total elapse time", null);
				handler.process(long_running_sessions, "long running sessions", null);
				handler.process(wait_sessions, "wait sessions", null);
				handler.process(pq_sessions, "pq sessions", null);
				handler.process(rep_gap_max, "rep_gap_max", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Redis {
		public final static int active_sessions = HashUtil.hash("active sessions");
		public final static int total_sessions = HashUtil.hash("total sessions");
		public final static int used_cpu = HashUtil.hash("used_cpu");
		public final static int used_cpu_children = HashUtil.hash("used_cpu_children");

	    public synchronized static void send(H3 <Integer, String, String> handler) {
	    	sendCommon(handler);
			try {
				handler.process(active_sessions, "active sessions", null);
				handler.process(total_sessions, "total sessions", null);
				handler.process(used_cpu, "used_cpu", null);
				handler.process(used_cpu_children, "used_cpu_children", null);
			} catch (Exception e) {
			}
	    }
	}

	@Override
	public short getPackType() {
		return PackEnum.DB_COUNTER_REAL;
	}
}
