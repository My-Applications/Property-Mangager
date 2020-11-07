package dey.sayantan.property.management.model.enums;

public enum PaymentFrequency {
	YEARLY("Yearly"), DAILY("Daily"), MONTHLY("Monthly");

	private final String paymentFrequency;

	private PaymentFrequency(String paymentFrequency) {
		this.paymentFrequency = paymentFrequency;

	}

	public String paymentFrequency() {
		return paymentFrequency;
	}
}
