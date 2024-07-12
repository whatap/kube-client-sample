package whatap.lang;

import whatap.util.ClassUtil;
import whatap.util.IntKeyMap;
import whatap.util.StringIntMap;

public class EventLevel {

	public final static byte CRITICAL = 30;
	public final static byte WARNING = 20;
	public final static byte INFO = 10;
	public final static byte NONE = 0;

	public static IntKeyMap<String> values = ClassUtil.getConstantValueMap(EventLevel.class, Byte.TYPE);
	public static StringIntMap names = ClassUtil.getConstantKeyMap(EventLevel.class, Byte.TYPE);

}
