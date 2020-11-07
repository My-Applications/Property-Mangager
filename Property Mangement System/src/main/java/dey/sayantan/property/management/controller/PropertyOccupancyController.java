package dey.sayantan.property.management.controller;

import java.io.IOException;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import dey.sayantan.property.management.model.PropertyOccupancy;
import dey.sayantan.property.management.service.PropertyOccupancyService;
import dey.sayantan.property.management.service.TenantService;
import dey.sayantan.property.management.validator.CommonValidatorUtil;

@RestController
public class PropertyOccupancyController {

	@Autowired
	ObjectMapper objectMapper;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	PropertyOccupancyService propertyOccupancyService;

	@Autowired
	TenantService tenantService;

	@PostMapping("/CreatePropertyOccupancy")
	public PropertyOccupancy processRequest(HttpServletRequest requestPayload, HttpServletResponse response) {
		PropertyOccupancy propertyOccupancy = new PropertyOccupancy();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			propertyOccupancy = objectMapper.readValue(request, PropertyOccupancy.class);
			JSONObject obj = new JSONObject(request);
			String registrationId = obj.getString("registrationId");
			propertyOccupancy.setTenantUuid(tenantService.getByRegistrationId(registrationId).getUuid());
			propertyOccupancyService.addPropertyOccupancy(propertyOccupancy);
			return propertyOccupancy;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return propertyOccupancy;
	}


	@PutMapping("/UpdatePropertyOccupancy")
	public PropertyOccupancy update(HttpServletRequest requestPayload, HttpServletResponse response) {
		PropertyOccupancy propertyOccupancy = new PropertyOccupancy();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			propertyOccupancy = objectMapper.readValue(request, PropertyOccupancy.class);
			propertyOccupancy.setUuid(UUID.randomUUID());
			propertyOccupancy.setTimestamp(Instant.now());
			propertyOccupancy.setUuid(UUID.randomUUID());
			propertyOccupancy.setTimestamp(Instant.now());
			propertyOccupancyService.addPropertyOccupancy(propertyOccupancy);
			return propertyOccupancy;

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return propertyOccupancy;
	}

	@DeleteMapping("/DeletePropertyOccupancy")
	public PropertyOccupancy deletePropertyOccupancy(HttpServletRequest requestPayload) {
		String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
		JSONObject obj = new JSONObject(request);
		String uuidString = obj.getString("proprieterId");
		UUID propertyOccupancyUuid = UUID.fromString(uuidString);
		PropertyOccupancy propertyOccupancy = propertyOccupancyService.findByUuid(propertyOccupancyUuid);
		// TODO make is_deleted = true
		propertyOccupancyService.deletePropertyOccupancy(propertyOccupancy.getRowId());
		return propertyOccupancy;
	}

	@GetMapping("/PropertyOccupancyViewsAll")
	public List<PropertyOccupancy> showView() {
		return propertyOccupancyService.getAllPropertyOccupancy();
	}

	@GetMapping("/PropertyOccupancyViews/{id}")
	public PropertyOccupancy showPropertyOccupancy(@PathVariable String id) throws Exception {
		UUID tenantUuid;
		try {
			tenantUuid = tenantService.getByRegistrationId(id).getUuid();
		} catch (Exception e) {
			logger.error("Error Getting Tenant UUID for the Registartion Id : " + e.getLocalizedMessage());
			e.printStackTrace();
			throw new Exception("Error Finding Tenant Details For the Provided Registration Number");
		}
		return propertyOccupancyService.findByUuid(tenantUuid);
	}

}
