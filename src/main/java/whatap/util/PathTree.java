/*
* *  Copyright 2015 the original author or authors. 
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
/**
 * @author PaulKim
 */
package whatap.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Stack;

public class PathTree<V> {
	private static class PATH {
		public String[] nodes;
		public int level;

		public PATH(String[] nodes) {
			this.nodes = nodes;
		}

		public boolean hasChild() {
			return level + 1 < nodes.length;
		}

		public String node() {
			return nodes[level];
		}

		@Override
		public String toString() {
			return "PATH [nodes=" + Arrays.toString(nodes) + ", level=" + level + "]";
		}
	}
	public class ENTRY {
		String node;
		V value;
		ENTRY right;
		ENTRY child;
		ENTRY parent;

		public boolean include(String v) {
			return node.equals("*") || node.equals(v);
		}

		public List<String> path() {
			Stack<String> sk = new Stack<String>();
			ENTRY cur = this;
			while (cur != PathTree.this.top) {
				sk.add(cur.node);
				cur = cur.parent;
			}
			ArrayList<String> arr = new ArrayList<String>(sk.size());
			while (sk.isEmpty() == false) {
				arr.add(sk.pop());
			}
			return arr;
		}

		public V value() {
			return this.value;
		}

		public String node() {
			return this.node;
		}
	}

	protected ENTRY top = new ENTRY();
	protected int count;

	public V insert(String path, V value) {
		if (StringUtil.isEmpty(path) )
			return null;
		return insert(StringUtil.split(path, '/'), value);
	}

	public synchronized V insert(String[] paths, V value) {
		if (ArrayUtil.isEmpty(paths) || value == null)
			return null;
		if(paths.length==1)
			return null;
		
		PATH path = new PATH(paths);
		if (top.child == null) {
			ENTRY cur = top;
			cur.child = new ENTRY();
			cur.child.parent = cur;
			cur = cur.child;
			cur.node = path.node();
			count++;
			return expand(cur, path, value);
		} else {
			return insert(top, top.child, path, value);
		}
	}

	private V expand(ENTRY cur, PATH path, V value) {
		// 현재의 트리를 아래쪽으로 확장한다.
		while (path.hasChild()) {
			path.level++;
			cur.child = new ENTRY();
			cur.child.parent = cur;
			cur = cur.child;
			cur.node = path.node();
			count++;
		}
		V old = cur.value;
		cur.value = value;
		return old;
	}

	private V insert(ENTRY p, ENTRY cur, PATH path, V value) {
		
		
		if (path.node().equals(cur.node)) {
			if (path.hasChild() == false) {
				V old = cur.value;
				cur.value = value;
				return old;
			}
			path.level++;
			if (cur.child != null) {
				return insert(cur, cur.child, path, value);
			} else {
				cur.child = new ENTRY();
				cur.child.parent=cur;//20160622패치함..paul
				cur = cur.child;
				cur.node = path.node();
				return expand(cur, path, value);
			}
		} else if (cur.right != null) {
			return insert(p, cur.right, path, value);
		} else {
			
			if (path.node().equals("*")) {
				// *노드는 오른쪽끝에 추가한다.
				cur.right = new ENTRY();
				cur.right.parent = p;
				cur = cur.right;
				cur.node = path.node();
				count++;
				return expand(cur, path, value);
			} else {
				// 일반 노드는 부모 노드의 첫번째 자식으로 등록한다.
				cur = new ENTRY();
				cur.parent = p;
				cur.right = p.child;
				p.child = cur;
				cur.node = path.node();
				count++;
				return expand(cur, path, value);
			}
		}
	}

	public V find(String path) {
		try {
			return find(StringUtil.split(path, '/'));
		} catch (Exception e) {
			return null;
		}
	}

	public V find(String[] path) {
		if (path == null || path.length == 0)
			return null;
		try {
			return find(top.child, new PATH(path));
		} catch (Exception e) {
			return null;
		}
	}

	private V find(ENTRY cur, PATH m) {
		if (cur.include(m.node())) {
			if (m.hasChild() == false) {
				return cur.value;
			}
			m.level++;
			if (cur.child != null) {
				return find(cur.child, m);
			}
		} else if (cur.right != null) {
			return find(cur.right, m);
		}
		return null;
	}

	public int size() {
		return count;
	}

	public synchronized Enumeration<List<String>> paths() {
		return new Enumer(TYPE.PATHS);
	}

	public synchronized Enumeration<String> values() {
		return new Enumer(TYPE.VALUES);
	}

	public synchronized Enumeration<ENTRY> entries() {
		return new Enumer(TYPE.ENTRIES);
	}

	private class Enumer implements Enumeration {
		ENTRY entry = PathTree.this.top.child;
		private byte type;

		Enumer(byte type) {
			this.type = type;
		}

		public boolean hasMoreElements() {
			return entry != null;
		}

