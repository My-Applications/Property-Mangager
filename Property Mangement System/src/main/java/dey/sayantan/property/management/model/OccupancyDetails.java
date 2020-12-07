package dey.sayantan.property.management.model;

import java.time.LocalDate;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import dey.sayantan.property.management.core.EntityRoot;

@Entity(name = "occupancy_details")
@Table(name = "occupancy_details")
public class OccupancyDetails extends EntityRoot {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "property_occupancy_id", referencedColumnName = "rowId")
	private PropertyOccupancy propertyOccupancy;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@Column
	private String description;

	@Column(name = "uuid", length = 36, nullable = false, updatable = false)
	protected UUID uuid;

	public OccupancyDetails() {
		super();
	}

	public OccupancyDetails(PropertyOccupancy propertyOccupancy, UUID propertyUUID, LocalDate startDate,
			LocalDate endDate, String description) {
		super();
		this.propertyOccupancy = propertyOccupancy;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
	}

	public PropertyOccupancy getPropertyOccupancy() {
		return propertyOccupancy;
	}

	public void setPropertyOccupancy(PropertyOccupancy propertyOccupancy) {
		this.propertyOccupancy = propertyOccupancy;
	}

	public LocalDate getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDate startDate) {
		this.startDate = startDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public void setEndDate(LocalDate endDate) {
		this.endDate = endDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public UUID getUuid() {
		return uuid;
	}

	public void setUuid(UUID uuid) {
		this.uuid = uuid;
	}

}
