package at.tspi.ttnclientj2.exceptions;

public class TTNMessageParsingException extends Exception {
	private static final long serialVersionUID = 1L;
	public TTNMessageParsingException() { }
	public TTNMessageParsingException(String message) { super(message); }
	public TTNMessageParsingException(Throwable cause) { super(cause); }
	public TTNMessageParsingException(String message, Throwable cause) { super(message, cause); }
	public TTNMessageParsingException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
}
