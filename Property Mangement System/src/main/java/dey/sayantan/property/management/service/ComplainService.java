package dey.sayantan.property.management.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dey.sayantan.property.management.model.Complain;
import dey.sayantan.property.management.model.Tenant;
import dey.sayantan.property.management.repositry.ComplainRepository;
import dey.sayantan.property.management.repositry.TenantRepository;
import dey.sayantan.property.management.validator.ComplainValidator;

@Service
public class ComplainService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ComplainRepository<Complain> complainRepository;

	@Autowired
	ComplainValidator validator;

	@Autowired
	TenantRepository<Tenant> tenantRepository;

	@Transactional
	public List<Complain> getAllComplains() {
		return (List<Complain>) complainRepository.findAll();
	}

	@Transactional
	public Complain findByUuid(UUID complainUuid) {
		return complainRepository.findByUuid(complainUuid);
	}

	@Transactional
	public Complain deleteComplain(UUID complainUuid) throws Exception {
		Complain snapshotToBeDeleted;
		snapshotToBeDeleted = complainRepository.findByUuid(complainUuid);
		if (snapshotToBeDeleted == null) {
			logger.error("Complain Not Found ");
			throw new Exception("Complain Not Found For Uuid: " + complainUuid);
		}
		snapshotToBeDeleted.setTimestamp(Instant.now());
		snapshotToBeDeleted.setIsDeleted(true);
		complainRepository.save(snapshotToBeDeleted);
		return snapshotToBeDeleted;
	}

	@Transactional
	public boolean addComplain(Complain complain) throws Exception {
		try {
			validator.validate(complain);
			verifyTenant(complain.getTenantUuid());
			complain.setUuid(UUID.randomUUID());
			complain.setTimestamp(Instant.now());
			return complainRepository.save(complain) != null;
		} catch (Exception e) {
			logger.error("Error Creating Complain : " + e.getMessage());
			throw new Exception(e);
		}

	}

	@Transactional
	public Complain updateComplain(Complain complain) throws Exception {
		Complain complainSnapshot;
		logger.info("Updating complain for " + complain.getUuid());
		validator.validate(complain);
		complainSnapshot = complainRepository.findByUuid(complain.getUuid());
		if (complainSnapshot == null) {
			logger.error("Error finding complain for UUID " + complain.getUuid());
			throw new Exception("Complain Not Found For Uuid : " + complain.getUuid());
		}
		complain.setUuid(complainSnapshot.getUuid());
		complain.setTenantUuid(complainSnapshot.getTenantUuid()); // TODO
		complain.setTimestamp(Instant.now());
		try {
			return complainRepository.save(complain);

		} catch (Exception e) {
			logger.error("Error While Updating Entity " + complain.getUuid() + " due to " + e.getMessage());
			throw new Exception("Error While Updating Tenant With UUID " + complain.getUuid());
		}

	}

	private void verifyTenant(UUID tenantUuid) throws Exception {
		if (tenantRepository.findByUuid(tenantUuid) == null) {
			logger.error("Tenant Not Found For Whom Complain Needs To Be Created " + tenantUuid);
			throw new Exception("Tenant Not Found");
		}

	}

	@Transactional
	public List<Complain> findAllByTenantUuid(UUID tenantUuid) throws Exception {
		try {
			verifyTenant(tenantUuid);
			return complainRepository.findAllByTenantUuid(tenantUuid);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("Error Finding Complain Details For Tenant");
		}
	}

}
