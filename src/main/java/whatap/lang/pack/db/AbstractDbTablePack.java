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
import whatap.lang.pack.AbstractPack;
import whatap.lang.pack.Pack;
import whatap.lang.value.IntMapValue;
import whatap.util.StringKeyLinkedMap;
import whatap.util.StringKeyLinkedMap.StringKeyLinkedEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

abstract public class AbstractDbTablePack extends AbstractPack {
	protected StringKeyLinkedMap<List<IntMapValue>> dbms = new StringKeyLinkedMap<List<IntMapValue>>() {
		protected List<IntMapValue> create(String db) {
			return new ArrayList<IntMapValue>();
		};
	};

	public String[] getDBNames() {
		return dbms.keyArray();
	}

	public int size() {
		return dbms.size();
	}

	public int size(String dbname) {
		List<IntMapValue> m = dbms.get(dbname);
		return m == null ? 0 : m.size();
	}

	public void add(String db, IntMapValue row) {
		this.dbms.intern(db).add(row);
	}
	public void add(String db, List<IntMapValue> rows) {
		this.dbms.put(db, rows);
	}

	public IntMapValue get(String db, int inx) {
		List<IntMapValue> dbm = this.dbms.get(db);
		return dbm != null ? dbm.get(inx) : null;
	}

	public List<IntMapValue> get(String db) {
		return this.dbms.get(db);
	}

	abstract public short getPackType();

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeShort(this.dbms.size());
		Enumeration<StringKeyLinkedEntry<List<IntMapValue>>> en = this.dbms.entries();
		while (en.hasMoreElements()) {
			StringKeyLinkedEntry<List<IntMapValue>> ent = en.nextElement();
			dout.writeText(ent.getKey());
			dout.writeShort(ent.getValue().size());
			for (IntMapValue r : ent.getValue()) {
				try {
					r.write(dout);
				}catch(IOException e){
					throw new DataIOException(e);
				}
			}
		}
	}

	public Pack read(DataInputX din) {
		super.read(din);
		int sz = din.readShort();
		for (int i = 0; i < sz; i++) {
			String db = din.readText();
			int listSz = din.readShort();
			List<IntMapValue> list = new ArrayList<IntMapValue>();
			for (int j = 0; j < listSz; j++) {
				try {
					list.add((IntMapValue) new IntMapValue().read(din));
				}catch(IOException e){
					throw new DataIOException(e);
				}
			}

			this.dbms.put(db, list);
		}
		return this;
	}

}
