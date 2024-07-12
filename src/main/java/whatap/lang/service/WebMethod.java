package whatap.lang.service;

import whatap.util.ClassUtil;
import whatap.util.IntKeyMap;
import whatap.util.StringIntMap;

public class WebMethod {
	public static final byte GET = 1;
	public static final byte POST = 2;
	public static final byte PUT = 3;
	public static final byte DELETE = 4;
	public static final byte PATCH = 5;
	public static final byte OPTIONS = 6;
	public static final byte HEAD = 7;
	public static final byte TRACE = 8;

	public static IntKeyMap<String> values = ClassUtil.getConstantValueMap(WebMethod.class, Byte.TYPE);
	public static StringIntMap names = ClassUtil.getConstantKeyMap(WebMethod.class, Byte.TYPE);
}
