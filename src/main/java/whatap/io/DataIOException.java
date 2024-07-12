package whatap.io;

public class DataIOException extends RuntimeException {

	public DataIOException() {
	}

	public DataIOException(String message) {
		super(message);
	}

	public DataIOException(Throwable cause) {
		super(cause);
	}

	public DataIOException(String message, Throwable cause) {
		super(message, cause);
	}

}
