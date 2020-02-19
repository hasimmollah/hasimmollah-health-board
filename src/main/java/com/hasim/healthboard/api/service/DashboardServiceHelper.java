package com.hasim.healthboard.api.service;

import java.util.List;

import com.hasim.healthboard.api.model.ApplicationVO;

public interface DashboardServiceHelper {

    public List<ApplicationVO> checkHelth(List<ApplicationVO> applicationVOList, boolean isCreateFlow);
}
