
package com.hasim.healthboard.api.util;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hasim.healthboard.api.constant.CommonConstant;
import com.hasim.healthboard.api.entity.Application;
import com.hasim.healthboard.api.model.ApplicationData;
import com.hasim.healthboard.api.model.ApplicationDataResponse;
import com.hasim.healthboard.api.model.ApplicationRequest;
import com.hasim.healthboard.api.model.ApplicationVO;

/**
 * Class for utility methods
 * 
 * @author Hasim Mollah
 *
 */
public class DashboardCommonUtil {
	private static final Logger logger = LogManager.getLogger(DashboardCommonUtil.class);
	private static final String VALIDATOR_PATTERN = "[<>]" + "|" + "&lt;" + "|" + "&gt;";

	private DashboardCommonUtil() {
		// making sure private constructor for util
	}

	/**
	 * @param requestParam
	 * @return boolean value.
	 */
	public static boolean validateRequestParameter(String requestParam) {

		Pattern pattern = Pattern.compile(VALIDATOR_PATTERN, Pattern.CASE_INSENSITIVE);

		Matcher headerMatcher = pattern.matcher(requestParam);
		return headerMatcher.find();
	}

	/**
	 * @param object
	 *            anyObject.
	 * @return PrintWriter writer.
	 * @throws JsonProcessingException
	 */
	public static String convertObjectToJson(Object object) throws JsonProcessingException {
		if (object == null) {
			return null;
		}
		ObjectMapper mapper = new ObjectMapper();
		return mapper.writeValueAsString(object);
	}

	public static <T> T convertStringToObject(String inputJson, Class<T> t) {
		ObjectMapper mapper = new ObjectMapper();
		T response = null;
		try {
			response = mapper.readValue(inputJson, t);
		} catch (Exception e) {
			logger.error(e);
		}
		return response;
	}

	public static ApplicationVO prepareApplicationVO(Application application) {
		ApplicationVO applicationVO = new ApplicationVO();
		applicationVO.setAppId(application.getAppId());
		applicationVO.setAppStatus(application.isAppStatus());
		applicationVO.setId(application.getId());
		applicationVO.setLab(application.getLab());
		applicationVO.setLastAlive(application.getLastAlive());
		applicationVO.setName(application.getName());
		applicationVO.setUrl(application.getUrl());
		applicationVO.setEnvironment(application.getEnvironment());
		applicationVO.setDownSince(application.getDownSince());
		logger.debug(" Application::: App Id = " + application.getAppId() + " Last Alive " + application.getLastAlive()
				+ " Down Since " + application.getDownSince());
		return applicationVO;

	}

	public static Application prepareApplicationFromApplicationVO(ApplicationVO applicationVO) {
		Application application = new Application();
		application.setAppId(applicationVO.getAppId());
		application.setAppStatus(applicationVO.isAppStatus());
		application.setId(applicationVO.getId());
		application.setLab(applicationVO.getLab());
		application.setLastAlive(applicationVO.getLastAlive());
		application.setDownSince(applicationVO.getDownSince());
		application.setName(applicationVO.getName());
		application.setUrl(applicationVO.getUrl());
		application.setEnvironment(applicationVO.getEnvironment());
		logger.debug("ApplicationVO :::: App Id = " + applicationVO.getAppId() + " Last Alive "
				+ applicationVO.getLastAlive() + " Down Since " + applicationVO.getDownSince());
		return application;

	}

	public static ApplicationData prepareApplicationVOToUserResponse(ApplicationVO applicationVO) {
		ApplicationData application = new ApplicationData();
		application.setAppId(applicationVO.getAppId());
		application.setAppStatus(applicationVO.isAppStatus());
		application.setId(applicationVO.getId());
		application.setLab(applicationVO.getLab());
		String lastAlive = null;
		if (applicationVO.getLastAlive() != null) {
			lastAlive = new SimpleDateFormat(CommonConstant.DATE_FORMAT).format(applicationVO.getLastAlive());
		}
		String downSince = null;
		if (applicationVO.getDownSince() != null) {
			downSince = new SimpleDateFormat(CommonConstant.DATE_FORMAT).format(applicationVO.getDownSince());
		}
		application.setDownSince(downSince);
		application.setLastAlive(lastAlive);
		application.setName(applicationVO.getName());
		application.setUrl(applicationVO.getUrl());
		application.setEnvironment(applicationVO.getEnvironment());
		return application;
	}

	public static ApplicationVO prepareApplicationVOFromRequest(ApplicationRequest applicationRequest,
			ApplicationVO applicationVO) {
		if (applicationVO == null) {
			applicationVO = new ApplicationVO();
		}
		applicationVO.setAppId(applicationRequest.getAppId());
		applicationVO.setLab(applicationRequest.getLab());

		applicationVO.setName(applicationRequest.getName());
		applicationVO.setUrl(applicationRequest.getUrl());
		applicationVO.setEnvironment(applicationRequest.getEnvironment());
		return applicationVO;
	}

	public static List<ApplicationDataResponse> convertApplicationVoListToApplicationDataResponseList(
			List<ApplicationVO> applicationVOList) {
		logger.info(" Started  getAllApplications ");
		final Map<String, Map<String, List<ApplicationData>>> applicationResponseByEnvAndLab = applicationVOList != null
				&& !applicationVOList.isEmpty()
						? applicationVOList.stream().map(new ApplicationVoToApplicationDataConverter())
								.sorted((applicationData1,applicationData2)->applicationData1.getAppId().compareTo(applicationData2.getAppId()))
								.collect(Collectors.groupingBy(ApplicationData::getEnvironment,
										Collectors.groupingBy(ApplicationData::getLab)))
						: null;

		logger.debug(" applicationResponseByEnvAndLab ::: " + applicationResponseByEnvAndLab);

		List<ApplicationDataResponse> applicationDataResponseList = applicationResponseByEnvAndLab != null
				? applicationResponseByEnvAndLab.entrySet().stream()
						.map(new ApplicationVoToApplicationDataResponseConverter())
						.collect(Collectors.toList())
				: Collections.emptyList();

		logger.info(" Finished  getAllApplications ");
		return applicationDataResponseList;
	}

}
