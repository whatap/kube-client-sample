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

import whatap.lang.pack.bsm.BsmRecordPack;
import whatap.lang.pack.db.DbActiveSessionPack;
import whatap.lang.pack.db.DbAgentInfoPack;
import whatap.lang.pack.db.DbCloudWatchPack;
import whatap.lang.pack.db.DbCounterMinPack;
import whatap.lang.pack.db.DbDbSizePack;
import whatap.lang.pack.db.DbDeadLockInfoPack;
import whatap.lang.pack.db.DbIndex2Pack;
import whatap.lang.pack.db.DbIndexPack;
import whatap.lang.pack.db.DbLockInfoPack;
import whatap.lang.pack.db.DbNamePack;
import whatap.lang.pack.db.DbPqInfoPack;
import whatap.lang.pack.db.DbRealCounterPack;
import whatap.lang.pack.db.DbSettingsPack;
import whatap.lang.pack.db.DbSgaPack;
import whatap.lang.pack.db.DbSqlStatPack;
import whatap.lang.pack.db.DbTablesPack;
import whatap.lang.pack.db.DbTablespacePack;
import whatap.lang.pack.db.DbVacuumRunningPack;
import whatap.lang.pack.db.DbWaitClassPack;
import whatap.lang.pack.os.XosCounterPack;
import whatap.lang.pack.os.XosProcessListPack;
import whatap.lang.pack.sm.SMBasePack;
import whatap.lang.pack.sm.SMDiskPack;
import whatap.lang.pack.sm.SMNetworkPack;
import whatap.lang.value.DecimalValue;
import whatap.lang.value.DoubleValue;
import whatap.lang.value.FloatValue;
import whatap.lang.value.NullValue;
import whatap.lang.value.TextValue;
import whatap.lang.value.Value;

public class PackEnum {
	public final static short PARAMETER = 0x0100;
	public final static short COUNTER = 0x0200;
	public final static short COUNTER_1 = 0x0201;
	public final static short DOMAIN_COUNT_deprecated20180910 = 0x0210;

	public final static short PROFILE = 0x0300;
	public final static short BIZMON_HELP = 0x0301;
	public final static short PROFILE_STEP_SPLIT = 0x0302;
	public final static short ACTIVESTACK = 0x0400;
	public final static short ACTIVESTACK_1 = 0x0401;
	public final static short TEXT = 0x0700;
	public final static short HvTEXT = 0x0701;
	public final static short ERROR_SNAP = 0x0800;
	public final static short ERROR_SNAP_1 = 0x0801;
	public final static short REALTIME_USER = 0x0f00;
	public final static short REALTIME_USER_1 = 0x0f01;

	public final static short STAT_SERVICE = 0x0900;
	public final static short STAT_SERVICE_1 = 0x0901;
	public final static short STAT_SERVICE_2 = 0x0902;
	public final static short STAT_GENERAL = 0x0910;
	public final static short STAT_GENERAL_1 = 0x0911;
	public final static short STAT_SQL = 0x0a00;
	public final static short STAT_SQL_1 = 0x0a01;
	public final static short STAT_HTTPC = 0x0b00;
	public final static short STAT_HTTPC_1 = 0x0b01;
	public final static short STAT_ERROR = 0x0c00;
	public final static short STAT_ERROR_1 = 0x0c01;
	public final static short STAT_METHOD = 0x0e00;
	public final static short STAT_TOP_SERVICE = 0x1000;
	public final static short STAT_TOP_SERVICE_1 = 0x1001;
	public final static short STAT_REMOTE_IP = 0x1100;
	public final static short STAT_REMOTE_IP_1 = 0x1101;
	public final static short STAT_USER_AGENT = 0x1200;
	public final static short STAT_USER_AGENT_1 = 0x1201;// 서버에서 예약
	public final static short STAT_USER_AGENT_2 = 0x1202;
	//
	public final static short EVENT = 0x1400;
	//public final static short LOG = 0x1410;
	public final static short HITMAP = 0x1500;
	public final static short HITMAP_1 = 0x1501;
	public final static short HITVIEW = 0x1506;
	public final static short TAG_COUNT = 0x1601;

