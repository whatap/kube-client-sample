package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbStatementsPack extends AbstractDbTablePack {
	public final static int query_param = HashUtil.hash("query param");
	public final static int query_hash = HashUtil.hash("query hash");
	
	// mysql
	public final static int digest = HashUtil.hash("digest");
	public final static int schema_name = HashUtil.hash("schema_name");
	public final static int digest_text = HashUtil.hash("digest_text");
	public final static int count_star = HashUtil.hash("count_star");
	public final static int sum_timer_wait = HashUtil.hash("sum_timer_wait");
	public final static int sum_lock_time = HashUtil.hash("sum_lock_time");
	public final static int sum_rows_affected = HashUtil.hash("sum_rows_affected");
	public final static int sum_rows_sent = HashUtil.hash("sum_rows_sent");
	public final static int sum_rows_examined = HashUtil.hash("sum_rows_examined");
	public final static int sum_created_tmp_disk_tables = HashUtil.hash("sum_created_tmp_disk_tables");
	public final static int sum_created_tmp_tables = HashUtil.hash("sum_created_tmp_tables");
	public final static int sum_select_full_join = HashUtil.hash("sum_select_full_join");
	public final static int sum_select_full_range_join = HashUtil.hash("sum_select_full_range_join");
	public final static int sum_select_range = HashUtil.hash("sum_select_range");
	public final static int sum_select_range_check = HashUtil.hash("sum_select_range_check");
	public final static int sum_select_scan = HashUtil.hash("sum_select_scan");
	public final static int sum_sort_merge_passes = HashUtil.hash("sum_sort_merge_passes");
	public final static int sum_sort_range = HashUtil.hash("sum_sort_range");
	public final static int sum_sort_rows = HashUtil.hash("sum_sort_rows");
	public final static int sum_sort_scan = HashUtil.hash("sum_sort_scan");
	public final static int sum_no_index_used = HashUtil.hash("sum_no_index_used");
	public final static int sum_no_good_index_used = HashUtil.hash("sum_no_good_index_used");
	public final static int first_seen = HashUtil.hash("first_seen");
	public final static int last_seen = HashUtil.hash("last_seen");

	// oracle
	public final static int db = HashUtil.hash("db"); // db name or con name
	public final static int owner = HashUtil.hash("owner"); // oracle
	public final static int mb = HashUtil.hash("MB"); // oracle

	
	
	// postgresql
	public final static int userid = HashUtil.hash("userid");
	public final static int dbid = HashUtil.hash("dbid");
	public final static int queryid = HashUtil.hash("queryid");
	public final static int query = HashUtil.hash("query");
	public final static int calls = HashUtil.hash("calls");
	public final static int total_time = HashUtil.hash("total_time");
	public final static int rows = HashUtil.hash("rows");
	public final static int shared_blks_hit = HashUtil.hash("shared_blks_hit");
	public final static int shared_blks_read = HashUtil.hash("shared_blks_read");
	public final static int shared_blks_dirtied = HashUtil.hash("shared_blks_dirtied");
	public final static int shared_blks_written = HashUtil.hash("shared_blks_written");
	public final static int local_blks_hit = HashUtil.hash("local_blks_hit");
	public final static int local_blks_read = HashUtil.hash("local_blks_read");
	public final static int local_blks_dirtied = HashUtil.hash("local_blks_dirtied");
	public final static int local_blks_written = HashUtil.hash("local_blks_written");
	public final static int temp_blks_read = HashUtil.hash("temp_blks_read");
	public final static int temp_blks_written = HashUtil.hash("temp_blks_written");
	public final static int blk_read_time = HashUtil.hash("blk_read_time");
	public final static int blk_write_time = HashUtil.hash("blk_write_time");

	public synchronized static void send(H3<Integer, String, String> handler) {
		try {
			handler.process(query_hash, "query hash", null);
			handler.process(query_param, "query param", null);
			
			handler.process(digest, "digest", null);
			handler.process(schema_name, "schema_name", null);
			handler.process(digest_text, "digest_text", null);
			handler.process(count_star, "count_star", null);
			handler.process(sum_timer_wait, "sum_timer_wait", null);
			handler.process(sum_lock_time, "sum_lock_time", null);
			handler.process(sum_rows_affected, "sum_rows_affected", null);
			handler.process(sum_rows_sent, "sum_rows_sent", null);
			handler.process(sum_rows_examined, "sum_rows_examined", null);
			handler.process(sum_created_tmp_disk_tables, "sum_created_tmp_disk_tables", null);
			handler.process(sum_created_tmp_tables, "sum_created_tmp_tables", null);
			handler.process(sum_select_full_join, "sum_select_full_join", null);
			handler.process(sum_select_full_range_join, "sum_select_full_range_join", null);
			handler.process(sum_select_range, "sum_select_range", null);
			handler.process(sum_select_range_check, "sum_select_range_check", null);
			handler.process(sum_select_scan, "sum_select_scan", null);
			handler.process(sum_sort_merge_passes, "sum_sort_merge_passes", null);
			handler.process(sum_sort_range, "sum_sort_range", null);
			handler.process(sum_sort_rows, "sum_sort_rows", null);
			handler.process(sum_sort_scan, "sum_sort_scan", null);
			handler.process(sum_no_index_used, "sum_no_index_used", null);
			handler.process(sum_no_good_index_used, "sum_no_good_index_used", null);
			handler.process(first_seen, "first_seen", null);
			handler.process(last_seen, "last_seen", null);

			handler.process(db, "db", null);
			handler.process(owner, "owner", null);
			handler.process(mb, "MB", null);

			handler.process(userid, "userid", null);
			handler.process(dbid, "dbid", null);
			handler.process(queryid, "queryid", null);
			handler.process(query, "query", null);
			handler.process(calls, "calls", null);
			handler.process(total_time, "total_time", null);
			handler.process(rows, "rows", null);
			handler.process(shared_blks_hit, "shared_blks_hit", null);
			handler.process(shared_blks_read, "shared_blks_read", null);
			handler.process(shared_blks_dirtied, "shared_blks_dirtied", null);
			handler.process(shared_blks_written, "shared_blks_written", null);
			handler.process(local_blks_hit, "local_blks_hit", null);
			handler.process(local_blks_read, "local_blks_read", null);
			handler.process(local_blks_dirtied, "local_blks_dirtied", null);
			handler.process(local_blks_written, "local_blks_written", null);
			handler.process(temp_blks_read, "temp_blks_read", null);
			handler.process(temp_blks_written, "temp_blks_written", null);
			handler.process(blk_read_time, "blk_read_time", null);
			handler.process(blk_write_time, "blk_write_time", null);
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
		return PackEnum.DB_STATEMENTS;
	}
}
