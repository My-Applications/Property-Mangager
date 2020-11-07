package dey.sayantan.property.management.service;

import java.time.Instant;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dey.sayantan.property.management.model.PropertyOccupancy;
import dey.sayantan.property.management.repositry.PropertyOccupancyRepository;

@Service
public class PropertyOccupancyService {

	@Autowired
	PropertyOccupancyRepository<PropertyOccupancy> propertyOccupancyRepository;

	@Transactional
	public List<PropertyOccupancy> getAllPropertyOccupancy() {
		return (List<PropertyOccupancy>) propertyOccupancyRepository.findAll();
	}

	@Transactional
	public Optional<PropertyOccupancy> getById(Long id) {
		return propertyOccupancyRepository.findById(id);
	}

	@Transactional
	public PropertyOccupancy findByUuid(UUID tenantUuid) {
		return propertyOccupancyRepository.findByTenantUuid(tenantUuid);
	}

	@Transactional
	public void deletePropertyOccupancy(Long propertyOccupancyId) {
		propertyOccupancyRepository.deleteById(propertyOccupancyId);
	}

	@Transactional
	public boolean addPropertyOccupancy(PropertyOccupancy propertyOccupancy) {
		if (propertyOccupancy.getEndDate() == null)
			propertyOccupancy.setEndDate(LocalDate.parse("9999-12-31"));
		propertyOccupancy.setUuid(UUID.randomUUID());
		propertyOccupancy.setTimestamp(Instant.now());
		return propertyOccupancyRepository.save(propertyOccupancy) != null;
	}

	@Transactional
	public boolean updatePropertyOccupancy(PropertyOccupancy propertyOccupancy) {
		return propertyOccupancyRepository.save(propertyOccupancy) != null;
	}

}
