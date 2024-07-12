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
import whatap.lang.slog.SLog;
import whatap.util.AnsiPrint;
import whatap.util.DateUtil;

/**
 * Object that contains multiple counter information
 */
abstract public class AbstractPack implements Pack {
	public long pcode;
	public int oid;
	public int okind;
    public int onode;
	public long time;
	
	//pack_uuid를 통해서 중복전송 후 중복 제거용도로 사용할 것이다.
	//2024-02-12 sjkim
	public long pack_uuid;
	transient public boolean journaled;

	public String toString() {
		return "pcode=" + pcode + ", oid=" + oid + ",okind=" + okind + ",onode=" + onode + ",time="
				+ DateUtil.timestamp(time);
	}
	public String toSimpleString() {
		return this.getClass().getName() + " pcode=" + pcode + ", oid=" + oid + ",okind=" + okind + ",onode=" + onode
				+ ",time=" + DateUtil.timestamp(time);
	}

	protected void toFmtString(StringBuilder sb, String k, Object v) {
		sb.append(",").append(AnsiPrint.green(k)).append("=").append(AnsiPrint.green(v));
	}

	public String toFormatString() {
		StringBuilder builder = new StringBuilder();
		builder.append("pcode=");
		builder.append(AnsiPrint.green(pcode));
		builder.append(",oid=").append(AnsiPrint.green(oid));
		builder.append(",okind=").append(AnsiPrint.green(okind));
		builder.append(",onode=").append(AnsiPrint.green(onode));
		if(pack_uuid!=0) {
			builder.append(",pack_uuid=").append(AnsiPrint.green(pack_uuid));
		}
		return builder.toString();
	}

	abstract public short getPackType();

	public void write(DataOutputX dout) {
		if (this.pack_uuid != 0) {
			dout.writeByte(10);
			dout.writeDecimal(this.pcode);
			dout.writeDecimal(this.oid);
			dout.writeDecimal(this.okind);
			dout.writeDecimal(this.onode);
			dout.writeDecimal(this.time);
			dout.writeDecimal(this.pack_uuid);
			return;
		}
		if((this.okind | this.onode) == 0) {
			dout.writeDecimal(pcode);
			dout.writeInt(oid);
			dout.writeLong(time);
		} else {
			dout.writeByte(9);
			dout.writeDecimal(this.pcode);
			dout.writeInt(this.oid);
			dout.writeInt(this.okind);
			dout.writeInt(this.onode);
			dout.writeLong(this.time);
		}
	}

	public Pack read(DataInputX din) {
		byte ver = din.readByte();
		if(ver<=8) {
			this.pcode = din.readDecimal(ver);
			this.oid = din.readInt();
			this.time = din.readLong();
			return this;
		}
		if (ver == 10) {
			this.pcode = din.readDecimal();
			this.oid = (int) din.readDecimal();
			this.okind = (int) din.readDecimal();
			this.onode = (int) din.readDecimal();
			this.time = din.readDecimal();
			this.pack_uuid = din.readDecimal();
		} else {
			this.pcode = din.readDecimal();
			this.oid = din.readInt();
			this.okind = din.readInt();
			this.onode = din.readInt();
			this.time = din.readLong();
		}
		return this;
	}
	public long getPcode() {
		return pcode;
	}

	public int getOid() {
		return oid;
	}

	public long getTime() {
		return time;
	}
	public int getOKind() {
		return this.okind;
	}
	public int getONode() {
		return this.onode;
	}
	public SLog slog(SLog log) {
		log.a("time", DateUtil.timestamp(time));
		log.a("pcode", pcode);
		if (oid != 0)
			log.a("oid", oid);
		if (okind != 0)
			log.a("okind", okind);
		if (onode != 0)
			log.a("onode", onode);
		return log;
	}
}
