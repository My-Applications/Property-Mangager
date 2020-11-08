package dey.sayantan.property.management.exceptions;

import java.time.LocalDate;
import java.util.UUID;

public class ErrorResponse {
	private String status;
	private UUID errorMesageId;
	private LocalDate serverTime;
	private Details details;

	public ErrorResponse() {
		status = "error";
		errorMesageId = UUID.randomUUID();
		serverTime = LocalDate.now();
	}

	public UUID getErrorMesageId() {
		return errorMesageId;
	}

	public void setErrorMesageId(UUID errorMesageId) {
		this.errorMesageId = errorMesageId;
	}

	public LocalDate getServerTime() {
		return serverTime;
	}

	public void setServerTime(LocalDate serverTime) {
		this.serverTime = serverTime;
	}

	public void setDetail(String requestType, String errorMessage) {
		details = new Details(requestType, errorMessage);
	}

	public String getStatus() {
		return status;
	}

	public Details getDetail() {
		return details;
	}
}

class Details {
	private String requestType;
	private String errorMessage;

	public Details(String requestType, String errorMessage) {
		super();
		this.requestType = requestType;
		this.errorMessage = errorMessage;
	}

	public String getRequestType() {
		return requestType;
	}

	public void setRequestType(String requestType) {
		this.requestType = requestType;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}