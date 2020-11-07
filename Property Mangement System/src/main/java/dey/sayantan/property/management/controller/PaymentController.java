package dey.sayantan.property.management.controller;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;


import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import dey.sayantan.property.management.model.Payment;
import dey.sayantan.property.management.service.PaymentService;
import dey.sayantan.property.management.service.TenantService;
import dey.sayantan.property.management.validator.CommonValidatorUtil;

@RestController
public class PaymentController {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	PaymentService paymentService;

	@Autowired
	TenantService tenantService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/CreatePayment")
	public Payment createPayment(HttpServletRequest requestPayload, HttpServletResponse response) throws Exception {
		Payment payment = new Payment();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			payment = objectMapper.readValue(request, Payment.class);
			paymentService.addPayment(payment);
			return payment;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		}

	}

	@PutMapping("/UpdatePayment")
	public Payment update(HttpServletRequest requestPayload, HttpServletResponse response) throws Exception {
		Payment payment = new Payment();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			payment = objectMapper.readValue(request, Payment.class);
			JSONObject obj = new JSONObject(request);
			String uuidString = obj.getString("paymentUuid");
			UUID paymentUuid = UUID.fromString(uuidString);
			payment.setUuid(paymentUuid);
			return paymentService.updatePayment(payment);

		} catch (IOException e) {
			logger.error("Error Deserializing Payload To Object " + e.getMessage());
			throw new Exception("Error Deserializing Payload To Object ");
		}
	}

	@DeleteMapping("/DeletePayment")
	public Payment deletePayment(HttpServletRequest requestPayload) throws Exception {
		String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
		JSONObject obj = new JSONObject(request);
		String uuidString = obj.getString("paymentUuid");
		UUID paymentUuid = UUID.fromString(uuidString);
		return paymentService.deletePayment(paymentUuid);
	}

	@GetMapping("/PaymentViewsAll")
	public List<Payment> showPaymentAll() {
		return paymentService.getAllPayments();
	}

	/** fetch all payments for a tenant **/
	@GetMapping("/PaymentViews/PDF/{tenantId}")
	public ResponseEntity<byte[]> getTenantSpecificPaymentInPDF(@PathVariable String tenantId) throws Exception {
		logger.info("Requested To Find All Payments for tenant " + tenantId);
		UUID tenantUuid = null;
	//TODO if tenant absent
		tenantUuid = tenantService.getByRegistrationId(tenantId).getUuid();
		return paymentService.generatePDFForTenant(tenantUuid);
	}
	
	@GetMapping("/PaymentViews/{tenantId}")
	public List<Payment> getTenantSpecificPayment(@PathVariable String tenantId) throws Exception {
		logger.info("Requested To Find All Payments for tenant " + tenantId);
		UUID tenantUuid = null;
		tenantUuid = tenantService.getByRegistrationId(tenantId).getUuid();
		return paymentService.findAllByTenantUuid(tenantUuid);
	}

	// TODO Dirty WorkAround
	@GetMapping("/PaymentStatus/{tenantId}")
	public String getPaymentStatus(@PathVariable String tenantId) throws Exception {
		try {
			logger.info("Requested To Find All Payments for tenant " + tenantId);

			UUID tenantUuid = null;
			tenantUuid = tenantService.getByRegistrationId(tenantId).getUuid();
			String status = "PENDING";
			status = paymentService.findPaymentStatus(tenantUuid);
			return status;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("Error Finding Payment Status: " + e.getLocalizedMessage());
		}
	}

}
