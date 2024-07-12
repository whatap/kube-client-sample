package whatap.lang;

import whatap.util.ClassUtil;
import whatap.util.IntKeyMap;
import whatap.util.StringIntMap;

public class HitMapKind {

	public final static byte NONE = 0;
	public final static byte KUBE = 1;

	public static IntKeyMap<String> values = ClassUtil.getConstantValueMap(HitMapKind.class, Byte.TYPE);
	public static StringIntMap names = ClassUtil.getConstantKeyMap(HitMapKind.class, Byte.TYPE);

}
