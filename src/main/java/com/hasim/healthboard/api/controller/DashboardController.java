package com.hasim.healthboard.api.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.hasim.healthboard.api.model.ApplicationData;
import com.hasim.healthboard.api.model.ApplicationDataResponse;
import com.hasim.healthboard.api.model.ApplicationRequest;
import com.hasim.healthboard.api.service.DashboardFacade;

@RestController
@Validated
@EnableAutoConfiguration
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class DashboardController {
	private static final Logger logger = LogManager.getLogger(DashboardController.class);

	@Autowired
	DashboardFacade dashboardFacade;

	@Value("${currentEnvironment:DEV}")
	private String currentEnvironment;

	@Autowired
	private Environment env;

	@RequestMapping(value = "/health", method = RequestMethod.GET, produces = "application/json")
	@ResponseStatus(HttpStatus.OK)
	public String checkHealth() {
		return "Health Board api is up";
	}

	@RequestMapping(value = "/applications", method = RequestMethod.GET, headers = "Accept=application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<List<ApplicationDataResponse>> getApplications(@RequestHeader HttpHeaders headers,
			HttpServletRequest request) {
		logger.info("Started executing getApplications");

		List<ApplicationDataResponse> appllicationList = dashboardFacade.getAllApplications();
		logger.info("Finished executing getApplications");
		return new ResponseEntity<>(appllicationList, HttpStatus.OK);
	}

	@RequestMapping(value = "/applications/application", method = RequestMethod.POST, headers = "Accept=application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<ApplicationData> createOrUpdateApplication(
			@RequestBody(required = true) @Valid ApplicationRequest applicationRequest,
			@RequestHeader HttpHeaders headers, HttpServletRequest request) {
		logger.info(env.getProperty("currentEnvironment"));
		ApplicationData applicationResponse = dashboardFacade.createOrUpdateApplication(applicationRequest);
		return new ResponseEntity<ApplicationData>(applicationResponse, HttpStatus.OK);
	}

	@RequestMapping(value = "/applications/application/{appId}", method = RequestMethod.GET, headers = "Accept=application/json", produces = MediaType.APPLICATION_JSON_UTF8_VALUE, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
	@ResponseBody
	public ResponseEntity<ApplicationData> getApplication(@PathVariable("appId") String appId,
			@RequestHeader HttpHeaders headers, HttpServletRequest request) {
		logger.info("Started executing getApplication");
		logger.info("appId = " + appId);
		ApplicationData applicationResponse = dashboardFacade.getApplication(appId);
		logger.info("Finished executing getApplication");
		return new ResponseEntity<>(applicationResponse, HttpStatus.OK);
	}
}
