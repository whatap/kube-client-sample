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
import whatap.lang.PKIND;
import whatap.lang.POID;
import whatap.lang.value.MapValue;
import whatap.lang.value.Value;
import whatap.util.IntEnumer;
import whatap.util.IntIntMap;
import whatap.util.IntKeyLinkedMap;
import whatap.util.IntKeyLinkedMap.IntKeyLinkedEntry;
import whatap.util.LinkedMap;
import whatap.util.LinkedMap.LinkedEntry;
import whatap.util.LongKeyLinkedMap;

import java.io.IOException;
import java.util.Enumeration;

/**
 * Object that contains multiple counter information
 */
public class CounterPack1 extends AbstractPack {
	public byte version=2;
	public int duration;// sec
	public long cputime;

	public long heap_tot;
	public long heap_use;
	public long heap_perm;
	public int heap_pending_finalization;
	public long heap_max;

	public int gc_count;
	public long gc_time_sum;
	public int gc_oldgen_count;

	public int service_count;
	public int service_error;
	public long service_time_sum;
	
	public float tx_dbc_time;//avg time
	public float tx_sql_time;//avg time
	public float tx_httpc_time;//avg time

	public int sql_count;
	public int sql_error;
	public long sql_time_sum;

	public long sql_fetch_count;
	public long sql_fetch_time_sum;

	public int httpc_count;
	public int httpc_error;
	public long httpc_time_sum;

	public int active_tx_count;
	public short[] active_tx_slice=new short[3];

	public float cpu;
	public float cpu_sys;
	public float cpu_usr;
	public float cpu_wait;
	public float cpu_steal;
	public float cpu_irq;

	public float cpu_proc;
	public int cpu_cores;

	public float mem;
	public float swap;
	public float disk;

	public long thread_total_started;
	public int thread_count;
	public int thread_daemon;
	public int thread_peak_count;

	public long starttime;
	public long pack_dropped;

	public IntIntMap db_num_active;
	public IntIntMap db_num_idle;

	public NETSTAT netstat;

	public int host_ip;
	public int proc_fd;
	public float tps;
	public int resp_time;
	public float arrival_rate;
	
	public short apType;

	public WEBSOCKET websocket;

	public int mac_hash;

	public MapValue extra;

	public int pid;
	public final static String[] active_stat_keys = { "method", "sql", "httpc", "dbc", "socket" };
	public short[] active_stat=new short[active_stat_keys.length];

	public int threadpool_activeCount;
	public int threadpool_queueSize;

	public IntKeyLinkedMap<TxMeter> txcaller_oid_meter;
	public IntKeyLinkedMap<SqlMeter> sql_meter;
	public IntKeyLinkedMap<HttpcMeter> httpc_meter;
	public LinkedMap<PKIND, TxMeter> txcaller_group_meter;
	public LinkedMap<POID, TxMeter> txcaller_poid_meter;
	public TxMeter txcaller_unknown;

	public int containerKey;
	
	public int apdex_satisfied;
	public int apdex_tolerated;
	public int apdex_total;
	
	public int proc_fd_max;
	
	public float metering; // -1 초기화 안됨 , 0 old버전, 0.1이상 정상 수집 

	transient public long collect_interval_ms;
	public int resp90;
	public int resp95;
	public long time_sqr_sum;
	
	public short getPackType() {
		return PackEnum.COUNTER_1;
	}

	public static int TRACE_WAITING_PACK_COUNT = 0;

