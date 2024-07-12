package whatap.lang.pack;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.slog.SLog;
import whatap.util.CastUtil;
import whatap.util.DateUtil;
import whatap.util.IntIntMap;
import whatap.util.IntIntMap.IntIntEntry;
import whatap.util.LinkedMap;
import whatap.util.LinkedMap.LinkedEntry;
import whatap.util.Pair;

import java.util.Enumeration;

public class StatUserAgentPack1 extends AbstractPack {
	public IntIntMap userAgents = new IntIntMap();
	public LinkedMap<Pair<String, String>, Integer> osbrTab = null;

	private short packType;
	public long dataStartTime;
	
	public StatUserAgentPack1() {
		this(PackEnum.STAT_USER_AGENT);
	}
	public StatUserAgentPack1(short type) {
		this.packType = type;
	}
	public short getPackType() {
		return this.packType;
	}

	public String toString() {
		SLog slog = SLog.n("pack", this.getClass().getSimpleName());
		super.slog(slog);
		slog.a("records", this.userAgents.size());
		slog.a("type", getPackType());
		slog.a("packType", packType);
		slog.a("startTime", DateUtil.timestamp(this.dataStartTime));
		return slog.green();
	}

	public void write(DataOutputX dout) {
		super.write(dout);
		dout.writeDecimal(userAgents.size());
		Enumeration<IntIntEntry> en = userAgents.entries();
		while (en.hasMoreElements()) {
			IntIntEntry e = en.nextElement();
			dout.writeInt(e.getKey());
			dout.writeInt(e.getValue());
		}
		if(this.packType==PackEnum.STAT_USER_AGENT)
			return;
		
		if (this.osbrTab == null) {
			dout.writeDecimal(0);
		} else {
			dout.writeDecimal(this.osbrTab.size());
			Enumeration<LinkedEntry<Pair<String, String>, Integer>> en2 = this.osbrTab.entries();
			while (en2.hasMoreElements()) {
				LinkedEntry<Pair<String, String>, Integer> ent = en2.nextElement();
				dout.writeText(ent.getKey().getLeft());
				dout.writeText(ent.getKey().getRight());
				dout.writeInt(ent.getValue());
			}
		}
		if(this.packType==PackEnum.STAT_USER_AGENT_1)
			return;
		
		DataOutputX o =new DataOutputX();
		o.writeDecimal(dataStartTime);
		
		dout.writeBlob(o.toByteArray());
	}

	public Pack read(DataInputX din) {
		super.read(din);
		int cnt = (int) din.readDecimal();
		this.userAgents = new IntIntMap(cnt, 1.0f);
		for (int i = 0; i < cnt; i++) {
			int hash = din.readInt();
			int count = din.readInt();
			this.userAgents.put(hash, count);
		}
		if (this.packType == PackEnum.STAT_USER_AGENT)
			return this;

		int n = (int) din.readDecimal();
		if (n == 0) {
			this.osbrTab=null;
			return this;
		}
		this.osbrTab = new LinkedMap<Pair<String, String>, Integer>(cnt, 1.0f);
		for (int i = 0; i < n; i++) {
			String os = din.readText();
			String br = din.readText();
			int c = din.readInt();
			this.osbrTab.put(new Pair<String, String>(os, br), c);
		}
		if (this.packType == PackEnum.STAT_USER_AGENT_1)
			return this;
		
		DataInputX in = new DataInputX(din.readBlob());
		this.dataStartTime = in.readDecimal();
		return this;		
	}

	public void initOsBr() {
		if (this.osbrTab == null) {
			this.osbrTab = new LinkedMap<Pair<String, String>, Integer>(this.userAgents.size(), 1f);
		}
	}

	public void addOsBr(Pair<String, String> osBrowser, int value) {
		this.osbrTab.put(osBrowser, CastUtil.cint(this.osbrTab.get(osBrowser)) + value);
	}
}
