package com.hasim.healthboard.api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hasim.healthboard.api.model.ApplicationVO;

@Service
public interface DashboardService {

    public ApplicationVO  getApplication(String appId, boolean isCreateMode);
    public List< ApplicationVO> getApplications();
    public ApplicationVO createApplication(ApplicationVO applicationVO);
}
