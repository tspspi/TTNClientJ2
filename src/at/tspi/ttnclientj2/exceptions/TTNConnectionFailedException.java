package at.tspi.ttnclientj2.exceptions;

public class TTNConnectionFailedException extends Exception {
	private static final long serialVersionUID = 1L;
	public TTNConnectionFailedException() { }
	public TTNConnectionFailedException(String message) { super(message); }
	public TTNConnectionFailedException(Throwable cause) { super(cause); }
	public TTNConnectionFailedException(String message, Throwable cause) { super(message, cause); }
	public TTNConnectionFailedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
}