	public void write(DataOutputX o) {
		TRACE_WAITING_PACK_COUNT--;
		super.write(o);
		DataOutputX dout = new DataOutputX();
		dout.writeDecimal(duration);
		dout.writeDecimal(cputime);

		dout.writeDecimal(heap_tot);
		dout.writeDecimal(heap_use);
		dout.writeDecimal(heap_perm);
		dout.writeDecimal(heap_pending_finalization);

		dout.writeDecimal(gc_count);
		dout.writeDecimal(gc_time_sum);

		dout.writeDecimal(service_count);
		dout.writeDecimal(service_error);
		dout.writeDecimal(service_time_sum);

		dout.writeDecimal(sql_count);
		dout.writeDecimal(sql_error);
		dout.writeDecimal(sql_time_sum);
		dout.writeDecimal(sql_fetch_count);
		dout.writeDecimal(sql_fetch_time_sum);

		dout.writeDecimal(httpc_count);
		dout.writeDecimal(httpc_error);
		dout.writeDecimal(httpc_time_sum);

		dout.writeDecimal(active_tx_count);
		writeShortArray(dout, active_tx_slice);

		dout.writeFloat(cpu);
		dout.writeFloat(cpu_sys);
		dout.writeFloat(cpu_usr);
		dout.writeFloat(cpu_wait);
		dout.writeFloat(cpu_steal);
		dout.writeFloat(cpu_irq);

		dout.writeFloat(cpu_proc);
		dout.writeDecimal(cpu_cores);
		dout.writeFloat(mem);
		dout.writeFloat(swap);
		dout.writeFloat(disk);
		dout.writeDecimal(thread_total_started);
		dout.writeDecimal(thread_count);
		dout.writeDecimal(thread_daemon);
		dout.writeDecimal(thread_peak_count);

		if (this.db_num_active == null || this.db_num_idle == null) {
			dout.writeByte(0);
		} else {
			dout.writeByte(1);
			this.db_num_active.toBytes(dout);
			this.db_num_idle.toBytes(dout);
		}
		if (this.netstat == null) {
			dout.writeByte(0);
		} else {
			dout.writeByte(1);
			dout.writeDecimal(netstat.est);
			dout.writeDecimal(netstat.fin_w);
			dout.writeDecimal(netstat.clo_w);
			dout.writeDecimal(netstat.tim_w);
		}

		dout.writeDecimal(proc_fd);
		dout.writeFloat(tps);
		dout.writeDecimal(resp_time);

		dout.writeShort(this.apType);

		if (this.websocket == null) {
			dout.writeByte(0);
		} else {
			dout.writeByte(1);
			dout.writeDecimal(this.websocket.count);
			dout.writeDecimal(this.websocket.in);
			dout.writeDecimal(this.websocket.out);
		}
		dout.writeDecimal(starttime);
		dout.writeDecimal(pack_dropped);

		dout.writeDecimal(host_ip);
		dout.writeDecimal(mac_hash);

		if (extra == null || extra.size()==0) {
			dout.writeByte(0);
		} else {
			dout.writeByte(1);
			dout.writeValue(this.extra);
		}
		dout.writeInt(this.pid);
		int sz = this.active_stat == null ? 0 : this.active_stat.length;
		dout.writeByte(sz);
		for (int i = 0; i < sz; i++) {
			dout.writeShort(this.active_stat[i]);
		}

		dout.writeDecimal(this.threadpool_activeCount);
		dout.writeDecimal(this.threadpool_queueSize);

		write_txcaller_oid_meter(dout);
		write_sql_meter(dout);
		write_httpc_meter(dout);
		write_txcaller_group_meter(dout);
		dout.writeDecimal(0);
		write_txcaller_other(dout);

		dout.writeDecimal(this.containerKey);
		
		dout.writeFloat(this.tx_dbc_time);
		dout.writeFloat(this.tx_sql_time);
		dout.writeFloat(this.tx_httpc_time);
		
		dout.writeDecimal(apdex_satisfied);
		dout.writeDecimal(apdex_tolerated);

		dout.writeFloat(this.arrival_rate);
		
		dout.writeDecimal(this.gc_oldgen_count);
		dout.writeByte(this.version);
		dout.writeDecimal(this.heap_max);
		
		dout.writeDecimal(this.proc_fd_max);
		dout.writeFloat(this.metering);

		dout.writeDecimal(this.apdex_total);

		//DEV21준비 2021.09.10
		write_txcaller_poid_meter(dout);
		
		//v2.2.1 2022.11.25
		dout.writeDecimal(this.resp90);
		dout.writeDecimal(this.resp95);
		//
		dout.writeDecimal(this.time_sqr_sum);
		
		o.writeBlob(dout.toByteArray());
	}

