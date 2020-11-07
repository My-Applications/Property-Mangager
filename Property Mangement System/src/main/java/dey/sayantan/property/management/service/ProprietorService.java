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
import dey.sayantan.property.management.model.Proprietor;
import dey.sayantan.property.management.repositry.ProprietorRepository;
import dey.sayantan.property.management.validator.ProprietorValidator;

@Service
public class ProprietorService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ProprietorRepository<Proprietor> proprietorRepository;

	@Autowired
	ProprietorValidator validator;

	@Transactional
	public List<Proprietor> getAllProprietors() {
		return (List<Proprietor>) proprietorRepository.findAll();
	}

	@Transactional
	public Proprietor getById(String id) throws Exception {
		if (id.isEmpty() || id == null)
			throw new Exception("Invalid ID");
		try {
			return proprietorRepository.findByRegistrationId(id);
		} catch (Exception e) {
			logger.error("Error While Finding Proprietor for Registration Id " + id + "due to " + e.getMessage());
			throw new Exception("Error While Finding Proprietor for Registration Id " + id);
		}
	}

	@Transactional
	public Proprietor findByUuid(UUID proprietorUuid) throws Exception {
		try {
			return proprietorRepository.findByUuid(proprietorUuid);
		} catch (Exception e) {
			logger.error("Error While Finding Proprietor for Uuid " + proprietorUuid + "due to " + e.getMessage());
			throw new Exception("Error While Finding Proprietor for Uuid " + proprietorUuid);
		}
	}

	@Transactional
	public Proprietor deleteProprietor(UUID proprietorUuid) throws Exception {
		Proprietor snapshotToBeDeleted;
		snapshotToBeDeleted = proprietorRepository.findByUuid(proprietorUuid);
		if (snapshotToBeDeleted == null) {
			logger.error("Proprietor Not Found ");
			throw new Exception("Proprietor Not Found For Uuid: " + proprietorUuid);
		}
		snapshotToBeDeleted.setTimestamp(Instant.now());
		snapshotToBeDeleted.setIsDeleted(true);
		proprietorRepository.save(snapshotToBeDeleted);
		return snapshotToBeDeleted;
	}

	@Transactional
	public boolean addProprietor(Proprietor proprietor) throws Exception {
		validator.validate(proprietor);
		verifyProprietor(proprietor);
		proprietor.setUuid(UUID.randomUUID());
		proprietor.setTimestamp(Instant.now());
		proprietor.setPassword(encryptPassword(proprietor.getPassword()));
		return proprietorRepository.save(proprietor) != null;
	}

	@Transactional
	public Proprietor verifyProprietorCreds(LoginDTO loginDetails) throws Exception {
		try {
			Proprietor proprietor = null;
			proprietor = proprietorRepository.findByRegistrationId(loginDetails.getUserId());
			String encPassword = encryptPassword(loginDetails.getPassword());
			if (proprietor == null || !encPassword.equals(proprietor.getPassword())) {
				logger.error("Proprietor Details Incorrect");
				throw new Exception("Proprietor Details Incorrect");
			}
			return proprietor;
		} catch (Exception e) {
			logger.error("Error Encountered While Validating Login Credentials " + e.getMessage());
			throw new Exception("Error Validating Proprietor Login Credentials");
		}
	}

	private void verifyProprietor(Proprietor proprietor) throws Exception {
		if (proprietorRepository.findByRegistrationId(proprietor.getRegistrationId()) != null) {
			logger.error("Proprietor With Registration Id " + proprietor.getRegistrationId() + " already exists");
			throw new Exception("Proprietor With Similar Registration Id Already Exists");
		}

	}

	@Transactional
	public Proprietor updateProprietor(Proprietor proprietor) throws Exception {
		validator.validate(proprietor);
		Proprietor proprietorSnapshot;
		proprietorSnapshot = proprietorRepository.findByRegistrationId(proprietor.getRegistrationId());
		if (proprietorSnapshot == null) {
			logger.error("Proprietor Not Found ");
			throw new Exception("Proprietor Not Found For Registration Id: " + proprietor.getRegistrationId());
		}
		proprietor.setPassword(encryptPassword(proprietor.getPassword()));
		if (!proprietorSnapshot.getPassword().equals(proprietor.getPassword())) {
			logger.error("Password Entered is Wrong for proprietor " + proprietor.getUuid());
			throw new Exception("Password Entered is Wrong for proprietor");
		}
		proprietor.setUuid(proprietorSnapshot.getUuid());
		proprietor.setTimestamp(Instant.now());
		try {
			return proprietorRepository.save(proprietor);

		} catch (Exception e) {
			logger.error("Error While Updating Entity " + proprietor.getUuid() + " due to " + e.getMessage());
			throw new Exception("Error While Updating Proprietor With UUID " + proprietor.getUuid());
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

	public List<Proprietor> getAlls() {
		return proprietorRepository.sample();
	}
}
