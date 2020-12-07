package dey.sayantan.property.management.controller;

import java.io.IOException;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import dey.sayantan.property.management.core.LoginDTO;
import dey.sayantan.property.management.exceptions.PayloadDecodeException;
import dey.sayantan.property.management.model.Tenant;
import dey.sayantan.property.management.service.TenantService;
import dey.sayantan.property.management.validator.CommonValidatorUtil;

@RestController
public class TenantController {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	TenantService tenantService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/CreateTenant")
	public Tenant createTenant(HttpServletRequest requestPayload, HttpServletResponse response) throws Exception {
		Tenant tenant = new Tenant();
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			tenant = objectMapper.readValue(request, Tenant.class);
			tenantService.addTenant(tenant);
			return tenant;
	}

	@PostMapping("/LoginTenant")
	public Tenant loginTenant(HttpServletRequest requestPayload, HttpServletResponse response) throws Exception {
		Tenant tenant = new Tenant();
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			LoginDTO loginDetails = objectMapper.readValue(request, LoginDTO.class);
			tenant = tenantService.verifyTenantCreds(loginDetails);
			// TODO generate bearer token
			return tenant;
	}

	@PutMapping("/UpdateTenant")
	public Tenant update(HttpServletRequest requestPayload, HttpServletResponse response) throws Exception {
		Tenant tenant = new Tenant();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			tenant = objectMapper.readValue(request, Tenant.class);
			tenant = tenantService.updateTenant(tenant);

		} catch (IOException e) {
			logger.error("Error Deserializing Payload To Object " + e.getMessage());
			throw new PayloadDecodeException("Error Deserializing Payload To Object ");
		}
		return tenant;
	}

	@DeleteMapping("/DeleteTenant")
	public Tenant deleteTenant(HttpServletRequest requestPayload) throws Exception {
		String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
		JSONObject obj = new JSONObject(request);
		String uuidString = obj.getString("tenantUuid");
		UUID tenantUuid = UUID.fromString(uuidString);
		return tenantService.deleteTenant(tenantUuid);
	}

	@GetMapping("/TenantViewsAll")
	public List<Tenant> showView() {
		return tenantService.getAllTenants();
	}

	@GetMapping("/TenantViews/{id}")
	public Tenant showTenant(@PathVariable String id) throws Exception {
		Tenant tenantRequested = null;
		logger.info("Requested To Find Tenant for Registration ID " + id);
		tenantRequested = tenantService.getByRegistrationId(id);
		if (tenantRequested == null)
			throw new Exception("Tenant Not Found For The Registration ID");
		return tenantRequested;
	}

	@GetMapping("/TenantViews")
	public Tenant showTenantForUuid(@RequestParam(value = "uuid") UUID tenantUuid) throws Exception {
		logger.info("Requested To Find Tenant for Registration ID " + tenantUuid);
		return tenantService.findByUuid(tenantUuid);
	}

}