	private void write_txcaller_other(DataOutputX dout) {
		if (this.txcaller_unknown != null) {
			dout.writeByte(2);
			dout.writeDecimal(this.txcaller_unknown.time);
			dout.writeDecimal(this.txcaller_unknown.count);
			dout.writeDecimal(this.txcaller_unknown.error);
			if(this.txcaller_unknown.acts==null || this.txcaller_unknown.acts.length<3) {
		    	dout.writeDecimal(this.txcaller_unknown.actx);
			}else {
				dout.writeByte(9);
				writeShortArray(dout, this.txcaller_unknown.acts);
			}
		} else {
			dout.writeByte(0);
		}
	}

	private void write_txcaller_oid_meter(DataOutputX dout) {
		if (this.txcaller_oid_meter == null) {
			dout.writeDecimal(0);
		} else {
			dout.writeByte(9);
			dout.writeDecimal(this.txcaller_oid_meter.size());
			Enumeration<IntKeyLinkedEntry<TxMeter>> en = this.txcaller_oid_meter.entries();
			while (en.hasMoreElements()) {
				IntKeyLinkedEntry<TxMeter> ent = en.nextElement();
				TxMeter m = ent.getValue();

				dout.writeInt(ent.getKey());
				dout.writeDecimal(m.time);
				dout.writeDecimal(m.count);
				dout.writeDecimal(m.error);
				if(m.acts==null||m.acts.length<3) {
			    	dout.writeDecimal(m.actx);
				}else {
					dout.writeByte(9);
					writeShortArray(dout, m.acts);
				}
			}
		}
	}

	private void write_sql_meter(DataOutputX dout) {
		if (this.sql_meter == null) {
			dout.writeDecimal(0);
		} else {
			dout.writeByte(9);
			dout.writeDecimal(this.sql_meter.size());
			Enumeration<IntKeyLinkedEntry<SqlMeter>> en = this.sql_meter.entries();
			while (en.hasMoreElements()) {
				IntKeyLinkedEntry<SqlMeter> ent = en.nextElement();
				SqlMeter m = ent.getValue();

				dout.writeInt(ent.getKey());
				dout.writeDecimal(m.time);
				dout.writeDecimal(m.count);
				dout.writeDecimal(m.error);
				if (m.acts == null || m.acts.length < 3) {
			    	dout.writeDecimal(m.actx);
				}else {
					dout.writeByte(9);
					writeShortArray(dout, m.acts);
				}
				dout.writeDecimal(m.fetch_count);
				dout.writeDecimal(m.fetch_time);
				
			}
		}
	}

	private void write_httpc_meter(DataOutputX dout) {
		if (this.httpc_meter == null) {
			dout.writeDecimal(0);
		} else {
			dout.writeByte(9);
			dout.writeDecimal(this.httpc_meter.size());
			Enumeration<IntKeyLinkedEntry<HttpcMeter>> en = this.httpc_meter.entries();
			while (en.hasMoreElements()) {
				IntKeyLinkedEntry<HttpcMeter> ent = en.nextElement();
				HttpcMeter m = ent.getValue();

				dout.writeInt(ent.getKey());
				dout.writeDecimal(m.time);
				dout.writeDecimal(m.count);
				dout.writeDecimal(m.error);
				if (m.acts == null || m.acts.length < 3) {
			    	dout.writeDecimal(m.actx);
				}else {
					dout.writeByte(9);
					writeShortArray(dout, m.acts);
				}
			}
		}
	}

	private void write_txcaller_group_meter(DataOutputX dout) {
		if (this.txcaller_group_meter == null) {
			dout.writeDecimal(0);
		} else {
			dout.writeByte(9);
			dout.writeDecimal(this.txcaller_group_meter.size());
			Enumeration<LinkedEntry<PKIND, TxMeter>> en = this.txcaller_group_meter.entries();
			while (en.hasMoreElements()) {
				LinkedEntry<PKIND, TxMeter> ent = en.nextElement();
				TxMeter m = ent.getValue();
				dout.writeDecimal(ent.getKey().pcode);
				dout.writeDecimal(ent.getKey().okind);
				dout.writeDecimal(m.time);
				dout.writeDecimal(m.count);
				dout.writeDecimal(m.error);
				if (m.acts == null || m.acts.length < 3) {
			    	dout.writeDecimal(m.actx);
				}else {
					dout.writeByte(9);
					writeShortArray(dout, m.acts);
				}
				
			}
		}
	}

