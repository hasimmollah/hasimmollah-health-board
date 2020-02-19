package com.hasim.healthboard.api.util;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.hasim.healthboard.api.model.ApplicationData;
import com.hasim.healthboard.api.model.ApplicationDataInformation;
import com.hasim.healthboard.api.model.ApplicationDataResponse;

public class ApplicationVoToApplicationDataResponseConverter implements Function<Map.Entry<String, Map<String, List<ApplicationData>>>, ApplicationDataResponse> {

	@Override
	public ApplicationDataResponse apply(Map.Entry<String, Map<String, List<ApplicationData>>> t) {
		ApplicationDataResponse applicationDataResponse = new ApplicationDataResponse();
		applicationDataResponse.setEnvironment(t.getKey());
		Map<String, List<ApplicationData>> applicationDataInformationMap = t.getValue();
		List<ApplicationDataInformation> applicationDataInformationList = applicationDataInformationMap.entrySet()
				.stream().map((applicationDataInformationEntry) -> {
					ApplicationDataInformation applicationDataInformation = new ApplicationDataInformation();
					applicationDataInformation.setLab(applicationDataInformationEntry.getKey());
					applicationDataInformation.setApplicationDataList(applicationDataInformationEntry.getValue());
					return applicationDataInformation;
				}).collect(Collectors.toList());

		applicationDataResponse.setApplicationDataInformationList(applicationDataInformationList);
		return applicationDataResponse;
	}
}
