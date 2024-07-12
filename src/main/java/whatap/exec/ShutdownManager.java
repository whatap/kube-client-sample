/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package whatap.exec;

import whatap.util.IShutdown;

import java.util.List;
import java.util.Vector;
public class ShutdownManager {
	private static List<IShutdown> instances = new Vector<IShutdown>();
	public static void add(IShutdown instance) {
		instances.add(instance);
	}
	public synchronized static void shutdown() {
		if (instances.size() == 0) {
			return;
		}
		for (int i = 0; i < instances.size(); i++) {
			try {
				instances.get(i).shutdown();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		instances.clear();
	}
	static {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			public void run() {
				ShutdownManager.shutdown();
			}
		});
	}
}