	public static class TxMeter {
		public long time;
		public int count;
		public int error;
		public int actx;
		public short[] acts;

		public String toString() {
			return "[time=" + time + ", count=" + count + ", error=" + error + ", actx=" + actx + "]";
		}
	}

	public static class HttpcMeter extends TxMeter {

		@Override
		public String toString() {
			return "HttpcMeter " + super.toString();
		}

	}

	public static class SqlMeter extends TxMeter {
		public long fetch_count;
		public long fetch_time;

		@Override
		public String toString() {
			return "SqlMeter [fetch_count=" + fetch_count + ", fetch_time=" + fetch_time + "]" + super.toString();
		}

	}

	public Pack read(DataInputX in) {
		super.read(in);
		DataInputX din = new DataInputX(in.readBlob());
		this.duration = (int) din.readDecimal();
		this.cputime = din.readDecimal();
		this.heap_tot =  din.readDecimal();
		this.heap_use =  din.readDecimal();
		this.heap_perm = din.readDecimal();
		this.heap_pending_finalization = (int) din.readDecimal();

		this.gc_count = (int) din.readDecimal();
		this.gc_time_sum = din.readDecimal();

		this.service_count = (int) din.readDecimal();
		this.service_error = (int) din.readDecimal();
		this.service_time_sum = din.readDecimal();

		this.sql_count = (int) din.readDecimal();
		this.sql_error = (int) din.readDecimal();
		this.sql_time_sum = din.readDecimal();
		this.sql_fetch_count = din.readDecimal();
		this.sql_fetch_time_sum = din.readDecimal();

		this.httpc_count = (int) din.readDecimal();
		this.httpc_error = (int) din.readDecimal();
		this.httpc_time_sum = din.readDecimal();

		this.active_tx_count = (int) din.readDecimal();
		this.active_tx_slice = readShortArray(din,3);

		this.cpu = din.readFloat();
		this.cpu_sys = din.readFloat();
		this.cpu_usr = din.readFloat();
		this.cpu_wait = din.readFloat();
		this.cpu_steal = din.readFloat();
		this.cpu_irq = din.readFloat();

		this.cpu_proc = din.readFloat();
		this.cpu_cores = (int) din.readDecimal();

		this.mem = din.readFloat();
		this.swap = din.readFloat();
		this.disk = din.readFloat();

		this.thread_total_started = din.readDecimal();
		this.thread_count = (int) din.readDecimal();
		this.thread_daemon = (int) din.readDecimal();
		this.thread_peak_count = (int) din.readDecimal();

		if (din.readByte() != 0) {
			this.db_num_active = new IntIntMap(7, 1f);
			this.db_num_idle = new IntIntMap(7, 1f);
			this.db_num_active.toObject(din);
			this.db_num_idle.toObject(din);
		}
		if (din.readByte() != 0) {
			this.netstat = new NETSTAT();
			this.netstat.est = (int) din.readDecimal();
			this.netstat.fin_w = (int) din.readDecimal();
			this.netstat.clo_w = (int) din.readDecimal();
			this.netstat.tim_w = (int) din.readDecimal();
		}
		this.proc_fd = (int) din.readDecimal();
		this.tps = din.readFloat();
		this.resp_time = (int) din.readDecimal();

		this.apType = din.readShort();

		if (din.readByte() != 0) {
			this.websocket = new WEBSOCKET();
			this.websocket.count = (int) din.readDecimal();
			this.websocket.in = din.readDecimal();
			this.websocket.out = din.readDecimal();
		}
		this.starttime = din.readDecimal();
		this.pack_dropped = din.readDecimal();
		this.host_ip = (int) din.readDecimal();

		if (din.available() > 0) {
			this.mac_hash = (int) din.readDecimal();
		}
		if (din.available() > 0) {
			if (din.readByte() == 1) {
				Value v =din.readValue();
				if(v instanceof MapValue) {
					this.extra = (MapValue)v;
				}
			}
		}
		if (din.available() > 0) {
			this.pid = din.readInt();

			int sz = din.readByte();
			this.active_stat = new short[sz];
			for (int i = 0; i < sz; i++) {
				this.active_stat[i] = din.readShort();
			}
		} else {
			this.active_stat = new short[0];
		}

		if (din.available() > 0) {
			this.threadpool_activeCount = (int) din.readDecimal();
			this.threadpool_queueSize = (int) din.readDecimal();
		}
		if (din.available() > 0) {
			read_txcaller_oid_meter(din);
		}
		if (din.available() > 0) {
			read_sql_meter(din);
		}
		if (din.available() > 0) {
			read_httpc_meter(din);
		}
		if (din.available() > 0) {
			read_txcaller_group_meter(din);
		}
		if (din.available() > 0) {
			read_txcaller_okind_meter_deprecated(din);
		}
		if (din.available() > 0) {
			read_txcaller_unknown(din);
		}

		if (din.available() > 0) {
			this.containerKey = (int) din.readDecimal();
		}
		if (din.available() > 0) {
			this.tx_dbc_time = din.readFloat();
			this.tx_sql_time = din.readFloat();
			this.tx_httpc_time = din.readFloat();
		}
		
		if (din.available() > 0) {
			this.apdex_satisfied = (int) din.readDecimal();
			this.apdex_tolerated = (int) din.readDecimal();
		}
		
		if (din.available() > 0) {
			this.arrival_rate = din.readFloat();
		}
		if (din.available() > 0) {
			this.gc_oldgen_count = (int) din.readDecimal();
			this.version=din.readByte();
			this.heap_max=din.readDecimal();
		}
		if (din.available() > 0) {
			this.proc_fd_max = (int) din.readDecimal();
		}
		if (din.available() > 0) {
			this.metering =  din.readFloat();
		}
		if (din.available() > 0) {
			this.apdex_total =  (int) din.readDecimal();
		}
		
		//DEV2.1 2021.09.10
		if (din.available() > 0) {
			read_txcaller_poid_meter(din);
		}
		//v2.2.1 2022.11.25
		if (din.available() > 0) {
			this.resp90 = (int) din.readDecimal();
			this.resp95 = (int) din.readDecimal();
		}
		if (din.available() > 0) {
			this.time_sqr_sum = din.readDecimal();
		}
		return this;
	}

