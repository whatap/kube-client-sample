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
import whatap.lang.TextTypes;
import whatap.util.ArrayUtil;
import whatap.util.StringUtil;

import java.util.ArrayList;
import java.util.List;
public class TextPack extends AbstractPack {
	
	public static interface IText {

		int length();
		String toString(int length);
	}

	public static class Text implements IText {
		public Text(String str) {
			this.str = str;
		}

		public String str;

		public int length() {
			return str == null ? 0 : str.length();
		}

		public String toString() {
			return this.str;
		}
		
		public String toString(int length) {
			return StringUtil.truncate(this.str, length);
		}
		
	}

	//SQL 파라미터를 전송한다.
	public static class SqlParam implements IText {
		public int cipher;
		public byte[] p1;
		public byte[] p2;

		public SqlParam() {
		}

		public SqlParam(int cipher, byte[] p1, byte[] p2) {
			this.cipher = cipher;
			this.p1 = p1;
			this.p2 = p2;
		}

		public String toString() {
			return "****";
		}
		
		public String toString(int length) {
			return "****";
		}

		public int length() {
			return 4 + ArrayUtil.len(p1) + ArrayUtil.len(p2);
		}
	}

	public static class TextRec {
		public byte type;
		public int hash;
		public IText text;
		public TextRec() {
		}
		public TextRec(byte type, int hash, String text) {
			this.type = type;
			this.hash = hash;
			this.text = new Text(text);
		}
		public TextRec(byte type, int hash, SqlParam p) {
			this.type = type;
			this.hash = hash;
			this.text =p;
		}
		@Override
		public String toString() {
			return "[type=" + type + ", hash=" + hash + ", text=" + text + "]";
		}
		
	}
	public List<TextRec> records = new ArrayList<TextRec>();
	public TextPack() {
	}
	public short getPackType() {
		return PackEnum.TEXT;
	}
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append("TEXT");
		sb.append(super.toString());
		sb.append(",records=" + records.size());
		return sb.toString();
	}
	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeDecimal(records.size());
		for (int i = 0; i < this.records.size(); i++) {
			TextRec r = this.records.get(i);
			dout.writeByte(r.type);
			dout.writeInt(r.hash);
			if (r.type != TextTypes.SQLPARAM) {
				dout.writeText(((Text) r.text).str);
			} else {
				SqlParam sql = (SqlParam) r.text;
				dout.writeInt(sql.cipher);
				dout.writeBlob(sql.p1);
				dout.writeBlob(sql.p2);
			}
		}
	}
	public Pack read(DataInputX din) {
		super.read(din);
		int size = (int) din.readDecimal();
		for (int i = 0; i < size; i++) {
			TextRec r = new TextRec();
			r.type = din.readByte();
			r.hash = din.readInt();
			if (r.type != TextTypes.SQLPARAM) {
				r.text = new Text(din.readText());
			} else {
				SqlParam sql = new SqlParam();
				sql.cipher = din.readInt();
				sql.p1 = din.readBlob();
				sql.p2 = din.readBlob();
				r.text = sql;
			}
			this.records.add(r);
		}
		return this;
	}
}
