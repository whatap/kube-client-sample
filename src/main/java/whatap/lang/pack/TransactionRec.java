package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.util.IntKeyMap;
import whatap.util.IntKeyMap.IntKeyEntry;

import java.util.Enumeration;

public  class TransactionRec {
	public int hash;
    //ver
	public int count;//서버는 long
	public int error;//서버는 long
	public long time_sum;
	public int time_max;
	
	public int sql_count;//서버는 long
	public long sql_time;
	public long sql_fetch;
	public long sql_fetch_time;
	
	public int httpc_count;//서버는 long
	public long httpc_time;
	public long malloc_sum;
	public long cpu_sum;

	public IntKeyMap<TimeCount> sqlMap;
	public IntKeyMap<TimeCount> httpcMap;
	transient public boolean profiled;

	// ver3
	public int apdex_satisfied;//서버는 long
	public int apdex_tolerated;//서버는 long

	// ver4
	public int time_min;
	public long time_sqr_sum;
	
	//ver5
	public int drop_profile;
	public String appctx;

	public TransactionRec setUrlHash(int hash) {
		this.hash = hash;
		return this;
	}

	
	public static TransactionRec readRec(DataInputX in) {
		int urlhash = in.readInt();
		byte ver = in.readByte(); // old에서는 profield=boolean 값이었다. 그래서 이값을 버전값으로 활용한다.
		if(ver<=1) {
			throw new RuntimeException("not supported for a old version");
		}
		TransactionRec m = new TransactionRec();
		m.hash = urlhash;
		m.count =  (int)in.readDecimal();
		m.error =  (int)in.readDecimal();
		m.time_sum = in.readDecimal();
		m.time_max = (int) in.readDecimal();

		m.sql_count =  (int)in.readDecimal();
		m.sql_time = in.readDecimal();
		m.sql_fetch =  in.readDecimal();
		m.sql_fetch_time = in.readDecimal();

		m.httpc_count = (int)in.readDecimal();
		m.httpc_time = in.readDecimal();
		m.malloc_sum = in.readDecimal();
		m.cpu_sum = in.readDecimal();

		int sqlcnt = (int) in.readDecimal();
		if (sqlcnt > 0) {
			m.sqlMap = createMap(sqlcnt);
			for (int i = 0; i < sqlcnt; i++) {
				int hash = in.readInt();
				m.sqlMap.put(hash, new TimeCount().read(in));
			}
		}
		int httpcnt = (int) in.readDecimal();
		if (httpcnt > 0) {
			m.httpcMap = createMap(httpcnt);
			for (int i = 0; i < httpcnt; i++) {
				int hash = in.readInt();
				m.httpcMap.put(hash,  new TimeCount().read(in));
			}
		}
		if(ver <=2)
			return m;
		
		m.apdex_satisfied=  (int)in.readDecimal();
		m.apdex_tolerated=  (int)in.readDecimal();
		
		if(ver <=3)
			return m;
		
		//(주의) 2021.05.12추가됨, 금일 서버 배포 버전에 포함되지 않음  
		m.time_min= (int) in.readDecimal();
		m.time_sqr_sum= in.readDecimal();
		
		if(ver <=4)
			return m;
		
		DataInputX v5 = new DataInputX(in.readBlob());
		m.drop_profile=(int) v5.readDecimal();
		m.appctx=v5.readText();				
		return m;
	}
	public static IntKeyMap<TimeCount> createMap(int cnt) {
		return new IntKeyMap<TimeCount>(cnt, 1f) {
			@Override
			protected TimeCount create(int key) {
				return this.size() >= 1000 ? null : new TimeCount();
			}
		};
	}
	public static void writeRec(DataOutputX o, TransactionRec m, byte version) {
		o.writeInt(m.hash);
		o.writeByte(version); //0,1 = old version
		o.writeDecimal(m.count);
		o.writeDecimal(m.error);
		o.writeDecimal(m.time_sum);
		o.writeDecimal(m.time_max);
		o.writeDecimal(m.sql_count);
		o.writeDecimal(m.sql_time);
		o.writeDecimal(m.sql_fetch);
		o.writeDecimal(m.sql_fetch_time);

		o.writeDecimal(m.httpc_count);
		o.writeDecimal(m.httpc_time);
		o.writeDecimal(m.malloc_sum);
		o.writeDecimal(m.cpu_sum);

		if (m.sqlMap == null) {
			o.writeDecimal(0);
		} else {
			o.writeDecimal(m.sqlMap.size());
			Enumeration<IntKeyEntry<TimeCount>> en = m.sqlMap.entries();
			while (en.hasMoreElements()) {
				IntKeyEntry<TimeCount> ent = en.nextElement();
				o.writeInt(ent.getKey());
				ent.getValue().write(o);
			}
		}
		if (m.httpcMap == null) {
			o.writeDecimal(0);
		} else {
			o.writeDecimal(m.httpcMap.size());
			Enumeration<IntKeyEntry<TimeCount>> en = m.httpcMap.entries();
			while (en.hasMoreElements()) {
				IntKeyEntry<TimeCount> ent = en.nextElement();
				o.writeInt(ent.getKey());
				ent.getValue().write(o);
			}
		}
		
		if(version <=2)
			return;
		//ver3
		o.writeDecimal(m.apdex_satisfied);
		o.writeDecimal(m.apdex_tolerated);		

		if(version <=3)
			return;
		//ver4
		o.writeDecimal(m.time_min);
		o.writeDecimal(m.time_sqr_sum);
		
		if(version <=4)
			return;
		//ver5
		DataOutputX v5 = new DataOutputX();
		v5.writeDecimal(m.drop_profile);
		v5.writeText(m.appctx);
		o.writeBlob(v5.toByteArray());
		
		
	}

}