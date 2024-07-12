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
 *
 *  For this class
 *     many of method/field names
 *     basic ideas 
 *  are from org.apache.commons.lang3.SystemUtils.java
 *  
 */
package whatap.util;

public class SystemUtil {
	public static final String USER_NAME = System.getProperty("user.name");
	public static final boolean IS_JAVA_1_2 = System.getProperty("java.specification.version").startsWith("1.2");
	public static final boolean IS_JAVA_1_3 = System.getProperty("java.specification.version").startsWith("1.3");
	public static final boolean IS_JAVA_1_4 = System.getProperty("java.specification.version").startsWith("1.4");
	public static final boolean IS_JAVA_1_5 = System.getProperty("java.specification.version").startsWith("1.5");
	public static final boolean IS_JAVA_1_6 = System.getProperty("java.specification.version").startsWith("1.6");
	public static final boolean IS_JAVA_1_7 = System.getProperty("java.specification.version").startsWith("1.7");
	public static final boolean IS_JAVA_1_8 = System.getProperty("java.specification.version").startsWith("1.8");
	public static final boolean IS_JAVA_9 = System.getProperty("java.specification.version").startsWith("9");
	public static final boolean IS_JAVA_10 = System.getProperty("java.specification.version").startsWith("10");
	public static final boolean IS_JAVA_11 = System.getProperty("java.specification.version").startsWith("11");
	public static final boolean IS_JAVA_12 = System.getProperty("java.specification.version").startsWith("12");
	public static final boolean IS_JAVA_13 = System.getProperty("java.specification.version").startsWith("13");
	public static final boolean IS_JAVA_14 = System.getProperty("java.specification.version").startsWith("14");
	public static final boolean IS_JAVA_15 = System.getProperty("java.specification.version").startsWith("15");
	public static final boolean IS_JAVA_16 = System.getProperty("java.specification.version").startsWith("16");
	public static final boolean IS_JAVA_17 = System.getProperty("java.specification.version").startsWith("17");
	public static final boolean IS_JAVA_18 = System.getProperty("java.specification.version").startsWith("18");
	public static final boolean IS_JAVA_19 = System.getProperty("java.specification.version").startsWith("19");
	public static final boolean IS_JAVA_20 = System.getProperty("java.specification.version").startsWith("20");
	public static final boolean IS_JAVA_21 = System.getProperty("java.specification.version").startsWith("21");
	public static final boolean IS_JAVA_22 = System.getProperty("java.specification.version").startsWith("22");

	public static final String JAVA_VERSION = System.getProperty("java.version");
	public static final String JAVA_SPEC_VERSION = System.getProperty("java.specification.version");
	public static final String JAVA_VENDOR = System.getProperty("java.vendor");
	public static final boolean IS_JAVA_IBM = JAVA_VENDOR.startsWith("IBM");
	
	public static final String OS_NAME = System.getProperty("os.name");
	public static final boolean IS_AIX = OS_NAME.startsWith("AIX");
	public static final boolean IS_HP_UX = OS_NAME.startsWith("HP-UX");
	public static final boolean IS_LINUX = OS_NAME.toUpperCase().startsWith("LINUX");
	public static final boolean IS_MAC = OS_NAME.startsWith("Mac");
	public static final boolean IS_MAC_OSX = OS_NAME.startsWith("Mac OS X");
	public static final boolean IS_WINDOWS = OS_NAME.indexOf("Windows") >= 0;
	public static final boolean IS_SUNOS =  OS_NAME.equals("SunOS");

