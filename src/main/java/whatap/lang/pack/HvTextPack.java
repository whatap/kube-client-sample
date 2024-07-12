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
package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.LongEnumer;
import whatap.util.LongKeyMap;

import java.util.ArrayList;
import java.util.List;

public class HvTextPack extends AbstractPack {
	public static class Record {
		public String type;
		public long hash;
		public String text;

		public Record(String type, long hash, String text) {
			this.type = type;
			this.hash = hash;
			this.text = text;
		}
	}

	public List<Record> records = new ArrayList<Record>();

	public HvTextPack() {
	}

	public short getPackType() {
		return PackEnum.HvTEXT;
	}

	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("HvText");
		sb.append(super.toString());
		sb.append(",records=" + records.size());
		return sb.toString();
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeByte(0);
		byte[] b = writeRec(this.records);
		dout.writeBlob(b);
	}

	private static byte[] writeRec(List<Record> records) {
		DataOutputX o = new DataOutputX();
		o.writeDecimal(records.size());
		for (int i = 0; i < records.size(); i++) {
			Record r = records.get(i);
			o.writeText(r.type);
			o.writeLong(r.hash);
			o.writeText(r.text);
		}
		return o.toByteArray();
	}

	public Pack read(DataInputX din) {
		super.read(din);
		byte ver = din.readByte();
		byte[] b = din.readBlob();
		readRec(this.records, b);
		return this;
	}

	private static void readRec(List<Record> records, byte[] b) {
		DataInputX in = new DataInputX(b);
		int size = (int) in.readDecimal();
		for (int i = 0; i < size; i++) {
			String type = in.readText();
			long hash = in.readLong();
			String text = in.readText();
			records.add(new Record(type, hash, text));
		}
	}

	public void set(String type, LongKeyMap<String> m) {
		LongEnumer en = m.keys();
		while (en.hasMoreElements()) {
			long hash = en.nextLong();
			String val = m.get(hash);
			this.records.add(new Record(type, hash, val));
		}
	}

	public void add(Record r) {
		this.records.add(r);
	}

	public void add(String type, long hash, String text) {
		this.records.add(new Record(type, hash, text));
	}
}
