package dey.sayantan.property.management.validator;

import java.time.LocalDate;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import dey.sayantan.property.management.model.Complain;

@Component
public class ComplainValidator {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	public void validate(Complain complain) throws Exception {
		try {
			validateTenantUuid(complain.getTenantUuid());
			validateComplainDate(complain.getComplainDate());
			validateComplainType(complain.getComplainType());
		} catch (Exception e) {
			logger.error("Error While Validating Request ");
			throw new Exception("Validation Failed " + e.getMessage());
		}

	}

	private void validateTenantUuid(UUID tenantUuid) throws Exception {
		try {
			validateString(tenantUuid.toString());
		} catch (Exception e) {
			logger.error("Invalid field tenantUuid. ");
			throw new Exception("Invalid field tenantUuid");
		}

	}

	private void validateComplainType(String field) throws Exception {
		try {
			validateString(field.toString());
		} catch (Exception e) {
			logger.error("Invalid field field. ");
			throw new Exception("Invalid field field");
		}
	}

	private void validateComplainDate(LocalDate complainDate) throws Exception {
		if (complainDate == null) {
			logger.error("Invalid Complain Date ");
			throw new Exception("Invalid Complain Date");
		}

	}

	private void validateString(String field) throws Exception {
		if (field == null || field.isEmpty())
			throw new Exception();
	}

}