	public static int majorVersion = 49;
	private static float ver = 0;
	static {
		if (IS_JAVA_1_2) {
			majorVersion = 46;
			ver = 1.2f;
		} else if (IS_JAVA_1_3) {
			majorVersion = 47;
			ver = 1.3f;
		} else if (IS_JAVA_1_4) {
			majorVersion = 48;
			ver = 1.4f;
		} else if (IS_JAVA_1_5) {
			majorVersion = 49;
			ver = 1.5f;
		} else if (IS_JAVA_1_6) {
			majorVersion = 50;
			ver = 1.6f;
		} else if (IS_JAVA_1_7) {
			majorVersion = 51;
			ver = 1.7f;
		} else if (IS_JAVA_1_8) {
			majorVersion = 52;
			ver = 1.8f;
		} else if (IS_JAVA_9) {
			majorVersion = 53;
			ver = 9;
		} else if (IS_JAVA_10) {
			majorVersion = 54;
			ver = 10;
		} else if (IS_JAVA_11) {
			majorVersion = 55;
			ver = 11;
		} else if (IS_JAVA_12) {
			majorVersion = 56;
			ver = 12;
		} else if (IS_JAVA_13) {
			majorVersion = 57;
			ver = 13;
		} else if (IS_JAVA_14) {
			majorVersion = 58;
			ver = 14;
		} else if (IS_JAVA_15) {
			majorVersion = 59;
			ver = 15;
		} else if (IS_JAVA_16) {
			majorVersion = 60;
			ver = 16;
		} else if (IS_JAVA_17) {
			majorVersion = 61;
			ver = 17;
		} else if (IS_JAVA_18) {
			majorVersion = 62;
			ver = 18;
		} else if (IS_JAVA_19) {
			majorVersion = 63;
			ver = 19;
		} else if (IS_JAVA_20) {
			majorVersion = 64;
			ver = 20;
		} else if (IS_JAVA_21) {
			majorVersion = 65;
			ver = 21;
		} else if (IS_JAVA_22) {
			majorVersion = 66;
			ver = 22;
		} else {
			majorVersion = 61;
			try {
				String spec=System.getProperty("java.specification.version");
				if(spec!=null) {
					ver = Integer.parseInt(spec.substring(0, 2));
				}else {
					ver=17;
				}
			} catch (Exception e) {
				ver = 17;
			}
		}
	}
	public final static int MAJOR_JAVA5=49;
	public final static int MAJOR_JAVA6=50;
	public final static int MAJOR_JAVA7=51;
	public final static int MAJOR_JAVA8=52;
	public final static int MAJOR_JAVA9=53;
	public final static int MAJOR_JAVA10=54;
	public final static int MAJOR_JAVA11=55;
	public final static int MAJOR_JAVA12=56;
	public final static int MAJOR_JAVA13=57;
	public final static int MAJOR_JAVA14=58;
	public final static int MAJOR_JAVA15=59;
	public final static int MAJOR_JAVA16=60;
	public final static int MAJOR_JAVA17=61;
	public final static int MAJOR_JAVA18=62;
	public final static int MAJOR_JAVA19=63;
	public final static int MAJOR_JAVA20=64;
	public final static int MAJOR_JAVA21=65;
	public final static int MAJOR_JAVA22=66;

	public static String getJavaVersion(int major) {
		switch (major) {
		case 46:
			return "1.2";
		case 47:
			return "1.3";
		case 48:
			return "1.4";
		case 49:
			return "1.5";
		case 50:
			return "6";
		case 51:
			return "7";
		case 52:
			return "8";
		case 53:
			return "9";
		case 54:
			return "10";
		case 55:
			return "11";
		case 56:
			return "12";
		case 57:
			return "13";
		case 58:
			return "14";
		case 59:
			return "15";
		case 60:
			return "16";
		case 61:
			return "17";
		case 62:
			return "18";
		case 63:
			return "19";
		case 64:
			return "20";
		case 65:
			return "21";
		case 66:
			return "22";
		}
		return "17";
	}

	public static final boolean IS_JAVA_OVER17() {
		return ver >= 17;
	};
	public static final boolean IS_JAVA_OVER9() {
		return ver >= 9;
	};
	public static final boolean IS_JAVA_OVER8() {
		return ver >= 1.8f;
	};
}
