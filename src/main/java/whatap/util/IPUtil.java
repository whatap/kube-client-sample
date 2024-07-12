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
package whatap.util;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;

import java.util.ArrayList;

public class IPUtil {
	public static int IP_127_0_0_1=toInt("127.0.0.1");
	
	public static String toString(int ip) {
		return toString(DataOutputX.toBytes(ip));
	}
	public static int toInt(byte[] ip) {
		return DataInputX.toInt(ip, 0); 
	}
		
	public static String toString(byte[] ip) {
		if (ip == null)
			return "0.0.0.0";
		try {
			StringBuffer sb = new StringBuffer();
			sb.append(ip[0] & 0xff);
			sb.append(".");
			sb.append(ip[1] & 0xff);
			sb.append(".");
			sb.append(ip[2] & 0xff);
			sb.append(".");
			sb.append(ip[3] & 0xff);
			return sb.toString();
		} catch (Throwable e) {
			return "0.0.0.0";
		}
	}

	private static String[] split(String s) {
		ArrayList<String> arr = new ArrayList<String>(16);

		StringBuilder sb = new StringBuilder(32);
		for (int i = 0; i < s.length(); i++) {
			switch (s.charAt(i)) {
			case '0':
			case '1':
			case '2':
			case '3':
			case '4':
			case '5':
			case '6':
			case '7':
			case '8':
			case '9':
				sb.append(s.charAt(i));
				break;
			default:
				if (sb.length() > 0) {
					arr.add(sb.toString());
					sb = new StringBuilder(32);
				}
			}
		}
		if (sb.length() > 0) {
			arr.add(sb.toString());
			sb = new StringBuilder(32);
		}
		return arr.toArray(new String[arr.size()]);
	}

	public static byte[] toBytes(String ip) {
		if (ip == null) {
			return empty;
		}
		byte[] result = new byte[4];
		String[] s = split(ip);
		long val;
		try {
			if (s.length < 4)
				return empty;
			for (int i = 0; i < 4; i++) {
				val = Integer.parseInt(s[i]);
				if (val < 0 || val > 0xff)
					return empty;
				result[i] = (byte) (val & 0xff);
			}
		} catch (Throwable e) {
			return empty;
		}
		return result;
	}

	public static byte[] toBytes(int ip) {
		return DataOutputX.toBytes(ip);
	}

	public static boolean isOK(byte[] ip) {
		return ip != null && ip.length == 4;
	}

	public static boolean isNotLocal(byte[] ip) {
		return isOK(ip) && (ip[0] & 0xff) != 127;
	}

	private static byte[] empty = new byte[] { 0, 0, 0, 0 };

	public static int toInt(String ip) {
		return DataInputX.toInt(toBytes(ip), 0);
	}

	public static boolean isEmpty(byte[] ip) {
		if (ip == null || ip == empty || ip.length != 4)
			return true;
		return (ip[0] | ip[1] | ip[2] | ip[2]) == 0;
	}
}
