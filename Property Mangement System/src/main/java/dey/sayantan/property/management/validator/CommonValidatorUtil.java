package dey.sayantan.property.management.validator;

import java.io.IOException;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dey.sayantan.property.management.exceptions.PayloadDecodeException;

public class CommonValidatorUtil {

	private static final Logger logger = LoggerFactory.getLogger(CommonValidatorUtil.class);

	/** Extract JSON Payload from HTTP Request Body */
	public static String parsePayloadFromRequest(HttpServletRequest requestPayload) {
		String payload = "";
		try {
			payload = requestPayload.getReader().lines().collect(Collectors.joining((System.lineSeparator())));
			logger.info("Payload received : \n" + payload);
			if (payload == null || payload.isEmpty())
				throw new PayloadDecodeException("Payload/Tenant Details Not Provided");
		} catch (IOException e) {
			logger.error("Error Extracting payload :" + e.getMessage());
			throw new RuntimeException("Error Extracting payload ");
		}
		return payload;
	}

}
