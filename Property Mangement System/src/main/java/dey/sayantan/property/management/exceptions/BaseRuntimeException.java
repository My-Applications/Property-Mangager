package dey.sayantan.property.management.exceptions;

import org.springframework.http.HttpStatus;

public class BaseRuntimeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	private HttpStatus httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public BaseRuntimeException() {

	}

	public BaseRuntimeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public BaseRuntimeException(String message, Throwable cause) {
		super(message, cause);

	}

	public BaseRuntimeException(String message) {
		super(message);

	}

	public BaseRuntimeException(Throwable cause) {
		super(cause);

	}

}
