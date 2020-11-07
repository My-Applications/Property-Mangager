package dey.sayantan.property.management.view;

import java.time.LocalDate;
import java.util.UUID;

import dey.sayantan.property.management.model.Feedback;

public class FeedbackView {

	private UUID tenantUuid;

	private float rating;

	private String suggestion;

	private String appreciationMessage;

	private LocalDate feedbackDate;

	private String registrationId;

	public FeedbackView(Feedback feedback, String registrationId) {
		this.tenantUuid = feedback.getTenantUuid();
		this.rating = feedback.getRating();
		this.suggestion = feedback.getSuggestion();
		this.appreciationMessage = feedback.getAppreciationMessage();
		this.feedbackDate = feedback.getFeedbackDate();
		this.registrationId = registrationId;

	}

	public UUID getTenantUuid() {
		return tenantUuid;
	}

	public void setTenantUuid(UUID tenantUuid) {
		this.tenantUuid = tenantUuid;
	}

	public float getRating() {
		return rating;
	}

	public void setRating(float rating) {
		this.rating = rating;
	}

	public String getSuggestion() {
		return suggestion;
	}

	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}

	public String getAppreciationMessage() {
		return appreciationMessage;
	}

	public void setAppreciationMessage(String appreciationMessage) {
		this.appreciationMessage = appreciationMessage;
	}

	public LocalDate getFeedbackDate() {
		return feedbackDate;
	}

	public void setFeedbackDate(LocalDate feedbackDate) {
		this.feedbackDate = feedbackDate;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

}
