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
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import dey.sayantan.property.management.core.LoginDTO;
import dey.sayantan.property.management.model.Proprietor;
import dey.sayantan.property.management.service.ProprietorService;
import dey.sayantan.property.management.validator.CommonValidatorUtil;

@RestController
public class ProprietorController {

	@Autowired
	ObjectMapper objectMapper;

	@Autowired
	ProprietorService proprietorService;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@PostMapping("/CreateProprietor")
	public Proprietor createProprietor(HttpServletRequest requestPayload, HttpServletResponse response)
			throws Exception {
		Proprietor proprietor = new Proprietor();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			proprietor = objectMapper.readValue(request, Proprietor.class);
			proprietorService.addProprietor(proprietor);
			return proprietor;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		}

	}

	@PutMapping("/UpdateProprietor")
	public Proprietor update(HttpServletRequest requestPayload, HttpServletResponse response) throws Exception {
		Proprietor proprietor = new Proprietor();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			proprietor = objectMapper.readValue(request, Proprietor.class);
			proprietor = proprietorService.updateProprietor(proprietor);

		} catch (IOException e) {
			logger.error("Error Deserializing Payload To Object " + e.getMessage());
			throw new Exception("Error Deserializing Payload To Object ");
		}
		return proprietor;
	}

	@DeleteMapping("/DeleteProprietor")
	public Proprietor deleteProprietor(HttpServletRequest requestPayload) throws Exception {
		String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
		JSONObject obj = new JSONObject(request);
		String uuidString = obj.getString("proprietorUuid");
		UUID proprietorUuid = UUID.fromString(uuidString);
		return proprietorService.deleteProprietor(proprietorUuid);
	}

	//TODO
	@RequestMapping("/LoginOwner")
	public Proprietor loginProprietor(HttpServletRequest requestPayload, HttpServletResponse response)
			throws Exception {
		Proprietor proprietor = new Proprietor();
		try {
			String request = CommonValidatorUtil.parsePayloadFromRequest(requestPayload);
			LoginDTO loginDetails = objectMapper.readValue(request, LoginDTO.class);
			proprietorService.verifyProprietorCreds(loginDetails);
			// TODO generate bearer token
			return proprietor;
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception(e.getMessage());
		}

	}

	@GetMapping("/ProprietorViewsAll")
	public List<Proprietor> showView() {
		return proprietorService.getAllProprietors();
	}

	@GetMapping("/ProprietorViews/{id}")
	public Proprietor showProprietor(@PathVariable String id) throws Exception {
		logger.info("Requested To Find Proprietor for Registration ID " + id);
		return proprietorService.getById(id);
	}

	@GetMapping("/Proprietors")
	public List<Proprietor> showProprietors() throws Exception {
		// logger.info("Requested To Find Proprietor for Registration ID " + id);
		return proprietorService.getAlls();
	}


}
