package whatap.lang.pack.db;

import whatap.lang.H3;
import whatap.lang.pack.PackEnum;
import whatap.util.HashUtil;

public class DbCloudWatchPack extends AbstractDbTablePack {
		
		public final static int cw_time = HashUtil.hash("cw_time");
		public final static int cw_metric = HashUtil.hash("cw_metric");
		public final static int cw_value = HashUtil.hash("cw_value");
		public final static int cw_unit = HashUtil.hash("cw_unit");
		
		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
				handler.process(cw_time, "cw_time", null);
				handler.process(cw_metric, "cw_metric", null);
				handler.process(cw_value, "cw_value", null);
				handler.process(cw_unit, "cw_unit", null);
			} catch (Exception e) {
			}
		}

		public short getPackType() {
			return PackEnum.DB_CLOUD_WATCH;
		}

}
