package dey.sayantan.property.management.controller;

import java.io.IOException;
import java.util.ArrayList;
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

import dey.sayantan.property.management.model.Complain;
import dey.sayantan.property.management.service.ComplainService;
import dey.sayantan.property.management.service.TenantService;
import dey.sayantan.property.management.validator.CommonValidatorUtil;
import dey.sayantan.property.management.view.ComplainView;

@RestController
public class ComplainController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ComplainService complainService;

	@Autowired
	TenantService tenantService;

	@PostMapping("/CreateComplain")
	public Complain create(HttpServletRequest requestPayload, HttpServletResponse response) throws Exception {
		Complain complain = new Complain();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			complain = objectMapper.readValue(request, Complain.class);
			complainService.addComplain(complain);
			return complain;
		} catch (IOException e) {
			logger.error("Could Not Process Request " + e.getLocalizedMessage());
		}
		return complain;
	}

	@PutMapping("/UpdateComplain")
	public Complain update(HttpServletRequest requestPayload, HttpServletResponse response) throws Exception {
		Complain complain = new Complain();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			complain = objectMapper.readValue(request, Complain.class);
			JSONObject obj = new JSONObject(request);
			String uuidString = obj.getString("complainUuid");
			UUID complainUuid = UUID.fromString(uuidString);
			complain.setUuid(complainUuid);
			return complainService.updateComplain(complain);

		} catch (IOException e) {
			logger.error("Error Deserializing Payload To Object " + e.getMessage());
			throw new Exception("Error Deserializing Payload To Object ");
		}
	}

	@DeleteMapping("/DeleteComplain")
	public Complain deleteComplain(HttpServletRequest requestPayload) throws Exception {
		String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
		JSONObject obj = new JSONObject(request);
		String uuidString = obj.getString("complainUuid");
		UUID complainUuid = UUID.fromString(uuidString);
		return complainService.deleteComplain(complainUuid);
	}

	@GetMapping("/ComplainViewsAll")
	public List<ComplainView> showView() throws Exception {
		List<Complain> allComplainList = null;
		List<ComplainView> complainViewList = new ArrayList<>();
		try {
			allComplainList = complainService.getAllComplains();
			for (Complain complain : allComplainList) {
				String regId = tenantService.findByUuid(complain.getTenantUuid()).getRegistrationId();
				complainViewList.add(new ComplainView(complain, regId));
			}
			return complainViewList;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e.getCause());
		}
	}

	/** fetch all complains for a tenant **/
	@GetMapping("/ComplainViews/{tenantUuid}")
	public List<Complain> showComplainAll(@PathVariable UUID tenantUuid) throws Exception {
		logger.info("Requested To Find All Complains for tenant " + tenantUuid);
		return complainService.findAllByTenantUuid(tenantUuid);
	}

}