	public final static short COMPOSITE = 0x1700;
	public final static short BSM_RECORD = 0x1701;
//	public final static short AP_NUT = 0x1702;
//	public final static short ADDIN_COUNT = 0x1703;
	public final static short KUBE_MASTER_COUNT = 0x1704;
	public final static short KUBE_MASTER_STAT = 0x1705;
	public final static short KUBE_NODE = 0x1706;
	public final static short WEB_CHECK_COUNT = 0x1707;
	public final static short LOGSINK = 0x170a;
	public final static short ZIP = 0x170b;
	public final static short LOGSINK_ZIP = 0x170d;

	public final static short DB_AGENT_INFO = 0x4001;
	public final static short DB_NAME = 0x4002;
	public final static short DB_INDEX = 0x4003;
	public final static short DB_INDEX2 = 0x4004;	// 190319
	public final static short DB_COUNTER_REAL = 0x4006;
	public final static short DB_COUNTER_MIN = 0x4007;
	public final static short DB_ACTIVE_SESSION_LIST = 0x4008;
	public final static short DB_VACUUM_RUNNING = 0x4009;
	public final static short DB_SETTINGS = 0x400a;

	public final static short DB_DBSIZE = 0x400b;
	// public final static short DB_TABLESIZE = 0x400c;
	// public final static short DB_INDEXSIZE = 0x400d;

	public final static short DB_LOCK_INFO = 0x400e;
	public final static short DB_SQL_STAT = 0x400f;
	public final static short DB_PQ_INFO = 0x4010;
	public final static short DB_DEADLOCK_INFO = 0x4011;
	public final static short DB_TABLES = 0x4012;
	public final static short DB_TABLESPACE = 0x4013;
	public final static short DB_SGA = 0x4014;
	public final static short DB_WAIT_CLASS = 0x4015;
	public final static short DB_STATEMENTS = 0x4016;

	public final static short DB_AWR_SYSSTAT = 0x4020;
	public final static short DB_AWR_SYSEVENT = 0x4021;
	public final static short DB_AWR_SQLSTAT = 0x4022;

	public final static short DB_CLOUD_WATCH = 0x4080;

	public final static short ORA_COUNTER_REAL = 0x4106;
	public final static short ORA_ACTIVE_SESSION_LIST = 0x4108;

//	public final static short CW_AGENT_INFO = 0x5001;
//	public final static short CW_METRIC_INFO = 0x5002;
//	public final static short CW_METRIC_COUNTER = 0x5003;

	public final static short XOS_PROCESS_LIST = 0x5501;
	public final static short XOS_COUNTER = 0x5502;
	public final static short XOS_FILE = 0x5503;
	public final static short XOS_DISK_USAGE = 0x5504;
	public final static short XOS_MY_SLOW_QUERY = 0x5510;

	public final static short SM_BASE = 0x3000;
	public final static short SM_DISK = 0x3001;
	public final static short SM_NETWORK = 0x3002;
	public final static short SM_PROCESS = 0x3003;
	public final static short SM_PORT = 0x3004;
	public final static short SM_LOG_EVENT = 0x3005;
	public final static short SM_DOWN_CHECK = 0x3006;
	public static final short SM_PROC_GROUP = 0x3007;
	public final static short SM_BASE_1 = 0x3008;
	public static final short SM_META = 0x3009;
	public static final short SM_NUT = 0x3010;
	public final static short SM_ATTR = 0x3011;

