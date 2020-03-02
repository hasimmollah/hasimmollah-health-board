
package com.hasim.healthboard.api.service;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hasim.healthboard.api.exception.ApplicationNotFoundException;
import com.hasim.healthboard.api.model.ApplicationData;
import com.hasim.healthboard.api.model.ApplicationDataResponse;
import com.hasim.healthboard.api.model.ApplicationRequest;
import com.hasim.healthboard.api.model.ApplicationVO;
import com.hasim.healthboard.api.util.ApplicationVoToApplicationDataConverter;
import com.hasim.healthboard.api.util.DashboardCommonUtil;

@Service
public class DashboardFacade {
	private static final Logger logger = LogManager.getLogger(DashboardFacade.class);

	@Autowired
	DashboardService dashboardService;

	public List<ApplicationDataResponse> getAllApplications() {
		logger.info(" Started  getAllApplications ");
		List<ApplicationVO> applicationVOList = dashboardService.getApplications();

		List<ApplicationDataResponse> applicationDataResponseList = DashboardCommonUtil
				.convertApplicationVoListToApplicationDataResponseList(applicationVOList);
		logger.info(" Finished  getAllApplications ");
		return applicationDataResponseList;
	}

	public ApplicationData getApplication(String appid) {
		logger.info(" Started  getApplication with ::: " + appid);
		ApplicationVO applicationVO = dashboardService.getApplication(appid, false);
		if (applicationVO == null) {
			throw new ApplicationNotFoundException("No application found for the given id");

		}
		ApplicationVoToApplicationDataConverter applicationVoToResponseConverter = new ApplicationVoToApplicationDataConverter();
		ApplicationData applicationResponse = applicationVoToResponseConverter.apply(applicationVO);
		logger.debug(" applicationResponse ::: " + applicationResponse);
		logger.info(" Finished  getApplication with ::: " + appid);
		return applicationResponse;
	}

	public ApplicationData createOrUpdateApplication(ApplicationRequest applicationRequest) {

		ApplicationVO applicationVO = dashboardService.getApplication(applicationRequest.getAppId(), true);
		applicationVO = DashboardCommonUtil.prepareApplicationVOFromRequest(applicationRequest, applicationVO);
		ApplicationVO applicationVOOutput = dashboardService.createOrUpdateApplication(applicationVO);
		return DashboardCommonUtil.prepareApplicationVOToUserResponse(applicationVOOutput);
	}

}
