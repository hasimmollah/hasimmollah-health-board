package com.hasim.healthboard.api.service;

import java.util.Date;
import java.util.concurrent.Callable;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.hasim.healthboard.api.model.ApplicationVO;

public class AppStatusChecker implements Callable{
    private static final Logger logger = LogManager.getLogger(AppStatusChecker.class);
    
    private ApplicationVO applicationVO;
    private RestTemplate restTemplate;
    public AppStatusChecker(ApplicationVO applicationVO,RestTemplate restTemplate){
        this.applicationVO=applicationVO;
        this.restTemplate=restTemplate;
    }
    @Override
    public ApplicationVO call() throws Exception {
        String url =applicationVO.getUrl();
        HttpHeaders httpHeaders = new HttpHeaders();
        
        ResponseEntity<String> response =fetchCMASResponseGeneric(url, httpHeaders, HttpMethod.GET, String.class, null);
        if(response!=null && response.getStatusCodeValue()==200) {
            applicationVO.setAppStatus(true);
            applicationVO.setLastAlive(new Date());
            applicationVO.setDownSince(null);
        } else {
            applicationVO.setAppStatus(false);
            Date downSince = applicationVO.getDownSince();
            downSince = downSince!=null?downSince:new Date();
            applicationVO.setDownSince(downSince);
        }
        
        return applicationVO;
    }
    
    private <T, B> ResponseEntity<T> fetchCMASResponseGeneric(String url, HttpHeaders headers,
        HttpMethod httpMethod, Class<T> t, B body) {
        logger.info(" api url ",url);
        HttpEntity<B> entity = null;
        if (body != null) {
            entity = new HttpEntity<>(
                body,
                headers);
        } else {
            entity = new HttpEntity<>(
                headers);
        }

        ResponseEntity<T> response = null;
        
        try {
            response = restTemplate.exchange(url, httpMethod, entity, t);
           

        }  catch (Exception e) {
            logger.error(" Exception occurred ",e);
        }
        
        return response;
    }

}
