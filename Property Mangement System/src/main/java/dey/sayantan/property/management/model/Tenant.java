package dey.sayantan.property.management.model;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import dey.sayantan.property.management.core.EntityRoot;

@Entity
@Table(name = "Tenant")
public class Tenant extends EntityRoot implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String registrationId;
	@Column(nullable = false)
	private String tenantName;
	@Column(nullable = false)
	private LocalDate dateOfBirth;
	@Column(nullable = false, length = 310)
	private String permanentAddress;
	@Column(nullable = false, length = 15)
	private long contactNumber;
	@Column(nullable = false, length = 3)
	private String countryCode;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable = false)
	private String password;

	public Tenant() {
		super();
	}

	public Tenant(String registrationId, String tenantName, LocalDate dateOfBirth, String permanentAddress,
			long contactNumber, String countryCode) {
		super();
		this.registrationId = registrationId;
		this.tenantName = tenantName;
		this.dateOfBirth = dateOfBirth;
		this.permanentAddress = permanentAddress;
		this.contactNumber = contactNumber;
		this.countryCode = countryCode;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getTenantName() {
		return tenantName;
	}

	public void setTenantName(String tenantName) {
		this.tenantName = tenantName;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public String getPermanentAddress() {
		return permanentAddress;
	}

	public void setPermanentAddress(String permanentAddress) {
		this.permanentAddress = permanentAddress;
	}

	public long getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(long contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getCountryCode() {
		return countryCode;
	}

	public void setCountryCode(String countryCode) {
		this.countryCode = countryCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