	public static Pack create(short p) {
		switch (p) {
		case PARAMETER:
			return new ParamPack();
		case COUNTER_1:
			return new CounterPack1();
		case PROFILE:
			return new ProfilePack();
		case PROFILE_STEP_SPLIT:
			return new ProfileStepSplitPack();
//		case BIZMON_HELP:
//			return new BizMonHelpPack();
		// case ACTIVESTACK:
		// return new ActiveStackPack();
		case ACTIVESTACK_1:
			return new ActiveStackPack1();
		case TEXT:
			return new TextPack();
		case HvTEXT:
			return new HvTextPack();
		case ERROR_SNAP_1:
			return new ErrorSnapPack1();

		case STAT_SERVICE:
			return new StatTransactionPack();
		case STAT_SERVICE_1:
		case STAT_SERVICE_2:
			return new StatTransactionPack1(p);
		case STAT_GENERAL:
		case STAT_GENERAL_1:
			return new StatGeneralPack(p);
		case STAT_SQL:
		case STAT_SQL_1:
			return new StatSqlPack(p);
		case STAT_HTTPC:
		case STAT_HTTPC_1:
			return new StatHttpcPack(p);
		case STAT_ERROR:
		case STAT_ERROR_1:
			return new StatErrorPack(p);
		case REALTIME_USER:
		case REALTIME_USER_1:
			return new RealtimeUserPack1(p);
		// case STAT_TOP_SERVICE:
		// return new StatTopServicePack();
		case STAT_REMOTE_IP:
		case STAT_REMOTE_IP_1:
			return new StatRemoteIpPack(p);
		case STAT_USER_AGENT:
		case STAT_USER_AGENT_1:
		case STAT_USER_AGENT_2:
			return new StatUserAgentPack1(p);

		case EVENT:
			return new EventPack();
//		case LOG:
//			return new LogPack();
		// case HITMAP:
		// return new HitMapPack();
		case HITMAP_1:
			return new HitMapPack1();

		case HITVIEW:
			return new HitViewPack();

		case TAG_COUNT:
			return new TagCountPack();

		case COMPOSITE:
			return new CompositePack();
//		case ADDIN_COUNT:
//			return new AddinCountPack();
		case KUBE_MASTER_COUNT:
			return new KubeMasterCountPack();
		case KUBE_MASTER_STAT:
			return new KubeMasterStatPack();
		case KUBE_NODE:
			return new KubeNodePack();
		case WEB_CHECK_COUNT:
			return new WebCheckCountPack();

		case LOGSINK:
			return new LogSinkPack();
		case ZIP:
			return new ZipPack();
		case LOGSINK_ZIP:
			return new LogSinkZipPack();

		case DB_AGENT_INFO:
			return new DbAgentInfoPack();
		case DB_NAME:
			return new DbNamePack();
		case DB_INDEX:
			return new DbIndexPack();
		case DB_INDEX2:
			return new DbIndex2Pack();
		case DB_COUNTER_REAL:
			return new DbRealCounterPack();
		case DB_COUNTER_MIN:
			return new DbCounterMinPack();
		case DB_ACTIVE_SESSION_LIST:
			return new DbActiveSessionPack();
		case DB_VACUUM_RUNNING:
			return new DbVacuumRunningPack();
		case DB_SETTINGS:
			return new DbSettingsPack();
		case DB_LOCK_INFO:
			return new DbLockInfoPack();
		case DB_PQ_INFO:
			return new DbPqInfoPack();
		case DB_DBSIZE:
			return new DbDbSizePack();
		case DB_SQL_STAT:
			return new DbSqlStatPack();
		case DB_DEADLOCK_INFO:
			return new DbDeadLockInfoPack();
		case DB_TABLES:
			return new DbTablesPack();
		case DB_TABLESPACE:
			return new DbTablespacePack();
		case DB_SGA:
			return new DbSgaPack();
		case DB_WAIT_CLASS:
			return new DbWaitClassPack();

		case DB_CLOUD_WATCH:
			return new DbCloudWatchPack();

		case BSM_RECORD:
			return new BsmRecordPack();

//		case CW_METRIC_INFO:
//			return new CwMetricInfoPack();

		case XOS_PROCESS_LIST:
			return new XosProcessListPack();
		case XOS_COUNTER:
			return new XosCounterPack();

		case SM_BASE:
		case SM_BASE_1:
			return new SMBasePack(p);
		case SM_DISK:
			return new SMDiskPack();
		case SM_NETWORK:
			return new SMNetworkPack();

		default:
			if (nextEnum != null)
				return nextEnum.make(p);
			else {
			System.out.println("type="+p);
				throw UnknownPackError.instance;
			}
		}
	}

	public static interface NextEnum {
		public Pack make(short p);
	}

	private static NextEnum nextEnum = null;

	public static void setLocalEnum(NextEnum nextEnum) {
		PackEnum.nextEnum = nextEnum;
	}

	@SuppressWarnings("serial")
	public static class UnknownPackError extends Error {
		public final static UnknownPackError instance = new UnknownPackError("Unknown pack type");

		public UnknownPackError(String msg) {
			super(msg);
		}
	}

	public static Value toValue(Object value) throws Exception {
		if (value == null) {
			return new NullValue();
		} else if (value instanceof String) {
			return new TextValue((String) value);
		} else if (value instanceof Number) {
			if (value instanceof Float)
				return new FloatValue((Float) value);
			else if (value instanceof Double)
				return new DoubleValue((Double) value);
			else
				return new DecimalValue(((Number) value).longValue());
		} else {
			return new TextValue(value.toString());
		}
	}

	public static void main(String[] args) {
		System.out.println(Integer.toHexString(-31822));
	}
}
