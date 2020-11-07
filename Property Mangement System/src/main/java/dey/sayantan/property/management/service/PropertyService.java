package dey.sayantan.property.management.service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dey.sayantan.property.management.model.Property;
import dey.sayantan.property.management.repositry.PropertyRepository;

@Service
public class PropertyService {

	@Autowired
	PropertyRepository<Property> propertyRepository;

	@Transactional
	public List<Property> getAllPropertys() {
		return (List<Property>) propertyRepository.findAll();
	}

	@Transactional
	public Optional<Property> getById(Long id) {
		return propertyRepository.findById(id);
	}

	@Transactional
	public Property findByUuid(UUID propertyUuid) {
		return propertyRepository.findByUuid(propertyUuid);
	}

	@Transactional
	public void deleteProperty(Long propertyId) {
		propertyRepository.deleteById(propertyId);
	}

	@Transactional
	public boolean addProperty(Property property) {
		return propertyRepository.save(property) != null;
	}

	@Transactional
	public boolean updateProperty(Property property) {
		return propertyRepository.save(property) != null;
	}
}
