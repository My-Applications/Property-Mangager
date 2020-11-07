package dey.sayantan.property.management.exceptions;

import org.springframework.http.HttpStatus;

public class PayloadDecodeException extends RuntimeException {

	private static final long serialVersionUID = 1L;
    private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
    
	public HttpStatus getHttpStatus() {
		return httpStatus;
	}

	public void setHttpStatus(HttpStatus httpStatus) {
		this.httpStatus = httpStatus;
	}

	public PayloadDecodeException() {

	}

	public PayloadDecodeException(String message, Throwable cause, boolean enableSuppression,
			boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public PayloadDecodeException(String message, Throwable cause) {
		super(message, cause);

	}

	public PayloadDecodeException(String message) {
		super(message);

	}

	public PayloadDecodeException(Throwable cause) {
		super(cause);

	}

}
