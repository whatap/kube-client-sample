package whatap.lang;

import whatap.util.ClassUtil;
import whatap.util.IntKeyMap;
import whatap.util.StringIntMap;

public class LogName {
	public static final short JAVA_GC = 1001;
	
	public static IntKeyMap<String> values = ClassUtil.getConstantValueMap(LogName.class, Short.TYPE);
	public static StringIntMap names = ClassUtil.getConstantKeyMap(LogName.class, Short.TYPE);

}
