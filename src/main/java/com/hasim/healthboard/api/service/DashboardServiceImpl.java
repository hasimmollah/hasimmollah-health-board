package com.hasim.healthboard.api.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.hasim.healthboard.api.cache.DataCache;
import com.hasim.healthboard.api.constant.CommonConstant;
import com.hasim.healthboard.api.entity.Application;
import com.hasim.healthboard.api.exception.HealthBoardSQLException;
import com.hasim.healthboard.api.model.ApplicationDataResponse;
import com.hasim.healthboard.api.model.ApplicationVO;
import com.hasim.healthboard.api.repository.ApplicationRepository;
import com.hasim.healthboard.api.util.DashboardCommonUtil;

@Service
public class DashboardServiceImpl implements DashboardService {
	private static final Logger logger = LogManager.getLogger(DashboardServiceImpl.class);
	@Autowired
	private SimpMessagingTemplate simpMessagingTemplate;

	private final Function<Application, ApplicationVO> applicationEntityToVoconverter = new Function<Application, ApplicationVO>() {

		@Override
		public ApplicationVO apply(Application t) {
			return DashboardCommonUtil.prepareApplicationVO(t);
		}
	};

	private final Function<ApplicationVO, Application> applicationVoToEntityconverter = new Function<ApplicationVO, Application>() {

		@Override
		public Application apply(ApplicationVO t) {
			return DashboardCommonUtil.prepareApplicationFromApplicationVO(t);
		}
	};

	@Autowired
	@Qualifier(CommonConstant.CACHE_APPLICATION)
	private DataCache<String, ApplicationVO> applicationCache;
	@Autowired
	ApplicationRepository applicationRepository;

	@Autowired
	DashboardServiceHelper dashboardServiceHelper;

	@Override
	public ApplicationVO getApplication(String appId, boolean isCreateMode) {
		logger.info("Started executing getApplication");

		ApplicationVO applicationVOOutput = null;
		Application applicationOutput = applicationRepository.findByAppId(appId);
		if (applicationOutput != null) {
			applicationVOOutput = DashboardCommonUtil.prepareApplicationVO(applicationOutput);
		}
		logger.info("Finished executing getApplication");
		return applicationVOOutput;
	}

	@Override
	public List<ApplicationVO> getApplications() {
		logger.info("Started executing getApplications");
		List<ApplicationVO> applications = null;

		Iterable<Application> applicationEntityCol = null;
		try {
			applicationEntityCol = applicationRepository.findAll();
		} catch (Exception e) {
			logger.error(e);
			throw new HealthBoardSQLException(e);
		}
		applications = prepareApplicationVOList(applicationEntityCol);
		sendDataToTopic(applications);
		logger.info("Finished executing getApplications");
		return applications;
	}

	@Async
	private void fetchApplicationsAndSendToTopic() {
		List<ApplicationVO> applicationVOList = getApplications();
		sendDataToTopic(applicationVOList);
	}
	@Async
	private void sendDataToTopic(List<ApplicationVO> applicationVOList) {
		List<ApplicationDataResponse> applicationDataResponseList = DashboardCommonUtil
				.convertApplicationVoListToApplicationDataResponseList(applicationVOList);
		sendToTopic(CommonConstant.TOPIC_APPLICATIONS, applicationDataResponseList);
		
		Map<String,Map<String,Map<String,String>>> finalStatMap =  prepareAppStat(applicationVOList);
		
		Map<String,Collection<Map<String,String>>> mapToSendToTopic = new HashMap();
		for(String key:finalStatMap.keySet()) {
			mapToSendToTopic.put(key, finalStatMap.get(key).values());
		}
		
		sendToTopic(CommonConstant.TOPIC_APPLICATIONS_STAT, mapToSendToTopic);
	}
	
	
	private void sendToTopic(String topic, Object data) {
		String message = null;
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		
		try {
			message = ow.writeValueAsString(data);
		} catch (JsonProcessingException e) {
			logger.error(e);
		}
		simpMessagingTemplate.convertAndSend(topic, message);
	}
	
