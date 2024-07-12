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
package whatap.lang.step;

import whatap.io.DataInputX;
import whatap.io.DataOutputX;

import java.io.IOException;

public class StepEnum {
	public final static byte METHOD = 1;
	public final static byte SQL = 2;
	public final static byte RESULTSET = 3;
	public final static byte HTTPCALL = 4;
	public final static byte SOCKET = 5;
	public final static byte ACTIVE_STACK = 6;
	public final static byte MESSAGE = 7;
	public final static byte DBC = 8;
	public final static byte SQL_2 = 9;
	public final static byte METHOD_2 = 11;
	//
	public final static byte METHOD_3 = 12;
	public final static byte SQL_3 = 13;
	public final static byte HTTPCALL_3 = 14;
	public final static byte SECURE_MESSAGE = 15;
	public static final byte CHILD_THREAD = 16;
	
	public final static byte METHOD_X = 17;
	public final static byte SQL_X = 18;
	public final static byte HTTPCALL_X = 19;
	public final static byte REMOTE = 20;
	public final static byte DBC_X = 21;
	public final static byte MESSAGE_X = 22;
	
	public static Step create(byte type) throws IOException {
		switch (type) {
//		case METHOD:
//			return new MethodStep_1();
//		case METHOD_2:
//			return new MethodStep_2();
//		case METHOD_3:
//			return new MethodStep_3();
		case METHOD_X:
			return new MethodStepX();
//		case SQL:
//			return new SqlStep_1();
//		case SQL_2:
//			return new SqlStep_2();
//		case SQL_3:
//			return new SqlStep_3();
		case SQL_X:
			return new SqlStepX();
		case RESULTSET:
			return new ResultSetStep();
		case SOCKET:
			return new SocketStep();
//		case HTTPCALL:
//			return new HttpcStep_1();
//		case HTTPCALL_3:
//			return new HttpcStep_3();
		case HTTPCALL_X:
			return new HttpcStepX();
		case ACTIVE_STACK:
			return new ActiveStackStep();
		case MESSAGE:
			return new MessageStep();
		case SECURE_MESSAGE:
			return new SecureMsgStep();
		case DBC:
			return new DBCStep();
		case REMOTE:
			return new RemoteStep();
		case DBC_X:
			return new DBCStepX();
		case MESSAGE_X:
			return new MessageStepX();
//		case CHILD_THREAD:
//			return new ChildThreadStep();
		default:
			throw new RuntimeException("unknown step type=" + type);
		}
	}

	public static void main(String[] args) {
	   SqlStepX x = new SqlStepX();
	 
	   DataOutputX o = new DataOutputX();
	   o.writeStep(x);
	   byte[] b= o.toByteArray();
	   DataInputX i = new DataInputX(b);
	   x = (SqlStepX)i.readStep();
	   
	   
		System.out.println(x);
	}
}
