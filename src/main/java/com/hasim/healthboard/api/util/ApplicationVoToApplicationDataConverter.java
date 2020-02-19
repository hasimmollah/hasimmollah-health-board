package com.hasim.healthboard.api.util;

import java.util.function.Function;

import com.hasim.healthboard.api.model.ApplicationData;
import com.hasim.healthboard.api.model.ApplicationVO;

public class ApplicationVoToApplicationDataConverter implements Function<ApplicationVO, ApplicationData> {

	@Override
	public ApplicationData apply(ApplicationVO t) {
		return DashboardCommonUtil.prepareApplicationVOToUserResponse(t);
	}
}
