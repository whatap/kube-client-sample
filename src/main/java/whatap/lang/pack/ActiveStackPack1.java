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

import java.util.Arrays;

public class ActiveStackPack1 extends AbstractPack {
	public long activeStackId;
	public long txid;
	public int service;
	public int[] callstack;
	public int callstack_hash;

	public int elapsed;

	public boolean perfx_included=false;
	public int client_ip;
	public int cputime;
	public long malloc;
	
	public int sql_count;
	public int sql_time;
	public int active_sql_dbc;
	public int active_sql_hash;
	public int rs_count;
	public int rs_time;
	public int httpc_count;
	public int httpc_time;
	public int active_httpc_hash;
	
	public String thread_name;
	
	public short getPackType() {
		return PackEnum.ACTIVESTACK_1;
	}

	public void write(DataOutputX o) {
		super.write(o);
		byte ver = (byte)(this.thread_name==null?2:3);
		o.writeByte(ver); //v2
		o.writeLong(activeStackId);
		o.writeLong(txid);
		o.writeInt(service);
		o.writeInt(callstack_hash);
		o.writeArray(callstack);
		
		o.writeDecimal(elapsed);
		
		o.writeBoolean(this.perfx_included);
		if(this.perfx_included) {
			o.writeDecimal(this.client_ip);
    		o.writeDecimal(this.cputime);
			o.writeDecimal(this.malloc);
			o.writeDecimal(this.sql_count);
			o.writeDecimal(this.sql_time);
			o.writeDecimal(this.active_sql_dbc);
			o.writeDecimal(this.active_sql_hash);
			o.writeDecimal(this.rs_count);
			o.writeDecimal(this.rs_time);
			o.writeDecimal(this.httpc_count);
			o.writeDecimal(this.httpc_time);
			o.writeDecimal(this.active_httpc_hash);
		}
		if (ver >= 3) {
			DataOutputX dout = new DataOutputX();
			dout.writeText(this.thread_name);
			o.writeBlob(dout.toByteArray());
		}
	}

	public Pack read(DataInputX din) {
		super.read(din);
		byte ver = din.readByte();
		this.activeStackId = din.readLong();
		this.txid = din.readLong();
		this.service = din.readInt();
		this.callstack_hash = din.readInt();
		this.callstack = din.readArray(this.callstack);
		switch (ver) {
		case 0:
			return this;
		case 1:
			this.elapsed = (int) din.readDecimal();
			return this;
		default:
			this.elapsed = (int) din.readDecimal();
			break;
		}		
		this.perfx_included = din.readBoolean();
		if(this.perfx_included) {
			this.client_ip = (int)din.readDecimal();
			this.cputime= (int) din.readDecimal();
			this.malloc = din.readDecimal();
			this.sql_count = (int)din.readDecimal();
			this.sql_time = (int)din.readDecimal();
			this.active_sql_dbc = (int) din.readDecimal();
			this.active_sql_hash = (int) din.readDecimal();
			this.rs_count = (int) din.readDecimal();
			this.rs_time = (int) din.readDecimal();
			this.httpc_count = (int) din.readDecimal();
			this.httpc_time = (int) din.readDecimal();
			this.active_httpc_hash = (int) din.readDecimal();
		}
		if(ver >=3) {
			DataInputX din2 = new DataInputX(din.readBlob());
			this.thread_name = din2.readText();
		}
		return this;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ActiveStackPack1");
		builder.append(super.toString());
		builder.append("[seq=");
		builder.append(activeStackId);
		builder.append(", txid=");
		builder.append(txid);
		builder.append(", service=");
		builder.append(service);
		builder.append(", callstack=");
		builder.append(Arrays.toString(callstack));
		builder.append(", callstack_hash=");
		builder.append(callstack_hash);
		builder.append(", elapsed=");
		builder.append(elapsed);
		builder.append(", perfx_included=");
		builder.append(perfx_included);
		builder.append("]");
		return builder.toString();
	}

	public static void main(String[] args) {
		ActiveStackPack1 pp = new ActiveStackPack1();
		pp.callstack = new int[100];
		pp = (ActiveStackPack1) new DataInputX(new DataOutputX().writePack(pp).toByteArray()).readPack();
		System.out.println(pp);
	}
}
