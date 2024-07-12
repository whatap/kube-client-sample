package whatap.lang;

public class STOP extends RuntimeException {
	public STOP(String m) {
		super(m);
	}

	public final static STOP instance = new STOP("STOP");
}