	private void read_txcaller_unknown(DataInputX din) {
		byte ver = din.readByte();
		if (ver > 0) {
			this.txcaller_unknown = new TxMeter();
			this.txcaller_unknown.time = din.readDecimal();
			this.txcaller_unknown.count = (int) din.readDecimal();
			this.txcaller_unknown.error = (int) din.readDecimal();
			if (ver >= 2) {
				byte flag = din.readByte();
				if (flag <= 8) {
					this.txcaller_unknown.actx = (int) din.readDecimal(flag);
				} else {
					this.txcaller_unknown.acts = readShortArray(din,3);
					this.txcaller_unknown.actx = this.txcaller_unknown.acts[0] + this.txcaller_unknown.acts[1] + this.txcaller_unknown.acts[2];
				}
			}
		}
	}

	private void read_txcaller_group_meter(DataInputX din) {
		byte ver = din.readByte();
		if (ver == 0)
			return;
		int count;
		if (ver <= 8) {
			count = (int) din.readDecimal(ver);
		} else {
			count = (int) din.readDecimal();
		}
		this.txcaller_group_meter = new LinkedMap<PKIND, TxMeter>();
		for (int i = 0; i < count; i++) {
			TxMeter m = new TxMeter();
			long pcode = din.readDecimal();
			if (ver <= 8) {
				m.time = din.readDecimal();
				m.count = (int) din.readDecimal();
				m.error = (int) din.readDecimal();
				this.txcaller_group_meter.put(new PKIND(pcode, 0), m);
			} else {
				int okind = (int) din.readDecimal();
				m.time = din.readDecimal();
				m.count = (int) din.readDecimal();
				m.error = (int) din.readDecimal();
				byte flag = din.readByte();
				if (flag <= 8) {
					m.actx = (int) din.readDecimal(flag);
				} else {
					m.acts = readShortArray(din,3);
					m.actx = m.acts[0] + m.acts[1] + m.acts[2];
				}
				this.txcaller_group_meter.put(new PKIND(pcode, okind), m);
			}
		}

	}

