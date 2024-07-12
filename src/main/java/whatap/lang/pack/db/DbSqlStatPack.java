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

import whatap.io.DataIOException;
import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.H3;
import whatap.lang.pack.AbstractPack;
import whatap.lang.pack.Pack;
import whatap.lang.pack.PackEnum;
import whatap.lang.value.IntMapValue;
import whatap.util.HashUtil;
import whatap.util.IntKeyLinkedMap;
import whatap.util.IntKeyLinkedMap.IntKeyLinkedEntry;
import whatap.util.StringKeyLinkedMap;
import whatap.util.StringKeyLinkedMap.StringKeyLinkedEntry;

import java.io.IOException;
import java.util.Enumeration;

public class DbSqlStatPack extends AbstractPack {
//	public final static short sql_hash = 1;
//	public final static short sql_elapse = 2;
//	public final static short sql_elapse_wait = 3;
//	public final static short sql_elapse_max = 4;
//	public final static short sql_execute_cnt = 5;
//
//	public static IntKeyMap<String> values = ClassUtil.getConstantValueMap(DbSqlStatPack.class, Short.TYPE);
//	public static StringIntMap names = ClassUtil.getConstantKeyMap(DbSqlStatPack.class, Short.TYPE);

	// tag
	public final static short ALL = 0;
	public final static short DB = 1;


	public static class Oracle {
		// tag
		public final static short SCHEMANAME = 2;
		public final static short MACHINE = 3;
		public final static short PROGRAM = 4;
		public final static short MODULE = 5;
		public final static short OSUSER = 6;


		public final static int sqlHash = HashUtil.hash("sqlHash");

		public final static int schemaName = HashUtil.hash("schemaName");
		public final static int machine = HashUtil.hash("machine");
		public final static int program = HashUtil.hash("program");
		public final static int module = HashUtil.hash("module");
		public final static int osUser = HashUtil.hash("osUser");
		public final static int sql_id = HashUtil.hash("sql_id");

		public final static int sql_elapse = HashUtil.hash("sql elapse");
		public final static int sql_elapse_wait = HashUtil.hash("sql elapse wait");
		public final static int sql_elapse_max = HashUtil.hash("sql elapse max");
		public final static int sql_execute_cnt = HashUtil.hash("sql execute cnt");
		public final static int stat1 = HashUtil.hash("stat1");	// session logical reads
		public final static int stat2 = HashUtil.hash("stat2");	// physical reads
		public final static int stat3 = HashUtil.hash("stat3");	// parse count (hard)
		public final static int stat4 = HashUtil.hash("stat4");	// parse count (total)
		public final static int stat5 = HashUtil.hash("stat5");	// db block changes
		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
			handler.process(sqlHash, "sqlHash", null);

			handler.process(schemaName, "schemaName", null);
			handler.process(machine, "machine", null);
			handler.process(program, "program", null);
			handler.process(module, "module", null);
			handler.process(osUser, "osUser", null);
			
