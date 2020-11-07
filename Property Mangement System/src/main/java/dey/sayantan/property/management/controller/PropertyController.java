package dey.sayantan.property.management.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import dey.sayantan.property.management.model.Property;
import dey.sayantan.property.management.service.PropertyService;
import dey.sayantan.property.management.validator.CommonValidatorUtil;

@RestController
public class PropertyController {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	PropertyService propertyRepo;

	@PostMapping("/CreateProperty")
	public Property processRequest(HttpServletRequest requestPayload, HttpServletResponse response) {
		Property property = new Property();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			property = objectMapper.readValue(request, Property.class);
			property.setUuid(UUID.randomUUID());
			property.setTimestamp(Instant.now());
			propertyRepo.addProperty(property);
			return property;
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return property;
	}

	@PutMapping("/UpdateProperty")
	public Property update(HttpServletRequest requestPayload, HttpServletResponse response) {
		Property property = new Property();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			property = objectMapper.readValue(request, Property.class);
			property.setUuid(UUID.randomUUID());
			property.setTimestamp(Instant.now());
			propertyRepo.addProperty(property);
			return property;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return property;
	}

	@DeleteMapping("/DeleteProperty")
	public Property deleteProperty(HttpServletRequest requestPayload) {
		String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
		JSONObject obj = new JSONObject(request);
		String uuidString = obj.getString("proprieterId");
		UUID propertyUuid = UUID.fromString(uuidString);
		Property property = propertyRepo.findByUuid(propertyUuid);
		// TODO make is_deleted = true
		propertyRepo.deleteProperty(property.getRowId());
		return property;
	}

	@GetMapping("/PropertyViewsAll")
	public List<Property> showAllProperty() {
		return propertyRepo.getAllPropertys();
	}

	@GetMapping("/PropertyViews/{id}")
	public Optional<Property> showProperty(@PathVariable Long id) {
		return propertyRepo.getById(id);
	}

}
