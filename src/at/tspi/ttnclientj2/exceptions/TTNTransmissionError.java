package at.tspi.ttnclientj2.exceptions;

public class TTNTransmissionError extends Exception {
	private static final long serialVersionUID = 1L;
	public TTNTransmissionError() { }
	public TTNTransmissionError(String message) { super(message); }
	public TTNTransmissionError(Throwable cause) { super(cause); }
	public TTNTransmissionError(String message, Throwable cause) { super(message, cause); }
	public TTNTransmissionError(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) { super(message, cause, enableSuppression, writableStackTrace); }
}