			handler.process(sql_id, "sql_id", null);
			handler.process(sql_elapse, "sql elapse", null);
			handler.process(sql_elapse_wait, "sql elapse wait", null);
			handler.process(sql_elapse_max, "sql elapse max", null);
			handler.process(sql_execute_cnt, "sql execute cnt", null);
			handler.process(stat1, "stat1", null);
			handler.process(stat2, "stat2", null);
			handler.process(stat3, "stat3", null);
			handler.process(stat4, "stat4", null);
			handler.process(stat5, "stat5", null);

			} catch (Exception e) {
			}
		}
	}

	public static class Pg {
		// tag
		public final static short USER = 2;
		public final static short HOST = 3;
		public final static short APPLICATION = 4;


		public final static int sqlHash = HashUtil.hash("sqlHash");
		public final static int user = HashUtil.hash("user");
		public final static int host = HashUtil.hash("host");
		public final static int db = HashUtil.hash("db");
		public final static int application = HashUtil.hash("application");
		public final static int query_id = HashUtil.hash("query id");

		public final static int sql_elapse = HashUtil.hash("sql elapse");
		public final static int sql_elapse_wait = HashUtil.hash("sql elapse wait");
		public final static int sql_elapse_max = HashUtil.hash("sql elapse max");
		public final static int sql_execute_cnt = HashUtil.hash("sql execute cnt");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
			handler.process(sqlHash, "sqlHash", null);

			handler.process(user, "user", null);
			handler.process(host, "host", null);
			handler.process(db, "db", null);
			handler.process(application, "application", null);
			handler.process(query_id, "query id", null);

			handler.process(sql_elapse, "sql elapse", null);
			handler.process(sql_elapse_wait, "sql elapse wait", null);
			handler.process(sql_elapse_max, "sql elapse max", null);
			handler.process(sql_execute_cnt, "sql execute cnt", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Mysql {
		// tag
		public final static short USER = 2;
		public final static short HOST = 3;


		public final static int sqlHash = HashUtil.hash("sqlHash");

		public final static int user = HashUtil.hash("user");
		public final static int host = HashUtil.hash("host");
		public final static int db = HashUtil.hash("db");

		public final static int sql_elapse = HashUtil.hash("sql elapse");
		public final static int sql_elapse_max = HashUtil.hash("sql elapse max");
		public final static int sql_execute_cnt = HashUtil.hash("sql execute cnt");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
			handler.process(sqlHash, "sqlHash", null);

			handler.process(user, "user", null);
			handler.process(host, "host", null);
			handler.process(db, "db", null);

			handler.process(sql_elapse, "sql elapse", null);
			handler.process(sql_elapse_max, "sql elapse max", null);
			handler.process(sql_execute_cnt, "sql execute cnt", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Mssql {
		// tag
		public final static short USER = 2;
		public final static short HOST = 3;
		public final static short APPLICATION = 4;
		public final static short CLIENT = 5;

		public final static int sqlHash = HashUtil.hash("sqlHash");

		public final static int db = HashUtil.hash("db");
		public final static int host = HashUtil.hash("host");
		public final static int application = HashUtil.hash("application");
		public final static int user = HashUtil.hash("user");
		public final static int client= HashUtil.hash("client");

		public final static int sql_elapse = HashUtil.hash("sql elapse");
		public final static int sql_elapse_max = HashUtil.hash("sql elapse max");
		public final static int sql_execute_cnt = HashUtil.hash("sql execute cnt");
		public final static int stat1 = HashUtil.hash("stat1");	// cpu_time
		public final static int stat2 = HashUtil.hash("stat2");	// reads
		public final static int stat3 = HashUtil.hash("stat3");	// writes
		public final static int stat4 = HashUtil.hash("stat4");	// logical_reads

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
			handler.process(sqlHash, "sqlHash", null);

			handler.process(db, "db", null);
			handler.process(host, "host", null);
			handler.process(application, "application", null);
			handler.process(user, "user", null);
			handler.process(client, "client", null);

			handler.process(sql_elapse, "sql elapse", null);
			handler.process(sql_elapse_max, "sql elapse max", null);
			handler.process(sql_execute_cnt, "sql execute cnt", null);
			handler.process(stat1, "stat1", null);
			handler.process(stat2, "stat2", null);
			handler.process(stat3, "stat3", null);
			handler.process(stat4, "stat4", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Tibero {
		// tag
		public final static short USER = 2;
		public final static short MACHINE = 3;
		public final static short PROGRAM = 4;
		public final static short MODULE = 5;
		public final static short OSUSER = 6;


		public final static int sqlHash = HashUtil.hash("sqlHash");

		public final static int schemaName = HashUtil.hash("schemaName");
		public final static int machine = HashUtil.hash("machine");
		public final static int program = HashUtil.hash("program");
		public final static int module = HashUtil.hash("module");
		public final static int osUser = HashUtil.hash("osUser");
		public final static int sql_id = HashUtil.hash("sql_id");

		public final static int sql_elapse = HashUtil.hash("sql elapse");
		public final static int sql_elapse_wait = HashUtil.hash("sql elapse wait");
		public final static int sql_elapse_max = HashUtil.hash("sql elapse max");
		public final static int sql_execute_cnt = HashUtil.hash("sql execute cnt");
		public final static int stat1 = HashUtil.hash("stat1");	// session logical reads
		public final static int stat2 = HashUtil.hash("stat2");	// physical reads
		public final static int stat3 = HashUtil.hash("stat3");	// parse count (hard)
		public final static int stat4 = HashUtil.hash("stat4");	// parse count (total)
		public final static int stat5 = HashUtil.hash("stat5");	// db block changes
		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
			handler.process(sqlHash, "sqlHash", null);

			handler.process(schemaName, "schemaName", null);
			handler.process(machine, "machine", null);
			handler.process(program, "program", null);
			handler.process(module, "module", null);
			handler.process(osUser, "osUser", null);
			handler.process(sql_id, "sql_id", null);

			handler.process(sql_elapse, "sql elapse", null);
			handler.process(sql_elapse_wait, "sql elapse wait", null);
			handler.process(sql_elapse_max, "sql elapse max", null);
			handler.process(sql_execute_cnt, "sql execute cnt", null);
			handler.process(stat1, "stat1", null);
			handler.process(stat2, "stat2", null);
			handler.process(stat3, "stat3", null);
			handler.process(stat4, "stat4", null);
			handler.process(stat5, "stat5", null);

			} catch (Exception e) {
			}
		}
	}

	public static class Cubrid {
		// tag
		public final static short USER = 2;
		public final static short HOST = 3;
		public final static short PROGRAM = 4;

		public final static int sqlHash = HashUtil.hash("sqlHash");

		public final static int user = HashUtil.hash("user");
		public final static int host = HashUtil.hash("host");
		public final static int db = HashUtil.hash("db");
		public final static int program = HashUtil.hash("program");

		public final static int sql_elapse = HashUtil.hash("sql elapse");
		public final static int sql_elapse_max = HashUtil.hash("sql elapse max");
		public final static int sql_execute_cnt = HashUtil.hash("sql execute cnt");

		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
			handler.process(sqlHash, "sqlHash", null);

			handler.process(user, "user", null);
			handler.process(host, "host", null);
			handler.process(db, "db", null);
			handler.process(db, "program", null);

			handler.process(sql_elapse, "sql elapse", null);
			handler.process(sql_elapse_max, "sql elapse max", null);
			handler.process(sql_execute_cnt, "sql execute cnt", null);
			} catch (Exception e) {
			}
		}
	}

	public static class Altibase {
		// tag
		public final static short USERNAME = 2;
		public final static short COMM_NAME = 3;
		public final static short CLIENT_APP = 4;
		public final static short MODULE = 5;


		public final static int sqlHash = HashUtil.hash("sqlHash");

		public final static int username = HashUtil.hash("username");
		public final static int comm_name = HashUtil.hash("comm_name");
		public final static int client_app = HashUtil.hash("client_app");
		public final static int module = HashUtil.hash("module");

		public final static int sql_elapse = HashUtil.hash("sql elapse");
		public final static int sql_elapse_wait = HashUtil.hash("sql elapse wait");
		public final static int sql_elapse_max = HashUtil.hash("sql elapse max");
		public final static int sql_execute_cnt = HashUtil.hash("sql execute cnt");
		public final static int stat1 = HashUtil.hash("stat1");	// data page gets
		public final static int stat2 = HashUtil.hash("stat2");	// data page read
		public final static int stat3 = HashUtil.hash("stat3");	// memory table access count
		public final static int stat4 = HashUtil.hash("stat4");	// rebuild count
		public final static int stat5 = HashUtil.hash("stat5");	// execute success count
		public synchronized static void send(H3 <Integer, String, String> handler) {
			try {
			handler.process(sqlHash, "sqlHash", null);

			handler.process(username, "username", null);
			handler.process(comm_name, "comm_name", null);
			handler.process(client_app, "client_app", null);
			handler.process(module, "module", null);

			handler.process(sql_elapse, "sql elapse", null);
			handler.process(sql_elapse_wait, "sql elapse wait", null);
			handler.process(sql_elapse_max, "sql elapse max", null);
			handler.process(sql_execute_cnt, "sql execute cnt", null);
			handler.process(stat1, "stat1", null);
			handler.process(stat2, "stat2", null);
			handler.process(stat3, "stat3", null);
			handler.process(stat4, "stat4", null);
			handler.process(stat5, "stat5", null);

			} catch (Exception e) {
			}
		}
	}

	public short tag;

	public StringKeyLinkedMap<IntKeyLinkedMap<IntMapValue>> table = new StringKeyLinkedMap<IntKeyLinkedMap<IntMapValue>>();

	public String[] getKeyNames() {
		return table.keyArray();
	}

	public int size() {
		return table.size();
	}

	public int size(String keyName) {
		IntKeyLinkedMap<IntMapValue> m = table.get(keyName);
		return m == null ? 0 : m.size();
	}

	public void put(String keyName, int sql, IntMapValue r) {
		IntKeyLinkedMap<IntMapValue> m = table.get(keyName);
		if (m == null) {
			m = new IntKeyLinkedMap<IntMapValue>();
			table.put(keyName, m);
		}
		m.put(sql, r);
	}
	public void put(String keyName, IntKeyLinkedMap<IntMapValue> r) {
		table.put(keyName, r);
	}

	public IntMapValue get(String keyName, int sql) {
		IntKeyLinkedMap<IntMapValue> dbm = this.table.get(keyName);
		return dbm != null ? dbm.get(sql) : null;
	}

	//public IntKeyLinkedMap<IntMapValue> get(String db) {
		//return this.table.get(db);
	//}

	@Override
	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeShort(this.table.size());
		Enumeration<StringKeyLinkedEntry<IntKeyLinkedMap<IntMapValue>>> en = this.table.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<IntKeyLinkedMap<IntMapValue>> ent = en.nextElement();
			dout.writeText(ent.getKey());
			dout.writeShort(ent.getValue().size());
			Enumeration<IntKeyLinkedEntry<IntMapValue>> en2 = ent.getValue().entries();
			while (en2.hasMoreElements()) {
				IntKeyLinkedEntry<IntMapValue> ent2 = en2.nextElement();
				dout.writeInt(ent2.getKey());
				try {
					ent2.getValue().write(dout);
				}catch(IOException e){
					throw new DataIOException(e);
				}
			}
		}
		dout.writeShort(this.tag);
	}

	@Override
	public Pack read(DataInputX din) {
		super.read(din);
		int sz = din.readShort();
		for (int i = 0; i < sz; i++) {
			String keyName = din.readText();
			int sqlSz = din.readShort();
			IntKeyLinkedMap<IntMapValue> sqlSet = new IntKeyLinkedMap<IntMapValue>();
			for (int j = 0; j < sqlSz; j++) {
				int key = din.readInt();
				try {
					sqlSet.put(key, (IntMapValue) new IntMapValue().read(din));
				}catch(IOException e){
					throw new DataIOException(e);
				}
			}

			this.table.put(keyName, sqlSet);
		}
		this.tag = din.readShort();
		return this;
	}

	@Override
	public short getPackType() {
		return PackEnum.DB_SQL_STAT;
	}
}
