package dey.sayantan.property.management.service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dey.sayantan.property.management.model.Feedback;
import dey.sayantan.property.management.model.Tenant;
import dey.sayantan.property.management.repositry.FeedbackRepository;
import dey.sayantan.property.management.repositry.TenantRepository;
import dey.sayantan.property.management.validator.FeedbackValidator;

@Service
public class FeedbackService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	FeedbackRepository<Feedback> feedbackRepository;

	@Autowired
	FeedbackValidator validator;

	@Autowired
	TenantRepository<Tenant> tenantRepository;

	@Transactional
	public List<Feedback> getAllFeedbacks() {
		return (List<Feedback>) feedbackRepository.findAll();
	}

	@Transactional
	public Feedback findByUuid(UUID feedbackUuid) {
		return feedbackRepository.findByUuid(feedbackUuid);
	}

	@Transactional
	public Feedback deleteFeedback(UUID feedbackUuid) throws Exception {
		Feedback snapshotToBeDeleted;
		snapshotToBeDeleted = feedbackRepository.findByUuid(feedbackUuid);
		if (snapshotToBeDeleted == null) {
			logger.error("Feedback Not Found ");
			throw new Exception("Feedback Not Found For Uuid: " + feedbackUuid);
		}
		snapshotToBeDeleted.setTimestamp(Instant.now());
		snapshotToBeDeleted.setIsDeleted(true);
		feedbackRepository.save(snapshotToBeDeleted);
		return snapshotToBeDeleted;
	}

	@Transactional
	public boolean addFeedback(Feedback feedback) throws Exception {
		try {
			validator.validate(feedback);
			verifyTenant(feedback.getTenantUuid());
			feedback.setUuid(UUID.randomUUID());
			feedback.setTimestamp(Instant.now());
			return feedbackRepository.save(feedback) != null;
		} catch (Exception e) {
			logger.error("Error Creating Feedback : " + e.getMessage());
			throw new Exception(e);
		}

	}

	@Transactional
	public Feedback updateFeedback(Feedback feedback) throws Exception {
		Feedback feedbackSnapshot;
		logger.info("Updating feedback for " + feedback.getUuid());
		validator.validate(feedback);
		feedbackSnapshot = feedbackRepository.findByUuid(feedback.getUuid());
		if (feedbackSnapshot == null) {
			logger.error("Error finding feedback for UUID " + feedback.getUuid());
			throw new Exception("Feedback Not Found For Uuid : " + feedback.getUuid());
		}
		feedback.setUuid(feedbackSnapshot.getUuid());
		feedback.setTenantUuid(feedbackSnapshot.getTenantUuid()); // TODO
		feedback.setTimestamp(Instant.now());
		try {
			return feedbackRepository.save(feedback);

		} catch (Exception e) {
			logger.error("Error While Updating Entity " + feedback.getUuid() + " due to " + e.getMessage());
			throw new Exception("Error While Updating Tenant With UUID " + feedback.getUuid());
		}

	}

	private void verifyTenant(UUID tenantUuid) throws Exception {
		if (tenantRepository.findByUuid(tenantUuid) == null) {
			logger.error("Tenant Not Found For Whom Feedback Needs To Be Created " + tenantUuid);
			throw new Exception("Tenant Not Found");
		}

	}

	@Transactional
	public List<Feedback> findAllByTenantUuid(UUID tenantUuid) throws Exception {
		try {
			verifyTenant(tenantUuid);
			return feedbackRepository.findAllByTenantUuid(tenantUuid);
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("Error Finding Feedback Details For Tenant");
		}
	}

}
