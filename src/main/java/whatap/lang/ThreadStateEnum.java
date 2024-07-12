package whatap.lang;

import whatap.util.ClassUtil;

import java.util.Map;
public class ThreadStateEnum {
	public final static byte NONE = 0;
	public final static byte NEW = 1;
	public final static byte RUNNABLE = 2;
	public final static byte BLOCKED = 3;
	public final static byte WAITING = 4;
	public final static byte TIMED_WAITING = 5;
	public final static byte TERMINATED = 6;
	public static byte getState(Thread t) {
		if (t == null)
			return 0;
		switch (t.getState()) {
		case NEW:
			return NEW;
		case RUNNABLE:
			return RUNNABLE;
		case BLOCKED:
			return BLOCKED;
		case WAITING:
			return WAITING;
		case TIMED_WAITING:
			return TIMED_WAITING;
		case TERMINATED:
			return TERMINATED;
		}
		return NONE;
	}
	
	public static Map<Byte, String> values = ClassUtil.getPublicFinalValueMap(ThreadStateEnum.class, Byte.TYPE);
}