	private void read_txcaller_okind_meter_deprecated(DataInputX din) {
		int count = (int) din.readDecimal();
		for (int i = 0; i < count; i++) {
			TxMeter m = new TxMeter();
			int okind = din.readInt();
			m.time = din.readDecimal();
			m.count = (int) din.readDecimal();
			m.error = (int) din.readDecimal();
		}

	}

	private void read_txcaller_poid_meter(DataInputX din) {
		this.txcaller_poid_meter = new LinkedMap<POID, TxMeter>();
		int count = (int) din.readDecimal();
		for (int i = 0; i < count; i++) {
			TxMeter m = new TxMeter();
			long pcode = din.readDecimal();
			int oid = (int) din.readDecimal();
			m.time = din.readDecimal();
			m.count = (int) din.readDecimal();
			m.error = (int) din.readDecimal();
			m.acts = readShortArray(din,3);
			m.actx = m.acts[0] + m.acts[1] + m.acts[2];
			this.txcaller_poid_meter.put(new POID(pcode, oid), m);
		}
	}

	private void write_txcaller_poid_meter(DataOutputX dout) {
		if (this.txcaller_poid_meter == null) {
			dout.writeDecimal(0);
		} else {
			dout.writeDecimal(this.txcaller_poid_meter.size());
			Enumeration<LinkedEntry<POID, TxMeter>> en = this.txcaller_poid_meter.entries();
			while (en.hasMoreElements()) {
				LinkedEntry<POID, TxMeter> ent = en.nextElement();
				TxMeter m = ent.getValue();
				dout.writeDecimal(ent.getKey().pcode);
				dout.writeDecimal(ent.getKey().oid);
				dout.writeDecimal(m.time);
				dout.writeDecimal(m.count);
				dout.writeDecimal(m.error);
				writeShortArray(dout, m.acts);
			}
		}
	}

	private void read_httpc_meter(DataInputX din) {
		byte ver = din.readByte();
		if (ver == 0)
			return;
		int count;
		if (ver <= 8) {
			count = (int) din.readDecimal(ver);
		} else {
			count = (int) din.readDecimal();
		}
		this.httpc_meter = new IntKeyLinkedMap<HttpcMeter>();
		for (int i = 0; i < count; i++) {
			HttpcMeter m = new HttpcMeter();
			int host = din.readInt();
			m.time = din.readDecimal();
			m.count = (int) din.readDecimal();
			m.error = (int) din.readDecimal();
			if (ver >= 9) {
				byte flag = din.readByte();
				if (flag <= 8) {
					m.actx = (int) din.readDecimal(flag);
				} else {
					m.acts = readShortArray(din,3);
					m.actx = m.acts[0] + m.acts[1] + m.acts[2];
				}
			}
			this.httpc_meter.put(host, m);
		}

	}

	private void read_sql_meter(DataInputX din) {
		byte ver = din.readByte();
		if (ver == 0)
			return;
		int count;
		if (ver <= 8) {
			count = (int) din.readDecimal(ver);
		} else {
			count = (int) din.readDecimal();
		}
		this.sql_meter = new IntKeyLinkedMap<SqlMeter>();
		for (int i = 0; i < count; i++) {
			SqlMeter m = new SqlMeter();
			int dbc = din.readInt();
			m.time = din.readDecimal();
			m.count = (int) din.readDecimal();
			m.error = (int) din.readDecimal();
			if (ver >= 9) {
				byte flag = din.readByte();
				if (flag <= 8) {
					m.actx = (int) din.readDecimal(flag);
				} else {
					m.acts = readShortArray(din,3);
					m.actx = m.acts[0] + m.acts[1] + m.acts[2];
				}
			}
			m.fetch_count = din.readDecimal();
			m.fetch_time = din.readDecimal();
			this.sql_meter.put(dbc, m);
		}

	}

