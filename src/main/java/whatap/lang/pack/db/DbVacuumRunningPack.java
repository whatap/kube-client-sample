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

public class DbVacuumRunningPack extends AbstractDbTablePack {
	public final static int pid = HashUtil.hash("pid");
	public final static int datid = HashUtil.hash("datid");
	public final static int datname = HashUtil.hash("datname");
	public final static int relid = HashUtil.hash("relid");
	public final static int phase = HashUtil.hash("phase");
	public final static int heap_blks_total = HashUtil.hash("heap blks total");
	public final static int heap_blks_scanned = HashUtil.hash("heap blks scanned");
	public final static int heap_blks_vacuumed = HashUtil.hash("heap blks vacuumed");
	public final static int index_vacuum_count = HashUtil.hash("index vacuum count");
	public final static int max_dead_tuples = HashUtil.hash("max dead tuples");
	public final static int num_dead_tuples = HashUtil.hash("num dead tuples");

    public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(pid, "pid", null);
			handler.process(datid, "datid", null);
			handler.process(datname, "datname", null);
			handler.process(relid, "relid", null);
			handler.process(phase, "phase", null);
			handler.process(heap_blks_total, "heap blks total", null);
			handler.process(heap_blks_scanned, "heap blks scanned", null);
			handler.process(heap_blks_vacuumed, "heap blks vacuumed", null);
			handler.process(index_vacuum_count, "index vacuum count", null);
			handler.process(max_dead_tuples, "max dead tuples", null);
			handler.process(num_dead_tuples, "num dead tuples", null);
		} catch (Exception e) {
		}
	}

	public short getPackType() {
		return PackEnum.DB_VACUUM_RUNNING;
	}

}
