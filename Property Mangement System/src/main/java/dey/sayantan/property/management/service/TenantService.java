package dey.sayantan.property.management.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dey.sayantan.property.management.core.LoginDTO;
import dey.sayantan.property.management.model.Tenant;
import dey.sayantan.property.management.repositry.TenantRepository;
import dey.sayantan.property.management.validator.TenantValidator;

@Service
public class TenantService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	TenantRepository<Tenant> tenantRepository;

	@Autowired
	TenantValidator validator;

	@Transactional
	public List<Tenant> getAllTenants() {
		return (List<Tenant>) tenantRepository.findAll();
	}

	@Transactional
	public Tenant getByRegistrationId(String id) throws Exception {
		if (id.isEmpty() || id == null)
			throw new Exception("Invalid ID");
		try {
			return tenantRepository.findByRegistrationId(id);
		} catch (Exception e) {
			logger.error("Error While Finding Tenant for Registration Id " + id + "due to " + e.getMessage());
			throw new Exception("Error While Finding Tenant for Registration Id " + id);
		}
	}

	@Transactional
	public Tenant findByUuid(UUID tenantUuid) throws Exception {
		try {
			return tenantRepository.findByUuid(tenantUuid);
		} catch (Exception e) {
			logger.error("Error While Finding Tenant for Uuid " + tenantUuid + "due to " + e.getMessage());
			throw new Exception("Error While Finding Tenant for Uuid " + tenantUuid);
		}
	}

	@Transactional
	public Tenant deleteTenant(UUID tenantUuid) throws Exception {
		Tenant snapshotToBeDeleted;
		snapshotToBeDeleted = tenantRepository.findByUuid(tenantUuid);
		if (snapshotToBeDeleted == null) {
			logger.error("Tenant Not Found ");
			throw new Exception("Tenant Not Found For Uuid: " + tenantUuid);
		}
		snapshotToBeDeleted.setTimestamp(Instant.now());
		snapshotToBeDeleted.setIsDeleted(true);
		tenantRepository.save(snapshotToBeDeleted);
		return snapshotToBeDeleted;
	}

	@Transactional
	public boolean addTenant(Tenant tenant) throws Exception {
		validator.validate(tenant);
		verifyTenant(tenant);
		tenant.setUuid(UUID.randomUUID());
		tenant.setTimestamp(Instant.now());
		tenant.setPassword(encryptPassword(tenant.getPassword()));
		return tenantRepository.save(tenant) != null;
	}

	private void verifyTenant(Tenant tenant) throws Exception {
		if (tenantRepository.findByRegistrationId(tenant.getRegistrationId()) != null) {
			logger.error("Tenant With Registration Id " + tenant.getRegistrationId() + " already exists");
			throw new Exception("Tenant With Similar Registration Id Already Exists");
		}

	}

	@Transactional
	public Tenant verifyTenantCreds(LoginDTO loginDetails) throws Exception {

		Tenant tenant = null;
		String encPassword;
		try {
			if (loginDetails.getUserId().isEmpty() || loginDetails.getPassword().isEmpty()) {
				logger.error("Login Details Not Provided ");
				throw new Exception("Login Details Not Provided");
			}
			tenant = tenantRepository.findByRegistrationId(loginDetails.getUserId());
			encPassword = encryptPassword(loginDetails.getPassword());
		} catch (Exception e) {
			logger.error("Error Encountered While Validating Login Credentials " + e.getMessage());
			throw new Exception("Error Validating Tenant Login Credentials");
		}
		if (tenant == null || !encPassword.equals(tenant.getPassword())) {
			logger.error("Tenant Details Incorrect");
			throw new Exception("Tenant Details Incorrect");
		}
		return tenant;

	}

	@Transactional
	public Tenant updateTenant(Tenant tenant) throws Exception {
		validator.validate(tenant);
		Tenant tenantSnapshot;
		tenantSnapshot = tenantRepository.findByRegistrationId(tenant.getRegistrationId());
		if (tenantSnapshot == null) {
			logger.error("Tenant Not Found ");
			throw new Exception("Tenant Not Found For Registration Id: " + tenant.getRegistrationId());
		}
		tenant.setPassword(encryptPassword(tenant.getPassword()));
		if (!tenantSnapshot.getPassword().equals(tenant.getPassword())) {
			logger.error("Password Entered is Wrong for tenant " + tenant.getUuid());
			throw new Exception("Password Entered is Wrong for tenant");
		}
		tenant.setUuid(tenantSnapshot.getUuid());
		tenant.setTimestamp(Instant.now());
		try {
			return tenantRepository.save(tenant);

		} catch (Exception e) {
			logger.error("Error While Updating Entity " + tenant.getUuid() + " due to " + e.getMessage());
			throw new Exception("Error While Updating Tenant With UUID " + tenant.getUuid());
		}
	}

	private String encryptPassword(String password) throws Exception {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			md.update(password.getBytes());
			password = DatatypeConverter.printHexBinary(md.digest());
			return password;
		} catch (NoSuchAlgorithmException e) {
			logger.error("Error Encrypting Password:  " + e.getMessage());
			throw new Exception("Error Encrypting Password");
		}

	}

	public List<Tenant> getAlls() {
		return tenantRepository.sample();
	}
}
