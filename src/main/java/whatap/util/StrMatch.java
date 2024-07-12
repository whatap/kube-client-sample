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

public class StrMatch {
	protected byte comp = COMP.EQU;
	protected String start, end, mid, mid2;
	protected String pattern;
	public boolean equals(Object obj) {
		if (obj instanceof StrMatch) {
			return this.pattern.equals(((StrMatch) obj).pattern);
		}
		return false;
	}
	public int hashCode() {
		return pattern.hashCode();
	}
	public StrMatch(String pattern) {
		this('*', pattern);
	}
	private  String any1, any2;
	public StrMatch(char CHAR, String pattern) {
		this.pattern = pattern;
		this.any1 = new String(new char[] { CHAR });
		this.any2 = new String(new char[] { CHAR, CHAR });
		if (any1.equals(pattern) || any2.equals(pattern)) {
			comp = COMP.ANY;
			return;
		}
		int length = pattern.length();
		if (length < 2) {
			comp = COMP.EQU;
			mid = pattern;
			return;
		}
		boolean anyStart = pattern.charAt(0) == CHAR;
		boolean anyEnd = pattern.charAt(length - 1) == CHAR;
		int x = pattern.indexOf(CHAR, 1);
		boolean anyMid = x > 0 && x < (length - 1);
		if (anyMid) {
			if (anyStart && anyEnd) {
				comp = COMP.MID_MID;
				mid = pattern.substring(1, x);
				mid2 = pattern.substring(x + 1, length - 1);
			} else if (anyStart) {
				comp = COMP.MID_END;
				mid = pattern.substring(1, x);
				end = pattern.substring(x + 1);
			} else if (anyEnd) {
				comp = COMP.STR_MID;
				start = pattern.substring(0, x);
				mid = pattern.substring(x + 1, length - 1);
			} else {
				int y = pattern.indexOf(CHAR, x + 1);
				if (y > 0) {
					comp = COMP.STR_MID_END;
					start = pattern.substring(0, x);
					mid = pattern.substring(x + 1, y);
					end = pattern.substring(y + 1);
				} else {
					comp = COMP.STR_END;
					start = pattern.substring(0, x);
					end = pattern.substring(x + 1);
				}
			}
		} else {
			if (anyStart && anyEnd) {
				comp = COMP.MID;
				mid = pattern.substring(1, length - 1);
			} else if (anyStart) {
				comp = COMP.END;
				end = pattern.substring(1);
			} else if (anyEnd) {
				comp = COMP.STR;
				start = pattern.substring(0, length - 1);
			} else {
				comp = COMP.EQU;
				mid = pattern;
			}
		}
	}
	
	private StrMatch() {
	}

	public static StrMatch createForLikeOperator(String pattern) {
		StrMatch out = new StrMatch();
		out.pattern = pattern;
		if ("*".equals(pattern)) {
			out.comp = COMP.ANY;
			return out;
		}
		int length = pattern.length();
		if (length < 2) {
			out.comp = COMP.EQU;
			out.mid = pattern;
			return out;
		}
		boolean anyStart = pattern.charAt(0) == '*';
		boolean anyEnd = pattern.charAt(length - 1) == '*';
		if (anyStart) {
			if (anyEnd) {
				out.comp = COMP.MID;
				out.mid = pattern.substring(1, length - 1);
			} else {
				out.comp = COMP.END;
				out.end = pattern.substring(1);
			}
		} else if (anyEnd) {			
			out.comp = COMP.STR;
			out.start = pattern.substring(0, length - 1);
		} else {
			int x = pattern.indexOf('*', 1);
			if (x > 0) {
				out.comp = COMP.STR_END;
				out.start = pattern.substring(0, x);
				out.end = pattern.substring(x + 1);
			} else {
				out.comp = COMP.EQU;
				out.mid = pattern;
			}
		}
		return out;
	}
	public boolean include(String target) {
		if (target == null || target.length() == 0)
			return false;
		switch (comp) {
		case COMP.ANY:
			return true;
		case COMP.EQU:
			return target.equals(mid);
		case COMP.STR:
			return target.startsWith(start);
		case COMP.STR_MID:
			return target.startsWith(start) && target.indexOf(mid) >= 0;
		case COMP.STR_END:
			return target.startsWith(start) && target.endsWith(end);
		case COMP.STR_MID_END:
			return target.startsWith(start) && target.indexOf(mid, start.length()) >= 0 && target.endsWith(end);
		case COMP.MID:
			return target.indexOf(mid) >= 0;
		case COMP.MID_MID:
			int x = target.indexOf(mid);
			if (x < 0)
				return false;
			return target.indexOf(mid2, x + mid.length()) >= 0;
		case COMP.MID_END:
			return target.indexOf(mid) >= 0 && target.endsWith(end);
		case COMP.END:
			return target.endsWith(end);
		default:
			return false;
		}
	}
	public String toString() {
		return pattern;
	}
	public String getPattern() {
		return pattern;
	}
	public static void main(String[] args) {
		StrMatch sc = new StrMatch("**");
		System.out.println(sc.pattern);
	}
}
