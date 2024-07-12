package whatap.util;

/*
 *  Copyright 2015 Scouter Project.
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
 *
 */
public class AnsiPrint {
	public static boolean enable = SystemUtil.IS_WINDOWS == false;
	public static final String ANSI_RESET = "\u001B[0m";
	public static final String ANSI_BLACK = "\u001B[30m";
	public static final String ANSI_RED = "\u001B[31m";
	public static final String ANSI_GREEN = "\u001B[32m";
	public static final String ANSI_YELLOW = "\u001B[33m";
	public static final String ANSI_BLUE = "\u001B[34m";
	public static final String ANSI_PURPLE = "\u001B[35m";
	public static final String ANSI_CYAN = "\u001B[36m";
	public static final String ANSI_WHITE = "\u001B[37m";


	public static String red(Object s) {
		if (enable == false)
			return ObjectUtil.toString(s);
		return ANSI_RED + s + ANSI_RESET;
	}

	public static String yellow(Object s) {
		if (enable == false)
			return ObjectUtil.toString(s);
		return ANSI_YELLOW + s + ANSI_RESET;
	}

	public static String green(Object s) {
		if (enable == false)
			return ObjectUtil.toString(s);
		return ANSI_GREEN + s + ANSI_RESET;
	}

	public static String cyan(Object s) {
		if (enable == false)
			return ObjectUtil.toString(s);
		return ANSI_CYAN + s + ANSI_RESET;
	}

	public static String blue(Object s) {
		if (enable == false)
			return ObjectUtil.toString(s);
		return ANSI_BLUE + s + ANSI_RESET;
	}

	public static void redOut(String s) {
		System.out.println(red(s));
	}
	public static String erase(String l2) {
		char[] ch = l2.toCharArray();
		boolean ansiStart = false;
		int len = ch.length;
		StringBuilder out = new StringBuilder(len);
		for (int i = 0; i < len; i++) {
			char c = ch[i];
			switch (c) {
			case '\u001B':
				ansiStart = true;
				break;
			case '[':
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
				if (ansiStart == false) {
					out.append(c);
				}
				break;
			case 'm':
				if (ansiStart == true) {
					ansiStart = false;
				} else {
					out.append(c);
				}
				break;
			default:
				if (ansiStart)
					ansiStart = false;
				out.append(c);
			}
		}
		return out.toString();
	}
}
