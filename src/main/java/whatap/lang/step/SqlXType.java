package whatap.lang.step;

public class SqlXType {
	public final static byte STMT = 0x0;
	public final static byte PSTMT = 0x1;
	public final static byte CSTMT = 0x2;
	// public final static byte DYNA = 0x2;
	public final static byte METHOD_KNOWN = 0x00;
	public final static byte METHOD_EXECUTE = 0x10;
	public final static byte METHOD_UPDATE = 0x20;
	public final static byte METHOD_QUERY = 0x30;

	public static byte getClassType(byte xtype) {
		return (byte) (xtype & 0x0f);
	}

	public static byte getMethodType(byte xtype) {
		return (byte) (xtype & 0xf0);
	}

	public static String getClassString(byte xtype) {
		switch (getClassType(xtype)) {
		case STMT:
			return "STMT";
		case PSTMT:
			return " PSTMT";
		case CSTMT:
			return " CSTMT";
		}
		
		return "Unknown";
	}
}
