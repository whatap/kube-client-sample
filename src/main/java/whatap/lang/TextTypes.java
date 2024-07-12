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
package whatap.lang;

import whatap.util.ClassUtil;
import whatap.util.IntKeyMap;
import whatap.util.StringIntMap;

public class TextTypes {
	public final static byte SERVICE = 1;
	public final static byte SQL = 2;
	public final static byte DB_URL = 3;
	public final static byte HTTPC_URL = 4;
	public final static byte ERROR = 5;
	public final static byte METHOD = 6;
	public final static byte STACK_ELEMENTS = 7;
	public final static byte REFERER = 8;
	public final static byte USER_AGENT = 9;
	public final static byte HTTPC_HOST = 10;
	public final static byte MESSAGE = 11;
	public static final byte CRUD = 12;
	public static final byte ONAME = 13;
	public static final byte COMMAND = 14;
	public static final byte USER_AGENT_OS = 15;
	public static final byte USER_AGENT_BROWSER = 16;
	public static final byte CITY = 17;

	public static final byte LOGIN = 18;
	public final static byte SQLPARAM = 19;
	public final static byte HTTP_DOMAIN = 20;

	public static final byte SYS_DEVICE_ID = 21;
	public static final byte SYS_MOUNT_POINT = 22;
	public static final byte SYS_FILE_SYSTEM = 23;
	public static final byte SYS_NET_DESC = 24;
	public static final byte SYS_PROC_CMD1 = 26;
	public static final byte SYS_PROC_CMD2 = 27;
	public static final byte SYS_PROC_USER = 28;
	public static final byte SYS_PROC_STATE = 29;
	public static final byte SYS_PROC_FILENAME = 30;
	public static final byte SM_LOG_FILE = 31;
	public static final byte EXT_META = 32;

	public static final byte DB_COUNTER_NAME = 41;
	public static final byte DB_COUNTER_UNIT = 42;
	public static final byte DB_ATTRIBUTE = 45;
	public static final byte DB_CON_ID = 46;
	public static final byte DB_USER_ID = 47;

	public static final byte CW_AGENT_IP = 51;
	public static final byte CW_MXID = 52;

	public static final byte MTRACE_SPEC = 53;
	public static final byte MTRACE_CALLER_URL = 54;

	public static final byte ADDIN_AID_NAME = 55;
	public static final byte ADDIN_COUNT_NAME = 56;

	public static final byte OKIND_NAME = 57;
	public static final byte KUBE_COUNT_NAME = 59;

	public static final byte CONTAINER_ID = 60;
	public static final byte PODNAME = 61;

	public static final byte CONTAINER_IMAGE = 62;
	public static final byte ONODE_NAME = 63;
	public static final byte CONTAINER_COMMAND = 64;
	public static final byte REPLICASETNAME = 65;
	public static final byte NAMESPACE = 66;
//	public static final byte REPLICATIONCONTROLLERNAME = 67;

	public static IntKeyMap<String> values = ClassUtil.getConstantValueMap(TextTypes.class, Byte.TYPE);
	public static StringIntMap names = ClassUtil.getConstantKeyMap(TextTypes.class, Byte.TYPE);

}
