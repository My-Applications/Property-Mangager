package dey.sayantan.property.management.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import dey.sayantan.property.management.core.EntityRoot;

@Entity
@Table(name = "Payment")
public class Payment extends EntityRoot {

	@Column(nullable = false)
	private UUID tenantUuid;

	@Column(nullable = false)
	private String paymentType;

	@Column(nullable = false)
	private int paymentAmount;

	@Column(nullable = false)
	private LocalDate paymentDate;

	public Payment() {
	}

	public Payment(UUID tenantUuid, String paymentType, int paymentAmount, LocalDate paymentDate) {
		super();
		this.tenantUuid = tenantUuid;
		this.paymentType = paymentType;
		this.paymentAmount = paymentAmount;
		this.paymentDate = paymentDate;
	}

	public UUID getTenantUuid() {
		return tenantUuid;
	}

	public void setTenantUuid(UUID tenantUuid) {
		this.tenantUuid = tenantUuid;
	}

	public String getPaymentType() {
		return paymentType;
	}

	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	public int getPaymentAmount() {
		return paymentAmount;
	}

	public void setPaymentAmount(int paymentAmount) {
		this.paymentAmount = paymentAmount;
	}

	public LocalDate getPaymentDate() {
		return paymentDate;
	}

	public void setPaymentDate(LocalDate paymentDate) {
		this.paymentDate = paymentDate;
	}

}