		public Object nextElement() {
			if (entry == null)
				throw new NoSuchElementException("no more next");
			ENTRY e = entry;
			if (entry.child != null)
				entry = entry.child;
			else {
				while (entry!=null && entry.right == null) {
					entry = entry.parent;
					if (entry == PathTree.this.top) {
						entry = null;
						switch (type) {
						case TYPE.PATHS:
							return e.path();
						case TYPE.VALUES:
							return e.value;
						default:
							return e;
						}
					}
				}
				if(entry!=null){
					entry = entry.right;
				}
			}
			switch (type) {
			case TYPE.PATHS:
				return e.path();
			case TYPE.VALUES:
				return e.value;
			default:
				return e;
			}
		}
	}

//	public static void main(String[] args) {
//		PathTree<String> t = new PathTree<String>();
//		t.insert("/cube/pcode/*/history/series", "/cube/pcode/{pcode}/history/series");
//		t.insert("/tx_country/pcode/*/cube/point", "/tx_country/pcode/{pcode}/cube/point");
//		t.insert("/rt_user/pcode/*/cube/series", "/rt_user/pcode/{pcode}/cube/series");
//		t.insert("/summary/pcode/*/cube", "/summary/pcode/{pcode}/cube");
//		t.insert("/hitmap/pcode/*/cube/series", "/hitmap/pcode/{pcode}/cube/series");
//		t.insert("/tx/pcode/*/top/cube", "/tx/pcode/{pcode}/top/cube");
//		t.insert("/event/pcode/*/top/cube", "/event/pcode/{pcode}/top/cube");
//		t.insert("/tps_res_time/pcode/*/cube/series", "/tps_res_time/pcode/{pcode}/cube/series");
//		t.insert("/cpu_heap/pcode/*/cube/series", "/cpu_heap/pcode/{pcode}/cube/series");
//		t.insert("/summary/pcode/*", "/summary/pcode/{pcode}");
//		t.insert("/counter/pcode/*/oid/*/{name}", "/counter/pcode/{pcode}/oid/{oid:.*}/{name}");
//		t.insert("/counter/pcode/*/name", "/counter/pcode/{pcode}/name");
//		t.insert("/stat/pcode/*/oid/*", "/stat/pcode/{pcode}/oid/{oid}");
//		t.insert("/report/pcode/*/daily/summary", "/report/pcode/{pcode}/daily/summary");
//		t.insert("/config/*/*/get", "/config/{pcode}/{oid:.*}/get");
//		t.insert("/config/*/*/set", "/config/{pcode}/{oid:.*}/set");
//		t.insert("/agent/pcode/*/oid/*/show_config", "/agent/pcode/{pcode}/oid/{oid:.*}/show_config");
//		t.insert("/agent/pcode/*/oid/*/add_config", "/agent/pcode/{pcode}/oid/{oid:.*}/add_config");
//		t.insert("/agent/pcode/*/oids", "/agent/pcode/{pcode}/oids");
//		t.insert("/agent/pcode/*/oid/*/remove", "/agent/pcode/{pcode}/oid/{oid}/remove");
//		t.insert("/agent/pcode/*/oid/*/env", "/agent/pcode/{pcode}/oid/{oid}/env");
//		t.insert("/agent/pcode/*/oid/*/threadlist", "/agent/pcode/{pcode}/oid/{oid:.*}/threadlist");
//		t.insert("/agent/pcode/*/oid/*/thread/{threadId}", "/agent/pcode/{pcode}/oid/{oid:.*}/thread/{threadId}");
//		t.insert("*", "${springfox.documentation.swagger.v2.path:/v2/api-docs}");
//		t.insert("/yard/pcode/*/oid", "/yard/pcode/{pcode}/oid");
//		t.insert("/yard/pcode/*/oid/*/oname", "/yard/pcode/{pcode}/oid/{oid}/oname");
//		t.insert("/yard/pcode/*/disk", "/yard/pcode/{pcode}/disk");
//		t.insert("/yard/pcode/*/disk/clear", "/yard/pcode/{pcode}/disk/clear");
//
//
////		System.out.println(t.find("/pcode/123/disk/clear"));
////		System.out.println(t.find("/pcode/1234/disk"));
//		System.out.println(t.find("/config/123/4/get"));
	
//	}
	
	@Override
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("{\n");
		Enumeration<ENTRY> en = this.entries();
		while (en.hasMoreElements()) {
			ENTRY e = en.nextElement();
			sb.append("    ").append(e.path() + "=>" + e.value()).append("\n");
		}
		sb.append("}");
		return sb.toString();
	}
	
	public static void main(String[] args) {
		PathTree<String> t = new PathTree<String>();
		t.insert("/api/internal/v1/panels/*", "/api/internal/v1/panels/{panelNo}");
		t.insert("/api/internal/v1/panels/push", "/api/internal/v1/panels/push");
		System.out.println(t.find("/api/internal/v1/panels/123"));
		System.out.println(t.find("/api/internal/v1/panels/push"));
		System.out.println(t);
		
		
	}
}
