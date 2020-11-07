package dey.sayantan.property.management.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import dey.sayantan.property.management.core.EntityRoot;

@Entity
@Table(name = "Feedback")
public class Feedback extends EntityRoot {

	@Column(nullable = false)
	private UUID tenantUuid;

	@Column(nullable = false)
	private float rating;

	// Things that tenant like to be improved
	@Column(nullable = false)
	private String suggestion;

	// Things that tenant likes
	@Column(nullable = false)
	private String appreciationMessage;

	@Column(nullable = false)
	private LocalDate feedbackDate;

	public Feedback() {
	}

	public Feedback(UUID tenantUuid, float rating, String suggestion, String appreciationMessage,
			LocalDate feedbackDate) {
		super();
		this.tenantUuid = tenantUuid;
		this.rating = rating;
		this.suggestion = suggestion;
		this.appreciationMessage = appreciationMessage;
		this.feedbackDate = feedbackDate;
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

}
