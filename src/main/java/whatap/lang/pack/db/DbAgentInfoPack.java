package whatap.lang.pack.db;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;
import whatap.lang.H3;
import whatap.lang.pack.AbstractPack;
import whatap.lang.pack.Pack;
import whatap.lang.pack.PackEnum;
import whatap.lang.value.IntMapValue;
import whatap.util.HashUtil;

public class DbAgentInfoPack extends AbstractPack {
    public IntMapValue attr = new IntMapValue();

    public final static int dbIp = HashUtil.hash("db ip");
    public final static int dbIpList = HashUtil.hash("db ip list");
    public final static int dbPort = HashUtil.hash("db port");
    public final static int dbms = HashUtil.hash("db type");
    public final static int dbLoc = HashUtil.hash("db_loc");
    public final static int dbName = HashUtil.hash("db");
    public final static int dbVersion = HashUtil.hash("db version");
    public final static int xosVersion = HashUtil.hash("xos version");
    public final static int dbIsMulti = HashUtil.hash("db multi");
    public final static int dbInstNo = HashUtil.hash("db inst no");
    public final static int dbCpuCores = HashUtil.hash("cpu cores");
    public final static int agentIp = HashUtil.hash("agent_ip");
    public final static int startTime = HashUtil.hash("start time");
	public final static int indexListHash = HashUtil.hash("index list");	// index list 에는 stat, event 를 포함해서 보내주도록 한다.
	public final static int statListHash = HashUtil.hash("stat list");
	public final static int eventListHash = HashUtil.hash("event list");
	public final static int osListHash = HashUtil.hash("os list");
	public final static int osAgent = HashUtil.hash("os agent");	// os agent 의 사용 유무.
	public final static int replicationName = HashUtil.hash("replication_name");
	public final static int counterInterval = HashUtil.hash("counter_interval");
	public final static int dbSlowSetting = HashUtil.hash("db_slow_setting");
	public final static int agentSlowSetting = HashUtil.hash("agent_slow_setting");
	public final static int tablesSetting = HashUtil.hash("tables_setting");
	public final static int tableSpaceSetting = HashUtil.hash("table_space_setting");
	public final static int serviceIp = HashUtil.hash("service_ip");
	
	/*
	//public final static int agentVersion = HashUtil.hash("agent version");
	public final static int dbVersion = HashUtil.hash("DB version");
	public final static int cpuCount = HashUtil.hash("cpu count");
	public final static int physicalMemory = HashUtil.hash("physical memory");
	//public final static int osName = HashUtil.hash("OS name");
	//public final static int architecture = HashUtil.hash("architecture");

	public final static int freeDiskSpace = HashUtil.hash("free disk space");
	public final static int totalDiskSpace = HashUtil.hash("total disk space");
	*/

	public synchronized static void send(H3 <Integer, String, String> handler) {
		try {
			handler.process(dbIp, "db ip", null);
			handler.process(dbIpList, "db ip list", null);
			handler.process(dbPort, "db port", null);
			handler.process(dbms, "db type", null);
			handler.process(dbLoc, "db_loc", null);
			handler.process(dbName, "db", null);
			handler.process(dbVersion, "db version", null);
			handler.process(xosVersion, "xos version", null);
			handler.process(dbIsMulti, "db multi", null);
			handler.process(dbInstNo, "db inst no", null);
			handler.process(dbCpuCores, "cpu cores", null);
			handler.process(agentIp, "agent_ip", null);
			handler.process(startTime, "start time", null);
			handler.process(indexListHash, "index list", null);
			handler.process(statListHash, "stat list", null);
			handler.process(eventListHash, "event list", null);
			handler.process(osListHash, "os list", null);
			handler.process(osAgent, "os agent", null);
			handler.process(replicationName, "replication_name", null);
			handler.process(counterInterval, "counter_interval", null);
			handler.process(dbSlowSetting, "db_slow_setting", null);
			handler.process(agentSlowSetting, "agent_slow_setting", null);
			handler.process(tablesSetting, "tables_setting", null);
			handler.process(tableSpaceSetting, "table_space_setting", null);
			handler.process(serviceIp, "service_ip", null);

			/*
			handler.process(agentVersion, "agent version", null);
			handler.process(dbVersion, "DB version", null);
			handler.process(cpuCount, "cpu count", null);
			handler.process(physicalMemory, "physical memory", null);
			handler.process(osName, "OS name", null);
			handler.process(architecture, "architecture", null);

			handler.process(freeDiskSpace, "free disk space", null);
			handler.process(totalDiskSpace, "total disk space", null);
			*/
		} catch (Exception e) {
		}
	}

	@Override
	public short getPackType() {
        return PackEnum.DB_AGENT_INFO;
    }

    @Override
	public void write(DataOutputX dout) {
        super.write(dout);
        dout.writeValue(this.attr);
    }

    @Override
	public Pack read(DataInputX din) {
        super.read(din);
        din.readValue();
        return this;
    }
}
