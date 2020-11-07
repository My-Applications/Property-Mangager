package dey.sayantan.property.management.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import dey.sayantan.property.management.model.Feedback;
import dey.sayantan.property.management.service.FeedbackService;
import dey.sayantan.property.management.service.TenantService;
import dey.sayantan.property.management.validator.CommonValidatorUtil;
import dey.sayantan.property.management.view.FeedbackView;

@RestController
public class FeedbackController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	FeedbackService feedbackService;

	@Autowired
	TenantService tenantService;

	@PostMapping("/CreateFeedback")
	public Feedback create(HttpServletRequest requestPayload, HttpServletResponse response) throws Exception {
		Feedback feedback = new Feedback();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			feedback = objectMapper.readValue(request, Feedback.class);
			feedbackService.addFeedback(feedback);
			return feedback;
		} catch (IOException e) {
			logger.error("Could Not Process Request " + e.getLocalizedMessage());
		}
		return feedback;
	}

	@PutMapping("/UpdateFeedback")
	public Feedback update(HttpServletRequest requestPayload, HttpServletResponse response) throws Exception {
		Feedback feedback = new Feedback();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			feedback = objectMapper.readValue(request, Feedback.class);
			JSONObject obj = new JSONObject(request);
			String uuidString = obj.getString("feedbackUuid");
			UUID feedbackUuid = UUID.fromString(uuidString);
			feedback.setUuid(feedbackUuid);
			return feedbackService.updateFeedback(feedback);

		} catch (IOException e) {
			logger.error("Error Deserializing Payload To Object " + e.getMessage());
			throw new Exception("Error Deserializing Payload To Object ");
		}
	}

	@DeleteMapping("/DeleteFeedback")
	public Feedback deleteFeedback(HttpServletRequest requestPayload) throws Exception {
		String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
		JSONObject obj = new JSONObject(request);
		String uuidString = obj.getString("feedbackUuid");
		UUID feedbackUuid = UUID.fromString(uuidString);
		return feedbackService.deleteFeedback(feedbackUuid);
	}

	@GetMapping("/FeedbackViewsAll")
	public List<FeedbackView> showView() throws Exception {
		List<Feedback> allFeedbackList = null;
		List<FeedbackView> feedbackViewList = new ArrayList<>();
		try {
			allFeedbackList = feedbackService.getAllFeedbacks();
			for (Feedback feedback : allFeedbackList) {
				String regId = tenantService.findByUuid(feedback.getTenantUuid()).getRegistrationId();
				feedbackViewList.add(new FeedbackView(feedback, regId));
			}
			return feedbackViewList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e.getCause());
		}
	}

	/** fetch all feedbacks for a tenant **/
	@GetMapping("/FeedbackViews/{tenantUuid}")
	public List<Feedback> showFeedbackAll(@PathVariable UUID tenantUuid) throws Exception {
		logger.info("Requested To Find All Feedbacks for tenant " + tenantUuid);
		return feedbackService.findAllByTenantUuid(tenantUuid);
	}

}
