package dey.sayantan.property.management.view;

import java.time.LocalDate;
import java.util.UUID;

import dey.sayantan.property.management.model.Complain;

public class ComplainView {

	private UUID uuid;

	private UUID tenantUuid;

	private String complainType;

	private String priority;

	private String messageDetails;

	private String status;

	private LocalDate complainDate;

	private String registrationId;

	public ComplainView(Complain complain, String registrationId) {
		this.tenantUuid = complain.getTenantUuid();
		this.complainType = complain.getComplainType();
		this.priority = complain.getPriority();
		this.messageDetails = complain.getMessageDetails();
		this.status = complain.getStatus();
		this.complainDate = complain.getComplainDate();
		this.registrationId = registrationId;
		this.uuid = complain.getUuid();
	}

	public UUID getTenantUuid() {
		return tenantUuid;
	}

	public void setTenantUuid(UUID tenantUuid) {
		this.tenantUuid = tenantUuid;
	}

	public String getComplainType() {
		return complainType;
	}

	public void setComplainType(String complainType) {
		this.complainType = complainType;
	}

	public String getPriority() {
		return priority;
	}

	public void setPriority(String priority) {
		this.priority = priority;
	}

	public String getMessageDetails() {
		return messageDetails;
	}

	public void setMessageDetails(String messageDetails) {
		this.messageDetails = messageDetails;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public LocalDate getComplainDate() {
		return complainDate;
	}

	public void setComplainDate(LocalDate complainDate) {
		this.complainDate = complainDate;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

}
