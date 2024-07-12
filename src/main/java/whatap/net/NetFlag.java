package whatap.net;

public class NetFlag {
	public final static byte SECURE_HIDE = 0x0001;
	public final static byte SECURE_CYPHER = 0x0002;
	public final static byte ONE_WAY_NO_CYPHER = 0x0004;
	public final static byte RESERVED2 = 0x0008;
	public final static byte RESERVED3 = 0x0010;
	public final static byte RESERVED4 = 0x0020;
	public final static byte RESERVED5 = 0x0040;
	public final static byte RESERVED6 = (byte) 0x0080;

	public final static byte MASTER_SELECTION = (byte) 0xfa;
	public final static byte ACK = (byte) 0xfb;
	public final static byte PREPARE_AGENT = (byte) 0xfc;
	public static final byte KEY_EXTENSION = (byte) 0xfd;
    public final static byte TIME_SYNC = (byte) 0xfe;
	public static final byte KEY_RESET = (byte) 0xff;

	private byte flag;

	public NetFlag(byte flag) {
		this.flag = flag;
	}

	public NetFlag() {
	}

	public NetFlag set(byte mask) {
		this.flag = (byte) (this.flag | mask);
		return this;
	}

	public static byte getSecureMask(byte code) {
		if (code < 0)
			return 0;
		return (byte) ((code & SECURE_HIDE) | (code & SECURE_CYPHER));
	}

	public boolean isA(byte m) {
		return (this.flag & m) != 0;
	}

	public byte getFlag() {
		return this.flag;
	}

	public static boolean isSecure(byte code) {
		if (code <0)
			return false;
		return ((code & SECURE_HIDE) | (code & SECURE_CYPHER)) != 0;
	}
}
