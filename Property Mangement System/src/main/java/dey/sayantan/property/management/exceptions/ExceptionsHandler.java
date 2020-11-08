package dey.sayantan.property.management.exceptions;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@RestControllerAdvice
public class ExceptionsHandler {

	@Autowired
	ObjectMapper objectMapper;

	@ExceptionHandler({ DocumentProcessingException.class, PayloadDecodeException.class })
	public ResponseEntity<JsonNode> runtimeExceptionHandler(BaseRuntimeException exception,
			HttpServletRequest request) {
		exception.getClass().cast(exception);
		JsonNode errorResponse = createErrorResponse(exception, request);
		return ResponseEntity.status(exception.getHttpStatus()).body(errorResponse);
	}

	@ExceptionHandler({ Exception.class })
	public ResponseEntity<JsonNode> genericExceptinHandler(Exception exception, HttpServletRequest request) {
		exception.getClass().cast(exception);
		JsonNode errorResponse = createErrorResponse(exception, request);
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
	}

	private JsonNode createErrorResponse(Exception exception, HttpServletRequest request) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setDetail(request.getMethod() + ": " + request.getRequestURI().substring(1),
				exception.getMessage());
		return objectMapper.valueToTree(errorResponse);
	}

}
