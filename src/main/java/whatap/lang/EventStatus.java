package whatap.lang;

import whatap.util.ClassUtil;
import whatap.util.StringIntMap;

public class EventStatus {

    public final static int ON = 0x0001;
    public final static int OFF = 0x0002;
    public final static int DISABLED = 0x0004;
    public static final int ACKNOWLEDGE = 0x0008;

    public static StringIntMap keys = ClassUtil.getConstantKeyMap(EventStatus.class, Integer.TYPE);

}