	private void read_txcaller_oid_meter(DataInputX din) {

		byte ver = din.readByte();
		if (ver == 0)
			return;
		int count;
		if (ver <= 8) {
			count = (int) din.readDecimal(ver);
		} else {
			count = (int) din.readDecimal();
		}

		this.txcaller_oid_meter = new IntKeyLinkedMap<TxMeter>();
		for (int i = 0; i < count; i++) {
			TxMeter m = new TxMeter();
			int key = din.readInt();
			m.time = din.readDecimal();
			m.count = (int) din.readDecimal();
			m.error = (int) din.readDecimal();
			if (ver >= 9) {
				byte flag = din.readByte();
				if (flag <= 8) {
					m.actx = (int) din.readDecimal(flag);
				} else {
					m.acts = readShortArray(din,3);
					m.actx = m.acts[0] + m.acts[1] + m.acts[2];
				}
			}
			this.txcaller_oid_meter.put(key, m);
		}

	}

	public int dbc_count() {
		int dbc = 0;
		if (this.db_num_active != null) {
			IntEnumer en = this.db_num_active.values();
			while (en.hasMoreElements()) {
				dbc += en.nextInt();
			}
		}
		if (this.db_num_idle != null) {
			IntEnumer en = this.db_num_idle.values();
			while (en.hasMoreElements()) {
				dbc += en.nextInt();
			}
		}
		return dbc;
	}

	private void writeShortArray(DataOutputX out, short[] v) {
		if (v == null) {
			out.writeByte(0);
		} else {
			out.writeByte(v.length);
			for (int i = 0; i < v.length; i++) {
				out.writeShort(v[i]);
			}
		}
	}

	private short[] readShortArray(DataInputX din, int sz) {
		int len = din.readByte();
		short[] arr = new short[sz]; // sz는 최대 사이즈, 0인경우가 있음 
		for (int i = 0; i < len; i++) {
			arr[i] = din.readShort();
		}
		return arr;
	}

	public static void main(String[] args) throws IOException {
		CounterPack1 c = new CounterPack1();
		c.netstat = new NETSTAT();
		c = (CounterPack1) new DataInputX(new DataOutputX().writePack(c).toByteArray()).readPack();
		System.out.println(c);
	}

	public String toStringMt() {
		StringBuilder builder = new StringBuilder();
		builder.append(super.toString());
		builder.append(" [txcaller_oid_meter=");
		builder.append(txcaller_oid_meter);
		builder.append(", sql_meter=");
		builder.append(sql_meter);
		builder.append(", httpc_meter=");
		builder.append(httpc_meter);
		builder.append(", txcaller_group_meter=");
		builder.append(txcaller_group_meter);
		builder.append(", txcaller_unknown=");
		builder.append(txcaller_unknown);
		builder.append("]");
		return builder.toString();
	}
	
	public LongKeyLinkedMap<TxMeter> txcallerPcodeMeter() {
		LongKeyLinkedMap<TxMeter> out = new LongKeyLinkedMap<TxMeter>() {
			protected TxMeter create(long key) {
				return new TxMeter();
			}
		};
		if (this.txcaller_group_meter != null) {
			Enumeration<PKIND> en = this.txcaller_group_meter.keys();
			while (en.hasMoreElements()) {
				PKIND pk = en.nextElement();
				TxMeter m = this.txcaller_group_meter.get(pk);
				
				TxMeter outMeter = out.intern(pk.pcode);
				outMeter.actx += m.actx;
				outMeter.count += m.count;
				outMeter.error += m.error;
				outMeter.time += m.time;
			}
		}
		return out;
	}
}
