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
package whatap.io;

import whatap.util.DateUtil;
import whatap.util.IntKeyMap;
import whatap.util.ThreadUtil;

import java.util.Enumeration;
public class FlushCtr extends Thread {
	private static class FlushItem {
		long lastFlushTime = DateUtil.currentTime();;
		IFlushable object;
		public FlushItem(IFlushable object) {
			this.object = object;
		}
	}
	private static FlushCtr instance = null;
	public final static synchronized FlushCtr getInstance() {
		if (instance == null) {
			instance = new FlushCtr();
			instance.setDaemon(true);
			instance.setName("FlushCtr");
			instance.start();
		}
		return instance;
	}
	protected FlushCtr() {
	}
	public void shutdown() {
		running = false;
	}
	private IntKeyMap<FlushItem> table = new IntKeyMap<FlushItem>();
	private boolean running = true;
	public void run() {
		while (running) {
			try {
				process();
			} catch (Throwable t) {
				t.printStackTrace();
			}
			ThreadUtil.sleep(1000);
		}
	}
	public void regist(IFlushable object) {
		table.put(System.identityHashCode(object), new FlushItem(object));
	}
	public void unregist(IFlushable object) {
		table.remove(System.identityHashCode(object));
		if (object.isDirty()) {
			object.flush();
		}
	}
	private void process() {
		Enumeration<FlushItem> en = table.values();
		while (en.hasMoreElements()) {
			FlushItem f = en.nextElement();
			long now = DateUtil.currentTime();
			if (f.object.isDirty() && now >= f.lastFlushTime + f.object.interval()) {
				f.lastFlushTime = now;
				f.object.flush();
			}
		}
	}
}
