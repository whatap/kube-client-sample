/*
 *  Copyright 2016 whatap.io. 
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

public class UnsignedByte {
	public static int MAX_VALUE = 0xff;
	public static int MIN_VALUE = 0;

	public static int toInt(byte b) {
		return b & 0xff;
	}

	public static byte toByte(int x) {
		return (byte) x;
	}

	public static byte cutByte(int x) {
		return x > MAX_VALUE ? (byte) 0xff : (byte) x;
	}
}
