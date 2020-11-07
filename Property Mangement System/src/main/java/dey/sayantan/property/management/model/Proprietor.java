package dey.sayantan.property.management.model;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import dey.sayantan.property.management.core.EntityRoot;

@Entity
@Table(name = "proprietor")
public class Proprietor extends EntityRoot {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String proprietorName;
	@Column(nullable = false)
	private String address;
	@Column(nullable = false)
	private String registrationId;

	@JsonProperty(access = Access.WRITE_ONLY)
	@Column(nullable = false)
	private String password;

	public Proprietor() {
		super();
	}

	public Proprietor(UUID proprietorId, String proprietorName, String address, String registrationId,
			String password) {
		super();
		this.uuid = proprietorId;
		this.proprietorName = proprietorName;
		this.address = address;
		this.registrationId = registrationId;
		this.password = password;
	}

	public String getProprietorName() {
		return proprietorName;
	}

	public void setProprietorName(String proprietorName) {
		this.proprietorName = proprietorName;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getRegistrationId() {
		return registrationId;
	}

	public void setRegistrationId(String registrationId) {
		this.registrationId = registrationId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