	private Map<String,Map<String,Map<String,String>>> prepareAppStat(List<ApplicationVO> applicationVOList){
		 Map<String,Map<String,Map<String,String>>> finalResult = new HashMap();
		 
		 finalResult.put(CommonConstant.ENVIRONMENT, new HashMap());
		 finalResult.put(CommonConstant.LAB, new HashMap());
		 
		 for(ApplicationVO applicationVO:applicationVOList) {
			 Map<String,Map<String,String>> environmentMap = finalResult.get(CommonConstant.ENVIRONMENT);
			 
			 String environment = applicationVO.getEnvironment();
			 Map<String,String> statMap=environmentMap.get(environment);
			 if(statMap==null) {
				 statMap = new HashMap();
				 statMap.put(CommonConstant.NAME,environment);
			 }
			 
			 int liveCount = statMap.get(CommonConstant.LIVE_COUNT)!=null?Integer.valueOf(statMap.get(CommonConstant.LIVE_COUNT)):0;
			 int deadCount = statMap.get(CommonConstant.DEAD_COUNT)!=null?Integer.valueOf(statMap.get(CommonConstant.DEAD_COUNT)):0;
			 if(applicationVO.isAppStatus()) {
				 liveCount = liveCount+1;
			 } else {
				 deadCount=deadCount+1;
			 }
			 statMap.put(CommonConstant.LIVE_COUNT, String.valueOf(liveCount));
			 statMap.put(CommonConstant.DEAD_COUNT, String.valueOf(deadCount));
			 
			 environmentMap.put(environment, statMap);
			 
			 
			 
			 
			 Map<String,Map<String,String>> lapMap = finalResult.get(CommonConstant.LAB);
			 
			 
			 String lab = applicationVO.getLab();
			 Map<String,String> statLabMap=lapMap.get(lab);
			 if(statLabMap==null) {
				 statLabMap = new HashMap();
				 statLabMap.put(CommonConstant.NAME,lab);
			 }
			 
			 int liveCountLab = statLabMap.get(CommonConstant.LIVE_COUNT)!=null?Integer.valueOf(statLabMap.get(CommonConstant.LIVE_COUNT)):0;
			 int deadCountLab = statLabMap.get(CommonConstant.DEAD_COUNT)!=null?Integer.valueOf(statLabMap.get(CommonConstant.DEAD_COUNT)):0;
			 if(applicationVO.isAppStatus()) {
				 liveCountLab = liveCountLab+1;
			 } else {
				 deadCountLab=deadCountLab+1;
			 }
			 statLabMap.put(CommonConstant.LIVE_COUNT, String.valueOf(liveCountLab));
			 statLabMap.put(CommonConstant.DEAD_COUNT, String.valueOf(deadCountLab));
			 
			 lapMap.put(lab, statLabMap);
			 
			 
		 }
		 
		 return finalResult;
	}

	

	private List<ApplicationVO> prepareApplicationVOList(Iterable<Application> applicationItr) {
		List<ApplicationVO> applicationVOList = StreamSupport.stream(applicationItr.spliterator(), true)
				.map(applicationEntityToVoconverter).collect(Collectors.toList());
		return applicationVOList;
	}

	@PostConstruct
	@Transactional
	@Scheduled(initialDelayString = CommonConstant.CACHE_REFRESH_INTERVAL_APPLICATION, fixedRateString = CommonConstant.CACHE_REFRESH_INTERVAL_APPLICATION)
	public void fetchApplications() {
		logger.info("Started executing fetch fetchApplications");
		List<ApplicationVO> applicationVos = prepareApplicationVOList(applicationRepository.findAll());
		if (applicationVos != null && !applicationVos.isEmpty()) {
			applicationCache.refresh(applicationVos.stream()
					.collect(Collectors.toMap(ApplicationVO::getAppId, applicationVO -> applicationVO)));
		}

		logger.info("Finished executing fetch fetchApplications");
	}

	@Override
	public ApplicationVO createOrUpdateApplication(ApplicationVO applicationVO) {
		logger.info("Finished executing fetch createApplication");
		List<ApplicationVO> applicationVOListToCheckHelth = new ArrayList();
		applicationVOListToCheckHelth.add(applicationVO);
		List<ApplicationVO> applicationVOOutputList = dashboardServiceHelper.checkHelth(applicationVOListToCheckHelth,
				true);
		Application application = DashboardCommonUtil
				.prepareApplicationFromApplicationVO(applicationVOOutputList.get(0));
		Application applicationOutput = null;
		try {
			applicationOutput = applicationRepository.save(application);
		} catch (Exception e) {
			logger.error(e);
			throw new HealthBoardSQLException(e);
		}
		ApplicationVO applicationVOOutput = DashboardCommonUtil.prepareApplicationVO(applicationOutput);
		logger.info("Finished executing fetch createApplication");
		applicationCache.putValue(applicationVOOutput.getAppId(), applicationVOOutput);
		fetchApplicationsAndSendToTopic();
		return applicationVOOutput;

	}

	public List<ApplicationVO> createApplications(List<ApplicationVO> applicationVOs) {
		logger.info("Finished executing fetch createApplication");

		List<Application> applications = applicationVOs.stream().map(applicationVoToEntityconverter)
				.collect(Collectors.toList());

		Iterable<Application> applicationOutputList = null;
		try {
			applicationOutputList = applicationRepository.saveAll(applications);
		} catch (Exception e) {
			logger.error(e);
			throw new HealthBoardSQLException(e);
		}
		List<ApplicationVO> applicationVOsOutputList = prepareApplicationVOList(applicationOutputList);
		logger.info("Finished executing fetch createApplication");

		return applicationVOsOutputList;

	}

	@Scheduled(initialDelayString = CommonConstant.APP_DATA_REFRESH_INTERVAL_APPLICATION, fixedRateString = CommonConstant.APP_DATA_REFRESH_INTERVAL_APPLICATION)
	public void checkAndUpdateApplicationsstatus() {
		logger.info("Started executing fetch checkAndUpdateApplicationsstatus");
		List<ApplicationVO> applicationVOList = getApplications();

		List<ApplicationVO> applicationVOOutputList = dashboardServiceHelper.checkHelth(applicationVOList, false);

		List<ApplicationVO> applicationVOsOutputList = createApplications(applicationVOOutputList);

		if (applicationVOsOutputList != null && !applicationVOsOutputList.isEmpty()) {
			applicationCache.refresh(applicationVOsOutputList.stream()
					.collect(Collectors.toMap(ApplicationVO::getAppId, applicationVO -> applicationVO)));
		}
		applicationVOList = getApplications();
		sendDataToTopic(applicationVOList);
		logger.info("Finished executing fetch checkAndUpdateApplicationsstatus");
	}

}
