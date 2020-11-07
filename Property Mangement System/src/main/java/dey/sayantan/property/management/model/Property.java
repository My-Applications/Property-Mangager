package dey.sayantan.property.management.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import dey.sayantan.property.management.core.EntityRoot;

@Entity
@Table(name = "Property")
public class Property extends EntityRoot {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private String propertyId;

	@Column(nullable = false)
	private String propertyType;

	@Column(nullable = false)
	private long propertyCost;

	@Column(nullable = false, length = 500)
	private String description;

	@Column(nullable = true)
	private int capacity;

	public Property() {
		super();
	}

	public Property(String propertyId, String propertyType, long propertyCost, String description, int capacity) {
		super();
		this.propertyId = propertyId;
		this.propertyType = propertyType;
		this.propertyCost = propertyCost;
		this.description = description;
		this.capacity = capacity;
	}

	public String getPropertyId() {
		return propertyId;
	}

	public void setPropertyId(String propertyId) {
		this.propertyId = propertyId;
	}

	public String getPropertyType() {
		return propertyType;
	}

	public void setPropertyType(String propertyType) {
		this.propertyType = propertyType;
	}

	public long getPropertyCost() {
		return propertyCost;
	}

	public void setPropertyCost(long propertyCost) {
		this.propertyCost = propertyCost;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int getCapacity() {
		return capacity;
	}

	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}

}
