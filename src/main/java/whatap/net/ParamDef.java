package whatap.net;

import whatap.util.ClassUtil;
import whatap.util.IntKeyMap;
import whatap.util.StringIntMap;

public class ParamDef {
	public final static int KEEPALIVE = 1;
	public static final int AGENT_BOOT_ENV = 2;
	public final static int CONFIGURE_UPDATE = 3;
	public static final int CONFIGURE_GET = 4;
	public static final int COMPO_VERSIONS = 5;
	public static final int THREAD_LIST = 7;
	public static final int THREAD_DETAIL = 8;
	public static final int GET_ACTIVE_STACK = 9;
	public static final int HEAP_HISTO = 10;
	public static final int LOADED_CLASS_LIST = 11;
	public static final int GET_ENV = 12;
	public static final int SYSTEM_GC = 13;
	public static final int SET_CONFIG = 14;
	public static final int OPEN_SOCKET_LIST = 15;
	public static final int LOADED_CLASS_DETAIL = 16;
	public static final int LOADED_CLASS_REDEFINE = 17;

	//agent update
	public static final int AGENT_JAR_LIST = 18;
	public static final int AGENT_JAR_SAVE = 19;
	public static final int AGENT_JAR_DELETE = 20;

	public static final int RESET_STRING_SENT_MARK = 21;

	public static final int AGENT_LOG_LIST = 22;
	public static final int AGENT_LOG_READ = 23;

	public static final int THREAD_CONTROL = 24;

	public static final int GET_ACTIVE_TRANSACTION_LIST = 25;
	public static final int GET_ACTIVE_TRANSACTION_DETAIL = 26;
	public static final int GET_TOPOLOGY = 27;
	public static final int FIND_ACT_TX_LIST_FOR_DB = 46;

    public static final int INFRA_NET_STAT=28;
    public static final int INFRA_PS_EF=29;
    public static final int INFRA_DOCKER_LIST=30;
    public static final int INFRA_REMOTE_CMD=33;
    public static final int INFRA_PEER_LIST = 34;
    public static final int INFRA_AGENT_UPDATE = 35;

    //java 1.2.1-20180303
	public static final int AGENT_DUMP_LIST = 31;
	public static final int AGENT_DUMP_READ = 32;

	//36踰덇퉴吏� �궗�슜以�

    public static final int AGENT_STAT = 35;
	public static final int JVM_THREAD_DUMP = 37;
	public static final int JVM_HEAP_DUMP = 38;
	public static final int KUBENETES = 39;
	public static final int KUBE_MANIFEST = 48;
	public static final int GET_ACTIVE_STATS = 40;

	public static final int SET_URLNORM = 44;

	public static final int MODULE_DEPENDENCY = 101;
	public static final int WEB_CHECK_MASTER = 102;

	public static final int CONFBASE_GET = 103;
	public static final int CONFBASE_SET = 104;
	public static final int COLLECTION_LIST = 105;
	
	// dbx
	public static final int DBX_INFO = 201; // test
	public static final int DBX_SETTINGS = 202;
	public static final int DBX_USER_SCRIPT = 207;
	public static final int DBX_SCRIPT_LIST = 208;
	public static final int DBX_SCRIPT_SQL = 209;
	public static final int DBX_KILL_SESSION = 210;
	public static final int DBX_GET_ALERT = 211;
	public static final int DBX_SET_ALERT = 212;
	public static final int DBX_PLAN = 213;	// DBX_PLAN_HASH = 224
	public static final int DBX_SESSION_LIST = 214;

	public static final int DBX_SET_CPUCORES = 215;

	public static final int DBX_INDEX_LIST = 216;
	public static final int DBX_INDEX_ADD = 217;
	public static final int DBX_INDEX_DELETE = 218;

	public static final int DBX_GET_FILELIST = 219;
	public static final int DBX_GET_FILE = 220;

	public static final int DBX_GET_CLOUDWATCH_FILELIST = 221;
	public static final int DBX_GET_CLOUDWATCH_FILE = 222;
	public static final int DBX_PLAN_JSON = 226;
	public static final int DBX_PARAMETER = 227;


	// dbx - oracle
	public static final int DBX_SESSION_STAT = 203;
	public static final int DBX_SESSION_EVENT = 204;
	public static final int DBX_TOPSTAT = 205;
	public static final int DBX_TOPEVENT = 206;
	public static final int DBX_TABLE_INFO = 223;
	public static final int DBX_PLAN_HASH = 224;
	public static final int DBX_OBJECT_INFO = 225;

	// dbx - pg

	//ANY CALL
	public static final int CONFIG_DBC = 301;
	public static final int METHOD_PERF_STAT = 302;
	public static final int BLOCKING_DETECT = 303;

	// CloudWatch
	public static final int CW_INFO = 401;
	public static final int CW_EVENTS = 402;
	public static final int CW_CREDENTIAL = 403;
	public static final int CW_REGIONS = 404;
	public static final int CW_METRIC_NAMES = 405;
	public static final int CW_AWS_RESOURCE_IDS = 406 ;
	public static final int CW_STATISTICS = 407;
	public static final int CW_UNITS = 408;
	public static final int CW_METRICS = 409;


	public static final int AGENT_META_TAGS = 450;

	// INTEGRATIONS - Agent 에서 보낼때
	public static final int INTEGRATIONS_META_MAIN = 500;
	// INTEGRATIONS - Agency 에서 보낼때
	public static final int INTEGRATIONS_META_INFO = 501;


	public static IntKeyMap<String> values = ClassUtil.getConstantValueMap(ParamDef.class, Integer.TYPE);
	public static StringIntMap keys = ClassUtil.getConstantKeyMap(ParamDef.class, Integer.TYPE);

}
