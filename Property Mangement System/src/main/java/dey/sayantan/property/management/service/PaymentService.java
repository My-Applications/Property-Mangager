package dey.sayantan.property.management.service;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.Instant;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.util.Matrix;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dey.sayantan.property.management.exceptions.DocumentProcessingException;
import dey.sayantan.property.management.model.Payment;
import dey.sayantan.property.management.model.Tenant;
import dey.sayantan.property.management.repositry.PaymentRepository;
import dey.sayantan.property.management.repositry.TenantRepository;
import dey.sayantan.property.management.validator.PaymentValidator;

@Service
public class PaymentService {

	@Autowired
	PaymentValidator validator;

	@Autowired
	PaymentRepository<Payment> paymentRepository;

	@Autowired
	TenantRepository<Tenant> tenantRepository;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Transactional
	public List<Payment> getAllPayments() {
		return (List<Payment>) paymentRepository.findAll();
	}

	@Transactional
	public Payment findByUuid(UUID paymentUuid) {
		return paymentRepository.findByUuid(paymentUuid);
	}

	@Transactional
	public Payment deletePayment(UUID paymentUuid) throws Exception {
		Payment snapshotToBeDeleted;
		snapshotToBeDeleted = paymentRepository.findByUuid(paymentUuid);
		if (snapshotToBeDeleted == null) {
			logger.error("Payment Not Found ");
			throw new Exception("Payment Not Found For Uuid: " + paymentUuid);
		}
		snapshotToBeDeleted.setTimestamp(Instant.now());
		snapshotToBeDeleted.setIsDeleted(true);
		paymentRepository.save(snapshotToBeDeleted);
		return snapshotToBeDeleted;
	}

	@Transactional
	public boolean addPayment(Payment payment) throws Exception {
		try {
			validator.validate(payment);
			verifyTenant(payment.getTenantUuid());
			payment.setUuid(UUID.randomUUID());
			payment.setTimestamp(Instant.now());
			return paymentRepository.save(payment) != null;
		} catch (Exception e) {
			logger.error("Error Creating Payment : " + e.getMessage());
			throw new Exception(e);
		}

	}

	@Transactional
	public Payment updatePayment(Payment payment) throws Exception {
		Payment paymentSnapshot;
		logger.info("Updating payment for " + payment.getUuid());
		validator.validate(payment);
		paymentSnapshot = paymentRepository.findByUuid(payment.getUuid());
		if (paymentSnapshot == null) {
			logger.error("Error finding payment for UUID " + payment.getUuid());
			throw new Exception("Payment Not Found For Uuid : " + payment.getUuid());
		}
		payment.setUuid(paymentSnapshot.getUuid());
		payment.setTenantUuid(paymentSnapshot.getTenantUuid()); // TODO
		payment.setTimestamp(Instant.now());
		try {
			return paymentRepository.save(payment);

		} catch (Exception e) {
			logger.error("Error While Updating Entity " + payment.getUuid() + " due to " + e.getMessage());
			throw new Exception("Error While Updating Tenant With UUID " + payment.getUuid());
		}

	}

	@Transactional
	public List<Payment> findAllByTenantUuid(UUID tenantUuid) throws Exception {
		try {
			verifyTenant(tenantUuid);
			return paymentRepository.findAllByTenantUuid(tenantUuid);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("Error Finding Payment Details For Tenant");
		}
	}

	@Transactional
	public ResponseEntity<byte[]> generatePDFForTenant(UUID tenantUuid) throws Exception {
		try {
			verifyTenant(tenantUuid);
			return generatePDF(tenantUuid);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("Error Finding Payment Details For Tenant");
		}
	}

	private void verifyTenant(UUID tenantUuid) throws Exception {
		if (tenantRepository.findByUuid(tenantUuid) == null) {
			logger.error("Tenant Not Found For Whom Payment Needs To Be Made " + tenantUuid);
			throw new Exception("Tenant Not Found");
		}

	}

	public String findPaymentStatus(UUID tenantUuid) throws Exception {
		try {
			String status = "PENDING";
			Payment payment = paymentRepository.findLatestByTenantUuid(tenantUuid);// (tenantUuid);
			if (payment == null)
				return status;
			LocalDate lastPaymenDate = payment.getPaymentDate();
			LocalDate presentDate = LocalDate.now();

			if (ChronoUnit.DAYS.between(lastPaymenDate, presentDate) < 31) {
				status = "PAID";
			}
			return status;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e.getCause());
		}

	}

	private ResponseEntity<byte[]> generatePDF(UUID tenantUuid) {
		Tenant tenant = tenantRepository.findByUuid(tenantUuid);
		List<Payment> paymentList = paymentRepository.findAllByTenantUuid(tenantUuid);

		PDFCreator pdfCreator = new PDFCreator();
		byte[] contentInByteArray = pdfCreator.generateTenantSpecificPayslip(tenant, paymentList);
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_PDF);
		// Here you have to set the actual filename of your pdf
		String filename = generateFileName(tenantUuid);
		headers.setContentDispositionFormData(filename, filename);
		headers.setCacheControl("must-revalidate, post-check=0, pre-check=0");
		ResponseEntity<byte[]> response = new ResponseEntity<>(contentInByteArray, headers, HttpStatus.OK);

		return response;

	}

	private String generateFileName(UUID tenantUuid) {
		String filename = null;
		String extension = ".pdf";
		filename = tenantRepository.findByUuid(tenantUuid).getUuid().toString();
		filename += extension;
		return filename;
	}

}
