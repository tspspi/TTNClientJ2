package at.tspi.ttnclientj2.exceptions;

public class TTNAccessDeniedException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	public TTNAccessDeniedException() { }
	public TTNAccessDeniedException(String message) { super(message); }
	public TTNAccessDeniedException(Throwable cause) { super(cause); }
	public TTNAccessDeniedException(String message, Throwable cause) { super(message, cause); }
	public TTNAccessDeniedException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
}
