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
package whatap.lang.service;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.EventLevel;
import whatap.lang.value.MapValue;
import whatap.lang.value.Value;
import whatap.util.StringKeyLinkedMap.StringKeyLinkedEntry;

import java.util.Enumeration;

public class TxRecord {
	public long txid;
	public long endTime;

	public int service;
	public int elapsed;
	public long error;

	public int cpuTime;
	public long malloc;

	public int sqlCount;
	public int sqlTime;
	public int sqlFetchCount;
	public int sqlFetchTime;
	
	public int httpcCount;
	public int httpcTime;

	public boolean active;
	public long step_data_pos;
	public int cipher;

	public int ipaddr;
	public long wclientId;
	public int userAgent;
	public int referer;
	public int status;

	public long mtid;
	public int mdepth;
	public long mcaller;//txid

	public long mcaller_pcode;
	public int mcaller_okind;
	public int mcaller_oid;
	public int mcaller_spec;
	public int mcaller_url;
	public int mthis_spec;

	public byte http_method;

	public int domain;
	public MapValue fields;

	public int login;

	public byte errorLevel;

	public int oid;
	public int okind;
	public int onode;

	public String custid;

    public int dbcTime;
    
    public byte apdex;
    
	public long mcallerStepId;
    public String originUrl;
    
    public int step_split_count;

    public int methodCount;
   	public int methodTime;
	public String appctx;

	public void write(DataOutputX dout) {
		dout.writeByte(10); // version

		DataOutputX o = new DataOutputX();
		o.writeLong(txid);
		o.writeDecimal(endTime);
		o.writeDecimal(service);
		o.writeDecimal(elapsed);
		o.writeDecimal(error);
		o.writeDecimal(cpuTime);
		o.writeDecimal(malloc);

		o.writeDecimal(sqlCount);
		o.writeDecimal(sqlTime);
		o.writeDecimal(sqlFetchCount);
		o.writeDecimal(sqlFetchTime);

		o.writeDecimal(httpcCount);
		o.writeDecimal(httpcTime);

		o.writeBoolean(active);
		o.writeDecimal(step_data_pos);
		o.writeDecimal(cipher);

		o.writeInt(ipaddr);
		o.writeDecimal(wclientId);
		o.writeDecimal(userAgent);
		o.writeDecimal(referer);
		o.writeDecimal(status);

		if (mtid != 0) {
			o.writeByte(1);
			o.writeDecimal(mtid);
			o.writeDecimal(mdepth);
			o.writeDecimal(mcaller);
		} else {
			o.writeByte(0);
		}
//		if (mcaller_pcode != 0) {
//			o.writeByte(6);
//			o.writeDecimal(mcaller_pcode);
//			o.writeDecimal(mcaller_okind);
//			o.writeDecimal(mcaller_oid);
//			o.writeDecimal(mcaller_spec);
//			o.writeDecimal(mcaller_url);
//			o.writeDecimal(mthis_spec);// 보통 0, 서버에서 셋팅
//		} else {
//			o.writeByte(0);
//		}
		o.writeByte(6);
		o.writeDecimal(mcaller_pcode);
		o.writeDecimal(mcaller_okind);
		o.writeDecimal(mcaller_oid);
		o.writeDecimal(mcaller_spec);
		o.writeDecimal(mcaller_url);
		o.writeDecimal(mthis_spec);// 보통 0, 서버에서 셋팅

		o.writeByte(this.http_method);
		o.writeDecimal(domain);

		if (fields == null) {
			o.writeByte(0);
		} else {
			int sz = this.fields.size();
			o.writeByte(sz);
			Enumeration<StringKeyLinkedEntry<Value>> en = fields.entries();
			while(en.hasMoreElements()) {
				StringKeyLinkedEntry<Value> ent = en.nextElement();
				o.writeText(ent.getKey());
				o.writeValue(ent.getValue());
			}
		}
		o.writeDecimal(this.login);
		o.writeByte(errorLevel);

		// 2018.12.28
		o.writeDecimal(this.oid);
		o.writeDecimal(this.okind);
		o.writeDecimal(this.onode);

		// 2019.04.04
		o.writeText(this.custid);

		// 2019.07.09
		o.writeDecimal(this.dbcTime);

		//2021.05.13
		o.writeByte(this.apdex);

		//2021.12.10
		o.writeDecimal(this.mcallerStepId);
		o.writeText(this.originUrl);
		////////////// BLOB ///////////////
		
		o.writeDecimal(this.step_split_count);
		
		//2023.07.24
		o.writeDecimal(this.methodCount);
		o.writeDecimal(this.methodTime);
	
		o.writeText(this.appctx);
		dout.writeBlob(o.toByteArray());
	}

