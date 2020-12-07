package dey.sayantan.property.management.model;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import dey.sayantan.property.management.core.EntityRoot;

@Entity(name = "property_occupancy")
@Table(name = "property_occupancy")
public class PropertyOccupancy extends EntityRoot {

	private static final long serialVersionUID = 1L;

	@Column(nullable = false)
	private UUID tenantUuid;

	@Column(nullable = false)
	private UUID propertyUuid;

	@Column(nullable = false)
	private LocalDate startDate;

	@Column(nullable = false)
	private LocalDate endDate;

	@Column(nullable = false)
	private String description;

	@OneToMany(mappedBy = "propertyOccupancy", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<OccupancyDetails> occupancyDetailsList;

	public PropertyOccupancy() {
	}

	public PropertyOccupancy(UUID tenantUuid, UUID propertyUuid, LocalDate startDate, LocalDate endDate,
			String description) {
		super();
		this.tenantUuid = tenantUuid;
		this.propertyUuid = propertyUuid;
		this.startDate = startDate;
		this.endDate = endDate;
		this.description = description;
	}

	public UUID getTenantUuid() {
		return tenantUuid;
	}

	public void setTenantUuid(UUID tenantUuid) {
		this.tenantUuid = tenantUuid;
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

	public List<OccupancyDetails> getOccupancyDetailsList() {
		return occupancyDetailsList;
	}

	public void setOccupancyDetailsList(List<OccupancyDetails> occupancyDetailsList) {
		this.occupancyDetailsList = occupancyDetailsList;
	}

	public UUID getPropertyUuid() {
		return propertyUuid;
	}

	public void setPropertyUuid(UUID propertyUuid) {
		this.propertyUuid = propertyUuid;
	}

}