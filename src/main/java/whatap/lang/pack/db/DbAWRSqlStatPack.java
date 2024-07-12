package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbAWRSqlStatPack extends AbstractDbTablePack {
	public final static int sql_id = HashUtil.hash("sql_id");
	public final static int sql_hash = HashUtil.hash("sql_hash");
	public final static int module = HashUtil.hash("module");
	public final static int action = HashUtil.hash("action");
	public final static int schema = HashUtil.hash("schema");
	public final static int fetchs = HashUtil.hash("fetchs");
	public final static int end_of_fetch = HashUtil.hash("end_of_fetch");
	public final static int sorts = HashUtil.hash("sorts");
	public final static int executions = HashUtil.hash("executions");
	public final static int px_servers_execs = HashUtil.hash("px_servers_execs");
	public final static int loads = HashUtil.hash("loads");
	public final static int invalidations = HashUtil.hash("invalidations");
	public final static int parse_calls = HashUtil.hash("parse_calls");
	public final static int disk_reads = HashUtil.hash("disk_reads");
	public final static int buffer_gets = HashUtil.hash("buffer_gets");
	public final static int row_processed = HashUtil.hash("row_processed");
	public final static int cpu_time = HashUtil.hash("cpu_time");
	public final static int elapsed_time = HashUtil.hash("elapsed_time");
	public final static int iowait = HashUtil.hash("iowait");
	public final static int clwait = HashUtil.hash("clwait");
	public final static int apwait = HashUtil.hash("apwait");
	public final static int ccwait = HashUtil.hash("ccwait");
	public final static int direct_writes = HashUtil.hash("direct_writes");
	public final static int plsexec_time = HashUtil.hash("plsexec_time");
	public final static int javexec_time = HashUtil.hash("javexec_time");
	public final static int io_offload_elig_bytes = HashUtil.hash("io_offload_elig_bytes");
	public final static int io_interconnect_bytes = HashUtil.hash("io_interconnect_bytes");
	public final static int physical_read_requests = HashUtil.hash("physical_read_requests");
	public final static int physical_read_bytes = HashUtil.hash("physical_read_bytes");
	public final static int physical_write_requests = HashUtil.hash("physical_write_requests");
	public final static int physical_write_bytes = HashUtil.hash("physical_write_bytes");
	public final static int optimized_physical_reads = HashUtil.hash("optimized_physical_reads");
	public final static int cell_uncompresed_bytes = HashUtil.hash("cell_uncompresed_bytes");
	public final static int io_offload_return_bytes = HashUtil.hash("io_offload_return_bytes");
	// bind_data 는 안함.

	public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(sql_id, "sql_id", null);
			handler.process(sql_hash, "sql_hash", null);
			handler.process(module, "module", null);
			handler.process(action, "action", null);
			handler.process(schema, "schema", null);
			handler.process(fetchs, "fetchs", null);
			handler.process(end_of_fetch, "end_of_fetch", null);
			handler.process(sorts, "sorts", null);
			handler.process(executions, "executions", null);
			handler.process(px_servers_execs, "px_servers_execs", null);
			handler.process(loads, "loads", null);
			handler.process(invalidations, "invalidations", null);
			handler.process(parse_calls, "parse_calls", null);
			handler.process(disk_reads, "disk_reads", null);
			handler.process(buffer_gets, "buffer_gets", null);
			handler.process(row_processed, "row_processed", null);
			handler.process(cpu_time, "cpu_time", null);
			handler.process(elapsed_time, "elapsed_time", null);
			handler.process(iowait, "iowait", null);
			handler.process(clwait, "clwait", null);
			handler.process(apwait, "apwait", null);
			handler.process(ccwait, "ccwait", null);
			handler.process(direct_writes, "direct_writes", null);
			handler.process(plsexec_time, "plsexec_time", null);
			handler.process(javexec_time, "javexec_time", null);
			handler.process(io_offload_elig_bytes, "io_offload_elig_bytes", null);
			handler.process(io_interconnect_bytes, "io_interconnect_bytes", null);
			handler.process(physical_read_requests, "physical_read_requests", null);
			handler.process(physical_read_bytes, "physical_read_bytes", null);
			handler.process(physical_write_requests, "physical_write_requests", null);
			handler.process(physical_write_bytes, "physical_write_bytes", null);
			handler.process(optimized_physical_reads, "optimized_physical_reads", null);
			handler.process(cell_uncompresed_bytes, "cell_uncompresed_bytes", null);
			handler.process(io_offload_return_bytes, "io_offload_return_bytes", null);
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.DB_AWR_SQLSTAT;
	}

}
