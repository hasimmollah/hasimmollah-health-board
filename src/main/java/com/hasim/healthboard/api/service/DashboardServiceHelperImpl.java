
package com.hasim.healthboard.api.service;

import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import com.hasim.healthboard.api.model.ApplicationVO;


@Component
public class DashboardServiceHelperImpl implements DashboardServiceHelper {
    private static final Logger logger = LogManager.getLogger(DashboardServiceHelperImpl.class);

    @Autowired
    RestTemplate restTemplate;

    @Value("${currentEnvironment:}")
    private String currentEnvironment;

    @Override
    public List<ApplicationVO> checkHelth(List<ApplicationVO> applicationVOList, boolean isCreateFlow) {
        ExecutorService executorService = new ThreadPoolExecutor(
            10,
            20,
            0L,
            TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>());
        List<ApplicationVO> applicationVOListOutput = null;
        try {
            applicationVOListOutput = applicationVOList.stream().filter(applicationVO ->

            {
                return isCreateFlow||StringUtils.isEmpty(currentEnvironment)
                    || currentEnvironment.equalsIgnoreCase(applicationVO.getEnvironment());

            }

            ).map(applicationVO -> {
                ApplicationVO applicationVOOutput = null;
                try {
                    AppStatusChecker appStatusChecker = new AppStatusChecker(
                        applicationVO,
                        restTemplate);

                    FutureTask<ApplicationVO> ft = new FutureTask(
                        appStatusChecker);
                    executorService.submit(ft);
                    applicationVOOutput = ft.get();
                } catch (InterruptedException e) {
                    logger.error("error occurred", e);
                } catch (ExecutionException e) {
                    logger.error("error occurred", e);
                }
                logger.debug(" App Id = " + applicationVOOutput.getAppId() + " Last Alive "
                    + applicationVOOutput.getLastAlive() + " Down Since "
                    + applicationVOOutput.getDownSince());
                return applicationVOOutput;
            }).collect(Collectors.toList());
        } finally {
            executorService.shutdown();
        }
        return applicationVOListOutput;
    }

}
