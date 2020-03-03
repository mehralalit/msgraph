package edu.immune.cloud.msgraph.exception;

/**
 * Custom exception class to register exceptions that occur during connectivity with Microsoft Graph 
 * @author Lalit Mehra
 *
 */
public class ApiException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2779924287581636372L;

	public ApiException() {
		super();
	}

	public ApiException(String message) {
		super(message);
	}

	public ApiException(Throwable cause) {
		super(cause);
	}

}
