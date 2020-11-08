package dey.sayantan.property.management.exceptions;

import org.springframework.http.HttpStatus;

public class DuplicateResourceException extends BaseRuntimeException {

	private static final long serialVersionUID = 1L;
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public DuplicateResourceException() {

	}

	public DuplicateResourceException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public DuplicateResourceException(String message, Throwable cause) {
		super(message, cause);

	}

	public DuplicateResourceException(String message) {
		super(message);

	}

	public DuplicateResourceException(Throwable cause) {
		super(cause);

	}

}