	public TxRecord read(DataInputX din) {
		byte ver = din.readByte();
		if (ver < 10) {
			throw new RuntimeException("not supported version TxRecord");
		}
		DataInputX in = new DataInputX(din.readBlob());

		this.txid = in.readLong();
		this.endTime = in.readDecimal();
		this.service = (int) in.readDecimal();
		this.elapsed = (int) in.readDecimal();
		this.error = in.readDecimal();
		this.cpuTime = (int) in.readDecimal();
		this.malloc = in.readDecimal();

		this.sqlCount = (int) in.readDecimal();
		this.sqlTime = (int) in.readDecimal();
		this.sqlFetchCount = (int) in.readDecimal();
		this.sqlFetchTime = (int) in.readDecimal();

		this.httpcCount = (int) in.readDecimal();
		this.httpcTime = (int) in.readDecimal();

		this.active = in.readBoolean();
		this.step_data_pos = in.readDecimal();
		this.cipher = (int) in.readDecimal();

		this.ipaddr = in.readInt();
		this.wclientId = in.readDecimal();
		this.userAgent = (int) in.readDecimal();
		this.referer = (int) in.readDecimal();
		this.status = (int) in.readDecimal();

		if (in.readByte() > 0) {
			this.mtid = in.readDecimal();
			this.mdepth = (int) in.readDecimal();
			this.mcaller = in.readDecimal();
		}
		switch (in.readByte()) {
		case 1:
			this.mcaller_pcode = in.readDecimal();
			break;
		case 3:
			this.mcaller_pcode = in.readDecimal();
			this.mcaller_spec = (int) in.readDecimal();
			this.mcaller_url = (int) in.readDecimal();
			break;
		case 4:
			this.mcaller_pcode = in.readDecimal();
			this.mcaller_spec = (int) in.readDecimal();
			this.mcaller_url = (int) in.readDecimal();
			this.mthis_spec = (int) in.readDecimal();
			break;
		case 5:
			this.mcaller_pcode = in.readDecimal();
			this.mcaller_oid = (int) in.readDecimal();
			this.mcaller_spec = (int) in.readDecimal();
			this.mcaller_url = (int) in.readDecimal();
			this.mthis_spec = (int) in.readDecimal();
			break;
		case 6:
			this.mcaller_pcode = in.readDecimal();
			this.mcaller_okind = (int) in.readDecimal();
			this.mcaller_oid = (int) in.readDecimal();
			this.mcaller_spec = (int) in.readDecimal();
			this.mcaller_url = (int) in.readDecimal();
			this.mthis_spec = (int) in.readDecimal();
			break;
		}
		this.http_method = in.readByte();
		this.domain = (int) in.readDecimal();

		int n = in.readByte() & 0xff;
		if (n > 0) {
			this.fields = new MapValue();
			for (int i = 0; i < n; i++) {
				String key = in.readText();
				Value val = in.readValue();
				this.fields.put(key, val);
			}
		}
		if (in.available() > 0) {
			this.login = (int) in.readDecimal();
		}
		if (in.available() > 0) {
			this.errorLevel = in.readByte();
		} else {
			if (this.error != 0) {
				this.errorLevel = EventLevel.WARNING;
			}
		}
		if (in.available() > 0) {
			this.oid = (int) in.readDecimal();
			this.okind = (int) in.readDecimal();
			this.onode = (int) in.readDecimal();
		}
		if (in.available() > 0) {
			this.custid = in.readText();
		}
		if (in.available() > 0) {
			this.dbcTime = (int) in.readDecimal();
		}
		if (in.available() > 0) {
			this.apdex = in.readByte();
		}
		if (in.available() > 0) {
			this.mcallerStepId = in.readDecimal();
			this.originUrl=in.readText();
		}
		if (in.available() > 0) {
			this.step_split_count = (int) in.readDecimal();
		}
		
		//2023.07.24
		if (in.available() > 0) {
			this.methodCount = (int) in.readDecimal();
			this.methodTime = (int) in.readDecimal();
		}	
		
		if (in.available() > 0) {
			this.appctx=in.readText();
		}
		return this;
	}

	public byte[] toBytes() {
		DataOutputX o = new DataOutputX();
		this.write(o);
		return o.toByteArray();
	}

	public TxRecord toObject(byte[] b) {
		this.read(new DataInputX(b));
		return this;
	}

}
