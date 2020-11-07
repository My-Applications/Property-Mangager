package dey.sayantan.property.management.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import dey.sayantan.property.management.core.EntityRoot;

@Entity
@Table(name = "Complain")
public class Complain extends EntityRoot {

	@Column(nullable = false)
	private UUID tenantUuid;

	@Column(nullable = false)
	private String complainType;

	// High/Medium/Low
	@Column
	private String priority="LOW";

	@Column(nullable = false)
	private String messageDetails;

	// Active/In Progress/Solved
	@Column
	private String status="ACTIVE";

	@Column(nullable = false)
	private LocalDate complainDate;

	public Complain() {
	}

	public Complain(UUID tenantUuid, String complainType, String priority, String messageDetails, String status,
			LocalDate complainDate) {
		super();
		this.tenantUuid = tenantUuid;
		this.complainType = complainType;
		this.priority = priority;
		this.messageDetails = messageDetails;
		this.status = status;
		this.complainDate = complainDate;
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

}
